package org.fst.backup

import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.JFrame
import javax.swing.text.PlainDocument

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.Frame
import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService

class Application {

	public Application(String[] args) {
		CommonViewModel commonViewModel = createDefaultCommonViewModel()
		readDefaultSourceAndTargetDir(args, commonViewModel)
		JFrame frame = createComponent(commonViewModel)
		showFrame(frame)
		commonViewModel.tabsModel.selectedIndex = Tab.CREATE.ordinal()
	}

	private CommonViewModel createDefaultCommonViewModel() {
		CommonViewModel commonViewModel = new CommonViewModel()
		commonViewModel.consoleDocument = new PlainDocument()
		commonViewModel.consoleStatus = 'Status'
		commonViewModel.incrementsListModel = new DefaultListModel<String>()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		commonViewModel.tabsModel = new DefaultSingleSelectionModel()
		return commonViewModel
	}

	private void readDefaultSourceAndTargetDir(String[] args, CommonViewModel commonViewModel) {
		Configuration configuration = new ReadCliService().read(args)
		commonViewModel.sourceDir = configuration.defaultSourceDir
		commonViewModel.targetDir = configuration.defaultTargetDir
	}

	private JFrame createComponent(CommonViewModel commonViewModel) {
		return new Frame().createComponent(commonViewModel, new SwingBuilder())
	}

	private void showFrame(JFrame frame) {
		frame.setVisible(true)
	}
}
