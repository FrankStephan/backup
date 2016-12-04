package org.fst.backup.service

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.Increment
import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException

class VerificationService {

	private RDiffCommands rdiffCommands = new RDiffCommands()

	boolean verifyIncrement(Increment increment, CommandLineCallback outputCallback, CommandLineCallback errorCallback) throws FileIsNotADirectoryException, DirectoryNotExistsException{
		return verify(increment.targetPath, increment.secondsSinceTheEpoch, outputCallback, errorCallback)
	}

	boolean verifyMirror(String targetPath, CommandLineCallback outputCallback, CommandLineCallback errorCallback) throws FileIsNotADirectoryException, DirectoryNotExistsException{
		return verify(targetPath, 'now', outputCallback, errorCallback)
	}

	private boolean verify(String targetPath, def when, CommandLineCallback outputCallback, CommandLineCallback errorCallback) throws FileIsNotADirectoryException, DirectoryNotExistsException{
		File targetDir = new File(targetPath)
		if (targetDir.exists()) {
			if (targetDir.isDirectory()) {
				ProcessStatus processStatus = rdiffCommands.verify(targetDir, when, outputCallback, errorCallback)
				if (ProcessStatus.SUCCESS == processStatus) {
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
