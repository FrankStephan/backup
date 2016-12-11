package org.fst.backup.test.unit

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import javax.swing.JFrame

import org.fst.backup.Application
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.Frame
import org.fst.backup.gui.frame.console.LimitedLengthDocument
import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService
import org.fst.backup.test.AbstractTest

class ApplicationTest extends AbstractTest {

	String[] args
	MockFor readCliService
	MockFor frame
	JFrame jFrame
	Configuration configuration

	void setUp() {
		super.setUp()
		args = ['-s', sourceDir, '-t', targetDir]
		jFrame = new JFrame()
		configuration = new Configuration()
	}

	void testReadArgsFromCli() {
		prepareAndExecute({String[] _args ->
			assert args == _args
		})
	}

	void testFrameIsCreatedWithCommonViewModel() {
		prepareAndExecute(null, {CommonViewModel commonViewModel ->
			assert null != commonViewModel
		})
	}

	void testCommonViewModelIsCreatedProperly() {
		prepareAndExecute(null, {CommonViewModel commonViewModel ->
			assert (null != commonViewModel.consoleDocument && commonViewModel.consoleDocument instanceof LimitedLengthDocument)
			assert 'Status' == commonViewModel.consoleStatus
			assert null == commonViewModel.consoleStatusColor
			assert null != commonViewModel.incrementsListModel
			assert null != commonViewModel.incrementsListSelectionModel
			assert null == commonViewModel.restoreDir
			assert null == commonViewModel.selectedIncrement
			assert null != commonViewModel.tabsModel
		})
	}

	void testDefaultSourceAndTargetDirAreSetFromCli() {
		configuration = new Configuration(defaultSourceDir: sourceDir, defaultTargetDir: targetDir)
		prepareAndExecute(null, {CommonViewModel commonViewModel ->
			assert sourceDir == commonViewModel.sourceDir
			assert targetDir == commonViewModel.targetDir
		})
	}

	void testCreateTabIsOpenedInitiallyAfterFrameIsVisible() {
		CommonViewModel commonViewModel
		prepareAndExecute(null, {CommonViewModel _commonViewModel ->
			assert -1 == _commonViewModel.tabsModel.selectedIndex
			commonViewModel = _commonViewModel
		})
		assert Tab.CREATE.ordinal() == commonViewModel.tabsModel.selectedIndex
	}

	void testFrameIsOpened() {
		assert false == jFrame.isVisible()
		prepareAndExecute()
		assert true == jFrame.isVisible()
	}


	private void prepareAndExecute(Closure assertReadCli = null, Closure assertCommonViewModel = null) {
		readCliService = new MockFor(ReadCliService.class)
		readCliService.demand.read(1) {String[] _args ->
			assertReadCli?.call(args)
			return configuration
		}

		frame = new MockFor(Frame.class)
		frame.demand.createComponent(1) { CommonViewModel commonViewModel, SwingBuilder swing ->
			assertCommonViewModel?.call(commonViewModel)
			return jFrame
		}

		frame.use {
			readCliService.use { new Application(args) }
		}
	}
}
