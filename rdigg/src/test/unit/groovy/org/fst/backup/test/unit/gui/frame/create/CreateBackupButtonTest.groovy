package org.fst.backup.test.unit.gui.frame.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor
import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.DefaultSingleSelectionModel
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.text.PlainDocument

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.IncrementListEntry
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.create.CreateBackupButton
import org.fst.backup.gui.frame.create.DocumentWriter
import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.Increment
import org.fst.backup.service.CreateAndVerifyIncrementService
import org.fst.backup.service.ShutdownSystemService
import org.fst.backup.test.AbstractTest

class CreateBackupButtonTest extends AbstractTest {

	MockFor createIncrementService = new MockFor(CreateAndVerifyIncrementService.class)
	CommonViewModel commonViewModel
	SwingBuilder swing
	JButton button

	boolean isOnFinishClosureInvoked = false
	Closure onFinish = { isOnFinishClosureInvoked = true }

	String[] cmdOutput
	String[] cmdError

	void setUp() {
		super.setUp()
		commonViewModel = new CommonViewModel()
		commonViewModel.sourceDir = sourceDir
		commonViewModel.targetDir = targetDir
		commonViewModel.tabsModel = new DefaultSingleSelectionModel()
		commonViewModel.consoleDocument = new PlainDocument()
		createIncrement()
		commonViewModel.selectedIncrement = new IncrementListEntry('', increment)
		swing = new SwingBuilder()
		button = new CreateBackupButton().createComponent(commonViewModel, swing, onFinish)
	}

	private void verifyServiceInvocation(Closure assertParams) {
		createIncrementService.demand.createAndVerify(1) { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assertParams?.call(sourceDir, targetDir, outputCallback, errorCallback)
			cmdOutput?.each { String it ->
				outputCallback.callback(it)
			}
			cmdError?.each {String it ->
				errorCallback.callback(it)
			}
		}
	}

	private void clickButton() {
		createIncrementService.use { button.doClick() }
	}

	private void assertConsoleContainsCmdLinesAndErrLines() {
		def expectedOut = cmdOutput?.join()
		def expectedErr = cmdError?.join()
		def expected = expectedOut + expectedErr
		def actual = commonViewModel.consoleDocument.getText(0, commonViewModel.consoleDocument.getLength())
		assert expected.trim() == actual.trim()
	}

	void testButtonCallsService() {
		verifyServiceInvocation()
		clickButton()
	}

	void testServiceReceivesCorrectSourceAndTargetDir() {
		verifyServiceInvocation { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert commonViewModel.sourceDir == sourceDir
			assert commonViewModel.targetDir == targetDir
		}
		clickButton()
	}

	void testConsoleTabIsOpened() {
		verifyServiceInvocation { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert Tab.CONSOLE.ordinal() == commonViewModel.tabsModel.selectedIndex
		}
		clickButton()
	}

	void testConsoleStatusIsRedAtTheBeginning() {
		verifyServiceInvocation { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
		}
		clickButton()
	}

	void testConsoleStatusChangesFromRedToGreenOnFinish() {
		verifyServiceInvocation()
		clickButton()
		assert Color.GREEN == commonViewModel.consoleStatusColor
		assert 'Status: Abgeschlossen' == commonViewModel.consoleStatus
	}

	void testConsoleStatusIsRedAgainAtTheBeginning() {
		verifyServiceInvocation()
		clickButton()
		verifyServiceInvocation { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
		}
		clickButton()
	}

	void testStreamToConsole() {
		verifyServiceInvocation() { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert commonViewModel.consoleDocument == ((DocumentWriter) outputCallback).document
			assert commonViewModel.consoleDocument == ((DocumentWriter) errorCallback).document
		}
		clickButton()
	}

	void testCmdLinesAreBlack() {
		verifyServiceInvocation() { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert Color.BLACK == ((DocumentWriter) outputCallback).textColor
		}
		clickButton()
	}

	void testErrLinesAreRed() {
		verifyServiceInvocation() { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert Color.RED == ((DocumentWriter) errorCallback).textColor
		}
		clickButton()
	}

	void testConsoleGetsClearedBeforeEachBackup() {
		cmdOutput = ['I have been', 'invoked the', 'first time'].collect { String it -> it + System.lineSeparator() }
		cmdError = ['Err1', 'Err2', 'Err3'].collect { String it -> it + System.lineSeparator() }
		verifyServiceInvocation()
		clickButton()
		assertConsoleContainsCmdLinesAndErrLines()

		cmdOutput = ['I have been', 'invoked the', 'second time'].collect { String it -> it + System.lineSeparator() }
		cmdError = []
		verifyServiceInvocation()
		clickButton()
		assertConsoleContainsCmdLinesAndErrLines()
	}

	void testOnFinishClosureIsInvoked() {
		verifyServiceInvocation()
		clickButton()
		assert true == isOnFinishClosureInvoked
	}

	void testCmdLinesAreWrittenToConsoleAsync() {
		cmdOutput = ['Line1', 'Line2', 'Line3'].collect { String it -> it + System.lineSeparator() }

		boolean isCreateBackupServiceInvoked = false
		boolean isInvokedOutsideUIThread = false
		verifyServiceInvocation { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			isCreateBackupServiceInvoked = true
		}

		final swingStub = new StubFor(SwingBuilder.class)
		swingStub.demand.button(1) { Map it ->
			return new SwingBuilder().button(it)
		}
		swingStub.demand.doOutside(1) { Closure it ->
			isInvokedOutsideUIThread = true

			assert 0 == commonViewModel.consoleDocument.length
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
			assert false == isCreateBackupServiceInvoked

			it()

			assert cmdOutput.join() == commonViewModel.consoleDocument.getText(0, commonViewModel.consoleDocument.getLength())
			assert Color.GREEN == commonViewModel.consoleStatusColor
			assert 'Status: Abgeschlossen' == commonViewModel.consoleStatus
			assert true == isOnFinishClosureInvoked
			assert true == isCreateBackupServiceInvoked
		}

		button = new CreateBackupButton().createComponent(commonViewModel, swingStub.proxyInstance(), onFinish)

		clickButton()
		assert true == isInvokedOutsideUIThread
	}

	void testCallsShutdownSystemServiceIfSelected() {
		JFrame frame
		MockFor shutdownSystemService = new MockFor(ShutdownSystemService.class)
		shutdownSystemService.demand.shutdown(1) { JFrame _frame ->
			assert frame == _frame
		}

		verifyServiceInvocation()
		commonViewModel.shutdownSystemOnFinish = true
		shutdownSystemService.use {
			frame = swing.frame(title: 'test') { button = new CreateBackupButton().createComponent(commonViewModel, swing, onFinish) }
			frame.setVisible(true)
			clickButton()
		}
	}

	void testDontCallShutdownSystemServiceIfNotSelected() {
		MockFor shutdownSystemService = new MockFor(ShutdownSystemService.class)
		shutdownSystemService.demand.shutdown(0) { JFrame _frame ->
		}

		verifyServiceInvocation()
		commonViewModel.shutdownSystemOnFinish = false
		shutdownSystemService.use { clickButton() }
	}
}