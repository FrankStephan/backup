package org.fst.backup.rdiff

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.io.IoBuilder

class RDiffCommandExecutor {

	Process execute(String command, Writer outWriter=null, Writer errWriter=null) {
		Process process = command.execute()
		Appendable output = IoBuilder.forLogger().setLevel(Level.INFO).filter((Writer) outWriter).buildPrintWriter()
		Appendable error = IoBuilder.forLogger().setLevel(Level.ERROR).filter((Writer) errWriter).buildPrintWriter()
		process.consumeProcessOutput(output, error)
		return process
	}
}
