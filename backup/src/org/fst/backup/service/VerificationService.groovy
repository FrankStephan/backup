package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException

class VerificationService {

	private RDiffCommands rdiffCommands = new RDiffCommands()

	boolean verifyIncrement(Increment increment) throws FileIsNotADirectoryException, DirectoryNotExistsException{
		File targetDir = new File(increment.targetPath)
		if (targetDir.exists()) {
			if (targetDir.isDirectory()) {
				Process process = rdiffCommands.verify(targetDir, increment.secondsSinceTheEpoch)
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
}
