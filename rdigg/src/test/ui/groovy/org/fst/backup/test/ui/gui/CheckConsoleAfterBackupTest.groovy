package org.fst.backup.test.ui.gui

import static org.fst.backup.test.ui.gui.UITestStep.*
import static org.junit.Assert.*

import org.fst.backup.test.IncrementDestroyer

class CheckConsoleAfterBackupTest extends AbstractUITest {

	void testCmdLinesAndErrAreWritten() {

		CREATE_SOME_SOURCE_FILES.execute()
		IncrementDestroyer.destroyIncrementBeforeVerification(new File(targetPath, 'a0.suf')).use {
			CREATE_INCREMENT.execute()
		}
		String consoleContent
		VIEW_CONSOLE.execute(null, {it -> consoleContent = it})
		assert consoleContent.contains('Using rdiff-backup version 1.2.8')
		assert consoleContent.contains('Could not restore file a0.suf')
	}
}
