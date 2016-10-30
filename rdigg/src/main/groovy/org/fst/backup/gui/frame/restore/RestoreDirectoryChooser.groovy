package org.fst.backup.gui.frame.restore

import groovy.swing.SwingBuilder

import javax.swing.JFileChooser

import org.fst.backup.gui.BorderedFileChooser
import org.fst.backup.gui.CommonViewModel


class RestoreDirectoryChooser {

	JFileChooser createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		return new BorderedFileChooser().createComponent('Wiederherstellungsverzeichnis', swing, null, { commonViewModel.restoreDir = it })
	}
}
