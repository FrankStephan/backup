package org.fst.backup.test.unit.gui.create

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor
import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.DefaultSingleSelectionModel
import javax.swing.JButton
import javax.swing.text.PlainDocument

import org.fst.backup.model.Increment
import org.fst.backup.service.CreateIncrementService
import org.fst.backup.test.AbstractTest
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.IncrementListEntry
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.create.CreateBackupButton

class CreateBackupButtonTest extends AbstractTest {

	MockFor createIncrementService = new MockFor(CreateIncrementService.class)
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

	private void verifyCreateIncrementServiceInvocation(Closure assertParams) {
		createIncrementService.demand.createIncrement(1) {File sourceDir, File targetDir, Closure commandLineCallback ->
			assertParams?.call(sourceDir, targetDir, commandLineCallback)
			commandLines?.each { it -> commandLineCallback(it) }
		}
	}

	private void clickButton() {
		createIncrementService.use { button.doClick() }
	}

	private void assertConsoleEqualsCommandLines() {
		def expected = commandLines.join(System.lineSeparator()).trim()
		def actual = commonViewModel.consoleDocument.getText(0, commonViewModel.consoleDocument.getLength()).trim()
		assert expected == actual
	}

	void testButtonCallsCreateBackupService() {
		verifyCreateIncrementServiceInvocation()
		clickButton()
	}

	void testCreateBackupServiceReceivesCorrectParams() {
		verifyCreateIncrementServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
			assert commonViewModel.sourceDir == sourceDir
			assert commonViewModel.targetDir == targetDir
		}
		clickButton()
	}

	void testConsoleTabIsOpened() {
		verifyCreateIncrementServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
			assert Tab.CONSOLE.ordinal() == commonViewModel.tabsModel.selectedIndex
		}
		clickButton()
	}

	void testConsoleStatusIsRedAtTheBeginning() {
		verifyCreateIncrementServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
		}
		clickButton()
	}

	void testConsoleStatusChangesFromRedToGreenOnFinish() {
		verifyCreateIncrementServiceInvocation()
		clickButton()
		assert Color.GREEN == commonViewModel.consoleStatusColor
		assert 'Status: Abgeschlossen' == commonViewModel.consoleStatus
	}

	void testConsoleStatusIsRedAgainAtTheBeginning() {
		verifyCreateIncrementServiceInvocation()
		clickButton()
		verifyCreateIncrementServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
		}
		clickButton()
	}

	void testComandLinesGetWrittenToConsoleDocument() {
		commandLines = ['Line1', 'Line2', 'Line3']
		verifyCreateIncrementServiceInvocation()
		clickButton()
		assertConsoleEqualsCommandLines()
	}

	void testConsoleGetsClearedBeforeEachBackup() {
		commandLines = ['I have been', 'invoked the', 'first time']
		verifyCreateIncrementServiceInvocation()
		clickButton()

		commandLines = ['I have been', 'invoked the', 'second time']
		verifyCreateIncrementServiceInvocation()
		clickButton()
		assertConsoleEqualsCommandLines()
	}

	void testOnFinishClosureIsInvoked() {
		verifyCreateIncrementServiceInvocation()
		clickButton()
		assert true == isOnFinishClosureInvoked
	}

	void testCmdLinesAreWrittenToConsoleAsync() {
		commandLines = ['Line1', 'Line2', 'Line3']

		boolean isCreateBackupServiceInvoked = false
		boolean isInvokedOutsideUIThread = false
		verifyCreateIncrementServiceInvocation { File sourceDir, File targetDir, Closure commandLineCallback ->
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