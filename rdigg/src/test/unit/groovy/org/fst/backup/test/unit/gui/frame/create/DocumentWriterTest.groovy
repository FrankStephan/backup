package org.fst.backup.test.unit.gui.frame.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import java.awt.Color

import javax.swing.text.AttributeSet
import javax.swing.text.Document
import javax.swing.text.PlainDocument
import javax.swing.text.StyleConstants

import org.fst.backup.gui.frame.create.DocumentWriter

class DocumentWriterTest extends GroovyTestCase {

	void testCallbackIsWrittenToDocument() {
		String text = 'text'
		int length = 24648
		MockFor documentMock = new MockFor(Document.class)
		documentMock.demand.getLength(1) { return length }
		documentMock.demand.insertString(1) { int offset, String str, AttributeSet a ->
			assert offset == length
			assert text == str
			assert Color.BLUE == StyleConstants.getForeground(a)
		}

		DocumentWriter documentWriter = new DocumentWriter(document: documentMock.proxyInstance(), textColor: Color.BLUE)
		documentWriter.callback(text)
	}

	void testSynchronizesConcurrentWrites() {
		Document doc = new PlainDocument()
		DocumentWriter documentWriter2 = new DocumentWriter(document: doc , textColor: Color.RED)

		Thread parallelCallbackThread = new Thread({ -> documentWriter2.callback('writer2') })

		ProxyMetaClass documentWriterProxy = ProxyMetaClass.getInstance(DocumentWriter.class)
		documentWriterProxy.interceptor = new Interceptor() {
					@Override
					public Object beforeInvoke(Object object, String methodName,
							Object[] arguments) {
						if ('callback' == methodName) {
							parallelCallbackThread.start()
							parallelCallbackThread.join()
						}
						return null
					}

					@Override
					public Object afterInvoke(Object object, String methodName,
							Object[] arguments, Object result) {
						return result
					}

					@Override
					public boolean doInvoke() {
						return true
					}
				}

		documentWriterProxy.use {
			DocumentWriter documentWriter1 = new DocumentWriter(document: doc, textColor: Color.BLACK)
			documentWriter1.callback('writer1')
		}

		assert 'writer2writer1' == doc.getText(0, doc.getLength())
	}
}
