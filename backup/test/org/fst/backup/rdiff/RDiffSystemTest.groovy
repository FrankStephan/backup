package org.fst.backup.rdiff

import static org.junit.Assert.*

import org.fst.backup.test.RDiffBackupHelper

class RDiffSystemTest extends GroovyTestCase {

	static String TMP_DIR = 'RDiffSystemTest-tmp/'
	static String SOURCE_DIR = 'RDiffSystemTest-tmp/source/'
	static String TARGET_DIR = 'RDiffSystemTest-tmp/target/'

	RDiffBackupHelper helper = new RDiffBackupHelper()

	private List<String> listIncrements() {
		RDiffCommandBuilder commandBuilder = new RDiffCommandBuilder()
		String command = commandBuilder.build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_INCREMENTS_ARG, RDiffCommand.PARSABLE_OUTPUT_ARG)
		command = command + ' ' + TARGET_DIR
		Process process = command.execute()

		String increments = process.text
		return increments.readLines()
	}

	private List<String> listFiles(def when) {
		RDiffCommandBuilder commandBuilder = new RDiffCommandBuilder()
		String command = commandBuilder.build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_AT_TIME_ARG)
		command = command + ' ' + when + ' ' + TARGET_DIR
		Process process = command.execute()

		String text = process.text

		return text.readLines()
	}

	private List<String> extractSecondsSinceTheEpochPerIncrement(List<String> increments) {
		return increments.collect { it.split() [0] }
	}

	void testRDiffIsAvailableAndHasCorrectVersion() {
		def command = new RDiffCommandBuilder().build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.VERSION_ARG)
		def process = command.execute()
		assertEquals('rdiff-backup 1.2.8', process.text.trim())
	}

	void testListIncrementsAfterBackups() {
		helper.createTwoIncrements(SOURCE_DIR, TARGET_DIR)
		assert 2 == listIncrements().size()
	}

	void testListIncrementsChronologicallyAscending() {
		helper.createTwoIncrements(SOURCE_DIR, TARGET_DIR)
		List<String> increments = listIncrements()
		assert increments.size() == 2

		String secondsSinceEpoch1 = increments[0].split()[0]
		String secondsSinceEpoch2 = increments[1].split()[0]
		assert secondsSinceEpoch1 < secondsSinceEpoch2
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

	void testListFilesFromEmptyBackup() {
		helper.createEmptyBackup(SOURCE_DIR, TARGET_DIR)
		def paths = listFiles('now')

		assert 1 == paths.size()
		assert '.' == paths[0]
	}

	void testListFilesFromBackupDirectoryNow() {
		helper.createTwoIncrements(SOURCE_DIR, TARGET_DIR)
		def paths = listFiles('now')

		assert 3 == paths.size()
		assert '.' == paths[0]
		assert helper.file1.getName() == paths[1]
		assert helper.file2.getName() == paths[2]
	}

	void testListFilesFromBackupDirectoryOlder() {
		helper.createTwoIncrements(SOURCE_DIR, TARGET_DIR)
		def increments = listIncrements()
		def secondsSinceTheEpochPerIncrement = extractSecondsSinceTheEpochPerIncrement(increments)
		def paths = listFiles(secondsSinceTheEpochPerIncrement[0])
		println paths
		assert 2 == paths.size()
		assert '.' == paths[0]
		assert helper.file1.getName() == paths[1]
	}

	void tearDown() {
		helper.cleanUp(TMP_DIR)
	}
}
