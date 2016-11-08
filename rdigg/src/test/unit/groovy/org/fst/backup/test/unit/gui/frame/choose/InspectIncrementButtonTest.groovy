package org.fst.backup.test.unit.gui.frame.choose

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.JButton
import javax.swing.ListSelectionModel

import org.fst.backup.test.AbstractTest
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.IncrementListEntry
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.choose.InspectIncrementButton

class InspectIncrementButtonTest extends AbstractTest {

	JButton button
	CommonViewModel commonViewModel

	void setUp() {
		super.setUp()
		commonViewModel = new CommonViewModel()
		commonViewModel.incrementsListModel = new DefaultListModel<>()
		commonViewModel.incrementsListModel.add(0, new IncrementListEntry('18.09.16 16:08:04', createIncrement()))
		commonViewModel.incrementsListModel.add(1, new IncrementListEntry('18.09.16 16:08:05', createIncrement()))
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		commonViewModel.incrementsListSelectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION
		commonViewModel.tabsModel = new DefaultSingleSelectionModel()
		commonViewModel.tabsModel.selectedIndex = Tab.CHOOSE.ordinal()
		button = new InspectIncrementButton().createComponent(commonViewModel, new SwingBuilder())
	}

	void testNothingHappensIfIncrementsListIsEmpty() {
		commonViewModel.incrementsListModel.clear()
		button.doClick()
		assert Tab.CHOOSE.ordinal() == commonViewModel.tabsModel.selectedIndex
		assert null == commonViewModel.selectedIncrement
	}

	void testNothingHappensIfNoIncrementIsSelected() {
		button.doClick()

		assert Tab.CHOOSE.ordinal() == commonViewModel.tabsModel.selectedIndex
		assert null == commonViewModel.selectedIncrement
	}

	void testSelectedIncrementFromIncrementsListIsUsed() {
		commonViewModel.incrementsListSelectionModel.setSelectionInterval(0, 0)
		button.doClick()
		assert commonViewModel.incrementsListModel.get(0) == commonViewModel.selectedIncrement

		commonViewModel.incrementsListSelectionModel.setSelectionInterval(1, 1)
		button.doClick()
		assert commonViewModel.incrementsListModel.get(1) == commonViewModel.selectedIncrement
	}

	void testInspectTabIsOpened() {
		commonViewModel.incrementsListSelectionModel.setSelectionInterval(0, 0)
		button.doClick()
		assert Tab.INSPECT.ordinal() == commonViewModel.tabsModel.selectedIndex
	}

	void testSelectedIncrementIsSetToNullIfIfIncrementsListGetsEmpty() {
		commonViewModel.incrementsListSelectionModel.setSelectionInterval(0, 0)
		button.doClick()
		assert commonViewModel.incrementsListModel.get(0) == commonViewModel.selectedIncrement

		commonViewModel.tabsModel.selectedIndex = Tab.CHOOSE.ordinal()

		commonViewModel.incrementsListModel.clear()
		button.doClick()
		assert null == commonViewModel.selectedIncrement
	}
}
