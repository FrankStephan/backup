package org.fst.backup.test.system.ui

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.JList
import javax.swing.text.PlainDocument

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.choose.BackupDirectoryChooser
import org.fst.backup.ui.frame.choose.IncrementsList
import org.fst.backup.ui.frame.create.CreateBackupButton
import org.fst.backup.ui.frame.create.SourceFileChooser
import org.fst.backup.ui.frame.create.TargetFileChooser

class ListBackupTest extends AbstractTest {

	void testIncrementsListShowsOneIncrementAfterBackup() {

		def ftb = new FileTreeBuilder(sourceDir)
		ftb {
			'a0.suf'('')
			a0 {
				a1 { 'a2.suf'('') }
			}
			'b0.suf'('')
			'c0.suf'('')
		}

		CommonViewModel commonViewModel = new CommonViewModel()
		commonViewModel.tabsModel = new DefaultSingleSelectionModel()
		commonViewModel.incrementsListModel = new DefaultListModel<>()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		commonViewModel.consoleDocument = new PlainDocument()
		SwingBuilder swing = new SwingBuilder()

		def sfc = new SourceFileChooser().createComponent(commonViewModel, swing)
		def tfc = new TargetFileChooser().createComponent(commonViewModel, swing)
		def button = new CreateBackupButton().createComponent(commonViewModel, swing, {})
		def chooser = new BackupDirectoryChooser().createComponent(commonViewModel, swing)

		JList list = new IncrementsList().createComponent(commonViewModel, swing)
		assert 0 == list.getModel().size

		// Create backup
		sfc.selectedFile = sourceDir
		tfc.selectedFile = targetDir
		button.doClick()

		chooser.selectedFile = targetDir

		assert 1 == list.getModel().size
	}
}