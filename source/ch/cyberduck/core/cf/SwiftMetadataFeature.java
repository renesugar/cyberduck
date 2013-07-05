package ch.cyberduck.core.cf;

/*
 * Copyright (c) 2002-2013 David Kocher. All rights reserved.
 * http://cyberduck.ch/
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
 *
 * Bug fixes, suggestions and comments should be sent to feedback@cyberduck.ch
 */

import ch.cyberduck.core.Path;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.DefaultIOExceptionMappingService;
import ch.cyberduck.core.exception.FilesExceptionMappingService;
import ch.cyberduck.core.features.Metadata;
import ch.cyberduck.core.i18n.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;

import com.rackspacecloud.client.cloudfiles.FilesContainerMetaData;
import com.rackspacecloud.client.cloudfiles.FilesException;
import com.rackspacecloud.client.cloudfiles.FilesObjectMetaData;

/**
 * @version $Id:$
 */
public class SwiftMetadataFeature implements Metadata {
    private static final Logger log = Logger.getLogger(SwiftMetadataFeature.class);

    private CFSession session;

    public SwiftMetadataFeature(final CFSession session) {
        this.session = session;
    }

    @Override
    public Map<String, String> get(final Path file) throws BackgroundException {
        try {
            session.message(MessageFormat.format(Locale.localizedString("Reading metadata of {0}", "Status"),
                    file.getName()));

            if(file.attributes().isFile()) {
                final FilesObjectMetaData meta
                        = session.getClient().getObjectMetaData(session.getRegion(file.getContainer()),
                        file.getContainer().getName(), file.getKey());
                return meta.getMetaData();
            }
            if(file.attributes().isVolume()) {
                final FilesContainerMetaData meta
                        = session.getClient().getContainerMetaData(session.getRegion(file.getContainer()),
                        file.getContainer().getName());
                return meta.getMetaData();
            }
            return Collections.emptyMap();
        }
        catch(FilesException e) {
            throw new FilesExceptionMappingService().map("Cannot read file attributes", e, file);
        }
        catch(IOException e) {
            throw new DefaultIOExceptionMappingService().map("Cannot read file attributes", e, file);
        }
    }


    @Override
    public void write(final Path file, final Map<String, String> metadata) throws BackgroundException {
        try {
            session.message(MessageFormat.format(Locale.localizedString("Writing metadata of {0}", "Status"),
                    file.getName()));

            if(file.attributes().isFile()) {
                session.getClient().updateObjectMetadata(session.getRegion(file.getContainer()),
                        file.getContainer().getName(), file.getKey(), metadata);
            }
            else if(file.attributes().isVolume()) {
                for(Map.Entry<String, String> entry : file.attributes().getMetadata().entrySet()) {
                    // Choose metadata values to remove
                    if(!metadata.containsKey(entry.getKey())) {
                        log.debug(String.format("Remove metadata with key %s", entry.getKey()));
                        metadata.put(entry.getKey(), StringUtils.EMPTY);
                    }
                }
                session.getClient().updateContainerMetadata(session.getRegion(file.getContainer()),
                        file.getContainer().getName(), metadata);
            }
        }
        catch(FilesException e) {
            throw new FilesExceptionMappingService().map("Cannot read file attributes", e, file);
        }
        catch(IOException e) {
            throw new DefaultIOExceptionMappingService().map("Cannot read file attributes", e, file);
        }
    }
}
