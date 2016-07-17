package org.fst.backup.ui

import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.JButton

import org.fst.backup.service.CreateBackupService

class CreateBackupButton {


	final CreateBackupModel model

	CreateBackupButton(CreateBackupModel model) {
		this.model = model
	}

	JButton createBackupButton(SwingBuilder swing, Closure onFinish ) {
		swing.button(
				text: 'Backup ausführen',
				actionPerformed: {
					model.tabIndex = 2
					model.consoleStatusColor = Color.RED
					model.consoleStatus = 'Status: Laufend'
					model.consoleDocument.remove(0, model.consoleDocument.length)
					swing.doOutside {
						new CreateBackupService().createBackup(model.sourceDir, model.targetDir, {
							model.consoleDocument.insertString(model.consoleDocument.length, it + System.lineSeparator(), null)
						} )
						model.consoleStatus = 'Status: Abgeschlossen'
						model.consoleStatusColor = Color.GREEN
						onFinish.call()
					}
				}
				)
	}
}
