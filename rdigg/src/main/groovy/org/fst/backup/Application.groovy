package org.fst.backup

import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.JFrame

import org.apache.logging.log4j.core.lookup.MainMapLookup
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.Frame
import org.fst.backup.gui.frame.console.LimitedLengthDocument
import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService

class Application {

	public Application(String[] args) {
		Configuration configuration = readConfiguration(args)
		configureLogging(configuration)
		CommonViewModel commonViewModel = createDefaultCommonViewModel()
		configureDefaultSourceAndTargetDir(configuration, commonViewModel)
		JFrame frame = createComponent(commonViewModel)
		showFrame(frame)
		commonViewModel.tabsModel.selectedIndex = Tab.CREATE.ordinal()
	}

	private CommonViewModel createDefaultCommonViewModel() {
		CommonViewModel commonViewModel = new CommonViewModel()
		commonViewModel.consoleDocument = new LimitedLengthDocument(maxLength: 10000)
		commonViewModel.consoleStatus = 'Status'
		commonViewModel.incrementsListModel = new DefaultListModel<String>()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		commonViewModel.tabsModel = new DefaultSingleSelectionModel()
		return commonViewModel
	}

	private Configuration readConfiguration(String[] args) {
		Configuration configuration = new ReadCliService().read(args)
	}

	private void configureDefaultSourceAndTargetDir(Configuration configuration, CommonViewModel commonViewModel) {
		commonViewModel.sourceDir = configuration.defaultSourceDir
		commonViewModel.targetDir = configuration.defaultTargetDir
	}

	private void configureLogging(Configuration configuration) {
		MainMapLookup.setMainArguments('logFileBaseDir', configuration.logFileBaseDir?.getPath())
	}

	private JFrame createComponent(CommonViewModel commonViewModel) {
		return new Frame().createComponent(commonViewModel, new SwingBuilder())
	}

	private void showFrame(JFrame frame) {
		frame.setVisible(true)
	}
}
