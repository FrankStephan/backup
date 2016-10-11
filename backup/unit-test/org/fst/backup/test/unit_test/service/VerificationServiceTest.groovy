package org.fst.backup.test.unit_test.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.VerificationService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest

class VerificationServiceTest extends AbstractTest {

	VerificationService verificationService
	MockFor rdiffCommands
	Appendable cmdOut = new MockFor(Appendable.class).proxyInstance()
	Appendable cmdErr = new MockFor(Appendable.class).proxyInstance()

	void setUp() {
		super.setUp()
		rdiffCommands = mockRDiffCommands()
	}

	void testNotExisitingTargetDirIsDenied() {
		targetDir = new File(tmpPath + 'NE2/')
		targetPath = targetDir.absolutePath
		createIncrement()
		shouldFail (DirectoryNotExistsException) { invokeService() }
	}

	void testNonDirectoryTargetFileIsDenied() {
		targetDir = new File (tmpPath, 'File1.txt') << 'Content'
		targetPath = targetDir.absolutePath
		createIncrement()
		shouldFail (FileIsNotADirectoryException) { invokeService() }
	}

	void testOutAndErrAreWrittenToAppenables() {
		mockRDiffCommands(0)
		createIncrement()

		mockRDiffCommands(1)
		createIncrement()
	}

	void testReturnFalseIfRDiffFails() {
		mockRDiffCommands(1)
		createIncrement()
		assert false == invokeService()
	}

	void testReturnTrueIfRDiffSucceeds() {
		mockRDiffCommands(0)
		createIncrement()
		assert true == invokeService()
	}

	private boolean invokeService() {
		boolean retVal
		rdiffCommands.use {
			verificationService = new VerificationService()
			retVal = verificationService.verifyIncrement(increment, cmdOut, cmdErr)
		}
		return retVal
	}

	private MockFor mockRDiffCommands(int exitValue=0) {
		rdiffCommands = new MockFor(RDiffCommands.class)
		MockFor process = new MockFor(Process.class)
		process.demand.waitForProcessOutput(1) {Appendable cmdOut, Appendable cmdErr ->
			assert this.cmdOut == cmdOut
			assert this.cmdErr == cmdErr
		}
		process.demand.exitValue(1) { return exitValue }

		rdiffCommands.demand.verify(1) { File _targetDir, long secondsSinceTheEpoch ->
			assert new File(increment.targetPath) == _targetDir
			assert increment.secondsSinceTheEpoch == secondsSinceTheEpoch
			return process.proxyInstance()
		}
		return rdiffCommands
	}
}
