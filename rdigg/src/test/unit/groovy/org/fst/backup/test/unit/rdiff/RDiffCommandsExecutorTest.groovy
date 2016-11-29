package org.fst.backup.test.unit.rdiff

import static org.junit.Assert.*

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.rdiff.RDiffCommandExecutor
import org.fst.backup.test.TestCallback

class RDiffCommandsExecutorTest extends GroovyTestCase {

	static PrintStream sysout = System.out
	static PrintStream syserr = System.err
	static ByteArrayOutputStream capturedSysout = new ByteArrayOutputStream()
	static ByteArrayOutputStream capturedSyserr = new ByteArrayOutputStream()

	static {
		System.setOut(new PrintStream(capturedSysout, true))
		System.setErr(new PrintStream(capturedSyserr, true))
	}

	CommandLineCallback outputCallback = new TestCallback()
	CommandLineCallback errorCallback = new TestCallback()
	Process process

	void setUp() {
		super.setUp()
		capturedSysout.reset()
		capturedSyserr.reset()
	}

	void testSuccessfulCommandOuputGetsWritten() {
		callDirCommandAndWaitForLogging()
		assertProcessOutputIsWritten()
	}

	void testFailingCommandErrGetsWritten() {
		callUnknownCommandAndWaitForLogging()
		assertProcessErrorIsWritten()
	}

	void testSystemOutEqualsProcessOutput() {
		callDirCommandAndWaitForLogging()

		List<String> expectedLines = capturedSysout.toString().readLines().collect {String it ->
			extractLogEntry(it)
		}
		List<String> actualLines = outputCallback.toString().readLines()

		assert expectedLines == actualLines
	}

	void testSystemErrEqualsProcessError() {
		callUnknownCommandAndWaitForLogging()

		List<String> expectedLines = capturedSyserr.toString().readLines().collect {String it ->
			extractLogEntry(it)
		}
		List<String> actualLines = errorCallback.toString().readLines()
	}

	void testProcessOutputIsLoggedOnInfoLevel() {
		callDirCommandAndWaitForLogging()
		assert capturedSysout.toString().readLines().every {String it -> it.contains('INFO') }
	}

	void testProcessErrorIsLoggedOnErrorLevel() {
		callUnknownCommandAndWaitForLogging()
		assert capturedSysout.toString().readLines().every {String it -> it.contains('ERROR') }
	}

	void testAcceptsNullForOutWriter() {
		outputCallback = null
		callDirCommandAndWaitForLogging()
		assertProcessOutputIsWritten()
	}

	void testAcceptsNullForErrWriter() {
		errorCallback = null
		callUnknownCommandAndWaitForLogging()
		assertProcessErrorIsWritten()
	}

	void testOutWriterAndErrWriterParamsAreOptional() {
		process = new RDiffCommandExecutor().execute('cmd /c dir')
		process.waitFor()
	}

	private void callCommandAndWaitForLogging(String command) {
		process = new RDiffCommandExecutor().execute(command, outputCallback, errorCallback)
		process.waitFor()
		waitToFinishLoggingToSystemOut()
	}

	private void callDirCommandAndWaitForLogging() {
		callCommandAndWaitForLogging('cmd /c dir')
	}

	private void callUnknownCommandAndWaitForLogging() {
		callCommandAndWaitForLogging('cmd /c sillyCommand')
	}

	private waitToFinishLoggingToSystemOut() {
		Thread.sleep(500L)
	}

	private assertProcessOutputIsWritten() {
		assert errorCallback.toString().isEmpty()
		assert !outputCallback.toString().isEmpty()
		assert 0 == process.exitValue()
	}

	private assertProcessErrorIsWritten() {
		assert !errorCallback.toString().isEmpty()
		assert outputCallback.toString().isEmpty()
		assert 0 != process.exitValue()
	}

	private String extractLoggingInfo(String logEntry) {
		return logEntry.substring(0, logEntry.indexOf(' - ') + 3)
	}

	private String extractLogEntry(String logEntry) {
		return logEntry.substring(logEntry.indexOf(' - ') + 3, logEntry.length())
	}
}
