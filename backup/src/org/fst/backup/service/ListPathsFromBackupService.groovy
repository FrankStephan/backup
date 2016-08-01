package org.fst.backup.service

import java.nio.file.Files

import org.fst.backup.rdiff.RDiffCommand
import org.fst.backup.rdiff.RDiffCommandBuilder
import org.fst.backup.service.exception.NoBackupDirectoryException;

class ListPathsFromBackupService {

	File root

	File createRootFileWithBackupDirectoryStructure(File targetDir) {



		root = Files.createTempDirectory(targetDir.getName())
	}

	String[] retrieveAllPathsFromBackupDir(File targetDir, long incrementSecondsSinceTheEpoch) {
		String command = new RDiffCommandBuilder().build(RDiffCommand.RDIFF_COMMAND, RDiffCommand.LIST_AT_TIME_ARG)
		command = command + ' ' + incrementSecondsSinceTheEpoch + ' ' + targetDir.absolutePath
		Process process = command.execute()
		String text =  process.text

		if (process.exitValue() == 0) {
			return text.readLines() as String[]
		} else {
			throw new NoBackupDirectoryException()
		}
	}
}
