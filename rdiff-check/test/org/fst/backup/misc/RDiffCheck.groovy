package org.fst.backup.misc

import static org.junit.Assert.*

import org.fst.backup.rdiff.RDiffCommands

class RDiffCheck extends GroovyTestCase {

	static final String TMP_DIR = RDiffCheck.class.getSimpleName()
	static final String SOURCE_DIR = TMP_DIR + '/source/'
	static final String TARGET_DIR = TMP_DIR + '/target/'
	static final String RESTORE_DIR = TMP_DIR + '/restore/'
	static final String FILE1_NAME = 'File1.txt'
	static final String FILE2_NAME = 'File2.txt'

	File file1
	File file2

	String cmdLineOutput
	int exitValue

	RDiffCommands rdiffCommands = new RDiffCommands()

	void testRDiffIsAvailableAndHasCorrectVersion() {
		def process = rdiffCommands.version()
		assertEquals('rdiff-backup 1.2.8', process.text.trim())
	}

	void testBackupCommandIsExecuted() {
		createTwoIncrements()
		backup()
		assert cmdLineOutput.contains('Using rdiff-backup version 1.2.8')
		assert 0 == exitValue
	}
	
	void testVerifyConsistentBackupDir() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = extractSecondsSinceTheEpochPerIncrement(listIncrements())
		Process p = rdiffCommands.verify(new File(TARGET_DIR), secondsSinceTheEpochPerIncrement[0])
		cmdLineOutput = p.text.trim()
		exitValue = p.exitValue()
		assert 'Every file verified successfully.' == cmdLineOutput
		assert exitValue == 0
	}
	
	void testVerifyCorruptedBackupDir() {
		createTwoIncrements()
		fail()
		def secondsSinceTheEpochPerIncrement = extractSecondsSinceTheEpochPerIncrement(listIncrements())
		Process p = rdiffCommands.verify(new File(TARGET_DIR), secondsSinceTheEpochPerIncrement[0])
		cmdLineOutput = p.text.trim()
		exitValue = p.exitValue()
		assert 'Every file verified successfully.' == cmdLineOutput
		assert exitValue == 0
	}

	void testListIncrementsNoBackupDir() {
		new File(TARGET_DIR).mkdirs()
		listIncrements()
		assert 1 == exitValue
	}

	void testListIncrementsAfterBackups() {
		createTwoIncrements()
		assert 2 == listIncrements().size()
	}

	void testListIncrementsFormat() {
		createTwoIncrements()
		List<String> increments = listIncrements()
		assert 2 == increments.size()

		assert increments.every {
			String[] increment = it.split()
			return increment[0].isNumber() && 'directory' == increment[1]
		}
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
		def process = rdiffCommands.listFiles(new File(TARGET_DIR), 'now')
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
		assert 2 == paths.size()
		assert '.' == paths[0]
		assert file1.getName() == paths[1]
	}

	void testListFilesReturnsRelativePaths() {
		createTwoIncrements()
		def paths = listFiles('now')
		assert 3 == paths.size()
		assert paths.every { !it.contains(TARGET_DIR) }
	}

	void testRestoreWithNonEmptyRestoreDir() {
		createTwoIncrements()
		File restoreDir = new File(SOURCE_DIR)
		restoreDir.mkdir()
		def process = rdiffCommands.restore(new File(TARGET_DIR), restoreDir, 'now')
		process.errorStream.eachLine { println it }
		assert 1 == process.exitValue()
	}

	void testRestoreFromNonBackupDir() {
		createTwoIncrements()
		File restoreDir = new File(RESTORE_DIR)
		restoreDir.mkdir()
		def process = rdiffCommands.restore(new File(TMP_DIR), restoreDir, 'now')
		process.errorStream.eachLine { println it }
		assert 1 == process.exitValue()
	}

	void testRestoreFromBackupDirectoryMirror() {
		createTwoIncrements()
		File restoreDir = new File(RESTORE_DIR)
		restoreDir.mkdir()
		def increments = listIncrements()
		def secondsSinceTheEpochPerIncrement = extractSecondsSinceTheEpochPerIncrement(increments)
		def secondsSinceTheEpochMirror = secondsSinceTheEpochPerIncrement[1]
		restore(secondsSinceTheEpochMirror)
		assert [FILE1_NAME, FILE2_NAME]== restoreDir.list()
	}

	void testRestoreFromBackupDirectoryOlder() {
		createTwoIncrements()
		File restoreDir = new File(RESTORE_DIR)
		restoreDir.mkdir()
		def increments = listIncrements()
		def secondsSinceTheEpochPerIncrement = extractSecondsSinceTheEpochPerIncrement(increments)
		def secondsSinceTheEpochOlder = secondsSinceTheEpochPerIncrement[0]
		restore(secondsSinceTheEpochOlder)
		assert [FILE1_NAME]== restoreDir.list()
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
		Process p = rdiffCommands.backup(new File(SOURCE_DIR), new File(TARGET_DIR))
		cmdLineOutput = p.text
		exitValue = p.exitValue()
	}

	private List<String> listIncrements() {
		def process = rdiffCommands.listIncrements(new File(TARGET_DIR))
		String increments = process.text
		exitValue = process.exitValue()
		return increments.readLines()
	}

	private List<String> listFiles(def when) {
		def process = rdiffCommands.listFiles(new File(TARGET_DIR), when)
		String text = process.text
		exitValue = process.exitValue()
		return text.readLines()
	}

	private void restore(def when) {
		def process = rdiffCommands.restore(new File(TARGET_DIR), new File(RESTORE_DIR), when)
		String text = process.text
		println text
		exitValue = process.exitValue()
	}

	private List<String> extractSecondsSinceTheEpochPerIncrement(List<String> increments) {
		return increments.collect { it.split() [0] }
	}

	private void waitSinceRDiffCanOnlyDoOneBackupPerSecond() {
		Thread.sleep(1020L)
	}
}
