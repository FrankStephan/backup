package org.fst.backup.service

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException

class CreateAndVerifyIncrementService {

	void createAndVerify(File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback) throws FileIsNotADirectoryException, DirectoryNotExistsException {
		new CreateIncrementService().createIncrement(sourceDir, targetDir, outputCallback, errorCallback)
		new VerificationService().verifyMirror(targetDir.getPath(), outputCallback, errorCallback)
	}
}
