package org.fst.backup.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractFileSystemTest
import org.fst.backup.test.FileHelper

class CreateBackupServiceTest extends AbstractFileSystemTest {

	CreateBackupService service = new CreateBackupService()
	File sourceDir
	File targetDir
	MockFor rdiffCommands

	void setUp() {
		super.setUp()
		sourceDir = new File(sourePath)
		targetDir = new File(targetPath)
		sourceDir.mkdirs()
		targetDir.mkdir()
		rdiffCommands = mockRDiffCommands()
	}

	void testNotExisitingDirectoriesAreDenied() {
		sourceDir = new File(tmpPath + 'NE1/')
		targetDir = new File(tmpPath + 'NE2/')
		shouldFail(DirectoryNotExistsException) {
			service.createBackup(sourceDir, targetDir, {})
		}
	}

	void testNonDirectoryFilesAreDenied() {
		def fileHelper = new FileHelper()
		File file1 = fileHelper.createFile(tmpPath, 'File1.txt')
		File file2 = fileHelper.createFile(tmpPath, 'File2.txt')

		shouldFail(FileIsNotADirectoryException) {
			service.createBackup(file1, file2, {})
		}
		shouldFail(FileIsNotADirectoryException) {
			service.createBackup(sourceDir, file2, {})
		}
		shouldFail(FileIsNotADirectoryException) {
			service.createBackup(file1, targetDir, {})
		}
	}

	void testBackupCommandIsExecutedWithCorrectDirs() {
		rdiffCommands.use {
			def CreateBackupService service = new CreateBackupService()
			service.createBackup(sourceDir, targetDir, {})
		}
	}

	void testCallbackGetsInvokedPerLineFromCmd1() {
		rdiffCommands = mockRDiffCommands('Line1')
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		rdiffCommands.use {
			def CreateBackupService service = new CreateBackupService()
			service.createBackup(sourceDir, targetDir, callback)
			assert numberOfInvocations == 1
		}
	}

	void testCallbackGetsInvokedPerLineFromCmd2() {
		rdiffCommands = mockRDiffCommands('Line1' + System.lineSeparator() + 'Line2')
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		rdiffCommands.use {
			def CreateBackupService service = new CreateBackupService()
			service.createBackup(sourceDir, targetDir, callback)
			assert numberOfInvocations == 2
		}
	}

	private MockFor mockRDiffCommands(String cmdLineContent) {
		MockFor rdiffCommands = new MockFor(RDiffCommands.class)
		MockFor process = new MockFor(Process.class)
		ByteArrayInputStream is = new ByteArrayInputStream((cmdLineContent ?: '').getBytes())
		process.demand.getInputStream(1) { return is }
		rdiffCommands.demand.backup(1) {File f1, File f2 ->
			assert sourceDir == f1
			assert targetDir == f2
			return process.proxyInstance()
		}
		return rdiffCommands
	}
}
