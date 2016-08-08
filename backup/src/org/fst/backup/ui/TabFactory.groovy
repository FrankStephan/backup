package org.fst.backup.ui

import groovy.swing.SwingBuilder

import java.awt.Dimension
import java.awt.Font

import javax.swing.JButton
import javax.swing.JScrollPane
import javax.swing.border.TitledBorder
import javax.swing.text.DefaultCaret

import org.fst.backup.ui.viewmodel.CommonViewModel
import org.fst.backup.ui.viewmodel.IncrementListEntry
import org.fst.backup.ui.viewmodel.Tab


class TabFactory {

	SwingBuilder swing
	CommonViewModel commonViewModel
	JScrollPane consoleScrollPane

	public TabFactory(CommonViewModel commonViewModel, SwingBuilder swing) {
		this.commonViewModel = commonViewModel
		this.swing = swing
	}

	def create(Tab tab) {
		switch(tab) {
			case Tab.CHOOSE:
				return chooseTab()
			case Tab.INSPECT:
				return inspectTab()
			case Tab.CREATE:
				return createTab()
			case Tab.CONSOLE:
				return conoleTab()
		}
	}

	def chooseTab = {
		swing.hbox(name: 'Suchen') {
			new BackupDirectoryChooser().createComponent(commonViewModel, swing)
			vbox() {
				scrollPane(
						border: swing.titledBorder(title: 'Enthaltene Backups'),
						preferredSize: new Dimension(width:250, height:-1),
						minimumSize: new Dimension(width:250, height:-1)
						) { new IncrementsList().createComponent(commonViewModel, swing) }
				button(text: 'Durchsuchen', actionPerformed: {
					IncrementListEntry entry = commonViewModel.incrementsListModel.get(commonViewModel.incrementsListSelectionModel.leadIndex)


					commonViewModel.tabsModel.selectedIndex = Tab.INSPECT.ordinal()
				})
			}
		}
	}

	def inspectTab = {
		swing.vbox (name: 'Durchsuchen').add(new InspectBackupFileChooser().createComponent())
	}

	def createTab = {
		swing.vbox (name: 'Erstellen') {
			hbox() {
				new BorderedFileChooser().createComponent('Quellverzeichnis', swing, { commonViewModel.sourceDir = it } )
				new BorderedFileChooser().createComponent('Backupverzeichnis', swing, { commonViewModel.targetDir = it } )
			}
			hbox() {
				JButton createBackupButton = new CreateBackupButton().createComponent(commonViewModel, swing, { consoleScrollPane.repaint() })
				panel()
			}
		}
	}

	def conoleTab = {
		TitledBorder consoleScrollPaneBorder
		def box = swing.hbox (name: 'Konsole') {
			consoleScrollPane = scrollPane(border: consoleScrollPaneBorder = swing.titledBorder()) {
				def console = textArea()
				console.document = commonViewModel.consoleDocument
				DefaultCaret caret = (DefaultCaret)console.getCaret()
				caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE)
				console.setFont(Font.decode('Monospaced'))
				console.editable = false
			}
		}
		swing.bind(source: commonViewModel, sourceProperty: 'consoleStatus', target: consoleScrollPaneBorder, targetProperty: 'title')
		swing.bind(source: commonViewModel, sourceProperty: 'consoleStatusColor', target: consoleScrollPaneBorder, targetProperty: 'titleColor')
		return box
	}
}
