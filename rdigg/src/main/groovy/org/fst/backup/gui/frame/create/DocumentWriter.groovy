package org.fst.backup.gui.frame.create

import groovy.transform.TupleConstructor

import java.awt.Color

import javax.swing.text.Document
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

import org.fst.backup.model.CommandLineCallback

@TupleConstructor
class DocumentWriter implements CommandLineCallback {

	private Document document
	private Color textColor

	private SimpleAttributeSet color() {
		SimpleAttributeSet attributeSet = new SimpleAttributeSet()
		StyleConstants.setForeground(attributeSet, textColor)
		return attributeSet
	}

	@Override
	public void callback(String commandLineData) {
		document.insertString(document.getLength(), commandLineData, color())
	}

}
