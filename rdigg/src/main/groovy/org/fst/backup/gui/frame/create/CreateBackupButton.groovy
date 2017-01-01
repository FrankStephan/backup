package org.fst.backup.gui.frame.create

import groovy.swing.SwingBuilder

import java.awt.Color
import java.awt.event.ActionEvent

import javax.swing.JButton
import javax.swing.SwingUtilities

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.service.CreateAndVerifyIncrementService
import org.fst.backup.service.ShutdownSystemService

class CreateBackupButton {

	JButton createComponent(CommonViewModel commonViewModel, SwingBuilder swing, Closure onFinish) {
		JButton button = swing.button(
				text: 'Backup ausführen',
				actionPerformed: {ActionEvent e ->
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
						onFinish?.call()

						if (commonViewModel.shutdownSystemOnFinish) {
							new ShutdownSystemService().shutdown(SwingUtilities.getWindowAncestor(e.getSource()))
						}
					}
				}
				)
		return button
	}

	private clearConsole(CommonViewModel commonViewModel) {
		commonViewModel.consoleDocument.remove(0, commonViewModel.consoleDocument.length)
	}
}
