package org.fst.backup.ui
import groovy.swing.SwingBuilder

import java.awt.Dimension
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.DefaultListModel
import javax.swing.ImageIcon
import javax.swing.JFileChooser
import javax.swing.WindowConstants

import org.fst.backup.service.IncrementDateExtractorService
import org.fst.backup.service.ListIncrementsService


def incrementsListModel = new DefaultListModel<String>();

def updateIncrementsList = { File directory ->
	incrementsListModel.removeAllElements()
	if (directory != null) {
		List increments = new ListIncrementsService().listIncrements(directory)
		increments = increments.reverse()
		def incrementDateExtractorService = new IncrementDateExtractorService()
		increments.each { incrementsListModel.addElement(incrementDateExtractorService.extractDate(it)) }
	}
}

def swing = new SwingBuilder()

def backupDirectoryChooser = {
	def fc = swing.fileChooser(
			fileSelectionMode: JFileChooser.DIRECTORIES_ONLY,
			controlButtonsAreShown: false,
			multiSelectionEnabled: false,
			border: swing.titledBorder(title: 'Backupverzeichnis')
			)
	fc.addPropertyChangeListener(new PropertyChangeListener() {
				void propertyChange(PropertyChangeEvent pce) {
					if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(pce.getPropertyName())) {
						updateIncrementsList(((JFileChooser) pce.source).selectedFile)
					}
				}
			})
	return fc
}

def borderedFileChooser = { text ->
	swing.fileChooser(
			fileSelectionMode: JFileChooser.DIRECTORIES_ONLY,
			controlButtonsAreShown: false,
			border: swing.titledBorder(title: text)
			)
}

def width = 1100

swing.edt {
	lookAndFeel('nimbus')
	f = frame(
			title: 'RDiff Backup Explorer',
			size: [width, 300],
			locationRelativeTo: null,
			show: true,
			defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
			iconImage: new ImageIcon(getClass().getResource('icon.gif')).getImage()
			) {
				tabbedPane() {
					splitPane(
							name: 'Suchen',
							resizeWeight: 0.8d,
							leftComponent: backupDirectoryChooser(),
							rightComponent:	scrollPane(
							border: swing.titledBorder(title: 'Enthaltene Backups'),
							preferredSize: new Dimension(width:150, height:-1)
							) { list(model: incrementsListModel) })
					hbox(name: 'Suchen') {
						backupDirectoryChooser()
						scrollPane(
							border: swing.titledBorder(title: 'Enthaltene Backups'),
							preferredSize: new Dimension(width:250, height:-1),
							minimumSize: new Dimension(width:250, height:-1)
							) { list(model: incrementsListModel) }
						}
					hbox(name: 'Erstellen') {
								borderedFileChooser('Quellverzeichnis')
								borderedFileChooser('Backupverzeichnis')
							}
				}
			}
}

//swing.edt {
//	lookAndFeel('nimbus')
//	f = frame(
//			title: 'RDiff Backup Explorer',
//			size: [740, 300],
//			locationRelativeTo: null,
//			show: true,
//			defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
//			iconImage: new ImageIcon(getClass().getResource('icon.gif')).getImage()
//			) {
//				splitPane(
//						leftComponent: fileSelector(),
//						rightComponent: list(model: incrementsListModel)
//						)
//			}
//}







