package org.fst.backup.ui.frame.console

import groovy.swing.SwingBuilder

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextArea
import javax.swing.text.JTextComponent;

import org.fst.backup.ui.CommonViewModel

class ConsoleTextArea {
	
	def createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		return new SwingBuilder().textArea()
		
	}
	

}
