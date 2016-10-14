package org.fst.backup.misc

import static org.junit.Assert.*

class NewRequirements extends GroovyTestCase {

	void probeFileType() {
		//		Files.probeContentType(null)
	}

	void inspectBackupWithParallelThreads() {
		// Process cmd line by line?
	}

	void testCmdIsLogged() {
	}

	void testConsoleHasLimitedSize() {
	}

	void testFileNamesAreNotEditable() {
		fail()
	}

	void abortRDiffCommands() {
	}

	void checkRDiffSystem() {
	}

	public void testWhatHappensinUIIfAnyExceptionIsRaised() {
		fail("Not yet implemented")
	}

	void testBackupIsVerified() {
		fail()
	}

	public void testUserDir() {
		println System.getProperty("user.dir")
	}

	public void testDisplayRunningProcessOnShutdown() {
		fail()
	}

	public void testRemoteFile() {
		fail("Not yet implemented")
	}

	public void testConsoleShowsOutputAndErrors() {
		fail()
	}
}
