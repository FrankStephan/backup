package org.fst.backup.test.unit.gui.frame.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import org.fst.backup.gui.BorderedFileChooser
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.create.TargetFileChooser
import org.fst.backup.test.AbstractTest

class TargetFileChooserTest extends AbstractTest {

	void testTargetDirChangesOnFileSelection() {
		def commonViewModel = new CommonViewModel()
		def borderedFileChooser = new MockFor(BorderedFileChooser.class)
		borderedFileChooser.demand.createComponent(1) { String text, SwingBuilder swing, File currentDirectory, Closure setDir ->
			setDir(targetDir)
			assert targetDir == commonViewModel.targetDir
		}

		borderedFileChooser.use {
			def fc = new TargetFileChooser().createComponent(commonViewModel, new SwingBuilder())
		}
	}

	void testTargetDirIsSetAsCurrentDirInitially() {
		def commonViewModel = new CommonViewModel()
		commonViewModel.targetDir = targetDir
		def borderedFileChooser = new MockFor(BorderedFileChooser.class)
		borderedFileChooser.demand.createComponent(1) { String text, SwingBuilder swing, File currentDirectory, Closure setDir ->
			assert targetDir == currentDirectory
		}
		borderedFileChooser.use {
			def fc = new TargetFileChooser().createComponent(commonViewModel, new SwingBuilder())
		}
	}
}
