package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.RestoreIncrementService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest

class RestoreIncrementServiceTest extends AbstractTest {

	MockFor rdiffCommands

	void setUp() {
		super.setUp()
		rdiffCommands = mockRDiffCommands()
		createIncrement()
	}

	void testNotExistingTargetDir() {
		increment.targetPath = tmpPath + 'NotExisting/'
		RestoreIncrementService service = new RestoreIncrementService()
		shouldFail (DirectoryNotExistsException) {new RestoreIncrementService().restore(increment, new File(tmpPath + 'restoreDir/'), {})}
	}

	void testTargetIsNotADirectory() {
		targetDir = new File(tmpPath, 'File.txt') << 'I am a real file'
		increment.targetPath = targetDir.absolutePath
		RestoreIncrementService service = new RestoreIncrementService()
		shouldFail (FileIsNotADirectoryException) {new RestoreIncrementService().restore(increment, restoreDir, {})}
	}

	void testNotExisitingRestoreDir() {
		RestoreIncrementService service = new RestoreIncrementService()
		restoreDir = new File(tmpPath + 'NotExisting/')
		shouldFail (DirectoryNotExistsException) {new RestoreIncrementService().restore(increment, restoreDir, {})}
	}

	void testRestoreDirIsNotADirectory() {
		RestoreIncrementService service = new RestoreIncrementService()
		restoreDir = new File(tmpPath + 'aFile.txt/')
		restoreDir.createNewFile()
		shouldFail (FileIsNotADirectoryException) {new RestoreIncrementService().restore(increment, restoreDir, {})}
	}

	void testRestoreCommandIsExecutedWithCorrectParams1() {
		rdiffCommands.use {
			new RestoreIncrementService().restore(increment, restoreDir, {})
		}
	}

	void testCallbackGetsInvokedPerLineFromCmd1() {
		rdiffCommands = mockRDiffCommands('Line1')
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		rdiffCommands.use {
			new RestoreIncrementService().restore(increment, restoreDir, callback)
			assert numberOfInvocations == 1
		}
	}

	void testCallbackGetsInvokedPerLineFromCmd2() {
		rdiffCommands = mockRDiffCommands('Line1' + System.lineSeparator() + 'Line2')
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		rdiffCommands.use {
			new RestoreIncrementService().restore(increment, restoreDir, callback)
			assert numberOfInvocations == 2
		}
	}

	private MockFor mockRDiffCommands(String cmdLineContent) {
		MockFor rdiffCommands = new MockFor(RDiffCommands.class)
		MockFor process = new MockFor(Process.class)
		ByteArrayInputStream is = new ByteArrayInputStream((cmdLineContent ?: '').getBytes())
		process.demand.getInputStream(1) { return is }
		rdiffCommands.demand.restore(1) {File f1, File f2, def when ->
			assert restoreDir == f2
			assert targetDir == f1
			assert increment.secondsSinceTheEpoch == when
			return process.proxyInstance()
		}
		return rdiffCommands
	}
}
