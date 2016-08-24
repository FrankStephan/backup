package org.fst.backup.ui.frame

import groovy.swing.SwingBuilder

import javax.swing.JTabbedPane

import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.Tab

class TabsPane {

	public static final String NAME = 'TabsPane'

	JTabbedPane createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		TabFactory tabFactory = new TabFactory(commonViewModel, swing)
		return swing.tabbedPane(name: NAME, model: commonViewModel.tabsModel) {
			Tab.each { tabFactory.create(it) }
		}
	}
}
