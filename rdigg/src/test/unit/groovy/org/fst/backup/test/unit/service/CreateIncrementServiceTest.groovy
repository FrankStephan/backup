package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.CreateIncrementService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest

class CreateIncrementServiceTest extends AbstractTest {

	CreateIncrementService service = new CreateIncrementService()
	MockFor rdiffCommands

	void setUp() {
		super.setUp()
		rdiffCommands = mockRDiffCommands()
	}

	void testNotExisitingDirectoriesAreDenied() {
		sourceDir = new File(tmpPath + 'NE1/')
		targetDir = new File(tmpPath + 'NE2/')
		shouldFail(DirectoryNotExistsException) {
			service.createIncrement(sourceDir, targetDir, {})
		}
	}

	void testNonDirectoryFilesAreDenied() {
		File file1 = new File (tmpPath, 'File1.txt') << 'Content'
		File file2 = new File (tmpPath, 'File2.txt') << 'Content'

		shouldFail(FileIsNotADirectoryException) {
			service.createIncrement(file1, file2, {})
		}
		shouldFail(FileIsNotADirectoryException) {
			service.createIncrement(sourceDir, file2, {})
		}
		shouldFail(FileIsNotADirectoryException) {
			service.createIncrement(file1, targetDir, {})
		}
	}

	void testBackupCommandIsExecutedWithCorrectDirs() {
		rdiffCommands.use {
			def CreateIncrementService service = new CreateIncrementService()
			service.createIncrement(sourceDir, targetDir, {})
		}
	}

	void testCallbackGetsInvokedPerLineFromCmd1() {
		rdiffCommands = mockRDiffCommands('Line1')
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		rdiffCommands.use {
			def CreateIncrementService service = new CreateIncrementService()
			service.createIncrement(sourceDir, targetDir, callback)
			assert numberOfInvocations == 1
		}
	}

	void testCallbackGetsInvokedPerLineFromCmd2() {
		rdiffCommands = mockRDiffCommands('Line1' + System.lineSeparator() + 'Line2')
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		rdiffCommands.use {
			def CreateIncrementService service = new CreateIncrementService()
			service.createIncrement(sourceDir, targetDir, callback)
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
