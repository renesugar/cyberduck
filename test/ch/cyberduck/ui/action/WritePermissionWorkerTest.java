package ch.cyberduck.ui.action;

/*
 * Copyright (c) 2012 David Kocher. All rights reserved.
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
 * Bug fixes, suggestions and comments should be sent to:
 * dkocher@cyberduck.ch
 */

import ch.cyberduck.core.AbstractTestCase;
import ch.cyberduck.core.AttributedList;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.NullPath;
import ch.cyberduck.core.NullSession;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.Permission;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.features.UnixPermission;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @version $Id$
 */
public class WritePermissionWorkerTest extends AbstractTestCase {

    @Test
    public void testRun() throws Exception {
        final Permission permission = new Permission(744);
        final NullPath path = new NullPath("a", Path.DIRECTORY_TYPE) {
            @Override
            public AttributedList<Path> list() {
                final AttributedList<Path> children = new AttributedList<Path>();
                children.add(new NullPath("b", Path.FILE_TYPE));
                return children;
            }
        };
        final NullSession h = new NullSession(new Host("h")) {
            @Override
            public <T> T getFeature(final Class<T> type) {
                return (T) new UnixPermission() {
                    @Override
                    public void setUnixOwner(final Path file, final String owner) throws BackgroundException {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void setUnixGroup(final Path file, final String group) throws BackgroundException {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void setUnixPermission(final Path file, final Permission permission) throws BackgroundException {
                        if(file.getName().equals("a")) {
                            assertEquals(permission, permission);
                        }
                        else if(file.getName().equals("b")) {
                            assertEquals(new Permission(644), permission);
                        }
                        else {
                            fail();
                        }
                    }
                };
            }
        };
        final WritePermissionWorker worker = new WritePermissionWorker(h, Arrays.<Path>asList(path), permission, true) {
            @Override
            public void cleanup(Permission result) {
                throw new UnsupportedOperationException();
            }
        };
        assertEquals(permission, worker.run());
    }
}
