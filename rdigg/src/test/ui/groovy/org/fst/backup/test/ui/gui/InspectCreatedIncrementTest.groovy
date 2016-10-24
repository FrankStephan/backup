package org.fst.backup.test.ui.gui

import static org.fst.backup.test.ui.gui.UITestStep.*
import static org.junit.Assert.*

import javax.swing.JFileChooser
import javax.swing.JList

class InspectCreatedIncrementTest extends AbstractUITest {

	void test() {
		CREATE_SOME_SOURCE_FILES.execute()

		JFileChooser sfc
		CREATE_INCREMENT.execute(null) { sfc = it }
		CHOOSE_TARGET_DIR.execute()

		JList incrementsList
		LIST_INCREMENTS.execute(null) { incrementsList = it }
		JFileChooser ifc
		INSPECT_INCREMENT.execute([incrementsList: incrementsList, selectionIndex: 0]) { ifc = it }

		assertIncrementContainsFilesFromSource(sfc, ifc)
	}

	private assertIncrementContainsFilesFromSource(JFileChooser sfc, JFileChooser ifc) {
		assert subPaths(sfc.selectedFile) == subPaths(ifc.getCurrentDirectory())
	}
}
