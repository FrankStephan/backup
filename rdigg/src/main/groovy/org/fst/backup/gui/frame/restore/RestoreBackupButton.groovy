package org.fst.backup.gui.frame.restore

import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.JButton

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.create.DocumentWriter
import org.fst.backup.service.RestoreIncrementService


class RestoreBackupButton {

	JButton createComponent(CommonViewModel commonViewModel, SwingBuilder swing, Closure onFinish) {
		JButton button = swing.button(text: 'Wiederherstellen', actionPerformed: {
			if (commonViewModel.selectedIncrement != null) {
				commonViewModel.tabsModel.selectedIndex = Tab.CONSOLE.ordinal()
				commonViewModel.consoleStatusColor = Color.RED
				commonViewModel.consoleStatus = 'Status: Laufend'
				clearConsole(commonViewModel)

				def restoreIncrementService = new RestoreIncrementService()
				swing.doOutside {
					DocumentWriter outputWriter = new DocumentWriter(document: commonViewModel.consoleDocument, textColor: Color.BLACK)
					DocumentWriter errorWriter = new DocumentWriter(document: commonViewModel.consoleDocument, textColor: Color.RED)
					restoreIncrementService.restore(commonViewModel.selectedIncrement.increment, commonViewModel.restoreDir, outputWriter, errorWriter)
					commonViewModel.consoleStatus = 'Status: Abgeschlossen'
					commonViewModel.consoleStatusColor = Color.GREEN
					onFinish.call()
				}
			}
		})
		return button
	}

	private clearConsole(CommonViewModel commonViewModel) {
		commonViewModel.consoleDocument.remove(0, commonViewModel.consoleDocument.length)
	}
}
