package org.fst.shuffle_my_music.service

import javax.swing.JFrame

class ShutdownSystemService {

	void shutdown(JFrame frame) {
		initiateShutdownAfter(10)
		frame.dispose()
	}

	private void initiateShutdownAfter(int seconds) {
		('cmd /c shutdown.exe -s -t ' + seconds).execute()
	}
}
