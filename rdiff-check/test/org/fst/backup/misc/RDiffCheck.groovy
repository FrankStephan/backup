package org.fst.backup.misc

import static org.junit.Assert.*

import java.awt.Color

import javax.swing.text.PlainDocument

import org.fst.backup.gui.frame.create.DocumentWriter
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.test.GradleTestProperties

class RDiffCheck extends GroovyTestCase {

	static final String TMP_DIR = RDiffCheck.class.getSimpleName()
	static  String SOURCE_DIR = TMP_DIR + '/source/'
	static  String TARGET_DIR = TMP_DIR + '/target/'
	static final String RESTORE_DIR = TMP_DIR + '/restore/'
	static final String FILE1_NAME = 'File1.txt'
	static final String FILE2_NAME = 'File2.txt'

	private static final boolean DEBUG = true

	File file1
	File file2

	String cmdLineOutput
	String error
	int exitValue

	RDiffCommands rdiffCommands = new RDiffCommands()

	void testRDiffIsAvailableAndHasCorrectVersion() {
		def process = rdiffCommands.version()
		assertEquals('rdiff-backup 1.2.8', process.text.trim())
	}

	void testBackupCommandIsExecuted() {
		createTwoIncrements()
		assert cmdLineOutput.contains('Using rdiff-backup version 1.2.8')
		assert 0 == exitValue
	}

	void testBackupWithLostConnectionToRemoteDir() {

		use GradleTestProperties

		new File(SOURCE_DIR).mkdirs()
		file1 = new File(SOURCE_DIR, FILE1_NAME)

		for (int i=0; i<10000; i++) {
			file1 << ('Line x' + i + System.lineSeparator())
		}
		System.err.println('File created.')
		Process process = rdiffCommands.backup(new File(SOURCE_DIR), new File(TARGET_DIR))
		process.consumeProcessOutput(System.out, System.err)
		System.err.println('Sleep.')
		Thread.sleep(300L)
		System.err.println('Woke up.')
		System.err.println(new File(TARGET_DIR).deleteDir())
		process.waitFor()
	}

	void testVerifyConsistentBackupDir() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()
		verify(secondsSinceTheEpochPerIncrement[0])
		assert cmdLineOutput.contains('Every file verified successfully.')
		assert 0 == exitValue
	}

	void testVerifyFailsIfAFileIsMissing1() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()

		deleteFile1()
		verify(secondsSinceTheEpochPerIncrement[0])
		assert error.contains('Could not restore file File1.txt')
		assert 1 == exitValue
	}

	void testVerifyFailsIfAFileIsMissing2() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()

		deleteFile1()
		verify('now')
		assert error.contains('Could not restore file File1.txt')
		assert 1 == exitValue
	}

	void testVerifyFailsIfAFileIsMissing3() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()

		deleteFile2()
		verify('now')
		assert error.contains('Could not restore file File2.txt')
		assert 1 == exitValue
	}

	void testVerifyIsSuccessfulIfMissingFileIsPartOfANewerIncrement() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()

		deleteFile2()
		verify(secondsSinceTheEpochPerIncrement[0])
		assert cmdLineOutput.contains('Every file verified successfully.')
		assert 0 == exitValue
	}

	void testVerifySucceedsButWritesErrorIfMissingFileWasEmpty() {
		createTwoIncrements()
		file1.text = ''
		backup()
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()

		deleteFile1()
		verify('now')
		assert cmdLineOutput.contains('Every file verified successfully.')
		assert error.contains('Could not restore file File1.txt')
		assert 0 == exitValue
	}

	void testVerifyFailsWithOneEmptyAndOneNonEmptyFileMissing() {
		createTwoIncrements()
		file1.text = ''
		backup()
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()

		deleteFile1()
		deleteFile2()
		verify('now')
		assert error.contains('Could not restore file File1.txt')
		assert 1 == exitValue
	}

	private void deleteFile1() {
		new File(TARGET_DIR, FILE1_NAME).delete()
	}

	private void deleteFile2() {
		new File(TARGET_DIR, FILE2_NAME).delete()
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
		listFiles('now')
		assert cmdLineOutput.isEmpty()
		assert 1 == exitValue
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
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()
		def paths = listFiles(secondsSinceTheEpochPerIncrement[0])
		assert 2 == paths.size()
		assert '.' == paths[0]
		assert file1.getName() == paths[1]
	}

	void testListFilesReturnsRelativePaths() {
		createTwoIncrements()
		def paths = listFiles('now')
		assert 3 == paths.size()
		assert paths.every {
			!it.contains(TARGET_DIR)
		}
	}

	void testRestoreWithNonEmptyRestoreDir() {
		createTwoIncrements()
		restore('now', new File(SOURCE_DIR))
		assert 1 == exitValue
	}

	void testRestoreFromNonBackupDir() {
		createTwoIncrements()
		restore('now', new File(SOURCE_DIR), new File(TMP_DIR))
		assert 1 == exitValue
	}

	void testRestoreFromBackupDirectoryMirror() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()
		def secondsSinceTheEpochMirror = secondsSinceTheEpochPerIncrement[1]
		restore(secondsSinceTheEpochMirror)
		assert [FILE1_NAME, FILE2_NAME]== new File(RESTORE_DIR).list()
	}

	void testRestoreFromBackupDirectoryOlder() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()
		def secondsSinceTheEpochOlder = secondsSinceTheEpochPerIncrement[0]
		restore(secondsSinceTheEpochOlder)
		assert [FILE1_NAME]== new File(RESTORE_DIR).list()
	}

	void callbackIsReturnedLineByLine() {
		fail()
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
		Process process = rdiffCommands.backup(new File(SOURCE_DIR), new File(TARGET_DIR))
		generateProcessResult(process)
	}

	private void verify(def when) {
		Process process = rdiffCommands.verify(new File(TARGET_DIR), when)
		generateProcessResult(process)
	}

	private List<String> listIncrements() {
		def process = rdiffCommands.listIncrements(new File(TARGET_DIR))
		generateProcessResult(process)
		return cmdLineOutput.readLines()
	}

	private List<String> listFiles(def when) {
		def process = rdiffCommands.listFiles(new File(TARGET_DIR), when)
		generateProcessResult(process)
		return cmdLineOutput.readLines()
	}

	private void restore(def when, File restoreDir = new File(RESTORE_DIR), File targetDir = new File(TARGET_DIR)) {
		def process = rdiffCommands.restore(targetDir, restoreDir, when)
		generateProcessResult(process)
	}

	private void generateProcessResult(Process process) {
		PlainDocument outDoc = new PlainDocument()
		PlainDocument errDoc = new PlainDocument()
		DocumentWriter outWriter = new DocumentWriter(document: outDoc, textColor: Color.BLUE)
		DocumentWriter errWriter = new DocumentWriter(document: errDoc, textColor: Color.RED)

		process.waitForProcessOutput(outWriter, errWriter)
		cmdLineOutput = outDoc.getText(0, outDoc.getLength()).trim()
		error = errDoc.getText(0, errDoc.getLength()).trim()
		exitValue = process.exitValue()
		if (DEBUG) {
			println '----------------------------------------------------------------'
			println ('exitValue:' + System.lineSeparator() + exitValue)
			println '----------------------------------------------------------------'
			println ('cmdLineOutput:' + System.lineSeparator() + cmdLineOutput)
			println '----------------------------------------------------------------'
			System.err.println ('error:' + System.lineSeparator() + error)
			println '----------------------------------------------------------------'
		}
	}

	private List<String> extractSecondsSinceTheEpochPerIncrement(List<String> increments) {
		return increments.collect {
			it.split() [0]
		}
	}

	private List listSecondsSinceTheEpochPerIncrement() {
		def increments = listIncrements()
		def secondsSinceTheEpochPerIncrement = extractSecondsSinceTheEpochPerIncrement(increments)
		return secondsSinceTheEpochPerIncrement
	}

	private void waitSinceRDiffCanOnlyDoOneBackupPerSecond() {
		Thread.sleep(1020L)
	}
}
