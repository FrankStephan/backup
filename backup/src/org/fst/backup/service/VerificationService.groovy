package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException

class VerificationService {

	private RDiffCommands rdiffCommands = new RDiffCommands()

	boolean verifyIncrement(Increment increment, Appendable cmdOut, Appendable cmdErr) throws FileIsNotADirectoryException, DirectoryNotExistsException{
		return verify(increment.targetPath, increment.secondsSinceTheEpoch, cmdOut, cmdErr)
	}

	boolean verifyMirror(String targetPath, Appendable cmdOut, Appendable cmdErr) throws FileIsNotADirectoryException, DirectoryNotExistsException{
		return verify(targetPath, 'now', cmdOut, cmdErr)
	}

	private boolean verify(String targetPath, def when, Appendable cmdOut, Appendable cmdErr) throws FileIsNotADirectoryException, DirectoryNotExistsException{
		File targetDir = new File(targetPath)
		if (targetDir.exists()) {
			if (targetDir.isDirectory()) {
				Process process = rdiffCommands.verify(targetDir, when)
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
}
