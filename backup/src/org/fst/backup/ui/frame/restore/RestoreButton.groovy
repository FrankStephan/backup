package org.fst.backup.ui.frame.restore

import groovy.swing.SwingBuilder

import javax.swing.JButton

import org.fst.backup.ui.CommonViewModel

class RestoreButton {

	JButton createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		return swing.button()
		/*
		 * button(text: 'Wiederherstellen ->', actionPerformed: {
		 commonViewModel.tabsModel.selectedIndex = Tab.RESTORE.ordinal()
		 })
		 */
	}
}
