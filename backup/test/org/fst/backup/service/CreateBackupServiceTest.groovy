package org.fst.backup.service;

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.rdiff.test.FileHelper
import org.fst.backup.rdiff.test.RDiffBackupHelper

class CreateBackupServiceTest extends GroovyTestCase {

	static final String TMP_DIR = 'CreateBackupServiceTest-tmp/'
	static final String SOURCE_DIR = 'CreateBackupServiceTest-tmp/source/'
	static final String TARGET_DIR = 'CreateBackupServiceTest-tmp/target/'
	
	def backupHelper = new RDiffBackupHelper()
	
	def service = new CreateBackupService();
	
	void testNotExisitingDirectoriesAreDenied() {
		File sourceDir = new File(SOURCE_DIR)
		File targetDir = new File(TARGET_DIR)
		Closure callback = {};
		shouldFail(DirectoryNotExistsException) { service.createBackup(sourceDir, targetDir, {}) }
	}

	void testNonDirectoryFilesAreDenied() {
		def fileHelper = new FileHelper();
		File file1 = fileHelper.createFile(SOURCE_DIR, 'File1.txt');
		File file2 = fileHelper.createFile(SOURCE_DIR, 'File2.txt');
		File sourceDir = new File(SOURCE_DIR)
		File targetDir = new File(TARGET_DIR)
		sourceDir.mkdirs()
		targetDir.mkdirs()
		
		Closure callback = {};
		shouldFail(FileIsNotADirectoryException) { service.createBackup(file1, file2, callback) }
		shouldFail(FileIsNotADirectoryException) { service.createBackup(sourceDir, file2, callback) }
		shouldFail(FileIsNotADirectoryException) { service.createBackup(file1, targetDir, callback) }
	}
	
	void testBackupCommandIsExecuted() {
		backupHelper.createTwoIncrements(SOURCE_DIR, TARGET_DIR);
		def sb = new StringBuilder()
		Closure callback = { sb.append(it) }
		service.createBackup(new File(SOURCE_DIR), new File(TARGET_DIR), callback)
		String expectedLineInCmd ='Starting increment operation ' + new File(SOURCE_DIR).absolutePath + ' to ' + new File(TARGET_DIR).absolutePath
		println 'Expecting:' + expectedLineInCmd
		println sb.toString()
		
		assert sb.contains(expectedLineInCmd)		
	}
	
	void testCallbackGetsInvokedPerLineFromCmd() {
		backupHelper.createTwoIncrements(SOURCE_DIR, TARGET_DIR);
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		service.createBackup(new File(SOURCE_DIR), new File(TARGET_DIR), callback)
		String expectedLineInCmd ='Starting increment operation ' + new File(SOURCE_DIR).absolutePath + ' to ' + new File(TARGET_DIR).absolutePath
		println numberOfInvocations
		
		assert numberOfInvocations > 1
	}
	
	void tearDown() {
		backupHelper.cleanUp(TMP_DIR)
	}

}
