package org.fst.backup.ui.frame

import groovy.swing.SwingBuilder

import java.awt.Dimension
import java.awt.Font

import javax.swing.JScrollPane
import javax.swing.border.TitledBorder
import javax.swing.text.DefaultCaret

import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.Tab
import org.fst.backup.ui.frame.choose.BackupDirectoryChooser
import org.fst.backup.ui.frame.choose.IncrementsList
import org.fst.backup.ui.frame.choose.InspectIncrementButton
import org.fst.backup.ui.frame.choose.RestoreButton
import org.fst.backup.ui.frame.create.CreateBackupButton
import org.fst.backup.ui.frame.create.SourceFileChooser
import org.fst.backup.ui.frame.create.TargetFileChooser
import org.fst.backup.ui.frame.inspect.InspectIncrementFileChooser
import org.fst.backup.ui.frame.restore.RestoreBackupButton
import org.fst.backup.ui.frame.restore.RestoreDirectoryChooser


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
				new InspectIncrementButton().createComponent(commonViewModel, swing)
				new RestoreButton().createComponent(commonViewModel, swing)
			}
		}
	}

	def inspectTab = {
		swing.vbox (name: 'Durchsuchen') {
			new InspectIncrementFileChooser().createComponent(commonViewModel, swing)
		}
	}

	def restoreTab = {
		swing.vbox (name: 'Wiederherstellen') {
			hbox() {
				new RestoreDirectoryChooser().createComponent(commonViewModel, swing)
			}
			hbox() {
				panel()
				new RestoreBackupButton().createComponent(commonViewModel, swing, { consoleScrollPane.repaint() })
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
				new CreateBackupButton().createComponent(commonViewModel, swing, { consoleScrollPane.repaint() })
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
