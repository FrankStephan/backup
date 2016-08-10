package org.fst.backup.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NotABackupDirectoryException
import org.fst.backup.test.AbstractFileSystemTest
import org.fst.backup.test.RDiffBackupHelper

class ListPathsFromIncrementServiceTest extends AbstractFileSystemTest {

	RDiffBackupHelper helper =  new RDiffBackupHelper()
	Increment increment

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
		ListPathsFromIncrementService service = new ListPathsFromIncrementService()
		shouldFail (NotABackupDirectoryException) {service.listPathsFromIncrement(increment)}
	}

	void testListFiles() {
		createIncrement()
		def rdiffCommands = new MockFor(RDiffCommands.class)
		MockFor process = new MockFor(Process.class)
		process.demand.getText(1) { return '.' + System.lineSeparator() + 'a0/a1/a2.suf' }
		process.demand.exitValue(1) { return 0 }

		rdiffCommands.demand.listFiles(1) { String targetPath, def when ->
			assert increment.targetPath == targetPath
			assert increment.secondsSinceTheEpoch == when
			return process.proxyInstance()
		}

		rdiffCommands.use {
			ListPathsFromIncrementService service = new ListPathsFromIncrementService()
			assert ['.', 'a0/a1/a2.suf']== service.listPathsFromIncrement(increment)
		}
	}

	private void createIncrement() {
		increment = new Increment()
				.setSecondsSinceTheEpoch(nowAsSecondsSinceTheEpoch())
				.setTargetPath(targetPath)
	}

	private long nowAsSecondsSinceTheEpoch() {
		Calendar c = Calendar.getInstance()
		c.set(Calendar.MILLISECOND, 0)
		return c.timeInMillis / 1000
	}
}
