package org.fst.backup.ui

import groovy.swing.SwingBuilder

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.DefaultListModel
import javax.swing.JFileChooser

import org.fst.backup.service.IncrementDateExtractorService
import org.fst.backup.service.ListIncrementsService

class BackupDirectoryChooser {

	JFileChooser createComponent(DefaultListModel<String> incrementsListModel, SwingBuilder swing) {
		def fc = swing.fileChooser(
				fileSelectionMode: JFileChooser.DIRECTORIES_ONLY,
				controlButtonsAreShown: false,
				multiSelectionEnabled: false,
				border: swing.titledBorder(title: 'Backupverzeichnis')
				)
		fc.addPropertyChangeListener(new PropertyChangeListener() {
					void propertyChange(PropertyChangeEvent pce) {
						if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(pce.getPropertyName())) {
							BackupDirectoryChooser.this.updateIncrementsList(incrementsListModel, ((JFileChooser) pce.source).selectedFile)
						}
					}
				})
		return fc
	}

	private void updateIncrementsList(DefaultListModel<String> incrementsListModel, File directory) {
		incrementsListModel.removeAllElements()
		if (directory != null) {
			List increments = new ListIncrementsService().listIncrements(directory)
			increments = increments.reverse()
			def incrementDateExtractorService = new IncrementDateExtractorService()
			increments.each { String it -> incrementsListModel.addElement(incrementDateExtractorService.extractDate(it)) }
		}
	}
}