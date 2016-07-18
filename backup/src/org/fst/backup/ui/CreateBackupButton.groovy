package org.fst.backup.ui

import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.JButton

import org.fst.backup.service.CreateBackupService

class CreateBackupButton {


	final CommonViewModel commonViewModel

	CreateBackupButton(CommonViewModel model) {
		this.commonViewModel = model
	}

	JButton createBackupButton(SwingBuilder swing, Closure onFinish ) {
		swing.button(
				text: 'Backup ausführen',
				actionPerformed: {
					commonViewModel.tabsModel.selectedIndex = 2
					commonViewModel.consoleStatusColor = Color.RED
					commonViewModel.consoleStatus = 'Status: Laufend'
					commonViewModel.consoleDocument.remove(0, commonViewModel.consoleDocument.length)
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
}
