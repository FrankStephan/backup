package org.fst.backup.service

import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException

class CreateIncrementService {

	RDiffCommands commands = new RDiffCommands()

	void createIncrement (File sourceDir, File targetDir, Closure commandLineCallback) throws FileIsNotADirectoryException, DirectoryNotExistsException {
		if (sourceDir.exists() && targetDir.exists()) {
			if (sourceDir.isDirectory() && targetDir.isDirectory()) {
				Process process = commands.backup(sourceDir, targetDir)
				process.inputStream.eachLine { commandLineCallback(it) }
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
}

