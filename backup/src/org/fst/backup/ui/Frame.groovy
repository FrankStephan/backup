package org.fst.backup.ui
import groovy.swing.SwingBuilder

import java.awt.Dimension
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.DefaultListModel
import javax.swing.ImageIcon
import javax.swing.JFileChooser
import javax.swing.WindowConstants

import org.fst.backup.service.CreateBackupService
import org.fst.backup.service.IncrementDateExtractorService
import org.fst.backup.service.ListIncrementsService


def incrementsListModel1 = new DefaultListModel<String>();
def incrementsListModel2 = new DefaultListModel<String>();
def folderPairsListModel = new DefaultListModel<String>();
folderPairsListModel.addElement('C:\\Users\\Frank\\Documents\\SOURCE -> C:\\Users\\Frank\\Documents\\TARGET')
folderPairsListModel.addElement('C:\\Users\\Frank\\desktop\\source -> C:\\Users\\Frank\\desktop\\target')

def updateIncrementsList = { File directory ->
	incrementsListModel1.removeAllElements()
	if (directory != null) {
		List increments = new ListIncrementsService().listIncrements(directory)
		increments = increments.reverse()
		def incrementDateExtractorService = new IncrementDateExtractorService()
		increments.each { incrementsListModel1.addElement(incrementDateExtractorService.extractDate(it)) }
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

File sourceDir
File targetDir

def borderedFileChooser = { String text, Closure setDir ->
	def fc = swing.fileChooser(
			fileSelectionMode: JFileChooser.DIRECTORIES_ONLY,
			controlButtonsAreShown: false,
			border: swing.titledBorder(title: text)
			)
	fc.addPropertyChangeListener(new PropertyChangeListener() {
		void propertyChange(PropertyChangeEvent pce) {
			if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(pce.getPropertyName())) {
				setDir(((JFileChooser) pce.source).selectedFile)
			}
		}
	})
	return fc
}



def width = 1100
def height = 400

swing.edt {
	lookAndFeel('system')
	f = frame(
			title: 'RDiff Backup Explorer',
			size: [width, height],
			locationRelativeTo: null,
			show: true,
			defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
			iconImage: new ImageIcon(getClass().getResource('icon.gif')).getImage()
			) {
				tabbedPane() {
					hbox(name: 'Suchen') {
						backupDirectoryChooser()
						scrollPane(
								border: swing.titledBorder(title: 'Enthaltene Backups'),
								preferredSize: new Dimension(width:250, height:-1),
								minimumSize: new Dimension(width:250, height:-1)
								) { list(model: incrementsListModel1) }
					}
					vbox (name: 'Erstellen') {
						hbox() {
							borderedFileChooser('Quellverzeichnis', { sourceDir = it } )
							borderedFileChooser('Backupverzeichnis', { targetDir = it } )
						}
						hbox() {
							button(
									text: 'Backup ausführen',
									actionPerformed: {
										new CreateBackupService().createBackup(sourceDir, targetDir, {println it}  )  }
									)
							panel()
						}
					}
					hbox (name: 'Console') {
						

						scrollPane() { list(model: folderPairsListModel) }
						vbox() {
							button('Click')
							scrollPane() { list(model: folderPairsListModel) }
						}
					}
				}
			}
}


