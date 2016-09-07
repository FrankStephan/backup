package org.fst.backup.ui.frame.inspect

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.nio.file.Files

import javax.swing.JFileChooser
import javax.swing.UIManager

import org.fst.backup.model.Increment
import org.fst.backup.service.IncrementFileStructureService
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.IncrementListEntry


class InspectBackupFileChooser {

	JFileChooser createComponent(CommonViewModel commonViewModel) {
		JFileChooser fc = createReadOnlyFileChooser()
		fc.fileSystemView = createEmptyFileSystemView()
		updateFileChooserContents(fc)
		observeSelectedIncrement(commonViewModel, fc)
		return fc
	}

	private File createRoot() {
		File root = Files.createTempDirectory('rdigg').toFile()
		root.deleteOnExit()
		return root
	}
	
	private updateFileChooserContents(JFileChooser fc) {
		fc.rescanCurrentDirectory()
	}
	
	private JFileChooser createReadOnlyFileChooser() {
		Boolean old = UIManager.put('FileChooser.readOnly', Boolean.TRUE);  
		JFileChooser fc = createFileChooserFromRoot()
		UIManager.put('FileChooser.readOnly', old);
		return fc
	}
	
	private InspectBackupFileSystemView createEmptyFileSystemView() {
		def root = createRoot()
		InspectBackupFileSystemView fsv = new InspectBackupFileSystemView(root)
		ensureFirstRootIsDeletedOnExit(root)
		return fsv
	}
	
	private JFileChooser createFileChooserFromRoot() {
		InspectBackupFileSystemView fsv = new InspectBackupFileSystemView(createRoot())
		JFileChooser fc2 = new JFileChooser(fsv)
		fc2.controlButtonsAreShown = false
		return fc2
	}

	private void observeSelectedIncrement(CommonViewModel commonViewModel, JFileChooser fc) {
		commonViewModel.addPropertyChangeListener('selectedIncrement', new PropertyChangeListener() {
					void propertyChange(PropertyChangeEvent pce) {
						Increment newSelectedIncrement = (pce.newValue as IncrementListEntry).increment
						Increment oldSelectedIncrement = (pce.oldValue as IncrementListEntry)?.increment
						if (oldSelectedIncrement != newSelectedIncrement) {
							loadFileStructureFromIncrement(fc, newSelectedIncrement)
							updateFileChooserContents(fc)
						}
					}
				})
	}

	private void loadFileStructureFromIncrement(JFileChooser fc, Increment increment) {
		def previousRoot = (fc.getFileSystemView() as InspectBackupFileSystemView).root
		def newRoot = createRoot()
		def incrementFileStructureService = new IncrementFileStructureService()
		(fc.getFileSystemView() as InspectBackupFileSystemView).root = newRoot
		incrementFileStructureService.createIncrementFileStructure(increment, newRoot)
		deletePreviousRoot(previousRoot)
	}
	
	private void ensureFirstRootIsDeletedOnExit(File root) {
		root.deleteOnExit()
	}
	
	private deletePreviousRoot(File previousRoot) {
		previousRoot.deleteDir()
	}
	
	
}

