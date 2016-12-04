package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.model.ProcessStatus
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.service.exception.NotABackupDirectoryException

class ListIncrementsService {

	private RDiffCommands rdiffCommands = new RDiffCommands()

	List<Increment> listIncrements(File targetDir) throws FileIsNotADirectoryException, DirectoryNotExistsException {
		if (targetDir.exists()) {
			if (targetDir.directory) {
				StringBufferCallback outputCallback = new StringBufferCallback()
				ProcessStatus processStatus = rdiffCommands.listIncrements(targetDir, outputCallback)
				if (ProcessStatus.SUCCESS == processStatus) {
					List<String> lines = outputCallback.toString().readLines()
					List<Increment> increments = lines.collect {
						new Increment()
								.setSecondsSinceTheEpoch(it.split()[0] as long)
								.setTargetPath(targetDir.absolutePath)
					}
					return increments
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
