package org.fst.backup.ui
import groovy.swing.SwingBuilder

import java.awt.Dimension
import java.awt.Font
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.DefaultListModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JScrollPane
import javax.swing.JTabbedPane
import javax.swing.WindowConstants
import javax.swing.border.TitledBorder
import javax.swing.text.DefaultCaret
import javax.swing.text.PlainDocument



def incrementsListModel = new DefaultListModel<String>()

CommonViewModel commonViewModel = new CommonViewModel()
commonViewModel.consoleDocument = new PlainDocument()
commonViewModel.consoleStatus = 'Status'
commonViewModel.tabsModel = new DefaultSingleSelectionModel()

JTabbedPane tabs
JScrollPane consoleScrollPane
TitledBorder consoleScrollPaneBorder
def swing = new SwingBuilder()


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

def insepctBackupFileChooser = {
	File root = new File('C:\\dev\\repository\\backup\\backup\\root')
	BackupFileSystemView fsv = new BackupFileSystemView(root)
	println root.exists()
	JFileChooser fc2 = new JFileChooser(fsv)
	fc2.controlButtonsAreShown = false
	println fc2.fileSystemView
	return fc2
}




def width = 1100
def height = 400

swing.edt {
	lookAndFeel('nimbus')
	f = frame(
			title: 'rdigg',
			size: [width, height],
			locationRelativeTo: null,
			show: true,
			defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
			iconImage: new ImageIcon(getClass().getResource('icon.gif')).getImage()
			) {
				tabs = tabbedPane(model: commonViewModel.tabsModel) {
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
							borderedFileChooser('Quellverzeichnis', { commonViewModel.sourceDir = it } )
							borderedFileChooser('Backupverzeichnis', { commonViewModel.targetDir = it } )
						}
						hbox() {
							JButton createBackupButton = new CreateBackupButton(commonViewModel).createBackupButton(swing, { consoleScrollPane.repaint() })
							panel()
						}
					}
					hbox (name: 'Konsole') {
						consoleScrollPane = scrollPane(border: consoleScrollPaneBorder = swing.titledBorder()) {
							def console = textArea()
							console.document = commonViewModel.consoleDocument
							DefaultCaret caret = (DefaultCaret)console.getCaret()
							caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE)
							Font f = Font.decode('Monospaced')
							console.setFont(f)
							console.editable = false
						}
					}
					vbox (name: 'Durchsuchen').add(insepctBackupFileChooser())
				}
			}
}

swing.bind(source: commonViewModel, sourceProperty: 'consoleStatus', target: consoleScrollPaneBorder, targetProperty: 'title')
swing.bind(source: commonViewModel, sourceProperty: 'consoleStatusColor', target: consoleScrollPaneBorder, targetProperty: 'titleColor')
