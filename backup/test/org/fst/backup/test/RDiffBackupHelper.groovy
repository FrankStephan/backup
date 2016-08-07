package org.fst.backup.test

import org.fst.backup.rdiff.RDiffCommandElement
import org.fst.backup.rdiff.RDiffCommandBuilder


class RDiffBackupHelper {

	public static final String FILE1_NAME = 'File1.txt'
	public static final String FILE2_NAME = 'File2.txt'

	File file1
	File file2

	void createTwoIncrements(String sourceDir, String targetDir) {
		new File(sourceDir).mkdirs()

		file1 = new File(sourceDir, FILE1_NAME) << 'I am file 1.'
		backup(sourceDir, targetDir)
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()

		file2 = new File(sourceDir, FILE2_NAME) << 'I am file 2.'
		backup(sourceDir, targetDir)
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()
	}

	void createEmptyBackup(String sourceDir, String targetDir) {
		new File(sourceDir).mkdirs()
		backup(sourceDir, targetDir)
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()
	}

	private void backup(String sourceFolder, String targetFolder) {
		RDiffCommandBuilder commandBuilder = new RDiffCommandBuilder()
		String command = commandBuilder.build(RDiffCommandElement.RDIFF_COMMAND)
		command = command + ' ' + sourceFolder + ' ' + targetFolder
		Process p = command.execute()
		p.waitForProcessOutput()
	}

	private void waitSinceRDiffCanOnlyDoOneBackupPerSecond() {
		Thread.sleep(1020L)
	}

	void cleanUp(String dir) throws IOException {
		new File(dir).deleteDir()
	}
}
