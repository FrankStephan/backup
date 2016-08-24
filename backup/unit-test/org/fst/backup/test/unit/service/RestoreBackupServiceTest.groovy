package org.fst.backup.test.unit.service

import static org.junit.Assert.*

import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest

class RestoreBackupServiceTest extends AbstractTest {




	void testNotExisitingDirectoriesAreDenied() {
		sourceDir = new File(tmpPath + 'NE1/')
		targetDir = new File(tmpPath + 'NE2/')
		shouldFail(DirectoryNotExistsException) {
			service.createBackup(sourceDir, targetDir, {})
		}
	}

	void testNonDirectoryFilesAreDenied() {
		File file1 = new File (tmpPath, 'File1.txt') << 'Content'
		File file2 = new File (tmpPath, 'File2.txt') << 'Content'

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
}
