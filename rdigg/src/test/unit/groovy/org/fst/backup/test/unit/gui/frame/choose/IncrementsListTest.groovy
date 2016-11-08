package org.fst.backup.test.unit.gui.frame.choose

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.JList
import javax.swing.ListSelectionModel

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.choose.IncrementsList

class IncrementsListTest extends GroovyTestCase {

	JList list
	CommonViewModel commonViewModel = new CommonViewModel()
	SwingBuilder swing

	void setUp() {
		super.setUp()
		commonViewModel.incrementsListModel = new DefaultListModel<>()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		swing = new SwingBuilder()
		list = new IncrementsList().createComponent(commonViewModel, swing)
	}

	void testListUsesIncrementsListModel() {
		assert commonViewModel.incrementsListModel == list.getModel()
	}

	void testListUsesIncrementsListSelectionModel() {
		assert commonViewModel.incrementsListSelectionModel == list.getSelectionModel()
	}

	void testListHasSingleSelection() {
		assert ListSelectionModel.SINGLE_SELECTION == list.getSelectionMode()
	}
}
