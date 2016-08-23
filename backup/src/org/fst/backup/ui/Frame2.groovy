package org.fst.backup.ui
import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.WindowConstants
import javax.swing.text.PlainDocument

import org.fst.backup.ui.viewmodel.CommonViewModel
import org.fst.backup.ui.viewmodel.Tab

def swing = new SwingBuilder()

CommonViewModel commonViewModel = new CommonViewModel()
commonViewModel.consoleDocument = new PlainDocument()
commonViewModel.tabsModel = new DefaultSingleSelectionModel()
commonViewModel.incrementsListModel = new DefaultListModel<String>()
commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
commonViewModel.consoleStatus = 'Status'

TabFactory tabFactory = new TabFactory(commonViewModel, swing)

def width = 1100
def height = 400

JFrame f

swing.edt {
	lookAndFeel('nimbus')
	f = frame(
			title: 'rdigg',
			size: [width, height],
			locationRelativeTo: null,
			show: true,
			defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
			iconImage: new ImageIcon(getClass().getResource('icon.gif')).getImage()
			) {
				tabbedPane(model: commonViewModel.tabsModel) {
					tabFactory.create(Tab.CHOOSE)
					tabFactory.create(Tab.INSPECT)
					tabFactory.create(Tab.CREATE)
					tabFactory.create(Tab.CONSOLE)
				}
			}
}


