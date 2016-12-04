package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.CreateIncrementService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest
import org.fst.backup.test.TestCallback

class CreateIncrementServiceTest extends AbstractTest {

	CreateIncrementService service = new CreateIncrementService()
	MockFor rdiffCommands
	CommandLineCallback expectedOutputCallback
	CommandLineCallback expectedErrorCallback

	void setUp() {
		super.setUp()
		rdiffCommands = mockRDiffCommands()
	}

	void testNotExisitingDirectoriesAreDenied() {
		sourceDir = new File(tmpPath + 'NE1/')
		targetDir = new File(tmpPath + 'NE2/')
		shouldFail(DirectoryNotExistsException) {
			service.createIncrement(sourceDir, targetDir)
		}
	}

	void testNonDirectoryFilesAreDenied() {
		File file1 = new File (tmpPath, 'File1.txt') << 'Content'
		File file2 = new File (tmpPath, 'File2.txt') << 'Content'

		shouldFail(FileIsNotADirectoryException) {
			service.createIncrement(file1, file2)
		}
		shouldFail(FileIsNotADirectoryException) {
			service.createIncrement(sourceDir, file2)
		}
		shouldFail(FileIsNotADirectoryException) {
			service.createIncrement(file1, targetDir)
		}
	}

	void testBackupCommandIsExecutedWithCorrectDirs() {
		rdiffCommands.use {
			def CreateIncrementService service = new CreateIncrementService()
			service.createIncrement(sourceDir, targetDir)
		}
	}

	void testCallbacksAreForwarded() {
		expectedOutputCallback = new TestCallback()
		expectedErrorCallback = new TestCallback()
		rdiffCommands.use {
			def CreateIncrementService service = new CreateIncrementService()
			service.createIncrement(sourceDir, targetDir, expectedOutputCallback, expectedErrorCallback)
		}
	}

	private MockFor mockRDiffCommands() {
		MockFor rdiffCommands = new MockFor(RDiffCommands.class)
		rdiffCommands.demand.backup(1) {File f1, File f2, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert sourceDir == f1
			assert targetDir == f2
			assert expectedOutputCallback == outputCallback
			assert expectedErrorCallback == errorCallback
			return null
		}

		return rdiffCommands
	}
}
