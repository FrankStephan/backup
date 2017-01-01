package org.fst.backup.gui.frame

import groovy.swing.SwingBuilder

import java.awt.Dimension

import javax.swing.JScrollPane

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.choose.BackupDirectoryChooser
import org.fst.backup.gui.frame.choose.IncrementsList
import org.fst.backup.gui.frame.choose.InspectIncrementButton
import org.fst.backup.gui.frame.choose.RestoreButton
import org.fst.backup.gui.frame.console.ConsolePane
import org.fst.backup.gui.frame.console.ShutdownSystemCheckbox
import org.fst.backup.gui.frame.create.CreateBackupButton
import org.fst.backup.gui.frame.create.SourceFileChooser
import org.fst.backup.gui.frame.create.TargetFileChooser
import org.fst.backup.gui.frame.inspect.InspectIncrementFileChooser
import org.fst.backup.gui.frame.restore.RestoreBackupButton
import org.fst.backup.gui.frame.restore.RestoreDirectoryChooser


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
		swing.vbox (name: 'Konsole') {
			consoleScrollPane = new ConsolePane().createComponent(commonViewModel, swing)
			new ShutdownSystemCheckbox().createComponent(commonViewModel, swing)
		}
	}
}
