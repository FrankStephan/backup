package org.fst.backup.test.ui_test.ui

import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JList
import javax.swing.text.PlainDocument

import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.choose.BackupDirectoryChooser
import org.fst.backup.ui.frame.choose.IncrementsList
import org.fst.backup.ui.frame.choose.InspectIncrementButton
import org.fst.backup.ui.frame.create.CreateBackupButton
import org.fst.backup.ui.frame.create.SourceFileChooser
import org.fst.backup.ui.frame.create.TargetFileChooser
import org.fst.backup.ui.frame.inspect.InspectIncrementFileChooser

enum UITestStep {

	CREATE_SOME_SOURCE_FILES {
		Object execute(def params) {
			FileTreeBuilder ftb = new FileTreeBuilder(sourceDir)
			ftb {
				'a0.suf'('')
				a0 {
					a1 { 'a2.suf'('') }
				}
				'b0.suf'('')
				'c0.suf'('')
			}
		}
	},

	CREATE_BACKUP {
		Object execute(def params) {
			def sfc = new SourceFileChooser().createComponent(commonViewModel, swing)
			def tfc = new TargetFileChooser().createComponent(commonViewModel, swing)
			def button = new CreateBackupButton().createComponent(commonViewModel, swing, {})
			sfc.selectedFile = sourceDir
			tfc.selectedFile = targetDir
			button.doClick()
		}
	},

	CHOOSE_TARGET_DIR {
		Object execute(def params) {
			def chooser = new BackupDirectoryChooser().createComponent(commonViewModel, swing)
			chooser.selectedFile = targetDir
		}
	},

	LIST_INCREMENTS {
		Object execute(def params) {
			return new IncrementsList().createComponent(commonViewModel, swing)
		}
	},

	INSPECT_INCREMENT {
		Object execute(def params) {
			JList incrementsList = params['incrementsList']
			int selectionIndex = params['selectionIndex']
			incrementsList.setSelectedIndex(selectionIndex)

			JButton button = new InspectIncrementButton().createComponent(commonViewModel, swing)
			JFileChooser fc = new InspectIncrementFileChooser().createComponent(commonViewModel)
			button.doClick()
			return fc
		}
	}




	abstract execute(def params = [:])

	public void verify(def params = [:], Closure verifier) {
		def executionResult = execute(params)
		verifier(executionResult)
	}

	static File sourceDir
	static File targetDir
	static CommonViewModel commonViewModel = new CommonViewModel()
	static SwingBuilder swing = new SwingBuilder()

	static void init(File _sourceDir, File _targetDir) {
		sourceDir = _sourceDir
		targetDir = _targetDir

		commonViewModel.tabsModel = new DefaultSingleSelectionModel()
		commonViewModel.incrementsListModel = new DefaultListModel<>()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		commonViewModel.consoleDocument = new PlainDocument()
	}
}
