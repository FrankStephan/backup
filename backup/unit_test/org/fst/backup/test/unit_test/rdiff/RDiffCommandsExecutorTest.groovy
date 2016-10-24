package org.fst.backup.test.unit_test.rdiff

import static org.junit.Assert.*

import org.fst.backup.rdiff.RDiffCommandExecutor

class RDiffCommandsExecutorTest extends GroovyTestCase {

	public void testExecutorCallsCommandLine() {
		Process process = new RDiffCommandExecutor().execute('cmd /c dir')
		assert process.errorStream.readLines().isEmpty()
		assert !process.text.isEmpty()
		assert 0 == process.exitValue()
	}
}
