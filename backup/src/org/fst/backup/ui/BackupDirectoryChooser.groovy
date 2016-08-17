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

	private void updateIncrementsList(CommonViewModel commonViewModel, File targetDir) {
		commonViewModel.incrementsListModel.removeAllElements()
		if (targetDir != null) {
			List increments = new ListIncrementsService().listIncrements(targetDir)
			increments = increments.reverse()
			increments.each { Increment it ->
				long millisSinceTheEpoch = secondsToMillis(it.secondsSinceTheEpoch)
				Date date = new Date(millisSinceTheEpoch)
				def dateString = formatDate(date)
				IncrementListEntry entry = new IncrementListEntry(dateString, it)
				commonViewModel.incrementsListModel.addElement(entry)
			}
		}
	}

	private long secondsToMillis(long seconds) {
		seconds * 1000
	}

	private String formatDate(Date date) {
		DateGroovyMethods.getDateTimeString(date)
	}
}