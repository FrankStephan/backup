package org.fst.backup.gui.frame.create

import groovy.swing.SwingBuilder

import javax.swing.JFileChooser

import org.fst.backup.gui.BorderedFileChooser
import org.fst.backup.gui.CommonViewModel

class TargetFileChooser {

	JFileChooser createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		return new BorderedFileChooser().createComponent('Quellverzeichnis', swing, { commonViewModel.targetDir = it } )
	}
}
