package org.fst.backup.test.ui.gui


import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel
import javax.swing.DefaultSingleSelectionModel
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JList
import javax.swing.JScrollPane
import javax.swing.text.PlainDocument

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.choose.BackupDirectoryChooser
import org.fst.backup.gui.frame.choose.IncrementsList
import org.fst.backup.gui.frame.choose.InspectIncrementButton
import org.fst.backup.gui.frame.choose.RestoreButton
import org.fst.backup.gui.frame.console.ConsolePane
import org.fst.backup.gui.frame.create.CreateBackupButton
import org.fst.backup.gui.frame.create.SourceFileChooser
import org.fst.backup.gui.frame.create.TargetFileChooser
import org.fst.backup.gui.frame.inspect.InspectIncrementFileChooser
import org.fst.backup.gui.frame.restore.RestoreBackupButton
import org.fst.backup.gui.frame.restore.RestoreDirectoryChooser

enum UITestStep {

	CREATE_SOME_SOURCE_FILES {
		void execute(def params, Closure setResult) {
			FileTreeBuilder ftb = new FileTreeBuilder(sourceDir)
			ftb {
				'a0.suf'('I am a0')
				a0 {
					a1 { 'a2.suf'('I am a2') }
				}
				'b0.suf'('I am b0')
				'c0.suf'('I am c0')
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
	},


	CONSOLE {
		void execute(def params, Closure setResult) {
			JScrollPane pane = new ConsolePane().createComponent(commonViewModel, swing)
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
