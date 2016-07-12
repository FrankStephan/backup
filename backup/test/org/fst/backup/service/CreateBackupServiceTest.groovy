package org.fst.backup.service;

import static org.junit.Assert.*;

import org.fst.backup.rdiff.test.FileHelper;
import org.fst.backup.rdiff.test.RDiffBackupHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import groovy.util.GroovyTestCase;

class CreateBackupServiceTest extends GroovyTestCase {

	static final String TMP_DIR = 'CreateBackupServiceTest-tmp/'
	static final String SOURCE_DIR = 'CreateBackupServiceTest-tmp/source/'
	static final String TARGET_DIR = 'CreateBackupServiceTest-tmp/target/'
	
	def service = new CreateBackupService();
	
//	void testNotExisitingDirectoriesAreDenied() {
//		fail()
//	}

//	void testCallbackGetsInvokedPerLineFromCmd() {
//		// idea: use a concrete callback closure instead of a generic one
//	}

	void testNonDirectoryFilesAreDenied() {
		def fileHelper = new FileHelper();
		File file1 = fileHelper.createFile(SOURCE_DIR, 'File1.txt');
		File file2 = fileHelper.createFile(SOURCE_DIR, 'File2.txt');
		Closure callback = {};
		
		shouldFail(FileIsNotADirectoryException) { service.createBackup(file1, file2, callback) }
		shouldFail(FileIsNotADirectoryException) { service.createBackup(new File(SOURCE_DIR), file2, callback) }
		shouldFail(FileIsNotADirectoryException) { service.createBackup(file1, new File(TARGET_DIR), callback) }
	
		new File(TMP_DIR).deleteDir();
	}
	
	void testBackupCommandIsExecuted() {
		def backupHelper = new RDiffBackupHelper()
		backupHelper.createTwoIncrements(SOURCE_DIR, TARGET_DIR);
		def sb = new StringBuilder()
		Closure callback = { sb.append(it) }
		service.createBackup(new File(SOURCE_DIR), new File(TARGET_DIR), callback)
		String expectedLineInCmd ='Starting increment operation ' + new File(SOURCE_DIR).absolutePath + ' to ' + new File(TARGET_DIR).absolutePath
		println 'Expecting:' + expectedLineInCmd
		println sb.toString()
		
		assert sb.contains(expectedLineInCmd)		
		backupHelper.cleanUp(TMP_DIR);
	}

}
