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


class InspectIncrementFileChooser {

	JFileChooser createComponent(CommonViewModel commonViewModel) {
		JFileChooser fc = createReadOnlyFileChooser()
		fc.fileSystemView = createEmptyFileSystemView()
		updateFileChooserContents(fc)
		observeSelectedIncrement(commonViewModel, fc)
		return fc
	}

	private File createRoot() {
		File root = Files.createTempDirectory('rdigg').toFile()
		addShutdownHook { root.deleteDir() }
		return root
	}

	private updateFileChooserContents(JFileChooser fc) {
		fc.setCurrentDirectory((fc.getFileSystemView() as InspectIncrementFileSystemView).root)
		fc.rescanCurrentDirectory()
	}

	private JFileChooser createReadOnlyFileChooser() {
		Boolean old = UIManager.put('FileChooser.readOnly', Boolean.TRUE)
		JFileChooser fc = createFileChooserFromRoot()
		UIManager.put('FileChooser.readOnly', old)
		return fc
	}

	private InspectIncrementFileSystemView createEmptyFileSystemView() {
		def root = createRoot()
		InspectIncrementFileSystemView fsv = new InspectIncrementFileSystemView(root)
		ensureFirstRootIsDeletedOnExit(root)
		return fsv
	}

	private JFileChooser createFileChooserFromRoot() {
		InspectIncrementFileSystemView fsv = new InspectIncrementFileSystemView(createRoot())
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
		def previousRoot = (fc.getFileSystemView() as InspectIncrementFileSystemView).root
		def newRoot = createRoot()
		def incrementFileStructureService = new IncrementFileStructureService()
		(fc.getFileSystemView() as InspectIncrementFileSystemView).root = newRoot
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

