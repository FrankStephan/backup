package org.fst.backup.test.integration_test.ui


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
import org.fst.backup.ui.frame.choose.RestoreButton
import org.fst.backup.ui.frame.create.CreateBackupButton
import org.fst.backup.ui.frame.create.SourceFileChooser
import org.fst.backup.ui.frame.create.TargetFileChooser
import org.fst.backup.ui.frame.inspect.InspectIncrementFileChooser
import org.fst.backup.ui.frame.restore.RestoreBackupButton
import org.fst.backup.ui.frame.restore.RestoreDirectoryChooser

enum UITestStep {

	CREATE_SOME_SOURCE_FILES {
		void execute(def params, Closure setResult) {
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

	CREATE_INCREMENT {
		void execute(def params, Closure setResult) {
			def sfc = new SourceFileChooser().createComponent(commonViewModel, swing)
			def tfc = new TargetFileChooser().createComponent(commonViewModel, swing)
			def button = new CreateBackupButton().createComponent(commonViewModel, swing, {})
			sfc.selectedFile = sourceDir
			tfc.selectedFile = targetDir
			button.doClick()
			setResult?.call(sfc)
		}
	},

	CHECK_CONSOLE {
		void execute(def params, Closure setResult) {
		}
	},

	CHOOSE_TARGET_DIR {
		void execute(def params, Closure setResult) {
			def chooser = new BackupDirectoryChooser().createComponent(commonViewModel, swing)
			chooser.selectedFile = targetDir
		}
	},

	LIST_INCREMENTS {
		void execute(def params, Closure setResult) {
			def incrementsList = new IncrementsList().createComponent(commonViewModel, swing)
			setResult?.call(incrementsList)
		}
	},

	INSPECT_INCREMENT {
		void execute(def params, Closure setResult) {
			selectIncrement(params)

			JButton button = new InspectIncrementButton().createComponent(commonViewModel, swing)
			JFileChooser fc = new InspectIncrementFileChooser().createComponent(commonViewModel, swing)
			button.doClick()
			setResult(fc)
		}
	},

	SELECT_INCREMENT_TO_RESTORE {
		void execute(def params, Closure setResult) {
			selectIncrement(params)
			JButton button = new RestoreButton().createComponent(commonViewModel, swing)
			button.doClick()
		}
	},

	RESTORE_INCREMENT {
		void execute(def params, Closure setResult) {
			JFileChooser fc = new RestoreDirectoryChooser().createComponent(commonViewModel, swing)
			JButton button = new RestoreBackupButton().createComponent(commonViewModel, swing, {})

			fc.selectedFile = restoreDir
			button.doClick()
		}
	}

	abstract void execute(def params = [:], Closure setResult = null)

	static File sourceDir
	static File targetDir
	static File restoreDir
	static CommonViewModel commonViewModel = new CommonViewModel()
	static SwingBuilder swing = new SwingBuilder()

	static void init(File _sourceDir, File _targetDir, File _restoreDir) {
		sourceDir = _sourceDir
		targetDir = _targetDir
		restoreDir = _restoreDir
		commonViewModel.tabsModel = new DefaultSingleSelectionModel()
		commonViewModel.incrementsListModel = new DefaultListModel<>()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		commonViewModel.consoleDocument = new PlainDocument()
	}

	private static selectIncrement(params) {
		JList incrementsList = params['incrementsList']
		int selectionIndex = params['selectionIndex']
		incrementsList.setSelectedIndex(selectionIndex)
	}
}
