package org.fst.backup.gui.frame.choose

import groovy.swing.SwingBuilder

import javax.swing.JButton

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab

class RestoreButton {

	JButton createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		swing.button(text: 'Wiederherstellen ->', actionPerformed: {
			if (!commonViewModel.incrementsListModel.empty) {
				int leadSelectionIndex = commonViewModel.incrementsListSelectionModel.leadSelectionIndex
				if (leadSelectionIndex >= 0) {
					chooseSelectedIncrementForInspection(commonViewModel, leadSelectionIndex)
					switchToRestoreTab(commonViewModel)
				}
			} else {
				clearSelectedIncrement(commonViewModel)
			}
		})
	}

	private void chooseSelectedIncrementForInspection(CommonViewModel commonViewModel, int leadSelectionIndex) {
		commonViewModel.selectedIncrement = commonViewModel.incrementsListModel.get(leadSelectionIndex)
	}

	private void switchToRestoreTab(CommonViewModel commonViewModel) {
		commonViewModel.tabsModel.selectedIndex = Tab.RESTORE.ordinal()
	}

	private void clearSelectedIncrement(CommonViewModel commonViewModel) {
		commonViewModel.selectedIncrement = null
	}
}
