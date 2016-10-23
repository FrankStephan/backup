package org.fst.backup.test.unit_test.ui.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.BorderedFileChooser
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.create.SourceFileChooser

class SourceFileChooserTest extends AbstractTest {

	void testSourceDirChangesOnFileSelection() {
		def commonViewModel = new CommonViewModel()
		def borderedFileChooser = new MockFor(BorderedFileChooser.class)
		borderedFileChooser.demand.createComponent(1) { String text, SwingBuilder swing, Closure setDir ->
			setDir(sourceDir)
			assert sourceDir == commonViewModel.sourceDir
		}
		borderedFileChooser.use {
			def fc = new SourceFileChooser().createComponent(commonViewModel, new SwingBuilder())
		}
	}
}
