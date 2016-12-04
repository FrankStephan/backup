package org.fst.backup.test.unit.rdiff

import static org.junit.Assert.*

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommandExecutor
import org.fst.backup.test.AbstractTest
import org.fst.backup.test.TestCallback

class RDiffCommandsExecutorTest extends AbstractTest {

	static PrintStream sysout
	static PrintStream syserr
	static ByteArrayOutputStream capturedSysout
	static ByteArrayOutputStream capturedSyserr

	static {
		rememberSystemStreams()
		captureSystemStreams()
	}

	static void rememberSystemStreams() {
		sysout = System.out
		syserr = System.err
	}

	static void captureSystemStreams() {
		capturedSysout = new ByteArrayOutputStream()
		capturedSyserr = new ByteArrayOutputStream()
		System.setOut(new PrintStream(capturedSysout))
		System.setErr(new PrintStream(capturedSyserr))
	}

	CommandLineCallback outputCallback = new TestCallback()
	CommandLineCallback errorCallback = new TestCallback()
	ProcessStatus processStatus = null

	void setUp() {
		super.setUp()
		capturedSysout.reset()
		capturedSyserr.reset()
	}

	void tearDown() {
		super.tearDown()
	}

	void testMethodReturnsAfterProcessIsFinished() {
		assert null == processStatus
		callDirCommand()
		assert null != processStatus
	}

	void testSuccessfulCommandOuputGetsWritten() {
		callDirCommand()
		assertOutputCallbackIsInvoked()
	}

	void testFailingCommandErrGetsWritten() {
		callUnknownCommand()
		assertErrorCallbackIsInvoked()
	}

	void testLogAndOutputCallbackAreEqual() {
		callDirCommand()

		List<String> logged = capturedSysout.toString().readLines().collect { String it ->
			extractLogEntry(it)
		}
		List<String> callback = outputCallback.toString().readLines()

		assert logged == callback
	}

	void testErrorsAreLoggedToSysout() {
		callUnknownCommand()
		assert capturedSysout.size() > 0
		assert capturedSyserr.size() == 0
	}

	void testLogEqualsProcessError() {
		callUnknownCommand()

		List<String> logged = capturedSysout.toString().readLines().collect { String it ->
			extractLogEntry(it)
		}
		List<String> callback = errorCallback.toString().readLines()

		assert logged == callback
	}

	void testProcessOutputIsLoggedOnInfoLevel() {
		callDirCommand()
		assert capturedSysout.toString().readLines().every { String it ->
			it.contains('INFO')
		}
	}

	void testProcessErrorIsLoggedOnErrorLevel() {
		callUnknownCommand()
		assert capturedSysout.toString().readLines().every { String it ->
			it.contains('ERROR')
		}
	}

	void testAcceptsNullForOutputCallback() {
		outputCallback = null
		callDirCommand()
		assert capturedSysout.size() > 0
	}

	void testAcceptsNullForErrWriter() {
		errorCallback = null
		callUnknownCommand()
		assert capturedSysout.size() > 0
	}

	private void callCommand(String command) {
		processStatus = new RDiffCommandExecutor().execute(command, outputCallback, errorCallback)
	}

	private void callDirCommand() {
		callCommand('cmd /c dir' )
	}

	private void callUnknownCommand() {
		callCommand('cmd /c sillyCommand')
	}

	private assertOutputCallbackIsInvoked() {
		assert errorCallback.toString().isEmpty()
		assert !outputCallback.toString().isEmpty()
		assert ProcessStatus.SUCCESS == processStatus
	}

	private assertErrorCallbackIsInvoked() {
		assert !errorCallback.toString().isEmpty()
		assert outputCallback.toString().isEmpty()
		assert ProcessStatus.FAILURE == processStatus
	}

	private String extractLoggingInfo(String logEntry) {
		return logEntry.substring(0, logEntry.indexOf(' - ') + 3)
	}

	private String extractLogEntry(String logEntry) {
		return logEntry.substring(logEntry.indexOf(' - ') + 3, logEntry.length())
	}
}
