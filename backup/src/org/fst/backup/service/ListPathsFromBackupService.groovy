package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NotABackupDirectoryException


class ListPathsFromBackupService {

	RDiffCommands rdiffCommands = new RDiffCommands()

	String[] retrieveAllPathsFromBackupDir(Increment increment) {

		File targetDir = new File(increment.targetPath)
		if (targetDir.exists()) {
			if (targetDir.isDirectory()) {
				Process process = rdiffCommands.listFiles(increment.targetPath, increment.secondsSinceTheEpoch)
				String cmdLineContent = process.text
				if (0 == process.exitValue()) {
					return cmdLineContent.readLines() as String[]
				} else {
					throw new NotABackupDirectoryException()
				}
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
}
