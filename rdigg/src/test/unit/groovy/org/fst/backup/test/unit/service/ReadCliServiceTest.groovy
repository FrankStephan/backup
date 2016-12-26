package org.fst.backup.test.unit.service

import static org.junit.Assert.*

import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService
import org.fst.backup.test.AbstractTest

class ReadCliServiceTest extends AbstractTest {

	Configuration configuration

	void testSourceDirNotExists() {
		sourcePath = aNotExistingPath()
		prepareAndInvokeService()
		assert null == configuration.defaultSourceDir
	}

	void testSourceDirIsNotADirectory() {
		sourceDir = createSomeFile()
		sourcePath = sourceDir.getPath()
		prepareAndInvokeService()
		assert null == configuration.defaultSourceDir
	}

	void testTargetDirNotExists() {
		targetPath = aNotExistingPath()
		prepareAndInvokeService()
		assert null == configuration.defaultTargetDir
	}

	void testTargetDirIsNotADirectory() {
		targetDir = createSomeFile()
		targetPath = targetDir.getPath()
		prepareAndInvokeService()
		assert null == configuration.defaultTargetDir
	}

	void testConfigurationTakesVariablesFromArgs() {
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

	private String aNotExistingPath() {
		return tmpPath + 'NE/'
	}

	private File createSomeFile() {
		File file = new File(tmpPath + 'a.file')
		assert file.createNewFile()
		return file
	}
}
