package org.fst.backup.misc

import static org.junit.Assert.*

import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommandBuilder
import org.fst.backup.rdiff.RDiffCommandElement
import org.fst.backup.rdiff.RDiffCommandExecutor
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.test.TestCallback

class RDiffCheck extends GroovyTestCase {

	static final String TMP_DIR = RDiffCheck.class.getSimpleName()
	static  String SOURCE_DIR = TMP_DIR + '/source/'
	static  String TARGET_DIR = TMP_DIR + '/target/'
	static final String RESTORE_DIR = TMP_DIR + '/restore/'
	static final String FILE1_NAME = 'File1.txt'
	static final String FILE2_NAME = 'File2.txt'

	private static final boolean DEBUG = false

	File file1
	File file2

	TestCallback outputCallback
	TestCallback errorCallback

	String output
	String error
	ProcessStatus processStatus

	RDiffCommands rdiffCommands = new RDiffCommands()

	void testRDiffIsAvailableAndHasCorrectVersion() {
		resetCommandLineCallbacks()
		processStatus = rdiffCommands.version(outputCallback, errorCallback)
		generateProcessResult()
		assert ProcessStatus.SUCCESS == processStatus
		assertEquals('rdiff-backup 1.2.8', output.trim())
	}

	void testBackupCommandIsExecuted() {
		createTwoIncrements()
		assert output.contains('Using rdiff-backup version 1.2.8')
		assert ProcessStatus.SUCCESS == processStatus
	}

	void testNoCompareINodeOptionIsRequiredToBackupOnlyChangedFiles() {
		createTwoIncrements()

		String compareCommand = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND) + ' --compare ' + SOURCE_DIR + ' ' + TARGET_DIR
		new RDiffCommandExecutor().execute(compareCommand, outputCallback)
		generateProcessResult()
		assert output.contains('changed: ' + FILE1_NAME)
		assert output.contains('changed: ' + FILE2_NAME)

		resetCommandLineCallbacks()
		file1.append(' I have changed ')
		new RDiffCommandExecutor().execute(compareCommand, outputCallback)
		assert output.contains('changed: ' + FILE1_NAME)
		assert output.contains('changed: ' + FILE2_NAME)

		resetCommandLineCallbacks()
		file1.append('I have changed again ')
		String compareNoINodeCommand = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.NO_COMPARE_INODE) + ' --compare ' + SOURCE_DIR + ' ' + TARGET_DIR
		new RDiffCommandExecutor().execute(compareNoINodeCommand, outputCallback)
		generateProcessResult()
		assert output.contains('changed: ' + FILE1_NAME)
		assert !output.contains('changed: ' + FILE2_NAME)
	}

	void testVerifyConsistentBackupDir() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()
		verify(secondsSinceTheEpochPerIncrement[0])
		assert output.contains('Every file verified successfully.')
		assert ProcessStatus.SUCCESS == processStatus
	}

	void testVerifyFailsIfAFileIsMissing1() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()

		deleteFile1()
		verify(secondsSinceTheEpochPerIncrement[0])
		assert error.contains('Could not restore file File1.txt')
		assert ProcessStatus.FAILURE == processStatus
	}

	void testVerifyFailsIfAFileIsMissing2() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()

		deleteFile1()
		verify('now')
		assert error.contains('Could not restore file File1.txt')
		assert ProcessStatus.FAILURE == processStatus
	}

	void testVerifyFailsIfAFileIsMissing3() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()

		deleteFile2()
		verify('now')
		assert error.contains('Could not restore file File2.txt')
		assert ProcessStatus.FAILURE == processStatus
	}

	void testVerifyIsSuccessfulIfMissingFileIsPartOfANewerIncrement() {
		createTwoIncrements()
		def secondsSinceTheEpochPerIncrement = listSecondsSinceTheEpochPerIncrement()

		deleteFile2()
		verify(secondsSinceTheEpochPerIncrement[0])
		assert output.contains('Every file verified successfully.')
		assert ProcessStatus.SUCCESS == processStatus
	}

	void testVerifySucceedsButWritesErrorIfMissingFileWasEmpty() {
		createTwoIncrements()
		file1.text = ''
		backup()
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()

		deleteFile1()
		verify('now')
		assert output.contains('Every file verified successfully.')
		assert error.contains('Could not restore file File1.txt')
		assert ProcessStatus.SUCCESS == processStatus
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
		assert ProcessStatus.FAILURE == processStatus
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
		assert ProcessStatus.FAILURE == processStatus
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
		assert output.isEmpty()
		assert ProcessStatus.FAILURE == processStatus
		assert error.startsWith('Fatal Error:')
		assert error.trim().endsWith('It doesn\'t appear to be an rdiff-backup destination dir')
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
		assert ProcessStatus.FAILURE == processStatus
	}

	void testRestoreFromNonBackupDir() {
		createTwoIncrements()
		restore('now', new File(SOURCE_DIR), new File(TMP_DIR))
		assert ProcessStatus.FAILURE == processStatus
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
		resetCommandLineCallbacks()
		processStatus = rdiffCommands.backup(new File(SOURCE_DIR), new File(TARGET_DIR), outputCallback, errorCallback)
		generateProcessResult()
	}

	private void verify(def when) {
		resetCommandLineCallbacks()
		processStatus = rdiffCommands.verify(new File(TARGET_DIR), when, outputCallback, errorCallback)
		generateProcessResult()
	}

	private List<String> listIncrements() {
		resetCommandLineCallbacks()
		processStatus = rdiffCommands.listIncrements(new File(TARGET_DIR), outputCallback, errorCallback)
		generateProcessResult()
		return output.readLines()
	}

	private List<String> listFiles(def when) {
		resetCommandLineCallbacks()
		processStatus = rdiffCommands.listFiles(new File(TARGET_DIR), when, outputCallback, errorCallback)
		generateProcessResult()
		return output.readLines()
	}

	private void restore(def when, File restoreDir = new File(RESTORE_DIR), File targetDir = new File(TARGET_DIR)) {
		resetCommandLineCallbacks()
		processStatus = rdiffCommands.restore(targetDir, restoreDir, when, outputCallback, errorCallback)
		generateProcessResult()
	}

	private resetCommandLineCallbacks() {
		outputCallback = new TestCallback()
		errorCallback = new TestCallback()
		output == 'N.E.'
		error == 'N.E.'
	}

	private void generateProcessResult() {
		output = outputCallback.toString()
		error = errorCallback.toString()
		if (DEBUG) {
			println '----------------------------------------------------------------'
			println ('exitValue:' + System.lineSeparator() + processStatus)
			println '----------------------------------------------------------------'
			println ('cmdLineOutput:' + System.lineSeparator() + output)
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