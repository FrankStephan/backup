package org.fst.backup.test.integration.logging

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.lookup.MainMapLookup
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class LoggerTest {

	static String realLog4jConfigurationFile

	static String path = LoggerTest.class.getSimpleName() + '/'
	static String logFileBaseDirProperty = 'logFileBaseDir'

	@BeforeClass
	static void beforeClass() {
		LogManager.shutdown()
		realLog4jConfigurationFile = System.setProperty('log4j.configurationFile', 'src/main/resources/log4j2.xml')
		MainMapLookup.setMainArguments(logFileBaseDirProperty, path + 'logs')
	}

	@Test
	void testCreatesLogFileInTheSpecifiedDir() {
		LogManager.getLogger(this.getClass()).info('I\'m working')
		assert baseDirContainsLogFile()
	}

	@AfterClass
	public static void afterClass() {
		System.setProperty('log4j.configurationFile', realLog4jConfigurationFile)
		LoggerContext context = LoggerContext.getContext(false)
		context.stop(5000, TimeUnit.MILLISECONDS)
		assert new File(path).deleteDir()
	}

	private boolean baseDirContainsLogFile() {
		Path logFileBaseDir = Paths.get(path + 'logs')
		def logFiles = Files.list(logFileBaseDir)
		Path expectedLog = logFileBaseDir.resolve('app.log')
		boolean result = logFiles.toArray().contains(expectedLog)
		logFiles.close()
		return result
	}
}
