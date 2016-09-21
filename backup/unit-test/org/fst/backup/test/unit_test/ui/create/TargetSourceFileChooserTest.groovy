package org.fst.backup.test.unit_test.ui.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.BorderedFileChooser
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.create.TargetFileChooser

class TargetSourceFileChooserTest extends AbstractTest {

	void testTargetDirChangesOnFileSelection() {
		def commonViewModel = new CommonViewModel()
		def borderedFileChooser = new MockFor(BorderedFileChooser.class)
		borderedFileChooser.demand.createComponent(1) { String text, SwingBuilder swing, Closure setDir ->
			setDir()
			assert targetDir == commonViewModel.targetDir
		}
		def fc = new TargetFileChooser().createComponent(commonViewModel, new SwingBuilder())
		fc.selectedFile = targetDir
	}
}
