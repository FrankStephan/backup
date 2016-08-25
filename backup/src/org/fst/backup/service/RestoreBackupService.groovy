package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException

class RestoreBackupService {

	RDiffCommands rdiffCommands = new RDiffCommands()

	void restore(Increment increment, File restoreDir, Closure commandLineCallBack) throws DirectoryNotExistsException, FileIsNotADirectoryException {
		File targetDir = new File(increment.targetPath)
		if (targetDir.exists() && restoreDir.exists()) {
			if (targetDir.isDirectory() && restoreDir.isDirectory()) {
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
}
