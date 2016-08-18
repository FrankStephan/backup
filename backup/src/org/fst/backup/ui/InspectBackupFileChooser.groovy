package org.fst.backup.ui

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.nio.file.Files

import javax.swing.JFileChooser

import org.fst.backup.model.Increment
import org.fst.backup.service.IncrementFileStructureService
import org.fst.backup.ui.viewmodel.CommonViewModel
import org.fst.backup.ui.viewmodel.IncrementListEntry


class InspectBackupFileChooser {

	JFileChooser createComponent(CommonViewModel commonViewModel) {
		File root = createRoot()
		Increment selectedIncrement = selectedIncrement(commonViewModel)
		if (selectedIncrement != null) {
			loadFileStructure(selectedIncrement, root)
		}
		JFileChooser fc = createFileChooserFromRoot(root)
		watchSelectedIncrement(commonViewModel, root, fc)
		return fc
	}

	private void watchSelectedIncrement(CommonViewModel commonViewModel, File root, JFileChooser fc) {
		commonViewModel.addPropertyChangeListener('selectedIncrement', new PropertyChangeListener() {
					void propertyChange(PropertyChangeEvent pce) {
						IncrementListEntry selectedIncrement = pce.newValue
						if (pce.oldValue != selectedIncrement) {
							// TODO: Remove files loaded from last increment

							loadFileStructure(selectedIncrement.increment, root)
							fc.updateUI()
						}
					}
				})
	}

	private JFileChooser createFileChooserFromRoot(File root) {
		InspectBackupFileSystemView fsv = new InspectBackupFileSystemView(root)
		JFileChooser fc2 = new JFileChooser(fsv)
		fc2.controlButtonsAreShown = false
		return fc2
	}

	private File createRoot() {
		File root = Files.createTempDirectory('rdigg').toFile()
		root.deleteOnExit()
		return root
	}

	private Increment selectedIncrement(CommonViewModel commonViewModel) {
		int selectionIndex = commonViewModel.incrementsListSelectionModel.leadSelectionIndex
		if (selectionIndex != -1) {
			return commonViewModel.incrementsListModel.get().getIncrement()
		}
		return null
	}

	private void loadFileStructure(Increment increment, File root) {
		new IncrementFileStructureService().createIncrementFileStructure(increment, root)
	}
}

