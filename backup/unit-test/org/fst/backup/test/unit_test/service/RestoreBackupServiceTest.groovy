package org.fst.backup.test.unit_test.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.RestoreBackupService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest

class RestoreBackupServiceTest extends AbstractTest {

	MockFor rdiffCommands

	void setUp() {
		super.setUp()
		rdiffCommands = mockRDiffCommands()
		createIncrement()
	}

	void testNotExistingTargetDir() {
		increment.targetPath = tmpPath + 'NotExisting/'
		RestoreBackupService service = new RestoreBackupService()
		shouldFail (DirectoryNotExistsException) {new RestoreBackupService().restore(increment, new File(tmpPath + 'restoreDir/'), {})}
	}

	void testTargetIsNotADirectory() {
		targetDir = new File(tmpPath, 'File.txt') << 'I am a real file'
		increment.targetPath = targetDir.absolutePath
		RestoreBackupService service = new RestoreBackupService()
		shouldFail (FileIsNotADirectoryException) {new RestoreBackupService().restore(increment, restoreDir, {})}
	}

	void testNotExisitingRestoreDir() {
		RestoreBackupService service = new RestoreBackupService()
		restoreDir = new File(tmpPath + 'NotExisting/')
		shouldFail (DirectoryNotExistsException) {new RestoreBackupService().restore(increment, restoreDir, {})}
	}

	void testRestoreDirIsNotADirectory() {
		RestoreBackupService service = new RestoreBackupService()
		restoreDir = new File(tmpPath + 'aFile.txt/')
		restoreDir.createNewFile()
		shouldFail (FileIsNotADirectoryException) {new RestoreBackupService().restore(increment, restoreDir, {})}
	}

	void testRestoreCommandIsExecutedWithCorrectParams1() {
		rdiffCommands.use {
			new RestoreBackupService().restore(increment, restoreDir, {})
		}
	}

	void testCallbackGetsInvokedPerLineFromCmd1() {
		rdiffCommands = mockRDiffCommands('Line1')
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		rdiffCommands.use {
			new RestoreBackupService().restore(increment, restoreDir, callback)
			assert numberOfInvocations == 1
		}
	}

	void testCallbackGetsInvokedPerLineFromCmd2() {
		rdiffCommands = mockRDiffCommands('Line1' + System.lineSeparator() + 'Line2')
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		rdiffCommands.use {
			new RestoreBackupService().restore(increment, restoreDir, callback)
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
