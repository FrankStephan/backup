package org.fst.backup.rdiff


class RDiffCommands {

	RDiffCommandExecutor executor = new RDiffCommandExecutor()

	Process version() {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.VERSION_ARG)
		executor.execute(command)
	}

	Process backup(File sourceDir, File targetDir) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.HIGHEST_VERBOSITY)
		command = command + ' ' + sourceDir.absolutePath + ' ' + targetDir.absolutePath
		executor.execute(command)
	}
	
	Process verify(File targetDir, def when) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.VERIFY)
		command = command + ' ' + when + ' ' + targetDir.absolutePath
		executor.execute(command)
	}

	Process listIncrements(File targetDir) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.LIST_INCREMENTS_ARG, RDiffCommandElement.PARSABLE_OUTPUT_ARG)
		command = command + ' ' + targetDir.absolutePath
		executor.execute(command)
	}

	Process listFiles(File targetDir, def when) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.LIST_AT_TIME_ARG)
		command = command + ' ' + when + ' ' + targetDir.absolutePath
		executor.execute(command)
	}

	Process restore(File targetDir, File restoreDir, def when) {
		def command = new RDiffCommandBuilder().build(RDiffCommandElement.RDIFF_COMMAND, RDiffCommandElement.HIGHEST_VERBOSITY, RDiffCommandElement.RESTORE)
		command = command + ' ' + when + ' ' + targetDir.absolutePath + ' ' +  restoreDir.absolutePath
		executor.execute(command)
	}
}
