package org.fst.backup.test.unit.gui.frame.console

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.JScrollPane
import javax.swing.text.PlainDocument

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.console.ConsolePane
import org.fst.backup.test.AbstractTest

class ConsolePaneTest extends AbstractTest {

	JScrollPane pane
	CommonViewModel commonViewModel

	void setUp() {
		super.setUp()
		commonViewModel = new CommonViewModel()
		commonViewModel.consoleDocument = new PlainDocument()
		pane = new ConsolePane().createComponent(commonViewModel, new SwingBuilder())
	}

	void testConsoleIsEmptyInitially() {
	}


	void testStatusChanges() {
		//		CommonViewModel commonViewModel = new CommonViewModel()
		//
		//		new ConsolePane().createComponent(commonViewModel, new SwingBuilder())
	}
}
