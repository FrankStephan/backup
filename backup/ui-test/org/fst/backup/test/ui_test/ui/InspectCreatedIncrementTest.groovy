package org.fst.backup.test.ui_test.ui

import static org.fst.backup.test.ui_test.ui.UITestStep.*
import static org.junit.Assert.*

class InspectCreatedIncrementTest extends AbstractUITest {

	void test() {
		CREATE_BACKUP.execute()
		LIST_INCREMENTS.execute()
	}
}
