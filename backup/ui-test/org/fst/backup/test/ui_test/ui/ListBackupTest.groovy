package org.fst.backup.test.ui_test.ui

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.JList
import javax.swing.text.PlainDocument

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.choose.BackupDirectoryChooser
import org.fst.backup.ui.frame.choose.IncrementsList
import org.fst.backup.ui.frame.create.CreateBackupButton
import org.fst.backup.ui.frame.create.SourceFileChooser
import org.fst.backup.ui.frame.create.TargetFileChooser

class ListBackupTest extends AbstractUITest {

	void testIncrementsListShowsOneIncrementAfterBackup() {
		
		SystemUITestStep.LIST_INCREMENTS.verify { JList it ->
			assert null == it.getCellBounds(0, 0)
		}

		SystemUITestStep.CREATE_SOME_SOURCE_FILES.execute()
		SystemUITestStep.CREATE_BACKUP.execute()
		SystemUITestStep.CHOOSE_TARGET_DIR.execute()
		
		SystemUITestStep.LIST_INCREMENTS.verify { JList it ->
			assert null != it.getCellBounds(0, 0)
		}
	}
}