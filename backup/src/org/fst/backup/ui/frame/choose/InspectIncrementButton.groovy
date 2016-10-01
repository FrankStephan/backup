package org.fst.backup.ui.frame.choose

import groovy.swing.SwingBuilder

import javax.swing.JButton

import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.Tab


class InspectIncrementButton {

	JButton createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		swing.button(text: 'Durchsuchen ->', actionPerformed: {
			if (!commonViewModel.incrementsListModel.empty) {
				int leadSelectionIndex = commonViewModel.incrementsListSelectionModel.leadSelectionIndex
				if (leadSelectionIndex >= 0) {
					chooseSelectedIncrementForInspection(commonViewModel, leadSelectionIndex)
					switchToInspectTab(commonViewModel)
				}
			} else {
				clearSelectedIncrement(commonViewModel)
			}
		})
	}

	private void chooseSelectedIncrementForInspection(CommonViewModel commonViewModel, int leadSelectionIndex) {
		commonViewModel.selectedIncrement = commonViewModel.incrementsListModel.get(leadSelectionIndex)
	}

	private void switchToInspectTab(CommonViewModel commonViewModel) {
		commonViewModel.tabsModel.selectedIndex = Tab.INSPECT.ordinal()
	}

	private void clearSelectedIncrement(CommonViewModel commonViewModel) {
		commonViewModel.selectedIncrement = null
	}
}
