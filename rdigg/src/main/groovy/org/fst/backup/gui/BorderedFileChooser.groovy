package org.fst.backup.gui

import groovy.swing.SwingBuilder

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import javax.swing.JFileChooser

class BorderedFileChooser {

	JFileChooser createComponent (String text, SwingBuilder swing, Closure setDir) {
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
}
