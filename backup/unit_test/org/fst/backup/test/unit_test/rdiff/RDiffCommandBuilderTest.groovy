package org.fst.backup.test.unit_test.rdiff

import org.fst.backup.rdiff.RDiffCommandBuilder
import org.fst.backup.rdiff.RDiffCommandElement

class RDiffCommandBuilderTest extends GroovyTestCase {

	void testCommandAlwaysRunsInCMD() {
		def builder = new RDiffCommandBuilder()
		String result = builder.build()
		assert result.startsWith('cmd /c')
	}

	void testBuildWithOneCommand() {
		def builder = new RDiffCommandBuilder()
		String result = builder.build(RDiffCommandElement.RDIFF_COMMAND)
		assertEquals (result, 'cmd /c ' + RDiffCommandElement.RDIFF_COMMAND.string)
	}

	void testBuildWithTwoCommands() {
		def builder = new RDiffCommandBuilder()
		String result = builder.build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.VERSION_ARG)
		assertEquals (result, 'cmd /c ' + RDiffCommandElement.RDIFF_COMMAND.string + ' ' + RDiffCommandElement.VERSION_ARG.string)
	}
}
