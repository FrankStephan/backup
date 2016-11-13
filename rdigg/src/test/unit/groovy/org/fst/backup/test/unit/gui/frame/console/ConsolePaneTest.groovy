package org.fst.backup.test.unit.gui.frame.console

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.border.TitledBorder
import javax.swing.text.PlainDocument

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.console.ConsolePane
import org.fst.backup.test.AbstractTest

class ConsolePaneTest extends AbstractTest {

	private static final String INITIAL_TITLE = 'Status:'
	private static final Color INITIAL_COLOR = Color.GRAY

	JScrollPane pane
	JTextArea textArea
	TitledBorder border
	CommonViewModel commonViewModel

	void setUp() {
		super.setUp()
		commonViewModel = new CommonViewModel()
		commonViewModel.consoleDocument = new PlainDocument()
		commonViewModel.consoleStatus = INITIAL_TITLE
		commonViewModel.consoleStatusColor = INITIAL_COLOR
		pane = new ConsolePane().createComponent(commonViewModel, new SwingBuilder())
		textArea = pane.viewport.view
		border = pane.border
	}

	void testConsoleIsEmptyInitially() {
		assert '' == textArea.text
	}

	void testStatusIsTakenFromViewModel() {
		assert INITIAL_TITLE == border.title
	}

	void testStatusColorIsTakenFromViewModel() {
		assert INITIAL_COLOR == border.titleColor
	}

	void testStatusChangesAccordingToViewModel() {
		commonViewModel.consoleStatus = 'Status: Laufend'
		assert 'Status: Laufend' == border.title
	}

	void testStatusColorChangesAccordingToViewModel() {
		commonViewModel.consoleStatusColor = Color.RED
		assert Color.RED == border.titleColor
	}
}
