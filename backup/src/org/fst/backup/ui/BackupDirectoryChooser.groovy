package org.fst.backup.ui

import groovy.swing.SwingBuilder

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.JFileChooser

import org.codehaus.groovy.runtime.DateGroovyMethods
import org.fst.backup.model.Increment
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
			increments.each { Increment it ->
				Date date = new Date(it.secondsSinceTheEpoch * 1000)

				IncrementListEntry entry = new IncrementListEntry(DateGroovyMethods.getDateTimeString(date), it)
				commonViewModel.incrementsListModel.addElement(entry)
			}
		}
	}
}