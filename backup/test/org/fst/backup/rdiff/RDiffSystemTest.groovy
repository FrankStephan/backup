package org.fst.backup.rdiff;

import static org.junit.Assert.*;

import org.fst.backup.rdiff.test.RDiffBackupHelper;

class RDiffSystemTest extends GroovyTestCase {

	def String TMP_FOLDER = 'RDiffSystemTest-tmp/'
	def String SOURCE_FOLDER = 'RDiffSystemTest-tmp/source/'
    def String TARGET_FOLDER = 'RDiffSystemTest-tmp/target/'
    
    void testRDiffIsAvailableAndHasCorrectVersion() {
        def command = new RDiffCommandBuilder().build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.VERSION_ARG)
        def process = command.execute()
        assertEquals('rdiff-backup 1.2.8', process.text.trim())
    }
    
    void testRDiffListsIncrementsAfterBackups() {
		
		RDiffBackupHelper helper = new RDiffBackupHelper()
		helper.createTwoIncrements(SOURCE_FOLDER, TARGET_FOLDER)
		
		RDiffCommandBuilder commandBuilder = new RDiffCommandBuilder();
		String command = commandBuilder.build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_INCREMENTS_ARG, RDiffCommand.PARSABLE_OUTPUT_ARG)
		command = command + ' ' + TARGET_FOLDER
		Process process = command.execute()
		
		String increments = process.text
		println increments
		int i = 0;
		increments.eachLine { i++ }
		
		assertEquals(2, i)
		
		helper.cleanUp(TMP_FOLDER)
    }

	
	
	

}
