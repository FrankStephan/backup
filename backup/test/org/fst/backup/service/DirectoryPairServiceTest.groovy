package org.fst.backup.service;

import static org.junit.Assert.*

import org.fst.backup.rdiff.test.AbstractFileSystemTest
import org.fst.backup.rdiff.test.FileHelper

class DirectoryPairServiceTest extends AbstractFileSystemTest {

	def service = new DirectoryPairService(new File(new AppConfigurationService().directoryPairsFilePath))
	
	void tearDown() {
		super.tearDown()
		new File(new AppConfigurationService().directoryPairsFilePath).delete()
	}
	
	void testNotExisitingDirectoriesAreDenied() {
		def sourceDir = new File(tmpPath + 'NE1/')
		def targetDir = new File(tmpPath + 'NE2/')
		Closure callback = {};
		shouldFail(DirectoryNotExistsException) { this.service.addDirectoryPair(sourceDir, targetDir)  }
	}
	
	void testNonDirectoryFilesAreDenied() {
		def fileHelper = new FileHelper();
		File file1 = fileHelper.createFile(tmpPath, 'File1.txt');
		File file2 = fileHelper.createFile(tmpPath, 'File2.txt');
		File sourceDir = new File(sourePath)
		File targetDir = new File(targetPath)
		
		Closure callback = {};
		shouldFail(FileIsNotADirectoryException) { this.service.addDirectoryPair(file1, file2) }
		shouldFail(FileIsNotADirectoryException) { this.service.addDirectoryPair(sourceDir, file2) }
		shouldFail(FileIsNotADirectoryException) { this.service.addDirectoryPair(file1, targetDir) }
	}
	
	void testInitiallyEmptyListOfDirectoryPairs() {
		assert this.service.loadAllDirectoryPairs().empty
	}
	
	void testServiceAddsDirectoryPairs() {
		this.service.addDirectoryPair(new File(sourePath), new File(targetPath))
		def directoryPairs = this.service.loadAllDirectoryPairs()
		assert directoryPairs.size() == 1
		assert directoryPairs[0].sourceDir.absolutePath == sourePath
		assert directoryPairs[0].targetDir.absolutePath == sourePath
	}
	
}
