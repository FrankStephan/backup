package org.fst.backup.ui

import groovy.swing.SwingBuilder

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.JFileChooser

import org.fst.backup.service.IncrementDateService
import org.fst.backup.service.ListIncrementsService
import org.fst.backup.ui.viewmodel.CommonViewModel
import org.fst.backup.ui.viewmodel.IncrementListEntry

class BackupDirectoryChooser {

	JFileChooser createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		def fc = swing.fileChooser(
				fileSelectionMode: JFileChooser.DIRECTORIES_ONLY,
				controlButtonsAreShown: false,
				multiSelectionEnabled: false,
				border: swing.titledBorder(title: 'Backupverzeichnis')
				)
		fc.addPropertyChangeListener(new PropertyChangeListener() {
					void propertyChange(PropertyChangeEvent pce) {
						if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(pce.getPropertyName())) {
							BackupDirectoryChooser.this.updateIncrementsList(commonViewModel, ((JFileChooser) pce.source).selectedFile)
						}
					}
				})
		return fc
	}

	private void updateIncrementsList(CommonViewModel commonViewModel, File directory) {
		commonViewModel.incrementsListModel.removeAllElements()
		if (directory != null) {
			List increments = new ListIncrementsService().listIncrements(directory)
			increments = increments.reverse()
			def incrementDateExtractorService = new IncrementDateService()

			increments.each { String it ->
				Date secondsSinceTheEpoch = incrementDateExtractorService.extractDate(it)
				IncrementListEntry entry = new IncrementListEntry(secondsSinceTheEpoch.toString(), secondsSinceTheEpoch)
				commonViewModel.incrementsListModel.addElement(entry)
			}
		}
	}
}