package org.fst.backup.test.ui_test.ui

import static org.fst.backup.test.ui_test.ui.UITestStep.*
import static org.junit.Assert.*

import javax.swing.JList

class ListCreatedIncrementTest extends AbstractUITest {

	void testIncrementsListShowsOneIncrementAfterBackup() {

		LIST_INCREMENTS.verify { JList it ->
			assert null == it.getCellBounds(0, 0)
		}

		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_BACKUP.execute()
		CHOOSE_TARGET_DIR.execute()

		LIST_INCREMENTS.verify { JList it ->
			assert null != it.getCellBounds(0, 0)
		}
	}
}