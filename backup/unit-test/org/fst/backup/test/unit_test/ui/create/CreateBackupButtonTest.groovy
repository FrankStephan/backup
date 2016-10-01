package org.fst.backup.test.unit_test.ui.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor
import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.DefaultSingleSelectionModel
import javax.swing.JButton
import javax.swing.text.PlainDocument

import org.fst.backup.model.Increment
import org.fst.backup.service.CreateBackupService
import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.IncrementListEntry
import org.fst.backup.ui.Tab
import org.fst.backup.ui.frame.create.CreateBackupButton

class CreateBackupButtonTest extends AbstractTest {

	MockFor createBackupService = new MockFor(CreateBackupService.class)
	CommonViewModel commonViewModel
	SwingBuilder swing
	JButton button

	boolean isOnFinishClosureInvoked = false
	Closure onFinish = { isOnFinishClosureInvoked = true }

	String[] commandLines

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
		swingStub.demand.doOutside (1..2) {Closure it -> it()}
		swing = swingStub.proxyInstance()
		button = new CreateBackupButton().createComponent(commonViewModel, swing, onFinish)
	}

	private void verifyCreateBackupServiceInvocation(Closure assertParams) {
		createBackupService.demand.createBackup(1) {File sourceDir, File targetDir, Closure commandLineCallback ->
			assertParams?.call(sourceDir, targetDir, commandLineCallback)
			commandLines?.each { it -> commandLineCallback(it) }
		}
	}

	private void clickButton() {
		createBackupService.use { button.doClick() }
	}

	private void assertConsoleEqualsCommandLines() {
		def expected = commandLines.join(System.lineSeparator()).trim()
		def actual = commonViewModel.consoleDocument.getText(0, commonViewModel.consoleDocument.getLength()).trim()
		assert expected == actual
	}

	void testButtonCallsCreateBackupService() {
		verifyCreateBackupServiceInvocation()
		clickButton()
	}

	void testCreateBackupServiceReceivesCorrectParams() {
		verifyCreateBackupServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
			assert commonViewModel.sourceDir == sourceDir
			assert commonViewModel.targetDir == targetDir
		}
		clickButton()
	}

	void testConsoleTabIsOpened() {
		verifyCreateBackupServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
			assert Tab.CONSOLE.ordinal() == commonViewModel.tabsModel.selectedIndex
		}
		clickButton()
	}

	void testConsoleStatusIsRedAtTheBeginning() {
		verifyCreateBackupServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
		}
		clickButton()
	}

	void testConsoleStatusChangesFromRedToGreenOnFinish() {
		verifyCreateBackupServiceInvocation()
		clickButton()
		assert Color.GREEN == commonViewModel.consoleStatusColor
		assert 'Status: Abgeschlossen' == commonViewModel.consoleStatus
	}

	void testConsoleStatusIsRedAgainAtTheBeginning() {
		verifyCreateBackupServiceInvocation()
		clickButton()
		verifyCreateBackupServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
		}
		clickButton()
	}

	void testComandLinesGetWrittenToConsoleDocument() {
		commandLines = ['Line1', 'Line2', 'Line3']
		verifyCreateBackupServiceInvocation()
		clickButton()
		assertConsoleEqualsCommandLines()
	}

	void testConsoleGetsClearedBeforeEachBackup() {
		commandLines = ['I have been', 'invoked the', 'first time']
		verifyCreateBackupServiceInvocation()
		clickButton()

		commandLines = ['I have been', 'invoked the', 'second time']
		verifyCreateBackupServiceInvocation()
		clickButton()
		assertConsoleEqualsCommandLines()
	}

	void testOnFinishClosureIsInvoked() {
		verifyCreateBackupServiceInvocation()
		clickButton()
		assert true == isOnFinishClosureInvoked
	}

	void testCmdLinesAreWrittenToConsoleAsync() {
		commandLines = ['Line1', 'Line2', 'Line3']

		boolean isCreateBackupServiceInvoked = false
		boolean isInvokedOutsideUIThread = false
		verifyCreateBackupServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
			isCreateBackupServiceInvoked = true
		}

		final swingStub = new StubFor(SwingBuilder.class)
		swingStub.demand.button(1) { Map it ->
			return new SwingBuilder().button(it)
		}
		swingStub.demand.doOutside(1) {Closure it ->
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