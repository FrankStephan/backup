package org.fst.backup.ui

import javax.swing.JFileChooser

import org.fst.backup.ui.viewmodel.CommonViewModel;

class InspectBackupFileChooser {




	JFileChooser createComponent(CommonViewModel commonViewModel) {


		File root = new File('C:\\dev\\repository\\backup\\backup\\root')
		InspectBackupFileSystemView fsv = new InspectBackupFileSystemView(root)
		println root.exists()
		JFileChooser fc2 = new JFileChooser(fsv)
		fc2.controlButtonsAreShown = false
		println fc2.fileSystemView
		return fc2
	}
}

