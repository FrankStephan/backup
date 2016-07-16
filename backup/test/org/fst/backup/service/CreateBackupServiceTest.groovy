package org.fst.backup.service;

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.rdiff.test.AbstractFileSystemTest;
import org.fst.backup.rdiff.test.FileHelper
import org.fst.backup.rdiff.test.RDiffBackupHelper

class CreateBackupServiceTest extends AbstractFileSystemTest {

	private static final int NUMBER_OF_LINES_IN_CMD_FOR_BACKUP = 81

	def backupHelper = new RDiffBackupHelper()
	
	def service = new CreateBackupService();
	
	void testNotExisitingDirectoriesAreDenied() {
		def sourceDir = new File(tmpPath + 'NE1/')
		def targetDir = new File(tmpPath + 'NE2/')
		Closure callback = {};
		shouldFail(DirectoryNotExistsException) { service.createBackup(sourceDir, targetDir, {}) }
	}

	void testNonDirectoryFilesAreDenied() {
		def fileHelper = new FileHelper();
		File file1 = fileHelper.createFile(tmpPath, 'File1.txt');
		File file2 = fileHelper.createFile(tmpPath, 'File2.txt');
		File sourceDir = new File(sourePath)
		File targetDir = new File(targetPath)
		
		Closure callback = {};
		shouldFail(FileIsNotADirectoryException) { service.createBackup(file1, file2, callback) }
		shouldFail(FileIsNotADirectoryException) { service.createBackup(sourceDir, file2, callback) }
		shouldFail(FileIsNotADirectoryException) { service.createBackup(file1, targetDir, callback) }
	}
	
	void testBackupCommandIsExecuted() {
		backupHelper.createTwoIncrements(sourePath, targetPath);
		def sb = new StringBuilder()
		Closure callback = { sb.append(System.lineSeparator).append(it) }
		def sourceDir = new File(sourePath)
		def targetDir = new File(targetPath)
		service.createBackup(sourceDir, targetDir, callback)
		String expectedLineInCmd ='Starting increment operation ' + sourceDir.absolutePath + ' to ' + targetDir.absolutePath
		println 'Expecting:' + expectedLineInCmd
		println sb.toString()
		
		assert sb.contains(expectedLineInCmd)		
	}
	
	void testCallbackGetsInvokedPerLineFromCmd() {
		backupHelper.createTwoIncrements(sourePath, targetPath);
		int numberOfInvocations = 0
		Closure callback = { numberOfInvocations++ }
		def sourceDir = new File(sourePath)
		def targetDir = new File(targetPath)
		service.createBackup(sourceDir, targetDir, callback)
		String expectedLineInCmd ='Starting increment operation ' + sourceDir.absolutePath + ' to ' + targetDir.absolutePath
		println '# CMD lines: ' + numberOfInvocations
		
		assert numberOfInvocations == NUMBER_OF_LINES_IN_CMD_FOR_BACKUP
	}
}
