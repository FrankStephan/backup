package org.fst.backup.test.unit.service

import static org.junit.Assert.*

import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService
import org.fst.backup.test.AbstractTest

class ReadCliServiceTest extends AbstractTest {

	String logFileBasePath
	File logFileBaseDir
	Configuration configuration

	@Override
	public void setUp() {
		super.setUp()
		logFileBasePath = tmpPath + 'logs'
		logFileBaseDir = new File(logFileBasePath)
		logFileBaseDir.mkdir()
	}

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

	void testLogFileBaseDirNotExists() {
		logFileBasePath = aNotExistingPath()
		prepareAndInvokeService()
		assert null == configuration.logFileBaseDir
	}

	void testLogFileBaseDirIsNotADirectory() {
		logFileBaseDir = createSomeFile()
		logFileBasePath = logFileBaseDir.getPath()
		prepareAndInvokeService()
		assert null == configuration.logFileBaseDir
	}

	void testConfigurationTakesVariablesFromArgs() {
		prepareAndInvokeService()
		assert sourceDir == configuration.defaultSourceDir
		assert targetDir == configuration.defaultTargetDir
		assert logFileBaseDir == configuration.logFileBaseDir
	}

	void testCanHandleBackslashes() {
		sourceDir = new File(tmpPath + 'sdir/subDir')
		sourceDir.mkdirs()
		sourcePath = tmpPath + 'sdir\\subdir\\'
		targetDir = new File(tmpPath + 'tdir/subDir')
		targetDir.mkdirs()
		targetPath = tmpPath + 'tdir\\subdir\\'
		logFileBaseDir = new File(tmpPath + 'ldir/logs')
		logFileBaseDir.mkdirs()
		logFileBasePath = tmpPath + 'ldir\\logs'

		prepareAndInvokeService()
		assert sourceDir == configuration.defaultSourceDir
		assert targetDir == configuration.defaultTargetDir
		assert logFileBaseDir == configuration.logFileBaseDir
	}

	void testHandleEmptyDirs() {
		sourcePath = ''
		targetPath = ''
		logFileBasePath = ''

		prepareAndInvokeService()
		assert null == configuration.defaultSourceDir
		assert null == configuration.defaultTargetDir
		assert null == configuration.logFileBaseDir
	}

	private String[] buildArgs() {
		return ['-s', sourcePath, '-t', targetPath, '-l', logFileBasePath]
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
