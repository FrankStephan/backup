package org.fst.backup.ui.frame.create

import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.JButton

import org.fst.backup.service.CreateIncrementService
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.Tab

class CreateBackupButton {

	JButton createComponent(CommonViewModel commonViewModel, SwingBuilder swing, Closure onFinish) {
		swing.button(
				text: 'Backup ausf�hren',
				actionPerformed: {
					commonViewModel.tabsModel.selectedIndex = Tab.CONSOLE.ordinal()
					commonViewModel.consoleStatusColor = Color.RED
					commonViewModel.consoleStatus = 'Status: Laufend'
					clearConsole(commonViewModel)
					swing.doOutside {
						new CreateIncrementService().createIncrement(commonViewModel.sourceDir, commonViewModel.targetDir, {
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
