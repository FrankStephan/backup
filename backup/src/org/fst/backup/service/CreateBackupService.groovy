package org.fst.backup.service

import org.fst.backup.rdiff.RDiffCommand
import org.fst.backup.rdiff.RDiffCommandBuilder
import org.fst.backup.service.exception.DirectoryNotExistsException;
import org.fst.backup.service.exception.FileIsNotADirectoryException;

class CreateBackupService {

	void createBackup (File sourceDir, File targetDir, Closure commandLineCallback) throws FileIsNotADirectoryException, DirectoryNotExistsException {
		if (sourceDir.exists() && targetDir.exists()) {
			if (sourceDir.isDirectory() && targetDir.isDirectory()) {
				String commandString = new RDiffCommandBuilder().build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.HIGHEST_VERBOSITY)
				commandString = commandString + ' ' + sourceDir.absolutePath + ' ' + targetDir.absolutePath
				def process = commandString.execute()
				process.inputStream.eachLine { commandLineCallback(it) }
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
}

