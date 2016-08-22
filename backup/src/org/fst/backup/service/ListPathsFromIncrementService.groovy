package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NotABackupDirectoryException


class ListPathsFromIncrementService {

	RDiffCommands rdiffCommands = new RDiffCommands()

	List<String> listPathsFromIncrement(Increment increment) {
		File targetDir = new File(increment.targetPath)
		if (targetDir.exists()) {
			if (targetDir.isDirectory()) {
				Process process = rdiffCommands.listFiles(targetDir, increment.secondsSinceTheEpoch)
				List<String> paths = process.text.readLines()
				if (0 == process.exitValue()) {
					return paths
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
