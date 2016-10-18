package org.fst.backup.service

import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException


class CreateAndVerifyIncrementService {

	void createAndVerify(File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr) throws FileIsNotADirectoryException, DirectoryNotExistsException {
		def commandLineCallback = {String it ->
			cmdOut.append(it)
			cmdOut.append(System.lineSeparator())
		}
		new CreateIncrementService().createIncrement(sourceDir, targetDir, commandLineCallback)
		new VerificationService().verifyMirror(targetDir.getPath(), cmdOut, cmdErr)
	}
}
