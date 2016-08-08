package org.fst.backup.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NoBackupDirectoryException
import org.fst.backup.test.AbstractFileSystemTest

class ListIncrementsServiceTest extends AbstractFileSystemTest {

	File targetDir
	MockFor rdiffCommands

	ListIncrementsService service = new ListIncrementsService()

	void setUp() {
		super.setUp()
		targetDir = new File(targetPath)
		targetDir.mkdirs()
		rdiffCommands = mockRDiffCommands(0, '')
	}

	void testListIncrementsWithNotExistingDirectory() {
		File notExistingDir = new File(tmpPath + 'NotExisting/')
		shouldFail(DirectoryNotExistsException, { service.listIncrements(notExistingDir) })
	}

	void testListIncrementsWithNonDirectoryFile() {
		File file = new File(tmpPath, 'File.txt')
		file.createNewFile()
		shouldFail(FileIsNotADirectoryException, { service.listIncrements(file) })
	}

	void testListIncrementsWithoutIncrements() {
		rdiffCommands = mockRDiffCommands(1, '')
		rdiffCommands.use {
			ListIncrementsService service = new ListIncrementsService()
			shouldFail(NoBackupDirectoryException, { service.listIncrements(targetDir) })
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
		rdiffCommands.demand.listIncrements(1) {String targetDir ->
			assert this.targetDir.absolutePath == targetDir
			return process.proxyInstance()
		}

		return rdiffCommands
	}
}
