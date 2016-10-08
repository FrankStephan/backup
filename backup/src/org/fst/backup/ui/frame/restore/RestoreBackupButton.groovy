package org.fst.backup.ui.frame.restore

import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.JButton

import org.fst.backup.service.RestoreIncrementService
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.Tab


class RestoreBackupButton {

	JButton createComponent(CommonViewModel commonViewModel, SwingBuilder swing, Closure onFinish) {
		return swing.button(text: 'Wiederherstellen', actionPerformed: {
			if (commonViewModel.selectedIncrement != null) {
				commonViewModel.tabsModel.selectedIndex = Tab.CONSOLE.ordinal()
				commonViewModel.consoleStatusColor = Color.RED
				commonViewModel.consoleStatus = 'Status: Laufend'
				clearConsole(commonViewModel)

				def restoreIncrementService = new RestoreIncrementService()
				swing.doOutside {
					restoreIncrementService.restore(commonViewModel.selectedIncrement.increment, commonViewModel.restoreDir, {
						commonViewModel.consoleDocument.insertString(commonViewModel.consoleDocument.length, it + System.lineSeparator(), null)
					})
					commonViewModel.consoleStatus = 'Status: Abgeschlossen'
					commonViewModel.consoleStatusColor = Color.GREEN
					onFinish.call()
				}
			}
		})
	}

	private clearConsole(CommonViewModel commonViewModel) {
		commonViewModel.consoleDocument.remove(0, commonViewModel.consoleDocument.length)
	}
}
