package org.fst.backup.test.ui_test.ui

import static org.fst.backup.test.ui_test.ui.UITestStep.*
import static org.junit.Assert.*

import javax.swing.JFileChooser
import javax.swing.JList

import org.fst.backup.ui.Tab

class InspectCreatedIncrementTest extends AbstractUITest {

	void testIncrementContentsAreDisplayedOnInspectTab() {
		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_BACKUP.execute()
		CHOOSE_TARGET_DIR.execute()

		JList incrementsList = LIST_INCREMENTS.execute()
		INSPECT_INCREMENT.verify([incrementsList: incrementsList, selectionIndex: 0]) {JFileChooser fc ->
			assert Tab.INSPECT.ordinal() == commonViewModel.tabsModel.selectedIndex
			assert ['a0', 'a0.suf', 'b0.suf', 'c0.suf']== fc.getCurrentDirectory().list()
		}
	}
}
