package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NotABackupDirectoryException


class ListPathsFromIncrementService {

	RDiffCommands rdiffCommands = new RDiffCommands()

	List<String> listPathsFromIncrement(Increment increment) throws FileIsNotADirectoryException, DirectoryNotExistsException, NotABackupDirectoryException {
		File targetDir = new File(increment.targetPath)
		if (targetDir.exists()) {
			if (targetDir.isDirectory()) {
				StringBufferCallback outputCallback = new StringBufferCallback()
				ProcessStatus processStatus = rdiffCommands.listFiles(targetDir, increment.secondsSinceTheEpoch, outputCallback)
				if (ProcessStatus.SUCCESS == processStatus) {
					return outputCallback.toString().readLines()
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
