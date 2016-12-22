package org.fst.backup.test.unit.rdiff

import static org.junit.Assert.*

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.Appender
import org.apache.logging.log4j.core.Filter
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.WriterAppender
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.core.layout.PatternLayout
import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommandExecutor
import org.fst.backup.test.TestCallback
import org.junit.Before
import org.junit.Test

class RDiffCommandsExecutorTest {

	private static final String CMD_C_DIR = 'cmd /c dir'
	private static final String CMD_C_SILLY_COMMAND = 'cmd /c sillyCommand'

	static StringWriter outputLogger = captureProcessOutput()
	static StringWriter errorLogger = captureProcessError()

	CommandLineCallback outputCallback
	CommandLineCallback errorCallback
	ProcessStatus processStatus = null

	@Before
	void setUp() {
		clearLogs()
		outputCallback = new TestCallback()
		errorCallback = new TestCallback()
	}

	@Test
	void testMethodReturnsAfterProcessIsFinished() {
		assert null == processStatus
		callDirCommand()
		assert null != processStatus
	}

	@Test
	void testSuccessfulCommandInvokesOutputCallback() {
		callDirCommand()
		assert ProcessStatus.SUCCESS == processStatus
		assert errorCallback.toString().isEmpty()
		assert !outputCallback.toString().isEmpty()
	}

	@Test
	void testFailingCommandInvokesErrorCallback() {
		callUnknownCommand()
		assert ProcessStatus.FAILURE == processStatus
		assert !errorCallback.toString().isEmpty()
		assert outputCallback.toString().isEmpty()
	}

	@Test
	void testSuccessfulCommandIsLoggedOnInfoLevel() {
		callDirCommand()
		assert outputLogger.buffer.length() > 0
		assert outputLogger.toString().readLines().every { String it ->
			it.contains('INFO')
		}
	}

	@Test
	void testFailingCommandIsLoggedOnErrorLevel() {
		callUnknownCommand()
		assert errorLogger.buffer.length() > 0
		assert errorLogger.toString().readLines().every { String it ->
			it.contains('ERROR')
		}
	}

	@Test
	void testLogsStartAndEndOfProcess() {
		callDirCommand()
		List<String> logged = extractLogEntries(outputLogger)
		assert ('>> Executing: ' + CMD_C_DIR) == logged[0].trim()
		assert ('>> Finished with exitValue 0: ' + CMD_C_DIR) == logged[logged.size() - 1].trim()
	}

	@Test
	void testInfoLogEqualsOutputCallback() {
		callDirCommand()
		List<String> logged = extractLogEntries(outputLogger)
		List<String> callback = outputCallback.toString().readLines()
		assert trimStartAndEndOfProcess(logged) == callback
	}

	private List<String> trimStartAndEndOfProcess(List<String> logged) {
		return logged[1..(logged.size() - 2)]
	}

	@Test
	void testErrorLogEqualsErrorCallback() {
		callUnknownCommand()
		List<String> logged = extractLogEntries(errorLogger)
		List<String> callback = errorCallback.toString().readLines()
		assert logged == callback
	}

	@Test
	void testAcceptsNullForOutputCallback() {
		outputCallback = null
		callDirCommand()
		assert outputLogger.buffer.length() > 0
	}

	@Test
	void testAcceptsNullForErrWriter() {
		errorCallback = null
		callUnknownCommand()
		assert errorLogger.buffer.length() > 0
	}

	private void callCommand(String command) {
		processStatus = new RDiffCommandExecutor().execute(command, outputCallback, errorCallback)
	}

	private void callDirCommand() {
		callCommand(CMD_C_DIR)
	}

	private void callUnknownCommand() {
		callCommand(CMD_C_SILLY_COMMAND)
	}

	private static List<String> extractLogEntries(StringWriter logger) {
		return logger.toString().readLines().collect { String it ->
			extractLogEntry(it)
		}
	}

	private static String extractLogEntry(String logEntry) {
		return logEntry.substring(logEntry.indexOf(' - ') + 3, logEntry.length())
	}

	private static StringWriter captureProcessOutput() {
		StringWriter outputWriter = new StringWriter()
		addAppender(outputWriter, 'outputWriter', Level.INFO)
		return outputWriter
	}

	private static StringWriter captureProcessError() {
		StringWriter errorWriter = new StringWriter()
		addAppender(errorWriter, 'errorWriter', Level.ERROR)
		return errorWriter
	}

	private static void addAppender(final Writer writer, final String writerName, Level level) {
		LoggerContext context = LoggerContext.getContext(false)
		Configuration config = context.getConfiguration()
		PatternLayout layout = config.getAppender('RDiffCommandExecutor').getLayout()
		assert null != layout
		Appender appender = WriterAppender.createAppender(layout, null, writer, writerName, false, true)
		appender.start()
		config.addAppender(appender)
		updateLoggers(appender, config, level)
	}

	private static void updateLoggers(final Appender appender, final Configuration config, Level level) {
		Filter filter = null
		for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
			loggerConfig.addAppender(appender, level, filter)
		}
		config.getRootLogger().addAppender(appender, level, filter)
	}

	private void clearLogs() {
		outputLogger.buffer.setLength(0)
		errorLogger.buffer.setLength(0)
	}
}
