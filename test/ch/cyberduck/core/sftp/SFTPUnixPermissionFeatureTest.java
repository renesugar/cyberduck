package ch.cyberduck.core.sftp;

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

import ch.cyberduck.core.AbstractTestCase;
import ch.cyberduck.core.Credentials;
import ch.cyberduck.core.DefaultHostKeyController;
import ch.cyberduck.core.DisabledCancelCallback;
import ch.cyberduck.core.DisabledListProgressListener;
import ch.cyberduck.core.DisabledLoginController;
import ch.cyberduck.core.DisabledPasswordStore;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.Permission;
import ch.cyberduck.core.shared.DefaultHomeFinderService;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.EnumSet;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @version $Id$
 */
public class SFTPUnixPermissionFeatureTest extends AbstractTestCase {

    @Test
    @Ignore
    public void testSetUnixOwner() throws Exception {
        final Host host = new Host(new SFTPProtocol(), "test.cyberduck.ch", new Credentials(
                properties.getProperty("sftp.user"), properties.getProperty("sftp.password")
        ));
        final SFTPSession session = new SFTPSession(host);
        assertNotNull(session.open(new DefaultHostKeyController()));
        assertTrue(session.isConnected());
        assertNotNull(session.getClient());
        session.login(new DisabledPasswordStore(), new DisabledLoginController(), new DisabledCancelCallback());
        final Path home = new DefaultHomeFinderService(session).find();
        final long modified = System.currentTimeMillis();
        final Path test = new Path(home, "test", EnumSet.of(Path.Type.file));
        new SFTPUnixPermissionFeature(session).setUnixOwner(test, "80");
        assertEquals("80", session.list(home, new DisabledListProgressListener()).get(test.getReference()).attributes().getOwner());
        session.close();
    }

    @Test
    @Ignore
    public void testSetUnixGroup() throws Exception {
        final Host host = new Host(new SFTPProtocol(), "test.cyberduck.ch", new Credentials(
                properties.getProperty("sftp.user"), properties.getProperty("sftp.password")
        ));
        final SFTPSession session = new SFTPSession(host);
        assertNotNull(session.open(new DefaultHostKeyController()));
        assertTrue(session.isConnected());
        assertNotNull(session.getClient());
        session.login(new DisabledPasswordStore(), new DisabledLoginController(), new DisabledCancelCallback());
        final Path home = new DefaultHomeFinderService(session).find();
        final long modified = System.currentTimeMillis();
        final Path test = new Path(home, "test", EnumSet.of(Path.Type.file));
        new SFTPUnixPermissionFeature(session).setUnixGroup(test, "80");
        assertEquals("80", session.list(home, new DisabledListProgressListener()).get(test.getReference()).attributes().getGroup());
        session.close();
    }

    @Test
    public void testSetUnixPermission() throws Exception {
        final Host host = new Host(new SFTPProtocol(), "test.cyberduck.ch", new Credentials(
                properties.getProperty("sftp.user"), properties.getProperty("sftp.password")
        ));
        final SFTPSession session = new SFTPSession(host);
        assertNotNull(session.open(new DefaultHostKeyController()));
        assertTrue(session.isConnected());
        assertNotNull(session.getClient());
        session.login(new DisabledPasswordStore(), new DisabledLoginController(), new DisabledCancelCallback());
        final Path home = new DefaultHomeFinderService(session).find();
        final long modified = System.currentTimeMillis();
        final Path test = new Path(home, UUID.randomUUID().toString(), EnumSet.of(Path.Type.file));
        new SFTPTouchFeature(session).touch(test);
        new SFTPUnixPermissionFeature(session).setUnixPermission(test, new Permission(666));
        assertEquals("666", session.list(home, new DisabledListProgressListener()).get(test.getReference()).attributes().getPermission().getMode());
        new SFTPDeleteFeature(session).delete(Collections.<Path>singletonList(test), new DisabledLoginController());
        session.close();
    }

    @Test
    public void testRetainStickyBits() throws Exception {
        final Host host = new Host(new SFTPProtocol(), "test.cyberduck.ch", new Credentials(
                properties.getProperty("sftp.user"), properties.getProperty("sftp.password")
        ));
        final SFTPSession session = new SFTPSession(host);
        session.open(new DefaultHostKeyController());
        session.login(new DisabledPasswordStore(), new DisabledLoginController(), new DisabledCancelCallback());
        final Path test = new Path(new DefaultHomeFinderService(session).find(), UUID.randomUUID().toString(), EnumSet.of(Path.Type.file));
        new SFTPTouchFeature(session).touch(test);
        final SFTPUnixPermissionFeature feature = new SFTPUnixPermissionFeature(session);
        feature.setUnixPermission(test,
                new Permission(Permission.Action.all, Permission.Action.read, Permission.Action.read,
                        true, false, false));
        assertEquals(new Permission(Permission.Action.all, Permission.Action.read, Permission.Action.read,
                true, false, false), new SFTPListService(session).list(test.getParent(), new DisabledListProgressListener()).get(
                test.getReference()).attributes().getPermission());
        feature.setUnixPermission(test,
                new Permission(Permission.Action.all, Permission.Action.read, Permission.Action.read,
                        false, true, false));
        assertEquals(new Permission(Permission.Action.all, Permission.Action.read, Permission.Action.read,
                false, true, false), new SFTPListService(session).list(test.getParent(), new DisabledListProgressListener()).get(
                test.getReference()).attributes().getPermission());
        feature.setUnixPermission(test,
                new Permission(Permission.Action.all, Permission.Action.read, Permission.Action.read,
                        false, false, true));
        assertEquals(new Permission(Permission.Action.all, Permission.Action.read, Permission.Action.read,
                false, false, true), new SFTPListService(session).list(test.getParent(), new DisabledListProgressListener()).get(
                test.getReference()).attributes().getPermission());
        new SFTPDeleteFeature(session).delete(Collections.<Path>singletonList(test), new DisabledLoginController());
        session.close();
    }
}
