package org.fst.backup.test.unit.rdiff

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.rdiff.RDiffCommandBuilder
import org.fst.backup.rdiff.RDiffCommandElement
import org.fst.backup.rdiff.RDiffCommandExecutor
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.test.unit.AbstractFilesUsingTest

class RDiffCommandsTest extends AbstractFilesUsingTest  {
	private static final String TEST_COMMAND = 'answer 42'

	private String expectedCommand
	private Process process
	private def commandBuilder
	private def executor

	void setUp() {
		super.setUp()
		executor = new MockFor(RDiffCommandExecutor.class)
		commandBuilder = new MockFor(RDiffCommandBuilder.class)
		process = new MockFor(Process.class).proxyInstance()
		executor.demand.execute(1) { String command ->
			assert expectedCommand == command
			return process
		}
	}

	void testVersion() {
		expectedCommand = 'cmd /c rdiff-backup --version'
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup --version',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.VERSION_ARG
				)
		callMethodUnderTestAndVerifyProcess( {new RDiffCommands().version()} )
	}

	void testBackup() {
		expectedCommand = 'cmd /c rdiff-backup -v9 ' + sourceDir.absolutePath + ' ' + targetDir.absolutePath
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup -v9',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.HIGHEST_VERBOSITY
				)
		callMethodUnderTestAndVerifyProcess( {new RDiffCommands().backup(sourceDir, targetDir)} )
	}

	void testListIncrements() {
		expectedCommand = 'cmd /c rdiff-backup -l --parsable-output ' + targetDir.absolutePath
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup -l --parsable-output',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.LIST_INCREMENTS_ARG,
				RDiffCommandElement.PARSABLE_OUTPUT_ARG
				)
		callMethodUnderTestAndVerifyProcess( {
			new RDiffCommands().listIncrements(targetDir)
		} )
	}

	void testListFiles() {
		expectedCommand = 'cmd /c rdiff-backup --list-at-time 1467750198 ' + targetDir.absolutePath
		defineExpectedCommandBuilderInvocation('cmd /c rdiff-backup --list-at-time',
				RDiffCommandElement.RDIFF_COMMAND,
				RDiffCommandElement.LIST_AT_TIME_ARG,
				)
		callMethodUnderTestAndVerifyProcess( {
			new RDiffCommands().listFiles(targetDir, '1467750198')
		} )
	}

	private void callMethodUnderTestAndVerifyProcess(Closure<Process> methodUnderTest) {
		commandBuilder.use {
			executor.use {
				Process actual = methodUnderTest()
				assert process == actual
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
