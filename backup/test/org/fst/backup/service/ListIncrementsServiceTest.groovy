package org.fst.backup.service;

import static org.junit.Assert.*

import org.fst.backup.rdiff.test.RDiffBackupHelper
import org.junit.After

class ListIncrementsServiceTest extends GroovyTestCase {

	static final String TMP_DIR = 'ListIncrementsServiceTest-tmp/'
	static final String SOURCE_DIR = 'ListIncrementsServiceTest-tmp/source/'
	static final String TARGET_DIR = 'ListIncrementsServiceTest-tmp/target/'
	
	def service = new ListIncrementsService()
	
	void testListIncrementsWithNotExistingDirectory() {
		File targetDir = new File(TARGET_DIR)
		shouldFail(DirectoryNotExistsException, { service.listIncrements(targetDir) })
	}
	
	void testListIncrementsWithNonDirectoryFile() {
		File tmpDir = new File(TMP_DIR)
		tmpDir.mkdirs()
		File file = new File(tmpDir, 'File.txt')
		file.createNewFile()
		
		shouldFail(FileIsNotADirectoryException, { service.listIncrements(file) })
	}
	
	void testListIncrementsWithoutIncrements() {
		File targetDir = new File(TMP_DIR)
		targetDir.mkdirs()
		File file = new File(targetDir, 'NotAnIncrementFile.txt')
		file.createNewFile()
		
		assert service.listIncrements(targetDir).isEmpty()
	}
	
	void testListIncrementsWithTwoIncrements() {
		RDiffBackupHelper helper = new RDiffBackupHelper()
		helper.createTwoIncrements(SOURCE_DIR, TARGET_DIR)
		
		assert 2 == service.listIncrements(new File(TARGET_DIR)).size()
	}
	
	void tearDown() {
		new RDiffBackupHelper().cleanUp(TMP_DIR)
	}
	

}
