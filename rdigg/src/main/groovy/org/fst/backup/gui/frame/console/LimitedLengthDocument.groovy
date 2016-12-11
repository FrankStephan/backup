package org.fst.backup.gui.frame.console

import groovy.transform.TupleConstructor

import javax.swing.text.AttributeSet
import javax.swing.text.BadLocationException
import javax.swing.text.PlainDocument

@TupleConstructor
class LimitedLengthDocument extends PlainDocument {

	int maxLength

	@Override
	public void insertString(int offs, String str, AttributeSet a)
	throws BadLocationException {
		if (!maxLengthWillBeExceeded(str)) {
			super.insertString(offs, str, a)
		} else {
			if (insertedStringLengthIsGreaterThanMaxLength(str)) {
				replace(0, getLength(), str.substring(str.length() - maxLength), a)
			} else {
				int lengthToRemove = getLength() + str.length() - maxLength
				remove(0, lengthToRemove)
				super.insertString(offs-lengthToRemove, str, a)
			}
		}
	}

	private boolean maxLengthWillBeExceeded(String str) {
		int newLength = getLength() + str.length()
		return newLength > maxLength
	}

	private boolean insertedStringLengthIsGreaterThanMaxLength(String str) {
		return str.length() > maxLength
	}
}
