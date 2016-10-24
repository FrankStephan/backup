


package org.fst.backup.test.unit.service

import static org.junit.Assert.*

import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest

class ReadCliServiceTest extends AbstractTest {

	Configuration configuration

	void testSourceDirNotExists() {
		sourcePath = tmpPath + "NE/"
		shouldFail(DirectoryNotExistsException) {prepareAndInvokeService()}
	}

	void testSourceDirIsNotADirectory() {
		sourceDir = new File(tmpPath + "a.file")
		sourceDir.createNewFile()
		sourcePath = sourceDir.getPath()
		shouldFail(FileIsNotADirectoryException) {prepareAndInvokeService()}
	}

	void testTargetDirNotExists() {
		targetPath = tmpPath + "NE/"
		shouldFail(DirectoryNotExistsException) {prepareAndInvokeService()}
	}

	void testTargetDirIsNotADirectory() {
		targetDir = new File(tmpPath + "a.file")
		targetDir.createNewFile()
		targetPath = targetDir.getPath()
		shouldFail(FileIsNotADirectoryException) {prepareAndInvokeService()}
	}

	void testConfigurationContainsCorrectSourceAndTargetDir() {
		prepareAndInvokeService()
		assert configuration.defaultSourceDir == sourceDir
		assert configuration.defaultTargetDir == targetDir
	}

	private String[] buildArgs() {
		return ['-s', sourcePath, '-t', targetPath]
	}

	private void prepareAndInvokeService() {
		def args = buildArgs()
		configuration = new ReadCliService().read(args)
	}
}
