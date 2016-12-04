package org.fst.backup.rdiff

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.io.IoBuilder
import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.ProcessStatus

class RDiffCommandExecutor {

	ProcessStatus execute(String command, CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null) {
		Process process = command.execute()

		IoBuilder outputLoggerBuiler = IoBuilder.forLogger().setLevel(Level.INFO)
		IoBuilder errorLoggerBuilder = IoBuilder.forLogger().setLevel(Level.ERROR)

		if (null != outputCallback) {
			outputLoggerBuiler.filter((Writer) new CommandLineCallbackWriter(outputCallback))
		}
		if (null != errorCallback) {
			errorLoggerBuilder.filter((Writer) new CommandLineCallbackWriter(errorCallback))
		}

		PrintWriter outputWriter = outputLoggerBuiler.buildPrintWriter()
		PrintWriter errorWriter = errorLoggerBuilder.buildPrintWriter()
		process.waitForProcessOutput(outputWriter, errorWriter)

		return process.exitValue() == 0 ? ProcessStatus.SUCCESS : ProcessStatus.FAILURE
	}
}
