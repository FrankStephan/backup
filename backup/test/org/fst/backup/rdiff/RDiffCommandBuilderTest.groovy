package org.fst.backup.rdiff;

import groovy.util.GroovyTestCase;

class RDiffCommandBuilderTest extends GroovyTestCase {
	
	void testBuildWithOneCommand() {
        def builder = new RDiffCommandBuilder()
        String result = builder.build(RDiffCommand.RDIFF_COMMAND)
        assertEquals (result, 'cmd /c ' + RDiffCommand.RDIFF_COMMAND.commandString)
    }
    
    void testBuildWithTwoCommands() {
        def builder = new RDiffCommandBuilder()
        String result = builder.build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.VERSION_ARG)
        assertEquals (result, 'cmd /c ' + RDiffCommand.RDIFF_COMMAND.commandString + ' ' + RDiffCommand.VERSION_ARG.commandString)
    }

}
