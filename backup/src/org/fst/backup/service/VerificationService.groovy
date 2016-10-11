package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException

class VerificationService {

	private RDiffCommands rdiffCommands = new RDiffCommands()

	boolean verifyIncrement(Increment increment, Appendable cmdOut, Appendable cmdErr) throws FileIsNotADirectoryException, DirectoryNotExistsException{
		File targetDir = new File(increment.targetPath)
		if (targetDir.exists()) {
			if (targetDir.isDirectory()) {
				Process process = rdiffCommands.verify(targetDir, increment.secondsSinceTheEpoch)
				process.waitForProcessOutput(cmdOut, cmdErr)

				if (process.exitValue() == 0) {
					return true
				} else {
					return false
				}
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}

	boolean verifyMirror(String targetPath, Appendable outCallBack, Appendable errorCallBack) {
	}
}
