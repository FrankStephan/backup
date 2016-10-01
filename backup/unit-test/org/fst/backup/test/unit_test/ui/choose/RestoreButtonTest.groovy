package org.fst.backup.test.unit_test.ui.choose

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.JButton

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.restore.RestoreButton

class RestoreButtonTest extends AbstractTest {

	CommonViewModel commonViewModel
	JButton button
	void setUp() {
		super.setUp()
		commonViewModel = new CommonViewModel()
		commonViewModel.targetDir = targetDir
		button = new RestoreButton().createComponent(commonViewModel, new SwingBuilder())
	}

	void testTargetDirIsSelectedAsIncrement() {
		button.doClick()
		assert commonViewModel.selectedIncrement.increment.targetPath == targetPath

		fail("Not yet implemented")
	}

	void testRestoreTabIsOpened() {
		fail()
	}
}
