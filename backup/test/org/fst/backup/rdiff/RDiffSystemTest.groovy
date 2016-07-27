package org.fst.backup.rdiff

import static org.junit.Assert.*

import org.fst.backup.rdiff.test.RDiffBackupHelper

class RDiffSystemTest extends GroovyTestCase {

	static String TMP_DIR = 'RDiffSystemTest-tmp/'
	static String SOURCE_DIR = 'RDiffSystemTest-tmp/source/'
	static String TARGET_DIR = 'RDiffSystemTest-tmp/target/'

	RDiffBackupHelper helper = new RDiffBackupHelper()

	void testRDiffIsAvailableAndHasCorrectVersion() {
		def command = new RDiffCommandBuilder().build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.VERSION_ARG)
		def process = command.execute()
		assertEquals('rdiff-backup 1.2.8', process.text.trim())
	}

	void testRDiffListsIncrementsAfterBackups() {


		helper.createTwoIncrements(SOURCE_DIR, TARGET_DIR)

		String increments = listIncrements()
		println increments
		int i = 0
		increments.eachLine { i++ }

		assertEquals(2, i)
	}

	void testRDiffReturnsFilesByDate() {
		helper.createTwoIncrements(SOURCE_DIR, TARGET_DIR)
		def increments = listIncrements().readLines()
		assert increments.size() == 2
		String secondsEpochIncrement = increments[0].split()[0]
		String secondsEpochMirror = increments[1].split()[0]

		RDiffCommandBuilder commandBuilder = new RDiffCommandBuilder()
		String command = commandBuilder.build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_AT_TIME_ARG)
		command = command + ' ' + secondsEpochIncrement + ' ' + TARGET_DIR
		Process process = command.execute()

		String text =  process.text
		println text
		assert text.contains('File1.txt')
		assert !text.contains('File2.txt')

		command = commandBuilder.build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_AT_TIME_ARG)
		command = command + ' ' + secondsEpochMirror + ' ' + TARGET_DIR
		process = command.execute()
		text =  process.text
		println text
		assert text.contains('File1.txt')
		assert text.contains('File2.txt')
	}

	private String listIncrements() {
		RDiffCommandBuilder commandBuilder = new RDiffCommandBuilder()
		String command = commandBuilder.build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_INCREMENTS_ARG, RDiffCommand.PARSABLE_OUTPUT_ARG)
		command = command + ' ' + TARGET_DIR
		Process process = command.execute()

		String increments = process.text
		return increments
	}

	void testListFilesFromEmptyBackup() {
		helper.createEmptyBackup(SOURCE_DIR, TARGET_DIR)
		def increments = listIncrements().readLines()
		assert increments.size() == 1
		String secondsOfEpoch = increments[0].split()[0]

		RDiffCommandBuilder commandBuilder = new RDiffCommandBuilder()
		String command = commandBuilder.build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_AT_TIME_ARG)
		command = command + ' ' + secondsOfEpoch + ' ' + TARGET_DIR
		Process process = command.execute()

		String text =  process.text
		println text
		assert text.trim() == '.'
	}
	
	void testListFilesFromNonBackupDirectory() {
		File targetDir = new File(TARGET_DIR)
		targetDir.mkdirs()
		RDiffCommandBuilder commandBuilder = new RDiffCommandBuilder()
		String command = commandBuilder.build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_AT_TIME_ARG)
		command = command + ' now ' + TARGET_DIR
		Process process = command.execute()
		
		StringBuilder sb = new StringBuilder()
		process.errorStream.eachLine { sb.append(it)}
		String error = sb.toString()
		
		assert process.text.isEmpty()
		assert process.exitValue() == 1
		assert error.startsWith('Fatal Error:')
		assert error.endsWith('It doesn\'t appear to be an rdiff-backup destination dir')
	}


	void tearDown() {
		helper.cleanUp(TMP_DIR)
	}
}
