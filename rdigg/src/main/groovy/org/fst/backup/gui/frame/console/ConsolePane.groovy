package org.fst.backup.gui.frame.console

import groovy.swing.SwingBuilder

import java.awt.Font

import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.border.TitledBorder
import javax.swing.text.DefaultCaret

import org.fst.backup.gui.CommonViewModel

class ConsolePane {

	JScrollPane createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		TitledBorder consoleScrollPaneBorder = swing.titledBorder()
		JScrollPane consoleScrollPane = swing.scrollPane(border: consoleScrollPaneBorder) {
			JTextArea console = swing.textArea()
			console.document = commonViewModel.consoleDocument
			DefaultCaret caret = (DefaultCaret)console.getCaret()
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE)
			console.setFont(Font.decode('Monospaced'))
			console.editable = false
		}

		observeProcessStatus(commonViewModel, consoleScrollPaneBorder, swing)
		return consoleScrollPane
	}

	private void observeProcessStatus(CommonViewModel commonViewModel, TitledBorder consoleScrollPaneBorder, SwingBuilder swing) {
		swing.bind(source: commonViewModel, sourceProperty: 'consoleStatus', target: consoleScrollPaneBorder, targetProperty: 'title')
		swing.bind(source: commonViewModel, sourceProperty: 'consoleStatusColor', target: consoleScrollPaneBorder, targetProperty: 'titleColor')
	}
}
