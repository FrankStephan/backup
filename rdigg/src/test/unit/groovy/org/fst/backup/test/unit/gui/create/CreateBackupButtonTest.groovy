package org.fst.backup.test.unit.gui.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor
import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.DefaultSingleSelectionModel
import javax.swing.JButton
import javax.swing.text.PlainDocument

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.IncrementListEntry
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.create.CreateBackupButton
import org.fst.backup.gui.frame.create.DocumentWriter
import org.fst.backup.model.Increment
import org.fst.backup.service.CreateAndVerifyIncrementService
import org.fst.backup.test.AbstractTest

class CreateBackupButtonTest extends AbstractTest {

	MockFor createIncrementService = new MockFor(CreateAndVerifyIncrementService.class)
	CommonViewModel commonViewModel
	SwingBuilder swing
	JButton button

	boolean isOnFinishClosureInvoked = false
	Closure onFinish = { isOnFinishClosureInvoked = true }

	String[] commandLines
	String[] errLines

	void setUp() {
		super.setUp()
		commonViewModel = new CommonViewModel()
		commonViewModel.sourceDir = sourceDir
		commonViewModel.targetDir = targetDir
		commonViewModel.tabsModel = new DefaultSingleSelectionModel()
		commonViewModel.consoleDocument = new PlainDocument()
		createIncrement()
		commonViewModel.selectedIncrement = new IncrementListEntry('', increment)
		final swingStub = new StubFor(SwingBuilder.class)
		swingStub.demand.button(1) { Map it ->
			return new SwingBuilder().button(it)
		}
		swingStub.demand.doOutside (1..2) { Closure it -> it() }
		swing = swingStub.proxyInstance()
		button = new CreateBackupButton().createComponent(commonViewModel, swing, onFinish)
	}

	private void verifyServiceInvocation(Closure assertParams) {
		createIncrementService.demand.createAndVerify(1) { File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr ->
			assertParams?.call(sourceDir, targetDir, cmdOut, cmdErr)
			commandLines?.each { String it ->
				cmdOut.append(it).append(System.lineSeparator())
			}
			errLines?.each {String it ->
				cmdErr.append(it).append(System.lineSeparator())
			}
		}
	}

	private void clickButton() {
		createIncrementService.use { button.doClick() }
	}

	private void assertConsoleContainsCmdLinesAndErrLines() {
		def expectedCmd = commandLines?.join(System.lineSeparator())
		def expectedErr = errLines?.join(System.lineSeparator())
		def expected = expectedCmd + System.lineSeparator() + expectedErr
		def actual = commonViewModel.consoleDocument.getText(0, commonViewModel.consoleDocument.getLength())
		assert expected.trim() == actual.trim()
	}

	void testButtonCallsService() {
		verifyServiceInvocation()
		clickButton()
	}

	void testServiceReceivesCorrectSourceAndTargetDir() {
		verifyServiceInvocation { File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr ->
			assert commonViewModel.sourceDir == sourceDir
			assert commonViewModel.targetDir == targetDir
		}
		clickButton()
	}

	void testConsoleTabIsOpened() {
		verifyServiceInvocation { File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr ->
			assert Tab.CONSOLE.ordinal() == commonViewModel.tabsModel.selectedIndex
		}
		clickButton()
	}

	void testConsoleStatusIsRedAtTheBeginning() {
		verifyServiceInvocation { File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr ->
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
		verifyServiceInvocation { File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr ->
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
		}
		clickButton()
	}

	void testStreamToConsole() {
		verifyServiceInvocation() { File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr ->
			assert commonViewModel.consoleDocument == ((DocumentWriter) cmdOut).document
			assert commonViewModel.consoleDocument == ((DocumentWriter) cmdErr).document
		}
		clickButton()
	}

	void testCmdLinesAreBlack() {
		verifyServiceInvocation() { File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr ->
			assert Color.BLACK == ((DocumentWriter) cmdOut).textColor
		}
		clickButton()
	}

	void testErrLinesAreRed() {
		verifyServiceInvocation() { File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr ->
			assert Color.RED == ((DocumentWriter) cmdErr).textColor
		}
		clickButton()
	}

	void testConsoleGetsClearedBeforeEachBackup() {
		commandLines = ['I have been', 'invoked the', 'first time']
		errLines = ['Err1', 'Err2', 'Err3']
		verifyServiceInvocation()
		clickButton()
		assertConsoleContainsCmdLinesAndErrLines()

		commandLines = ['I have been', 'invoked the', 'second time']
		errLines = []
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
		commandLines = ['Line1', 'Line2', 'Line3']

		boolean isCreateBackupServiceInvoked = false
		boolean isInvokedOutsideUIThread = false
		verifyServiceInvocation { File sourceDir, File targetDir, Appendable cmdOut, Appendable cmdErr ->
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

			assert commandLines == commonViewModel.consoleDocument.getText(0, commonViewModel.consoleDocument.getLength()).readLines()
			assert Color.GREEN == commonViewModel.consoleStatusColor
			assert 'Status: Abgeschlossen' == commonViewModel.consoleStatus
			assert true == isOnFinishClosureInvoked
			assert true == isCreateBackupServiceInvoked
		}

		button = new CreateBackupButton().createComponent(commonViewModel, swingStub.proxyInstance(), onFinish)

		clickButton()
		assert true == isInvokedOutsideUIThread
	}
}