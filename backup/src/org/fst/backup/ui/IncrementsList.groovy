package org.fst.backup.ui

import groovy.swing.SwingBuilder

import javax.swing.JList

import org.fst.backup.ui.viewmodel.CommonViewModel


class IncrementsList {

	public static final String NAME = 'IncrementsList'

	JList createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		JList list = swing.list(
				name: NAME,
				model: commonViewModel.incrementsListModel,
				selectionModel: commonViewModel.incrementsListSelectionModel
				)
		return list
	}
}
