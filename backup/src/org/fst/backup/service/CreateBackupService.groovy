package org.fst.backup.service

import java.io.File;

import org.fst.backup.rdiff.RDiffCommand;
import org.fst.backup.rdiff.RDiffCommandBuilder;

class CreateBackupService {

	void createBackup (File sourceDir, File targetDir, Closure callback) {
		if (sourceDir.isDirectory() && targetDir.isDirectory()) {
			String commandString = new RDiffCommandBuilder().build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.HIGHEST_VERBOSITY)
			commandString = commandString + ' ' + sourceDir.absolutePath + ' ' + targetDir.absolutePath
			Process p = commandString.execute()
			callback(p.text)
		} else {
			throw new FileIsNotADirectoryException()
		}
	}
}

