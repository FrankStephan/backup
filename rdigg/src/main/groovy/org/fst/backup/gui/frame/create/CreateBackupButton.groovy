package org.fst.backup.gui.frame.create

import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.JButton

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.service.CreateAndVerifyIncrementService

class CreateBackupButton {

	JButton createComponent(CommonViewModel commonViewModel, SwingBuilder swing, Closure onFinish) {
		swing.button(
				text: 'Backup ausführen',
				actionPerformed: {
					commonViewModel.tabsModel.selectedIndex = Tab.CONSOLE.ordinal()
					commonViewModel.consoleStatusColor = Color.RED
					commonViewModel.consoleStatus = 'Status: Laufend'
					clearConsole(commonViewModel)
					swing.doOutside {
						DocumentWriter outWriter = new DocumentWriter(document: commonViewModel.consoleDocument, textColor: Color.BLACK)
						DocumentWriter errWriter = new DocumentWriter(document: commonViewModel.consoleDocument, textColor: Color.RED)
						new CreateAndVerifyIncrementService().createAndVerify(commonViewModel.sourceDir, commonViewModel.targetDir, outWriter, errWriter)
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
