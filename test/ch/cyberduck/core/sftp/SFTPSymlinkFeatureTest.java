package ch.cyberduck.core.sftp;

import ch.cyberduck.core.AbstractPath;
import ch.cyberduck.core.AbstractTestCase;
import ch.cyberduck.core.Credentials;
import ch.cyberduck.core.DefaultHostKeyController;
import ch.cyberduck.core.DisabledCancelCallback;
import ch.cyberduck.core.DisabledListProgressListener;
import ch.cyberduck.core.DisabledLoginController;
import ch.cyberduck.core.DisabledPasswordStore;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.shared.DefaultHomeFinderService;

import org.junit.Test;

import java.util.Collections;
import java.util.EnumSet;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @version $Id$
 */
public class SFTPSymlinkFeatureTest extends AbstractTestCase {

    @Test
    public void testSymlink() throws Exception {
        final Host host = new Host(new SFTPProtocol(), "test.cyberduck.ch", new Credentials(
                properties.getProperty("sftp.user"), properties.getProperty("sftp.password")
        ));
        final SFTPSession session = new SFTPSession(host);
        session.open(new DefaultHostKeyController());
        session.login(new DisabledPasswordStore(), new DisabledLoginController(), new DisabledCancelCallback());
        final Path target = new Path(new DefaultHomeFinderService(session).find(), UUID.randomUUID().toString(), EnumSet.of(Path.Type.file));
        new SFTPTouchFeature(session).touch(target);
        final Path link = new Path(new DefaultHomeFinderService(session).find(), UUID.randomUUID().toString(), EnumSet.of(Path.Type.file, AbstractPath.Type.symboliclink));
        new SFTPSymlinkFeature(session).symlink(link, target.getName());
        assertTrue(new SFTPFindFeature(session).find(link));
        assertEquals(EnumSet.of(Path.Type.file, AbstractPath.Type.symboliclink),
                session.list(new DefaultHomeFinderService(session).find(), new DisabledListProgressListener()).get(link.getReference()).getType());
        new SFTPDeleteFeature(session).delete(Collections.singletonList(link), new DisabledLoginController());
        assertFalse(new SFTPFindFeature(session).find(link));
        assertTrue(new SFTPFindFeature(session).find(target));
        new SFTPDeleteFeature(session).delete(Collections.singletonList(target), new DisabledLoginController());
        session.close();
    }
}
