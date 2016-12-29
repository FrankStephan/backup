package org.fst.backup.test.unit.gui.frame.console

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.JCheckBox

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.console.ShutdownSystemCheckbox

class ShutdownSystemCheckboxTest extends GroovyTestCase {

	void testTakesInitialSelectedStateFromCommonViewModel() {
		CommonViewModel commonViewModel = new CommonViewModel()
		JCheckBox checkbox = new ShutdownSystemCheckbox().createComponent(commonViewModel, new SwingBuilder())
		assert false == checkbox.selected

		commonViewModel = new CommonViewModel()
		commonViewModel.shutdownSystemOnFinish = true
		checkbox = new ShutdownSystemCheckbox().createComponent(commonViewModel, new SwingBuilder())
		assert true == checkbox.selected
	}

	void testChangesSelectedState() {
		CommonViewModel commonViewModel = new CommonViewModel()
		JCheckBox checkbox = new ShutdownSystemCheckbox().createComponent(commonViewModel, new SwingBuilder())
		commonViewModel.shutdownSystemOnFinish = true
		assert true == checkbox.selected
	}

	void testChangesCommonViewModel() {
		CommonViewModel commonViewModel = new CommonViewModel()
		JCheckBox checkbox = new ShutdownSystemCheckbox().createComponent(commonViewModel, new SwingBuilder())
		checkbox.selected = true
		assert true == commonViewModel.shutdownSystemOnFinish
	}
}
