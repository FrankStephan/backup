package org.fst.backup.service

import static org.junit.Assert.*

import org.fst.backup.rdiff.test.AbstractFileSystemTest
import org.junit.Test

class InspectBackupServiceTest extends AbstractFileSystemTest {

	InspectBackupService service = new InspectBackupService()

	File root = new File(tmpPath + 'root')

	void setUp() {
		super.setUp()
		root.mkdir()
	}

	void tearDown() {
		super.tearDown()
		root.deleteDir()
	}

	@Test
	public void testTargetDirIsIgnored() {


		fail("Not yet implemented")
	}
}
