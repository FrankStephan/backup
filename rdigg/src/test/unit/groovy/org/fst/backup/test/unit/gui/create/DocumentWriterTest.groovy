package org.fst.backup.test.unit.gui.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import java.awt.Color

import javax.swing.text.AttributeSet
import javax.swing.text.Document
import javax.swing.text.StyleConstants

import org.fst.backup.gui.frame.create.DocumentWriter
import org.fst.backup.test.AbstractTest

class DocumentWriterTest extends AbstractTest {

	DocumentWriter documentWriter

	public void testCallbackIsWrittenToDocument() {
		String text = 'text'
		int length = 24648
		MockFor document = new MockFor(Document.class)
		document.demand.getLength(1) { return length }
		document.demand.insertString(1) { int offset, String str, AttributeSet a ->
			assert offset == length
			assert text == str
			assert Color.BLUE == StyleConstants.getForeground(a)
		}

		documentWriter = new DocumentWriter(document: document.proxyInstance(), textColor: Color.BLUE)
		documentWriter.callback(text)
	}
}
