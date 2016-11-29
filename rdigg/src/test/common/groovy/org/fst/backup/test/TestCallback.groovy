package org.fst.backup.test

import org.fst.backup.model.CommandLineCallback

class TestCallback implements CommandLineCallback {
	
	StringBuffer sb = new StringBuffer()

	@Override
	public void callback(String commandLineData) {
		sb.append(commandLineData)
	}

	@Override
	public String toString() {
		return sb.toString()
	}
	

}
