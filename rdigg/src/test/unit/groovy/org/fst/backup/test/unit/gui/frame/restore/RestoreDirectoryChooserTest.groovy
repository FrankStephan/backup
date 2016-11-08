package org.fst.backup.test.unit.gui.frame.restore

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import org.fst.backup.gui.BorderedFileChooser
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.restore.RestoreDirectoryChooser
import org.fst.backup.test.AbstractTest

class RestoreDirectoryChooserTest extends AbstractTest {

	void testRestoreDirChangesOnFileSelection() {
		def commonViewModel = new CommonViewModel()
		def borderedFileChooser = new MockFor(BorderedFileChooser.class)
		borderedFileChooser.demand.createComponent(1) { String text, SwingBuilder swing, File currentDirectory, Closure setDir ->
			setDir(sourceDir)
			assert sourceDir == commonViewModel.restoreDir
		}

		borderedFileChooser.use {
			def fc = new RestoreDirectoryChooser().createComponent(commonViewModel, new SwingBuilder())
		}
	}

	void testCurrentDirIsFileChooserDefaultInitially() {
		def commonViewModel = new CommonViewModel()
		commonViewModel.restoreDir = restoreDir
		def borderedFileChooser = new MockFor(BorderedFileChooser.class)
		borderedFileChooser.demand.createComponent(1) { String text, SwingBuilder swing, File currentDirectory, Closure setDir ->
			assert null == currentDirectory
		}
		borderedFileChooser.use {
			def fc = new RestoreDirectoryChooser().createComponent(commonViewModel, new SwingBuilder())
		}
	}
}
