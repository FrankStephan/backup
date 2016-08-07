package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NoBackupDirectoryException


class ListIncrementsService {

	private RDiffCommands rdiffCommands = new RDiffCommands()

	List<Increment> listIncrements(File targetDir) throws FileIsNotADirectoryException, DirectoryNotExistsException {
		if (targetDir.exists()) {
			if (targetDir.directory) {
				Process process = rdiffCommands.listIncrements(targetDir.absolutePath)
				List<String> lines = process.text.readLines()
				if (0 == process.exitValue()) {
					List<Increment> increments = lines.collect {new Increment().setSecondsSinceTheEpoch(it.split()[0] as long) }
					return increments
				} else {
					throw new NoBackupDirectoryException()
				}
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
}
