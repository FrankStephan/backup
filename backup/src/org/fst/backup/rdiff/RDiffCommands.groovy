package org.fst.backup.rdiff


class RDiffCommands {

	Process version() {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.VERSION_ARG)
		return command.execute()
	}

	Process backup(File sourceDir, File targetDir) {
		def commandString = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.HIGHEST_VERBOSITY)
		commandString = commandString + ' ' + sourceDir.absolutePath + ' ' + targetDir.absolutePath
		return commandString.execute()
	}

	Process listIncrements(String targetDir) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.LIST_INCREMENTS_ARG, RDiffCommandElement.PARSABLE_OUTPUT_ARG)
		command = command + ' ' + targetDir
		return command.execute()
	}

	Process listFiles(String targetDir, def when) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.LIST_AT_TIME_ARG)
		command = command + ' ' + when + ' ' + targetDir
		return command.execute()
	}
}
