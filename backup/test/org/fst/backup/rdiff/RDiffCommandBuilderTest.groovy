package org.fst.backup.rdiff;

import groovy.util.GroovyTestCase;

class RDiffCommandBuilderTest extends GroovyTestCase {
	
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
