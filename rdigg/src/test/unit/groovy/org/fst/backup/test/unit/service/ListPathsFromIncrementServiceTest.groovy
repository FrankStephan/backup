package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.Increment
import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.ListPathsFromIncrementService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NotABackupDirectoryException
import org.fst.backup.test.AbstractTest

class ListPathsFromIncrementServiceTest extends AbstractTest {

	void testNotExistingTargetDir() {
		targetPath = tmpPath + 'NotExisting/'
		createIncrement()
		ListPathsFromIncrementService service = new ListPathsFromIncrementService()
		shouldFail (DirectoryNotExistsException) {service.listPathsFromIncrement(increment)}
	}

	void testTargetIsNotADirectory() {
		File file = new File(tmpPath, 'File.txt')
		file.createNewFile()
		targetPath = file.absolutePath
		createIncrement()
		ListPathsFromIncrementService service = new ListPathsFromIncrementService()
		shouldFail (FileIsNotADirectoryException) {service.listPathsFromIncrement(increment)}
	}

	void testTargetIsNoBackupDir() {
		createIncrement()
		def rdiffCommands = new MockFor(RDiffCommands.class)
		rdiffCommands.demand.listFiles(1) { File targetDir, def when, CommandLineCallback outputCallback ->
			return ProcessStatus.FAILURE
		}
		rdiffCommands.use {
			ListPathsFromIncrementService service = new ListPathsFromIncrementService()
			shouldFail (NotABackupDirectoryException) {service.listPathsFromIncrement(increment)}
		}
	}

	void testListFiles() {
		createIncrement()
		def rdiffCommands = new MockFor(RDiffCommands.class)
		rdiffCommands.demand.listFiles(1) { File targetDir, def when, CommandLineCallback outputCallback ->
			assert new File(increment.targetPath) == targetDir
			outputCallback.callback('.' + System.lineSeparator())
			outputCallback.callback('a0/a1/a2.suf')
			assert increment.secondsSinceTheEpoch == when
			return ProcessStatus.SUCCESS
		}

		rdiffCommands.use {
			ListPathsFromIncrementService service = new ListPathsFromIncrementService()
			assert ['.', 'a0/a1/a2.suf']== service.listPathsFromIncrement(increment)
		}
	}
}
