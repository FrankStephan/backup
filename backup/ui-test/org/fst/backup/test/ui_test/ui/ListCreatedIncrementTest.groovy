package org.fst.backup.test.ui_test.ui

import static org.fst.backup.test.ui_test.ui.UITestStep.*
import static org.junit.Assert.*

import java.awt.Rectangle

import javax.swing.JList

class ListCreatedIncrementTest extends AbstractUITest {

	void test() {

		JList incrementsList
		LIST_INCREMENTS.execute(null) { incrementsList = it }
		assert null == firstEntry(incrementsList)

		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_BACKUP.execute()
		CHOOSE_TARGET_DIR.execute()


		LIST_INCREMENTS.execute()
		assert null != firstEntry(incrementsList)
	}

	private Rectangle firstEntry(JList incrementsList) {
		return incrementsList.getCellBounds(0, 0)
	}
}