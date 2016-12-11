
package org.fst.backup.test.unit.gui.frame.console

import static org.junit.Assert.*

import javax.swing.text.Document
import javax.swing.text.SimpleAttributeSet

import org.fst.backup.gui.frame.console.LimitedLengthDocument
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized.class)
class LimitedLengthDocumentTest {

	@Parameters
	public static Collection params() {

		Closure insertFittingStrings = { Document doc ->
			doc.insertString(doc.getLength(), '12', new SimpleAttributeSet())
			doc.insertString(doc.getLength(), '34', null)
		}

		Closure insertSingleTooLongString = { Document doc ->
			doc.insertString(doc.getLength(), '12345', null)
		}

		Closure insertSingleTooLongStringAsSecond = { Document doc ->
			doc.insertString(doc.getLength(), 'ab', null)
			doc.insertString(doc.getLength(), 'cdefg', null)
		}

		Closure insertTwoStringsToExceedMaxLength = { Document doc ->
			doc.insertString(doc.getLength(), '123', null)
			doc.insertString(doc.getLength(), '45', null)
		}

		Closure insertLoop = { Document doc ->
			StringBuilder sb = new StringBuilder()
			for (int i=0; i<10; i++) {
				doc.insertString(doc.getLength(), String.valueOf(i), null)
			}
		}

		return Arrays.asList(
				[insertFittingStrings, '1234'].toArray(),
				[insertSingleTooLongString, '2345'].toArray(),
				[insertSingleTooLongStringAsSecond, 'defg'].toArray(),
				[insertTwoStringsToExceedMaxLength, '2345'].toArray(),
				[insertLoop, '6789'].toArray())
	}

	LimitedLengthDocument doc
	Closure doInsert
	String expectedText

	public LimitedLengthDocumentTest(Closure doInsert, String expectedText) {
		this.doInsert = doInsert
		this.expectedText = expectedText
	}

	@Before
	public void setUp() {
		doc = new LimitedLengthDocument(maxLength: 4)
	}

	@Test
	void testDocumentHasLengthZeroInitially() {
		assert 0 == doc.getLength()
	}

	@Test
	void testDocumentLengthDoesNotExceedMaxLength() {
		doInsert(doc)
		assert doc.getLength() <= doc.maxLength
	}

	@Test
	void testDocumentCutsOffFirstChars() {
		doInsert(doc)
		assert expectedText == doc.getText(0, doc.getLength())
	}
}
