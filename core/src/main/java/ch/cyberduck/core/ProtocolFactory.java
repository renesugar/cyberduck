package ch.cyberduck.core;

/*
 * Copyright (c) 2002-2017 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.exception.AccessDeniedException;
import ch.cyberduck.core.preferences.PreferencesFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ProtocolFactory {
    private static final Logger log = Logger.getLogger(ProtocolFactory.class);

    private static final ProtocolFactory global = new ProtocolFactory();

    public static ProtocolFactory get() {
        return global;
    }

    private final Set<Protocol> registered;
    private final Local bundle;

    public ProtocolFactory() {
        this(new LinkedHashSet<Protocol>());
    }

    public ProtocolFactory(final Set<Protocol> protocols) {
        this(LocalFactory.get(PreferencesFactory.get().getProperty("application.profiles.path")), protocols);
    }

    public ProtocolFactory(final Local bundle, final Set<Protocol> protocols) {
        this.bundle = bundle;
        this.registered = protocols;
    }

    public void register(Protocol... protocols) {
        // Order determines list in connection dropdown
        for(Protocol protocol : protocols) {
            register(protocol);
        }
        if(bundle.exists()) {
            try {
                for(Local f : bundle.list().filter(new ProfileFilter())) {
                    final Profile profile = ProfileReaderFactory.get().read(f);
                    if(null == profile.getProtocol()) {
                        continue;
                    }
                    if(log.isInfoEnabled()) {
                        log.info(String.format("Adding bundled protocol %s", profile));
                    }
                    // Replace previous possibly disable protocol in Preferences
                    registered.add(profile);
                }
            }
            catch(AccessDeniedException e) {
                log.warn(String.format("Failure reading collection %s %s", bundle, e.getMessage()));
            }
        }
        // Load thirdparty protocols
        final Local library = LocalFactory.get(PreferencesFactory.get().getProperty("application.support.path"),
                PreferencesFactory.get().getProperty("profiles.folder.name"));
        if(library.exists()) {
            try {
                for(Local profile : library.list().filter(new ProfileFilter())) {
                    final Profile protocol = ProfileReaderFactory.get().read(profile);
                    if(null == protocol) {
                        continue;
                    }
                    if(log.isInfoEnabled()) {
                        log.info(String.format("Adding thirdparty protocol %s", protocol));
                    }
                    // Replace previous possibly disable protocol in Preferences
                    registered.add(protocol);
                }
            }
            catch(AccessDeniedException e) {
                log.warn(String.format("Failure reading collection %s %s", library, e.getMessage()));
            }
        }
    }

    public void register(final Protocol protocol) {
        if(null == protocol) {
            log.error("Attempt to register unknown protocol");
            return;
        }
        registered.add(protocol);
    }

    /**
     * @return List of enabled protocols
     */
    public List<Protocol> find() {
        return this.find(Protocol::isEnabled);
    }

    /**
     * @param search Search filter for all registered protocols
     * @return List of registered protocols matching search criteria.
     */
    public List<Protocol> find(final Predicate<Protocol> search) {
        return registered.stream().filter(search).sorted().collect(Collectors.toList());
    }

    /**
     * @param identifier Provider name or hash code of protocol
     * @return Matching protocol or null if no match
     */
    public Protocol forName(final String identifier) {
        return registered.stream().filter(protocol -> String.valueOf(protocol.hashCode()).equals(identifier)).findFirst().orElse(
                this.forName(identifier, null)
        );
    }

    public Protocol forName(final String identifier, final String provider) {
        return this.forName(this.find(), identifier, provider);
    }

    public Protocol forName(final List<Protocol> registered, final String identifier, final String provider) {
        final Protocol match = registered.stream().filter(protocol -> {
            if(StringUtils.equals(protocol.getIdentifier(), identifier)) {
                if(null == provider) {
                    // Matching protocol with no custom provider
                    return true;
                }
                else {
                    return StringUtils.equals(protocol.getProvider(), provider);
                }
            }
            // Fallback for bug in 6.1
            if(StringUtils.equals(String.format("%s-%s", protocol.getIdentifier(), protocol.getProvider()), identifier)) {
                return true;
            }
            return false;
        }).findFirst().orElse(
                registered.stream().filter(protocol -> StringUtils.equals(protocol.getProvider(), identifier)).findFirst().orElse(
                        registered.stream().filter(protocol -> Arrays.asList(protocol.getSchemes()).contains(identifier)).findFirst().orElse(null)
                )
        );
        if(null == match) {
            if(registered.isEmpty()) {
                log.error("List of registered protocols is empty");
                return null;
            }
            final Protocol next = registered.iterator().next();
            log.warn(String.format("Missing registered protocol for identifier %s. Return first in list %s", identifier, next));
            return next;
        }
        return match;
    }

    /**
     * @param scheme Protocol scheme
     * @return Standard protocol for this scheme. This is ambigous
     */
    public Protocol forScheme(final Scheme scheme) {
        return forScheme(scheme.name());
    }

    public Protocol forType(final Protocol.Type type) {
        return this.find().stream().filter(protocol -> protocol.getType().equals(type)).findFirst().orElse(null);
    }

    public Protocol forScheme(final String scheme) {
        return this.find().stream().filter(protocol -> Arrays.asList(protocol.getSchemes()).contains(scheme)).findFirst().orElse(null);
    }

    private static final class ProfileFilter implements Filter<Local> {
        @Override
        public boolean accept(final Local file) {
            return "cyberduckprofile".equals(FilenameUtils.getExtension(file.getName()));
        }

        @Override
        public Pattern toPattern() {
            return Pattern.compile(".*\\.cyberduckprofile");
        }
    }
}