package org.fst.backup.test.unit.rdiff

import static org.junit.Assert.*

import org.fst.backup.rdiff.RDiffCommandExecutor

class RDiffCommandsExecutorTest extends GroovyTestCase {

	void testExecutorCallsCommandLine() {
		Process process = new RDiffCommandExecutor().execute('cmd /c dir')
		assert process.errorStream.readLines().isEmpty()
		assert !process.text.isEmpty()
		assert 0 == process.exitValue()
	}

	void testCmdLineIsLogged() {
		fail()
	}

	void testErrLineIsLogged() {
		fail()
	}
}
