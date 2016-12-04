package org.fst.backup.test.unit.gui.frame.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import org.fst.backup.gui.BorderedFileChooser
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.create.SourceFileChooser
import org.fst.backup.test.AbstractTest

class SourceFileChooserTest extends AbstractTest {

	void testSourceDirChangesOnFileSelection() {
		def commonViewModel = new CommonViewModel()
		def borderedFileChooser = new MockFor(BorderedFileChooser.class)
		borderedFileChooser.demand.createComponent(1) { String text, SwingBuilder swing, File currentDirectory, Closure setDir ->
			setDir(sourceDir)
			assert sourceDir == commonViewModel.sourceDir
		}
		borderedFileChooser.use {
			def fc = new SourceFileChooser().createComponent(commonViewModel, new SwingBuilder())
		}
	}

	void testSourceDirIsSetAsCurrentDirInitially() {
		def commonViewModel = new CommonViewModel()
		commonViewModel.sourceDir = sourceDir
		def borderedFileChooser = new MockFor(BorderedFileChooser.class)
		borderedFileChooser.demand.createComponent(1) { String text, SwingBuilder swing, File currentDirectory, Closure setDir ->
			assert sourceDir == currentDirectory
		}
		borderedFileChooser.use {
			def fc = new SourceFileChooser().createComponent(commonViewModel, new SwingBuilder())
		}
	}
}
