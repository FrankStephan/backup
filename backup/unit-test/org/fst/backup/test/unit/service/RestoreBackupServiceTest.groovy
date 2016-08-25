package org.fst.backup.test.unit.service

import static org.junit.Assert.*

import org.fst.backup.model.Increment
import org.fst.backup.service.RestoreBackupService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest

class RestoreBackupServiceTest extends AbstractTest {

	RestoreBackupService service = new RestoreBackupService()

	void testNotExistingTargetDir() {
		targetPath = tmpPath + 'NotExisting/'
		createIncrement()
		RestoreBackupService service = new RestoreBackupService()
		shouldFail (DirectoryNotExistsException) {service.restore(increment, new File(tmpPath + 'restoreDir/'), {})}
	}

	void testTargetIsNotADirectory() {
		targetDir = new File(tmpPath, 'File.txt') << 'I am a real file'
		targetPath = targetDir.absolutePath
		createIncrement()
		def restoreDir = new File(tmpPath + 'restoreDir/')
		restoreDir.mkdir()
		RestoreBackupService service = new RestoreBackupService()
		shouldFail (FileIsNotADirectoryException) {service.restore(increment, restoreDir, {})}
	}

	void testNotExisitingRestoreDir() {
		createIncrement()
		RestoreBackupService service = new RestoreBackupService()
		def restoreDir = new File(tmpPath + 'restoreDir/')
		shouldFail (DirectoryNotExistsException) {service.restore(increment, restoreDir, {})}
	}

	void testRestoreDirIsNotADirectory() {
		createIncrement()
		RestoreBackupService service = new RestoreBackupService()
		def restoreDir = new File(tmpPath + 'restoreDir/')
		restoreDir.createNewFile()
		shouldFail (FileIsNotADirectoryException) {service.restore(increment, restoreDir, {})}
	}
}
