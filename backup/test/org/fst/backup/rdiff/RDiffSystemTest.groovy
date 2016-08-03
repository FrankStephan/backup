package org.fst.backup.rdiff

import static org.junit.Assert.*

class RDiffSystemTest extends GroovyTestCase {

	static final String TMP_DIR = 'RDiffSystemTest-tmp/'
	static final String SOURCE_DIR = 'RDiffSystemTest-tmp/source/'
	static final String TARGET_DIR = 'RDiffSystemTest-tmp/target/'
	static final String FILE1_NAME = 'File1.txt'
	static final String FILE2_NAME = 'File2.txt'

	File file1
	File file2

	String cmdLineOutput
	int exitValue

	RDiffProcessExecutor executor = new RDiffProcessExecutor()

	void testRDiffIsAvailableAndHasCorrectVersion() {
		def process = executor.version()
		assertEquals('rdiff-backup 1.2.8', process.text.trim())
	}

	void testBackupCommandIsExecuted() {
		createTwoIncrements()
		backup()
		assert cmdLineOutput.contains('Using rdiff-backup version 1.2.8')
		assert 0 == exitValue
	}

	void testListIncrementsAfterBackups() {
		createTwoIncrements()
		assert 2 == listIncrements().size()
	}

	void testListIncrementsChronologicallyAscending() {
		createTwoIncrements()
		List<String> increments = listIncrements()
		assert increments.size() == 2

		def secondsSinceEpoch1 = increments[0].split()[0] as long
		def secondsSinceEpoch2 = increments[1].split()[0] as long
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
		new File(SOURCE_DIR).mkdirs()
		backup()
		def paths = listFiles('now')

		assert 1 == paths.size()
		assert '.' == paths[0]
	}

	void testListFilesFromBackupDirectoryNow() {
		createTwoIncrements()
		def paths = listFiles('now')

		assert 3 == paths.size()
		assert '.' == paths[0]
		assert file1.getName() == paths[1]
		assert file2.getName() == paths[2]
	}

	void testListFilesFromBackupDirectoryOlder() {
		createTwoIncrements()
		def increments = listIncrements()
		def secondsSinceTheEpochPerIncrement = extractSecondsSinceTheEpochPerIncrement(increments)
		def paths = listFiles(secondsSinceTheEpochPerIncrement[0])
		println paths
		assert 2 == paths.size()
		assert '.' == paths[0]
		assert file1.getName() == paths[1]
	}

	void tearDown() {
		new File(TMP_DIR).deleteDir()
	}

	private void createTwoIncrements() {
		new File(SOURCE_DIR).mkdirs()

		file1 = new File(SOURCE_DIR, FILE1_NAME) << 'I am file 1.'
		backup()
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()

		file2 = new File(SOURCE_DIR, FILE2_NAME) << 'I am file 2.'
		backup()
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()
	}

	private void createEmptyBackup(String SOURCE_DIR, String targetDir) {
		new File(SOURCE_DIR).mkdirs()
		backup()
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()
	}

	private void backup() {
		Process p = executor.backup(new File(SOURCE_DIR), new File(TARGET_DIR))
		cmdLineOutput = p.text
		exitValue = p.exitValue()
	}

	private void waitSinceRDiffCanOnlyDoOneBackupPerSecond() {
		Thread.sleep(1020L)
	}

	private List<String> listIncrements() {
		def process = executor.listIncrements(TARGET_DIR)
		String increments = process.text
		return increments.readLines()
	}

	private List<String> listFiles(def when) {
		def process = executor.listFiles(TARGET_DIR, when)
		String text = process.text
		return text.readLines()
	}

	private List<String> extractSecondsSinceTheEpochPerIncrement(List<String> increments) {
		return increments.collect { it.split() [0] }
	}
}
