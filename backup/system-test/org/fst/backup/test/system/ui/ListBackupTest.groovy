package org.fst.backup.test.system.ui

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.text.PlainDocument

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.choose.BackupDirectoryChooser
import org.fst.backup.ui.frame.choose.IncrementsList
import org.fst.backup.ui.frame.create.CreateBackupButton
import org.fst.backup.ui.frame.create.SourceFileChooser
import org.fst.backup.ui.frame.create.TargetFileChooser

class ListBackupTest extends AbstractTest {

	void test() {

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

		sfc.setCurrentDirectory(sourceDir)
		tfc.setCurrentDirectory(targetDir)
		button.doClick()

		chooser.selectedFile = targetDir

		def list = new IncrementsList().createComponent(commonViewModel, swing)
		println list.getModel()*.increment
	}
}