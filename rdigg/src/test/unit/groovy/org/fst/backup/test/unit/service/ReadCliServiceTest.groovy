


package org.fst.backup.test.unit.service

import static org.junit.Assert.*

import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService
import org.fst.backup.test.AbstractTest

class ReadCliServiceTest extends AbstractTest {

	Configuration configuration

	void testSourceDirNotExists() {
		sourcePath = tmpPath + 'NE/'
		prepareAndInvokeService()
		assert null == configuration.defaultSourceDir
	}

	void testSourceDirIsNotADirectory() {
		sourceDir = new File(tmpPath + 'a.file')
		sourceDir.createNewFile()
		sourcePath = sourceDir.getPath()
		prepareAndInvokeService()
		assert null == configuration.defaultSourceDir
	}

	void testTargetDirNotExists() {
		targetPath = tmpPath + 'NE/'
		prepareAndInvokeService()
		assert null == configuration.defaultTargetDir
	}

	void testTargetDirIsNotADirectory() {
		targetDir = new File(tmpPath + 'a.file')
		targetDir.createNewFile()
		targetPath = targetDir.getPath()
		prepareAndInvokeService()
		assert null == configuration.defaultTargetDir
	}

	void testConfigurationContainsCorrectSourceAndTargetDir() {
		prepareAndInvokeService()
		assert sourceDir == configuration.defaultSourceDir
		assert targetDir == configuration.defaultTargetDir
	}

	void testCanHandleBackslashes() {
		sourceDir = new File(tmpPath + 'sdir/subDir')
		sourceDir.mkdirs()
		sourcePath = tmpPath + 'sdir\\subdir\\'
		targetDir = new File(tmpPath + 'tdir/subDir')
		targetDir.mkdirs()
		targetPath = tmpPath + 'tdir\\subdir\\'

		prepareAndInvokeService()
		assert sourceDir == configuration.defaultSourceDir
		assert targetDir == configuration.defaultTargetDir
	}

	void testHandleEmptyDirs() {
		sourcePath = ''
		targetPath = ''

		prepareAndInvokeService()
		assert null == configuration.defaultSourceDir
		assert null == configuration.defaultTargetDir
	}

	private String[] buildArgs() {
		return ['-s', sourcePath, '-t', targetPath]
	}

	private void prepareAndInvokeService() {
		def args = buildArgs()
		configuration = new ReadCliService().read(args)
	}
}
