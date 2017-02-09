package org.fst.backup.rdiff

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.ProcessStatus


class RDiffCommands {

	RDiffCommandExecutor executor = new RDiffCommandExecutor()

	ProcessStatus version(CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.VERSION_ARG)
		executor.execute(command, outputCallback, errorCallback)
	}

	ProcessStatus backup(File sourceDir, File targetDir, CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.HIGHEST_VERBOSITY, RDiffCommandElement.NO_COMPARE_INODE)
		command = command + ' ' + sourceDir.absolutePath + ' ' + targetDir.absolutePath
		executor.execute(command, outputCallback, errorCallback)
	}

	ProcessStatus verify(File targetDir, def when, CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.HIGHEST_VERBOSITY, RDiffCommandElement.VERIFY)
		command = command + ' ' + when + ' ' + targetDir.absolutePath
		executor.execute(command, outputCallback, errorCallback)
	}

	ProcessStatus listIncrements(File targetDir, CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.LIST_INCREMENTS_ARG, RDiffCommandElement.PARSABLE_OUTPUT_ARG)
		command = command + ' ' + targetDir.absolutePath
		executor.execute(command, outputCallback, errorCallback)
	}

	ProcessStatus listFiles(File targetDir, def when, CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.LIST_AT_TIME_ARG)
		command = command + ' ' + when + ' ' + targetDir.absolutePath
		executor.execute(command, outputCallback, errorCallback)
	}

	ProcessStatus restore(File targetDir, File restoreDir, def when, CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.HIGHEST_VERBOSITY, RDiffCommandElement.RESTORE)
		command = command + ' ' + when + ' ' + targetDir.absolutePath + ' ' +  restoreDir.absolutePath
		executor.execute(command, outputCallback, errorCallback)
	}
}
