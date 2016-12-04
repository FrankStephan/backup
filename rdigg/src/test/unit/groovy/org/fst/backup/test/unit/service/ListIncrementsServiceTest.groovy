package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.ListIncrementsService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NotABackupDirectoryException
import org.fst.backup.test.AbstractTest
import org.fst.backup.test.TestCallback

class ListIncrementsServiceTest extends AbstractTest {

	MockFor rdiffCommands
	TestCallback ouputCallback = new TestCallback()
	ProcessStatus processStatus

	void setUp() {
		super.setUp()
		rdiffCommands = mockRDiffCommands('')
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
		processStatus = ProcessStatus.FAILURE
		rdiffCommands = mockRDiffCommands('')
		rdiffCommands.use {
			ListIncrementsService service = new ListIncrementsService()
			shouldFail(NotABackupDirectoryException, { service.listIncrements(targetDir) })
		}
	}

	void testListIncrementsWithTwoIncrements() {
		processStatus = ProcessStatus.SUCCESS
		rdiffCommands = mockRDiffCommands('1467750198 directory' + System.lineSeparator() + '1467750199 directory')
		rdiffCommands.use {
			ListIncrementsService service = new ListIncrementsService()
			def increments = service.listIncrements(targetDir)
			assert [1467750198, 1467750199]== increments*.secondsSinceTheEpoch
			assert increments.every { targetDir.absolutePath == it.targetPath }
		}
	}

	private MockFor mockRDiffCommands(String cmdLineData) {
		MockFor rdiffCommands = new MockFor(RDiffCommands.class)

		rdiffCommands.demand.listIncrements(1) {File targetDir, CommandLineCallback outputCallback ->
			assert this.targetDir == targetDir
			cmdLineData.eachLine {String it ->
				outputCallback.callback(it + System.lineSeparator())
			}
			return processStatus
		}

		return rdiffCommands
	}
}
