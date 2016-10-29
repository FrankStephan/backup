package org.fst.backup.test.unit

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.Main
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.Frame
import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService
import org.fst.backup.test.AbstractTest

class MainTest extends AbstractTest {

	void testDefaultSourceAndTargetDirAreSetFromArgs() {
		String[] args = ['-s', sourceDir, '-t', targetDir]
		MockFor readCliService = new MockFor(ReadCliService.class)
		readCliService.demand.read(1) {String[] _args ->
			assert args == _args
			return new Configuration(defaultSourceDir: sourceDir, defaultTargetDir: targetDir)
		}

		CommonViewModel commonViewModel = new CommonViewModel()
		MockFor frame = new MockFor(Frame.class)
		frame.demand.getCommonViewModel(2) { -> return commonViewModel }
		frame.demand.show(1) {}

		readCliService.use {
			frame.use { Main.main(args) }
		}

		assert sourceDir == commonViewModel.sourceDir
		assert targetDir == commonViewModel.targetDir
	}
}
