package org.fst.backup.rdiff

import groovy.transform.TupleConstructor

import org.fst.backup.model.CommandLineCallback

@TupleConstructor
class CommandLineCallbackWriter extends Writer {
	
	CommandLineCallback commandLineCallback

	@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			commandLineCallback.callback(String.valueOf(cbuf, off, len))
		}

		@Override
		public void flush() throws IOException {
		}

		@Override
		public void close() throws IOException {
		}
}
