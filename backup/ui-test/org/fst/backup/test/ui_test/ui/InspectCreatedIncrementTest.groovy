package org.fst.backup.test.ui_test.ui

import static org.fst.backup.test.ui_test.ui.UITestStep.*
import static org.junit.Assert.*

import javax.swing.JFileChooser
import javax.swing.JList

import org.fst.backup.ui.Tab

class InspectCreatedIncrementTest extends AbstractUITest {

	void test() {
		CREATE_SOME_SOURCE_FILES.execute()

		JFileChooser sfc
		CREATE_BACKUP.execute(null) { sfc = it }
		CHOOSE_TARGET_DIR.execute()

		JList incrementsList
		LIST_INCREMENTS.execute(null) { incrementsList = it }
		JFileChooser rfc
		INSPECT_INCREMENT.execute([incrementsList: incrementsList, selectionIndex: 0]) { rfc = it }

		assertInspectTabIsOpened()
		assertIncrementContainsFilesFromSource(sfc, rfc)
	}

	private assertInspectTabIsOpened() {
		assert Tab.INSPECT.ordinal() == commonViewModel.tabsModel.selectedIndex
	}

	private assertIncrementContainsFilesFromSource(JFileChooser sfc, JFileChooser rfc) {
		assert subPaths(sfc.selectedFile) == subPaths (rfc.getCurrentDirectory())
	}
}
