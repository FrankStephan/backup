package org.fst.backup.test.unit_test.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.VerificationService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest

class VerificationServiceTest extends AbstractTest {

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

	VerificationService verificationService = new VerificationService()
	MockFor rdiffCommands

	void setUp() {
		super.setUp()
		rdiffCommands = mockRDiffCommands()
	}

	void testNotExisitingTargetDirIsDenied() {
		targetDir = new File(tmpPath + 'NE2/')
		targetPath = targetDir.absolutePath
		createIncrement()
		shouldFail (DirectoryNotExistsException) { verificationService.verifyIncrement(increment) }
	}

	void testNonDirectoryTargetFileIsDenied() {
		targetDir = new File (tmpPath, 'File1.txt') << 'Content'
		targetPath = targetDir.absolutePath
		createIncrement()
		shouldFail (FileIsNotADirectoryException) { verificationService.verifyIncrement(increment) }
	}

	void testReturnFalseIfRDiffFails() {
		mockRDiffCommands('', '', 1)
		createIncrement()
		assert false == verificationService.verifyIncrement(increment)
	}


	private MockFor mockRDiffCommands(String cmdLineContent='', String errLineContent='', int exitValue=0) {
		MockFor rdiffCommands = new MockFor(RDiffCommands.class)
		MockFor process = new MockFor(Process.class)
		process.demand.waitForProcessOutput(1) {OutputStream out, OutputStream err ->
			out.write(cmdLineContent.getBytes())
			err.write(cmdLineContent.getBytes())
		}
		process.demand.exitValue(1) { return exitValue }

		rdiffCommands.demand.verify(1) { Increment _increment ->
			assert increment == _increment
			return process.proxyInstance()
		}

		//		ByteArrayInputStream is = new ByteArrayInputStream((cmdLineContent ?: '').getBytes())
		//		process.demand.getInputStream(1) { return is }
		//
		//
		//		rdiffCommands.demand.backup(1) {File f1, File f2 ->
		//			assert sourceDir == f1
		//			assert targetDir == f2
		//			return process.proxyInstance()
		//		}
		return rdiffCommands
	}
}
