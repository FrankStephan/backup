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

	static String path = LoggerTest.class.getSimpleName() + '/'
	static String logFileBaseDirProperty = 'logFileBaseDir'

	@BeforeClass
	static void beforeClass() {
		System.setProperty('log4j.configurationFile', 'src/main/resources/log4j2.xml')
		MainMapLookup.setMainArguments(logFileBaseDirProperty, path + 'logs')
	}

	@Test
	void testCreatesLogFileInTheSpecifiedDir() {
		LogManager.getLogger(this.getClass()).info('I\'m working')
		Path logFileBaseDir = Paths.get(path + 'logs')
		def logFiles = Files.list(logFileBaseDir)

		Path expectedLog = logFileBaseDir.resolve('app.log')
		boolean result = logFiles.toArray().contains(expectedLog)
		logFiles.close()

		assert result
	}

	@AfterClass
	public static void afterClass() {
		System.clearProperty('log4j.configurationFile')
		LoggerContext context = LoggerContext.getContext(false)
		context.stop(5000, TimeUnit.MILLISECONDS)
		new File(path).deleteDir()
	}
}
