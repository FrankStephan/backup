package org.fst.backup.service;

import static org.junit.Assert.*

import org.fst.backup.rdiff.test.AbstractFileSystemTest
import org.fst.backup.rdiff.test.RDiffBackupHelper

class ListIncrementsServiceTest extends AbstractFileSystemTest {

	def service = new ListIncrementsService()
	
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
		File file = new File(targetPath, 'NotAnIncrementFile.txt')
		file.createNewFile()
		
		assert service.listIncrements(new File(targetPath)).isEmpty()
	}
	
	void testListIncrementsWithTwoIncrements() {
		RDiffBackupHelper helper = new RDiffBackupHelper()
		helper.createTwoIncrements(sourePath, targetPath)
		
		assert 2 == service.listIncrements(new File(targetPath)).size()
	}
}
