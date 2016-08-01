package org.fst.backup.service

import org.fst.backup.rdiff.RDiffCommand
import org.fst.backup.rdiff.RDiffCommandBuilder
import org.fst.backup.service.exception.DirectoryNotExistsException;
import org.fst.backup.service.exception.FileIsNotADirectoryException;


class ListIncrementsService {
	
	List<String> listIncrements(File directory) throws FileIsNotADirectoryException {
		if (directory.exists()) {
			if (directory.directory) {
				String commandString = new RDiffCommandBuilder().build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_INCREMENTS_ARG, RDiffCommand.PARSABLE_OUTPUT_ARG)
				commandString = commandString + ' ' + directory.absolutePath
				Process process = commandString.execute()
				return process.text.readLines()
			} else {
			throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
}
