package org.fst.backup.ui

import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.JButton

import org.fst.backup.service.CreateBackupService

class CreateBackupButton {

	JButton createComponent(CommonViewModel commonViewModel, SwingBuilder swing, Closure onFinish) {
		swing.button(
				text: 'Backup ausführen',
				actionPerformed: {
					commonViewModel.tabsModel.selectedIndex = Tab.CONSOLE.ordinal
					commonViewModel.consoleStatusColor = Color.RED
					commonViewModel.consoleStatus = 'Status: Laufend'
					clearConsole(commonViewModel)
					swing.doOutside {
						new CreateBackupService().createBackup(commonViewModel.sourceDir, commonViewModel.targetDir, {
							commonViewModel.consoleDocument.insertString(commonViewModel.consoleDocument.length, it + System.lineSeparator(), null)
						} )
						commonViewModel.consoleStatus = 'Status: Abgeschlossen'
						commonViewModel.consoleStatusColor = Color.GREEN
						onFinish.call()
					}
				}
				)
	}

	private clearConsole(CommonViewModel commonViewModel) {
		commonViewModel.consoleDocument.remove(0, commonViewModel.consoleDocument.length)
	}
}
