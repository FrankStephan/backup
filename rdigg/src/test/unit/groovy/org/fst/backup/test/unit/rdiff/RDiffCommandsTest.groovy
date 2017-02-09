package org.fst.backup.test.unit.rdiff

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommandBuilder
import org.fst.backup.rdiff.RDiffCommandElement
import org.fst.backup.rdiff.RDiffCommandExecutor
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.test.AbstractTest
import org.fst.backup.test.TestCallback

class RDiffCommandsTest extends AbstractTest  {

	private String expectedCommand
	private CommandLineCallback expectedOutputCallback
	private CommandLineCallback expectedErrorCallback
	private ProcessStatus expectedProcessStatus
	private def commandBuilder
	private def executor

	void setUp() {
		super.setUp()
		executor = new MockFor(RDiffCommandExecutor.class)
		commandBuilder = new MockFor(RDiffCommandBuilder.class)
		executor.demand.execute(1) { String command, CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null ->
			assert expectedCommand == command
			assert expectedOutputCallback == outputCallback
			assert expectedErrorCallback == errorCallback
			return expectedProcessStatus
		}
	}

	void testVersion() {
		expectedCommand = 'cmd /c rdiff-backup --version'
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup --version',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.VERSION_ARG
				)
		expectedOutputCallback = new TestCallback()
		expectedErrorCallback = new TestCallback()
		callMethodUnderTestAndVerifyProcess( {
			new RDiffCommands().version(expectedOutputCallback, expectedErrorCallback)
		} )
	}

	void testBackup() {
		expectedCommand = 'cmd /c rdiff-backup -v9 --no-compare-inode ' + sourceDir.absolutePath + ' ' + targetDir.absolutePath
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup -v9 --no-compare-inode',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.HIGHEST_VERBOSITY,
				RDiffCommandElement.NO_COMPARE_INODE
				)

		expectedOutputCallback = new TestCallback()
		expectedErrorCallback = new TestCallback()
		callMethodUnderTestAndVerifyProcess( {
			new RDiffCommands().backup(sourceDir, targetDir, expectedOutputCallback, expectedErrorCallback)
		} )
	}

	void testVerify() {
		expectedCommand = 'cmd /c rdiff-backup --verify-at-time now ' + targetDir.absolutePath
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup --verify-at-time',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.HIGHEST_VERBOSITY,
				RDiffCommandElement.VERIFY
				)

		expectedOutputCallback = new TestCallback()
		expectedErrorCallback = new TestCallback()
		callMethodUnderTestAndVerifyProcess( {
			new RDiffCommands().verify(targetDir, 'now', expectedOutputCallback, expectedErrorCallback)
		} )
	}

	void testListIncrements() {
		expectedCommand = 'cmd /c rdiff-backup -l --parsable-output ' + targetDir.absolutePath
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup -l --parsable-output',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.LIST_INCREMENTS_ARG,
				RDiffCommandElement.PARSABLE_OUTPUT_ARG
				)
		expectedOutputCallback = new TestCallback()
		expectedErrorCallback = new TestCallback()
		callMethodUnderTestAndVerifyProcess( {
			new RDiffCommands().listIncrements(targetDir, expectedOutputCallback, expectedErrorCallback)
		} )
	}

	void testListFiles() {
		expectedCommand = 'cmd /c rdiff-backup --list-at-time 1467750198 ' + targetDir.absolutePath
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup --list-at-time',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.LIST_AT_TIME_ARG,
				)
		expectedOutputCallback = new TestCallback()
		expectedErrorCallback = new TestCallback()
		callMethodUnderTestAndVerifyProcess( {
			new RDiffCommands().listFiles(targetDir, '1467750198', expectedOutputCallback, expectedErrorCallback)
		} )
	}

	void testRestore() {
		expectedCommand = 'cmd /c rdiff-backup -r 1467750198 ' + targetDir.absolutePath + ' ' + sourceDir.absolutePath
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup -r',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.HIGHEST_VERBOSITY,
				RDiffCommandElement.RESTORE
				)
		expectedOutputCallback = new TestCallback()
		expectedErrorCallback = new TestCallback()
		callMethodUnderTestAndVerifyProcess( {
			new RDiffCommands().restore(targetDir, sourceDir, '1467750198', expectedOutputCallback, expectedErrorCallback)
		} )
	}

	void testProcessStatusIsForwarded() {
		expectedCommand = 'cmd /c rdiff-backup --version'
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup --version',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.VERSION_ARG
				)
		expectedOutputCallback = new TestCallback()
		expectedErrorCallback = new TestCallback()
		expectedProcessStatus = ProcessStatus.SUCCESS
		callMethodUnderTestAndVerifyProcess( {
			new RDiffCommands().version(expectedOutputCallback, expectedErrorCallback)
		} )
	}

	private void callMethodUnderTestAndVerifyProcess(Closure<Process> methodUnderTest) {
		commandBuilder.use {
			executor.use {
				ProcessStatus actualProcessStatus = methodUnderTest()
				assert expectedProcessStatus == actualProcessStatus
			}
		}
	}

	private void defineExpectedCommandBuilderInvocation(String returnedCommandString, RDiffCommandElement... expectedElements) {
		commandBuilder.demand.build (1) { RDiffCommandElement... commands ->
			assert commands == expectedElements
			return returnedCommandString
		}
	}
}
