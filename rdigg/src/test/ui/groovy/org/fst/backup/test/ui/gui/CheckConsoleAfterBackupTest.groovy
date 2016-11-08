package org.fst.backup.test.ui.gui

import static org.fst.backup.test.ui.gui.UITestStep.*
import static org.junit.Assert.*

class CheckConsoleAfterBackupTest extends AbstractUITest {

	void test() {

		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute()
	}
}
