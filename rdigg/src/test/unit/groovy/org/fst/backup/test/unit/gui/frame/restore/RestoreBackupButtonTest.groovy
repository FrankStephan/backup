package org.fst.backup.test.unit.gui.frame.restore

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor
import groovy.swing.SwingBuilder

import java.awt.Color

import javax.swing.DefaultSingleSelectionModel
import javax.swing.JButton
import javax.swing.text.PlainDocument

import org.fst.backup.model.Increment
import org.fst.backup.service.RestoreIncrementService
import org.fst.backup.test.AbstractTest
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.IncrementListEntry
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.restore.RestoreBackupButton

class RestoreBackupButtonTest extends AbstractTest {

	MockFor restoreIncrementService = new MockFor(RestoreIncrementService.class)
	CommonViewModel commonViewModel
	SwingBuilder swing
	JButton button

	boolean isOnFinishClosureInvoked = false
	Closure onFinish = { isOnFinishClosureInvoked = true }

	String[] commandLines

	void setUp() {
		super.setUp()
		commonViewModel = new CommonViewModel()
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
		button = new RestoreBackupButton().createComponent(commonViewModel, swing, onFinish)
	}

	private void verifyRestoreIncrementServiceInvocation(Closure assertParams) {
		restoreIncrementService.demand.restore(1) {Increment increment, File restoreDir, Closure commandLineCallback ->
			assertParams?.call(increment, restoreDir, commandLineCallback)
			commandLines?.each { it -> commandLineCallback(it) }
		}
	}

	private void clickButton() {
		restoreIncrementService.use { button.doClick() }
	}

	private void assertConsoleEqualsCommandLines() {
		def expected = commandLines.join(System.lineSeparator()).trim()
		def actual = commonViewModel.consoleDocument.getText(0, commonViewModel.consoleDocument.getLength()).trim()
		assert expected == actual
	}

	void testNothingHappensIfNoIncrementIsSelected() {
		commonViewModel.selectedIncrement = null
		restoreIncrementService.demand.restore(0) {Increment increment, File restoreDir, Closure commandLineCallback ->}
		clickButton()
	}

	void testButtonCallsRestoreIncrementService() {
		verifyRestoreIncrementServiceInvocation()
		clickButton()
	}

	void testRestoreIncrementServiceReceivesCorrectParams() {
		verifyRestoreIncrementServiceInvocation { Increment increment, File restoreDir, Closure commandLineCallback ->
			assert commonViewModel.selectedIncrement.increment == increment
			assert commonViewModel.restoreDir == restoreDir
		}
		clickButton()
	}

	void testConsoleTabIsOpened() {
		verifyRestoreIncrementServiceInvocation { Increment increment, File restoreDir, Closure commandLineCallback ->
			assert Tab.CONSOLE.ordinal() == commonViewModel.tabsModel.selectedIndex
		}
		clickButton()
	}

	void testConsoleStatusIsRedAtTheBeginning() {
		verifyRestoreIncrementServiceInvocation { Increment increment, File restoreDir, Closure commandLineCallback ->
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
		}
		clickButton()
	}

	void testConsoleStatusChangesFromRedToGreenOnFinish() {
		verifyRestoreIncrementServiceInvocation()
		clickButton()
		assert Color.GREEN == commonViewModel.consoleStatusColor
		assert 'Status: Abgeschlossen' == commonViewModel.consoleStatus
	}

	void testConsoleStatusIsRedAgainAtTheBeginning() {
		verifyRestoreIncrementServiceInvocation()
		clickButton()
		verifyRestoreIncrementServiceInvocation { Increment increment, File restoreDir, Closure commandLineCallback ->
			assert 'Status: Laufend' == commonViewModel.consoleStatus
			assert Color.RED == commonViewModel.consoleStatusColor
		}
		clickButton()
	}

	void testComandLinesGetWrittenToConsoleDocument() {
		commandLines = ['Line1', 'Line2', 'Line3']
		verifyRestoreIncrementServiceInvocation()
		clickButton()
		assertConsoleEqualsCommandLines()
	}

	void testConsoleGetsClearedBeforeEachBackup() {
		commandLines = ['I have been', 'invoked the', 'first time']
		verifyRestoreIncrementServiceInvocation()
		clickButton()

		commandLines = ['I have been', 'invoked the', 'second time']
		verifyRestoreIncrementServiceInvocation()
		clickButton()
		assertConsoleEqualsCommandLines()
	}

	void testOnFinishClosureIsInvoked() {
		verifyRestoreIncrementServiceInvocation()
		clickButton()
		assert true == isOnFinishClosureInvoked
	}

	void testCmdLinesAreWrittenToConsoleAsync() {
		commandLines = ['Line1', 'Line2', 'Line3']

		boolean isRestoreIncrementServiceInvoked = false
		boolean isInvokedOutsideUIThread = false
		verifyRestoreIncrementServiceInvocation { Increment increment, File restoreDir, Closure commandLineCallback ->
			isRestoreIncrementServiceInvoked = true
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
			assert false == isRestoreIncrementServiceInvoked

			it()

			assert commandLines == commonViewModel.consoleDocument.getText(0, commonViewModel.consoleDocument.getLength()).readLines()
			assert Color.GREEN == commonViewModel.consoleStatusColor
			assert 'Status: Abgeschlossen' == commonViewModel.consoleStatus
			assert true == isOnFinishClosureInvoked
			assert true == isRestoreIncrementServiceInvoked
		}

		button = new RestoreBackupButton().createComponent(commonViewModel, swingStub.proxyInstance(), onFinish)

		clickButton()
		assert true == isInvokedOutsideUIThread
	}
}
