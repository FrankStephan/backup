package org.fst.backup.test.unit

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import javax.swing.JFrame

import org.apache.logging.log4j.core.lookup.MainMapLookup
import org.fst.backup.Application
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.Frame
import org.fst.backup.gui.frame.console.LimitedLengthDocument
import org.fst.backup.model.Configuration
import org.fst.backup.service.ReadCliService
import org.fst.backup.test.AbstractTest
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
class ApplicationTest extends AbstractTest {

	static String realUserHome

	String[] args
	MockFor readCliService
	MockFor frame
	JFrame jFrame
	File logFileBaseDir

	Configuration configuration

	@BeforeClass
	static void beforeClass() {
		rememberUserHome()
		System.setProperty('user.home', ApplicationTest.class.getSimpleName() + '-tmp/')
	}

	private static rememberUserHome() {
		realUserHome = System.getProperty('user.home')
	}

	private static resetUserHome() {
		System.setProperty('user.home', realUserHome)
	}

	@AfterClass
	static void afterClass() {
		String testUserHome = System.getProperty('user.home')
		resetUserHome()
		if (testUserHome != realUserHome) {
			assert new File(testUserHome).deleteDir()
		}
	}

	@Before
	void setUp() {
		super.setUp()
		args = ['-s', sourceDir, '-t', targetDir]
		jFrame = new JFrame()
		configuration = new Configuration()
		logFileBaseDir = new File(System.getProperty('user.home'), '/.rdigg/logs')
	}

	@Test
	void testReadArgsFromCli() {
		prepareAndExecute({ String[] _args ->
			assert args == _args
		})
	}

	@Test
	void testFrameIsCreatedWithCommonViewModel() {
		prepareAndExecute(null, { CommonViewModel commonViewModel ->
			assert null != commonViewModel
		})
	}

	@Test
	void testCommonViewModelIsCreatedProperly() {
		prepareAndExecute(null, { CommonViewModel commonViewModel ->
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

	@Test
	void testDefaultSourceAndTargetDirAreSetFromConfiguration() {
		configuration = new Configuration(defaultSourceDir: sourceDir, defaultTargetDir: targetDir)
		prepareAndExecute(null, { CommonViewModel commonViewModel ->
			assert sourceDir == commonViewModel.sourceDir
			assert targetDir == commonViewModel.targetDir
		})
	}

	@Test
	void testLogFileBaseDirIsSetFromConfiguration() {
		prepareAndExecute()
		assert logFileBaseDir.getPath() == MainMapLookup.MAIN_SINGLETON.lookup('logFileBaseDir')
	}

	@Test
	void testCreateTabIsOpenedInitiallyAfterFrameIsVisible() {
		CommonViewModel commonViewModel
		prepareAndExecute(null, { CommonViewModel _commonViewModel ->
			assert -1 == _commonViewModel.tabsModel.selectedIndex
			commonViewModel = _commonViewModel
		})
		assert Tab.CREATE.ordinal() == commonViewModel.tabsModel.selectedIndex
	}

	@Test
	void testFrameIsOpened() {
		assert false == jFrame.isVisible()
		prepareAndExecute()
		assert true == jFrame.isVisible()
	}

	private void prepareAndExecute(Closure assertReadCli = null, Closure assertCommonViewModel = null) {
		readCliService = new MockFor(ReadCliService.class)
		readCliService.demand.read(1) { String[] _args ->
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
