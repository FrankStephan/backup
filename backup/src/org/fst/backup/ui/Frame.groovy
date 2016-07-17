package org.fst.backup.ui
import groovy.swing.SwingBuilder

import java.awt.Dimension
import java.awt.Font
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.DefaultListModel
import javax.swing.ImageIcon
import javax.swing.JFileChooser
import javax.swing.JTabbedPane
import javax.swing.WindowConstants
import javax.swing.text.PlainDocument

import org.fst.backup.service.CreateBackupService



def incrementsListModel = new DefaultListModel<String>();
def consoleDocument = new PlainDocument();

def swing = new SwingBuilder()


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

JTabbedPane tabs

def width = 1100
def height = 400

swing.edt {
	lookAndFeel('nimbus')
	f = frame(
			title: 'RDiff Backup Explorer',
			size: [width, height],
			locationRelativeTo: null,
			show: true,
			defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
			iconImage: new ImageIcon(getClass().getResource('icon.gif')).getImage()
			) {
				tabs = tabbedPane() {
					hbox(name: 'Suchen') {
						new BackupDirectoryChooser(incrementsListModel).backupDirectoryChooser(swing)
						scrollPane(
								border: swing.titledBorder(title: 'Enthaltene Backups'),
								preferredSize: new Dimension(width:250, height:-1),
								minimumSize: new Dimension(width:250, height:-1)
								) { list(model: incrementsListModel) }
					}
					vbox (name: 'Erstellen') {
						hbox() {
							borderedFileChooser('Quellverzeichnis', { sourceDir = it } )
							borderedFileChooser('Backupverzeichnis', { targetDir = it } )
						}
						hbox() {
							button(
									text: 'Backup ausf�hren',
									actionPerformed: {
										tabs.selectedIndex = 2
										new CreateBackupService().createBackup(sourceDir, targetDir, {
											consoleDocument.insertString(consoleDocument.length, it + System.lineSeparator(), null)
											println it
										} )
									}
									)
							panel()
						}
					}
					hbox (name: 'Console') {
						scrollPane() {
							def console = textArea()
							Font f = Font.decode('Monospaced');
							console.document = consoleDocument
							console.setFont(f)
							console.editable = false;
						}
					}
				}
			}
}


