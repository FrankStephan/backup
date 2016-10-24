package org.fst.backup.ui.frame.restore

import groovy.swing.SwingBuilder

import javax.swing.JFileChooser

import org.fst.backup.ui.BorderedFileChooser
import org.fst.backup.ui.CommonViewModel

class RestoreDirectoryChooser {

	JFileChooser createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		
		return new BorderedFileChooser().createComponent('Wiederherstellungsverzeichnis', swing, { commonViewModel.restoreDir = it })
		
	}
}
