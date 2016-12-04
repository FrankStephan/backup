package org.fst.backup.service

import org.fst.backup.model.CommandLineCallback

class StringBufferCallback implements CommandLineCallback {

	StringBuffer buffer = new StringBuffer()

	@Override
	public void callback(String commandLineData) {
		buffer.append(commandLineData)
	}

	public String toString() {
		return buffer.toString()
	}
}
