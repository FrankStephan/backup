package org.fst.backup.ui.frame

import groovy.swing.SwingBuilder

import java.awt.Dimension
import java.awt.Font

import javax.swing.JButton
import javax.swing.JScrollPane
import javax.swing.border.TitledBorder
import javax.swing.text.DefaultCaret

import org.fst.backup.ui.BorderedFileChooser
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.Tab
import org.fst.backup.ui.frame.choose.BackupDirectoryChooser
import org.fst.backup.ui.frame.choose.IncrementsList
import org.fst.backup.ui.frame.create.CreateBackupButton
import org.fst.backup.ui.frame.create.SourceFileChooser
import org.fst.backup.ui.frame.create.TargetFileChooser
import org.fst.backup.ui.frame.inspect.InspectIncrementFileChooser


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
			case Tab.RESTORE:
				return restoreTab()
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
				button(text: 'Durchsuchen ->', actionPerformed: {
					commonViewModel.selectedIncrement = commonViewModel.incrementsListModel.get(commonViewModel.incrementsListSelectionModel.leadIndex)
					commonViewModel.tabsModel.selectedIndex = Tab.INSPECT.ordinal()
				})

				button(text: 'Wiederherstellen ->', actionPerformed: {
					commonViewModel.tabsModel.selectedIndex = Tab.RESTORE.ordinal()
				})
			}
		}
	}

	def inspectTab = {
		swing.vbox (name: 'Durchsuchen').add(new InspectIncrementFileChooser().createComponent(commonViewModel))
	}

	def restoreTab = {
		swing.vbox (name: 'Wiederherstellen') {
			hbox() {
				new BorderedFileChooser().createComponent('Wiederherstellungsverzeichnis', swing, { commonViewModel.restoreDir = it })
			}
			hbox() {
				panel()
				button(text: 'Wiederherstellen', actionPerformed: {
					commonViewModel.tabsModel.selectedIndex = Tab.CONSOLE.ordinal()
				})
			}
		}
	}

	def createTab = {
		swing.vbox (name: 'Erstellen') {
			hbox() {
				new SourceFileChooser().createComponent(commonViewModel, swing)
				new TargetFileChooser().createComponent(commonViewModel, swing)
			}
			hbox() {
				panel()
				JButton createBackupButton = new CreateBackupButton().createComponent(commonViewModel, swing, { consoleScrollPane.repaint() })
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
