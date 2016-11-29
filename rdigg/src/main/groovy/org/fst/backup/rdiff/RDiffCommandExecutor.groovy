package org.fst.backup.rdiff

import groovy.transform.TupleConstructor;

import javax.swing.text.PlainDocument;

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.io.IoBuilder
import org.codehaus.groovy.runtime.IOGroovyMethods;
import org.fst.backup.model.CommandLineCallback;

class RDiffCommandExecutor {

	Process execute(String command, CommandLineCallback outputCallback=null, CommandLineCallback errorCallback=null) {
		Process process = command.execute()
		PrintWriter outputWriter = IoBuilder.forLogger().setLevel(Level.INFO).filter((Writer) new CommandLineCallbackWriter(outputCallback)).buildPrintWriter()
		PrintWriter errorWriter = IoBuilder.forLogger().setLevel(Level.ERROR).filter((Writer) new CommandLineCallbackWriter(errorCallback)).buildPrintWriter()
		process.consumeProcessOutput(outputWriter, errorWriter)
		return process
	}
}
