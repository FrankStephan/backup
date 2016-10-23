package org.fst.backup.ui.frame.create

import groovy.swing.SwingBuilder

import javax.swing.JFileChooser

import org.fst.backup.ui.BorderedFileChooser
import org.fst.backup.ui.CommonViewModel

class SourceFileChooser {

	JFileChooser createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		return new BorderedFileChooser().createComponent('Quellverzeichnis', swing, { commonViewModel.sourceDir = it } )
	}
}
