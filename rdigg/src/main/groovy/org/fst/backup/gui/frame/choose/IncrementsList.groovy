package org.fst.backup.gui.frame.choose

import groovy.swing.SwingBuilder

import javax.swing.JList
import javax.swing.ListSelectionModel;

import org.fst.backup.gui.CommonViewModel


class IncrementsList {

	JList createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		JList list = swing.list(
				model: commonViewModel.incrementsListModel,
				selectionModel: commonViewModel.incrementsListSelectionModel,
				selectionMode: ListSelectionModel.SINGLE_SELECTION
				)
		return list
	}
}
