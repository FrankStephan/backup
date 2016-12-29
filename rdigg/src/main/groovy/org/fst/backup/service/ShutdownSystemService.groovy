package org.fst.backup.service

import javax.swing.JFrame

import org.apache.logging.log4j.LogManager

class ShutdownSystemService {

	void shutdown(JFrame frame) {
		LogManager.getLogger(this.getClass()).info('>> Initiating system shutdown')
		flushLogEvents()
		initiateShutdownAfter(10)
		frame.dispose()
	}

	private void flushLogEvents() {
		LogManager.shutdown()
	}

	private void initiateShutdownAfter(int seconds) {
		('cmd /c shutdown.exe -s -t ' + seconds).execute()
	}
}
