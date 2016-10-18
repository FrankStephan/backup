package org.fst.backup.test.unit_test.ui.console;

import static org.junit.Assert.*

import javax.swing.JScrollPane;

import groovy.mock.interceptor.StubFor
import groovy.swing.SwingBuilder

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.console.ConsoleTextArea

class ConsoleTextAreaTest extends AbstractTest {

	CommonViewModel commonViewModel
	SwingBuilder swing
	
	
	
	public void setUp() {
		super.setUp();
		commonViewModel = new CommonViewModel()
		final swingStub = new StubFor(SwingBuilder.class)
//		swingStub.demand.button(1) { Map it ->
//			return new SwingBuilder().button(it)
//		}
//		swingStub.demand.doOutside (1..2) {Closure it -> it()}
		swing = swingStub.proxyInstance()
//		button = new RestoreBackupButton().createComponent(commonViewModel, swing, onFinish)
	}



	void testComponentIsAScrollPane() {
		JScrollPane component = new ConsoleTextArea().createComponent(commonViewModel, swing)
	}
	
	
	
	

}
