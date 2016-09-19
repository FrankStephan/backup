package org.fst.backup.test.unit_test.ui

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import javax.swing.JFileChooser

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.BorderedFileChooser

class BorderedFileChooserTest extends AbstractTest {

	void testSetDirClosureIsInvokedWhenSelectedFileChanges() {
		boolean isSetDirClosureInvoked = false

		def swingMock = new MockFor(SwingBuilder.class)
		swingMock.ignore(~'.*')
		JFileChooser fc = new BorderedFileChooser().createComponent('', swingMock.proxyInstance(), { isSetDirClosureInvoked = true } )
		fc.selectedFile = sourceDir
		assert true == isSetDirClosureInvoked
	}
}
