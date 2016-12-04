package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.Increment
import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.RestoreIncrementService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest
import org.fst.backup.test.TestCallback

class RestoreIncrementServiceTest extends AbstractTest {

	ProcessStatus processStatus
	CommandLineCallback expectedOutputCallback
	CommandLineCallback expectedErrorCallback

	void setUp() {
		super.setUp()
		createIncrement()
	}

	void testNotExistingTargetDir() {
		increment.targetPath = tmpPath + 'NotExisting/'
		RestoreIncrementService service = new RestoreIncrementService()
		shouldFail (DirectoryNotExistsException) {new RestoreIncrementService().restore(increment, new File(tmpPath + 'restoreDir/'))}
	}

	void testTargetIsNotADirectory() {
		targetDir = new File(tmpPath, 'File.txt') << 'I am a real file'
		increment.targetPath = targetDir.absolutePath
		RestoreIncrementService service = new RestoreIncrementService()
		shouldFail (FileIsNotADirectoryException) {new RestoreIncrementService().restore(increment, restoreDir)}
	}

	void testNotExisitingRestoreDir() {
		RestoreIncrementService service = new RestoreIncrementService()
		restoreDir = new File(tmpPath + 'NotExisting/')
		shouldFail (DirectoryNotExistsException) {new RestoreIncrementService().restore(increment, restoreDir)}
	}

	void testRestoreDirIsNotADirectory() {
		RestoreIncrementService service = new RestoreIncrementService()
		restoreDir = new File(tmpPath + 'aFile.txt/')
		restoreDir.createNewFile()
		shouldFail (FileIsNotADirectoryException) {new RestoreIncrementService().restore(increment, restoreDir)}
	}

	void testRestoreCommandIsExecutedWithCorrectParams() {
		processStatus = ProcessStatus.SUCCESS
		expectedOutputCallback = new TestCallback()
		expectedErrorCallback = new TestCallback()
		MockFor rdiffCommands = mockRDiffCommands()
		rdiffCommands.use {
			new RestoreIncrementService().restore(increment, restoreDir, expectedOutputCallback, expectedErrorCallback)
		}
	}

	private MockFor mockRDiffCommands() {
		MockFor rdiffCommands = new MockFor(RDiffCommands.class)
		rdiffCommands.demand.restore(1) {File f1, File f2, def when, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert restoreDir == f2
			assert targetDir == f1
			assert increment.secondsSinceTheEpoch == when
			assert expectedOutputCallback == outputCallback
			assert expectedErrorCallback == errorCallback
			return processStatus
		}
		return rdiffCommands
	}
}
