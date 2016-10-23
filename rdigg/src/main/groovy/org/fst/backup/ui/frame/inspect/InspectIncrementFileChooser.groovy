package org.fst.backup.ui.frame.inspect

import groovy.swing.SwingBuilder

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.nio.file.Files
import java.nio.file.Paths

import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.border.TitledBorder

import org.fst.backup.model.Increment
import org.fst.backup.service.IncrementFileStructureService
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.IncrementListEntry



class InspectIncrementFileChooser {

	JFileChooser createComponent(CommonViewModel commonViewModel, SwingBuilder swing) {
		JFileChooser fc = createReadOnlyFileChooser(swing)
		fc.fileSystemView = createEmptyFileSystemView()
		updateFileChooserContents(fc)
		observeSelectedIncrement(commonViewModel, fc)
		bindBorderTitle(commonViewModel, fc, swing)
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

	private JFileChooser createReadOnlyFileChooser(SwingBuilder swing) {
		Boolean old = UIManager.put('FileChooser.readOnly', Boolean.TRUE)
		JFileChooser fc = createFileChooserFromRoot(swing)
		UIManager.put('FileChooser.readOnly', old)
		return fc
	}

	private InspectIncrementFileSystemView createEmptyFileSystemView() {
		def root = createRoot()
		InspectIncrementFileSystemView fsv = new InspectIncrementFileSystemView(root)
		ensureFirstRootIsDeletedOnExit(root)
		return fsv
	}

	private JFileChooser createFileChooserFromRoot(SwingBuilder swing) {
		InspectIncrementFileSystemView fsv = new InspectIncrementFileSystemView(createRoot())
		JFileChooser fc2 = swing.fileChooser()
		fc2.setFileSystemView(fsv)
		TitledBorder titledBorder = swing.titledBorder()
		fc2.setBorder(titledBorder)
		fc2.controlButtonsAreShown = false
		return fc2
	}

	private void bindBorderTitle(CommonViewModel commonViewModel, JFileChooser fc, SwingBuilder swing) {
		TitledBorder titledBorder = fc.getBorder()
		swing.bind(source: commonViewModel, sourceProperty: 'selectedIncrement', target: titledBorder, targetProperty: 'title', converter: absoluteTargetPathFromSelectedIncrement)
	}

	Closure<String> absoluteTargetPathFromSelectedIncrement = { IncrementListEntry selectedIncrement ->
		String targetPath = selectedIncrement?.increment?.targetPath
		if (targetPath != null) {
			return Paths.get(targetPath).toAbsolutePath().toString()
		}
		return ' '
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
