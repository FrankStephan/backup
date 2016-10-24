package org.fst.backup.test.unit.gui

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.JFileChooser

import org.fst.backup.test.AbstractTest
import org.fst.backup.gui.BorderedFileChooser

class BorderedFileChooserTest extends AbstractTest {

	void testSetDirClosureIsInvokedWhenSelectedFileChanges() {
		boolean isSetDirClosureInvoked = false
		JFileChooser fc = new BorderedFileChooser().createComponent('', new SwingBuilder(), { isSetDirClosureInvoked = true } )
		fc.selectedFile = sourceDir
		assert true == isSetDirClosureInvoked
	}
}
