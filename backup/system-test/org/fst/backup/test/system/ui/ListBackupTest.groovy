package org.fst.backup.test.system.ui

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.JFileChooser

import org.fst.backup.ui.BorderedFileChooser
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.create.CreateBackupButton

class ListBackupTest extends GroovyTestCase {

	void test() {
		SwingBuilder swing = new SwingBuilder()
		JFileChooser sfc = new BorderedFileChooser().createComponent('Quellverzeichnis', swing, { commonViewModel.sourceDir = it } )


		CommonViewModel commonViewModel = new CommonViewModel()
		new CreateBackupButton().createComponent(swing, {}).doClick()
	}
}