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

	private void prepare(Closure assertText) {
		String text = 'text'
		int length = 24648
		MockFor document = new MockFor(Document.class)
		document.demand.getLength(1) { return length }
		document.demand.insertString(1) { int offset, String str, AttributeSet a ->
			assert offset == length
			assertText(str)
			assert Color.BLUE == StyleConstants.getForeground(a)
		}

		documentWriter = new DocumentWriter(document: document.proxyInstance(), textColor: Color.BLUE)
	}

	void testAppendText() {
		String text = 'text'
		prepare({String str -> assert text == str})
		documentWriter.append(text)
	}

	void testAppendSubSequence() {
		String text = 'text'
		prepare({String str -> assert text.subSequence(1, 4) == str})
		documentWriter.append(text, 1, 4)
	}

	void testAppendChar() {
		char c = 't'
		prepare({String str -> assert c == str})
		documentWriter.append(c)
	}
}
