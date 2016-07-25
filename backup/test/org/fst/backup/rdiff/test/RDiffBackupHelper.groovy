package org.fst.backup.rdiff.test

import org.fst.backup.rdiff.RDiffCommand
import org.fst.backup.rdiff.RDiffCommandBuilder


class RDiffBackupHelper {

	File file1
	File file2

	void createTwoIncrements(String sourceDir, String targetDir) {
		new File(sourceDir).mkdirs()

		file1 = new File(sourceDir, "File1.txt") << 'I am file 1.'
		backup(sourceDir, targetDir)
		waitSinceRDiffCanOnlyDoOneBackupPerSecond()

		file2 = new File(sourceDir, "File2.txt") << 'I am file 2.'
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
		String command = commandBuilder.build(RDiffCommand.RDIFF_COMMAND)
		command = command + ' ' + sourceFolder + ' ' + targetFolder
		command.execute()
	}

	private void waitSinceRDiffCanOnlyDoOneBackupPerSecond() {
		Thread.sleep(1000L)
	}

	void cleanUp(String dir) throws IOException {
		new File(dir).deleteDir()
	}
}
