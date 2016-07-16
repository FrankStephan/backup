package org.fst.backup.ui

import groovy.swing.SwingBuilder

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser
import javax.swing.ListModel;

import org.fst.backup.service.IncrementDateExtractorService
import org.fst.backup.service.ListIncrementsService

class BackupDirectoryChooser {
	
	final DefaultListModel<String> incrementsListModel
	
	BackupDirectoryChooser(DefaultListModel<String> incrementsListModel) {
		this.incrementsListModel = incrementsListModel
	}

	def backupDirectoryChooser(SwingBuilder swing) {
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

	private void updateIncrementsList(File directory) {
		incrementsListModel.removeAllElements()
		if (directory != null) {
			List increments = new ListIncrementsService().listIncrements(directory)
			increments = increments.reverse()
			def incrementDateExtractorService = new IncrementDateExtractorService()
			increments.each { incrementsListModel.addElement(incrementDateExtractorService.extractDate(it)) }
		}
	}
}