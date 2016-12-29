package org.fst.backup.rdiff

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.io.IoBuilder
import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.ProcessStatus

class RDiffCommandExecutor {

	ProcessStatus execute(String command, CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null) {
		Logger logger = LogManager.getLogger(this.getClass())

		IoBuilder outputLoggerBuiler = IoBuilder.forLogger(logger).setLevel(Level.INFO)
		IoBuilder errorLoggerBuilder = IoBuilder.forLogger(logger).setLevel(Level.ERROR)

		if (null != outputCallback) {
			outputLoggerBuiler.filter((Writer) new CommandLineCallbackWriter(outputCallback))
		}
		if (null != errorCallback) {
			errorLoggerBuilder.filter((Writer) new CommandLineCallbackWriter(errorCallback))
		}

		PrintWriter outputWriter = outputLoggerBuiler.buildPrintWriter()
		PrintWriter errorWriter = errorLoggerBuilder.buildPrintWriter()

		log(logger, '>> Executing: ' + command)
		Process process = command.execute()
		process.waitForProcessOutput(outputWriter, errorWriter)

		int exitValue = process.exitValue()
		log(logger, '>> Finished with exitValue ' + exitValue + ': ' + command)

		return exitValue == 0 ? ProcessStatus.SUCCESS : ProcessStatus.FAILURE
	}

	private void log(Logger logger, String s) {
		logger.info(s)
	}
}
