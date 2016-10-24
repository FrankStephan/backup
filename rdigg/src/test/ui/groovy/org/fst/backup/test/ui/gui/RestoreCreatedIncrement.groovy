package org.fst.backup.test.ui.gui

import static org.fst.backup.test.ui.gui.UITestStep.*
import static org.junit.Assert.*

import javax.swing.JList

class RestoreCreatedIncrement extends AbstractUITest {

	void test() {
		CREATE_SOME_SOURCE_FILES.execute()

		CREATE_INCREMENT.execute()
		CHOOSE_TARGET_DIR.execute()

		JList incrementsList
		LIST_INCREMENTS.execute(null) { incrementsList = it }

		SELECT_INCREMENT_TO_RESTORE.execute([incrementsList: incrementsList, selectionIndex: 0]) {}

		RESTORE_INCREMENT.execute()

		assertRestoreDirContainsFilesFromSource()
	}

	private assertRestoreDirContainsFilesFromSource() {
		assert subPaths(sourceDir) == subPaths(restoreDir)
	}
}
