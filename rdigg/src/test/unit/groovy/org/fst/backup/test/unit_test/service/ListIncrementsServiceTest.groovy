package org.fst.backup.test.unit_test.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.ListIncrementsService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NotABackupDirectoryException
import org.fst.backup.test.AbstractTest;

class ListIncrementsServiceTest extends AbstractTest {

	MockFor rdiffCommands

	void setUp() {
		super.setUp()
		rdiffCommands = mockRDiffCommands(0, '')
	}

	void testListIncrementsWithNotExistingDirectory() {
		File notExistingDir = new File(tmpPath + 'NotExisting/')
		ListIncrementsService service = new ListIncrementsService()
		shouldFail(DirectoryNotExistsException, { service.listIncrements(notExistingDir) })
	}

	void testListIncrementsWithNonDirectoryFile() {
		File file = new File(tmpPath, 'File.txt')
		file.createNewFile()
		ListIncrementsService service = new ListIncrementsService()
		shouldFail(FileIsNotADirectoryException, { service.listIncrements(file) })
	}

	void testListIncrementsWithoutIncrements() {
		rdiffCommands = mockRDiffCommands(1, '')
		rdiffCommands.use {
			ListIncrementsService service = new ListIncrementsService()
			shouldFail(NotABackupDirectoryException, { service.listIncrements(targetDir) })
		}
	}

	void testListIncrementsWithTwoIncrements() {
		rdiffCommands = mockRDiffCommands(0, '1467750198 directory' + System.lineSeparator() + '1467750199 directory')
		rdiffCommands.use {
			ListIncrementsService service = new ListIncrementsService()
			def increments = service.listIncrements(targetDir)
			assert [1467750198, 1467750199]== increments*.secondsSinceTheEpoch
			assert increments.every { targetDir.absolutePath == it.targetPath }
		}
	}

	private MockFor mockRDiffCommands(int exitValue, String cmdLineContent) {
		MockFor rdiffCommands = new MockFor(RDiffCommands.class)
		MockFor process = new MockFor(Process.class)

		process.demand.getText(1) { return cmdLineContent }
		process.demand.exitValue(1) { return exitValue }
		rdiffCommands.demand.listIncrements(1) {File targetDir ->
			assert this.targetDir == targetDir
			return process.proxyInstance()
		}

		return rdiffCommands
	}
}
