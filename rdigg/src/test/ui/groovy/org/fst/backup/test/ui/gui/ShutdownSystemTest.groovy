package org.fst.backup.test.ui.gui

import static org.fst.backup.test.ui.gui.UITestStep.*
import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import javax.swing.JFrame
import javax.swing.JList

import org.fst.backup.gui.frame.console.ShutdownSystemCheckbox
import org.fst.backup.service.ShutdownSystemService

class ShutdownSystemTest extends AbstractUITest {

	MockFor shutdownSystemService

	public void setUp() {
		super.setUp()
		shutdownSystemService = new MockFor(ShutdownSystemService.class)
		shutdownSystemService.demand.shutdown(1) { JFrame frame -> }
		CREATE_SOME_SOURCE_FILES.execute()
	}

	void testShutdownAfterBackup() {
		shutdownSystemService.use {
			CREATE_INCREMENT.execute([onFinish:{clickShutdownSystemCheckBox()}])
		}
	}

	void testShutdownAfterRestore() {
		CREATE_INCREMENT.execute()
		CHOOSE_TARGET_DIR.execute()

		JList incrementsList
		LIST_INCREMENTS.execute(null) { incrementsList = it }

		SELECT_INCREMENT_TO_RESTORE.execute([incrementsList: incrementsList, selectionIndex: 0]) {}

		shutdownSystemService.use {
			RESTORE_INCREMENT.execute([onFinish:{clickShutdownSystemCheckBox()}])
		}
	}

	private void clickShutdownSystemCheckBox() {
		new ShutdownSystemCheckbox().createComponent(commonViewModel, swing).doClick()
	}

	void tearDown() {
		super.tearDown()
		clickShutdownSystemCheckBox()
	}
}
