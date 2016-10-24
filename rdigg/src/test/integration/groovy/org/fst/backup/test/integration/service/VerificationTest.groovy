package org.fst.backup.test.integration.service

import static org.junit.Assert.*

import org.junit.Test

class VerificationTest {

	private static final String ERR_LINE_CONTENT = '''Warning: Could not restore file File1.txt!

A regular file was indicated by the metadata, but could not be
constructed from existing increments because last increment had type
None.  Instead of the actual file's data, an empty length file will be
created.  This error is probably caused by data loss in the
rdiff-backup destination directory, or a bug in rdiff-backup
Warning: Computed SHA1 digest of File1.txt
   da39a3ee5e6b4b0d3255bfef95601890afd80709
doesn't match recorded digest of
   6f6e6976c853511af4b14c78dab07558c7abf5c4
Your backup repository may be corrupted!'''

	private static final String CMD_LINE_CONTENT = 'Every file verified successfully.'

	@Test
	public void test() {
		fail("Not yet implemented")
	}
}
