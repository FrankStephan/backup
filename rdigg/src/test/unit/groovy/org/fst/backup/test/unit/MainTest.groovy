package org.fst.backup.test.unit

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.Main
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.Frame
import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService
import org.fst.backup.test.AbstractTest

class MainTest extends AbstractTest {

	CommonViewModel commonViewModel = new CommonViewModel()

	void testDefaultSourceAndTargetDirAreSetFromArgs() {
		prepareAndExecute()

		assert sourceDir == commonViewModel.sourceDir
		assert targetDir == commonViewModel.targetDir
	}

	void testCreateTabIsOpenAfterStartup() {
		prepareAndExecute()

		assert Tab.CREATE.ordinal() == commonViewModel.tabsModel.selectedIndex
	}

	void prepareAndExecute() {
		String[] args = ['-s', sourceDir, '-t', targetDir]
		MockFor readCliService = new MockFor(ReadCliService.class)
		readCliService.demand.read(1) {String[] _args ->
			assert args == _args
			return new Configuration(defaultSourceDir: sourceDir, defaultTargetDir: targetDir)
		}

		MockFor frame = new MockFor(Frame.class)
		frame.demand.getCommonViewModel(2) { -> return commonViewModel }
		frame.demand.show(1) {}

		readCliService.use {
			frame.use { Main.main(args) }
		}
	}
}
