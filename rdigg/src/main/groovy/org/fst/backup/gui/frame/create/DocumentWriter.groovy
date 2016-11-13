package org.fst.backup.gui.frame.create

import groovy.transform.TupleConstructor

import java.awt.Color

import javax.swing.text.Document
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

@TupleConstructor
class DocumentWriter implements Appendable {

	private Document document
	private Color textColor

	private SimpleAttributeSet color() {
		SimpleAttributeSet attributeSet = new SimpleAttributeSet()
		StyleConstants.setForeground(attributeSet, textColor)
		return attributeSet
	}

	@Override
	public Appendable append(CharSequence csq) throws IOException {
		document.insertString(document.getLength(), csq.toString(), color())
		return this
	}

	@Override
	public Appendable append(CharSequence csq, int start, int end)
	throws IOException {
		return append(csq.subSequence(start, end))
	}

	@Override
	public Appendable append(char c) throws IOException {
		return append(c.toString())
	}
}
