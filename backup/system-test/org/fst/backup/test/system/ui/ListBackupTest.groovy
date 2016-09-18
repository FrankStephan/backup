package org.fst.backup.test.system.ui

import static org.junit.Assert.*
import groovy.swing.SwingBuilder

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.CommonViewModel
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
		SwingBuilder swing = new SwingBuilder()

		def sfc = new SourceFileChooser().createComponent(commonViewModel, swing)
		def tfc = new TargetFileChooser().createComponent(commonViewModel, swing)
		def button = new CreateBackupButton().createComponent(swing, {})


		sfc.setCurrentDirectory(sourceDir)
		tfc.setCurrentDirectory(targetDir)


		button.doClick()

		fail()
	}
}