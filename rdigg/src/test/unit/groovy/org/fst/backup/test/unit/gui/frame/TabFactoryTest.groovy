package org.fst.backup.test.unit.gui.frame

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel
import javax.swing.DefaultListSelectionModel

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.TabFactory
import org.fst.backup.gui.frame.choose.BackupDirectoryChooser
import org.fst.backup.gui.frame.choose.IncrementsList
import org.fst.backup.gui.frame.choose.InspectIncrementButton
import org.fst.backup.gui.frame.choose.RestoreButton
import org.fst.backup.gui.frame.console.ConsolePane
import org.fst.backup.gui.frame.console.ShutdownSystemCheckbox
import org.fst.backup.gui.frame.create.CreateBackupButton
import org.fst.backup.gui.frame.create.SourceFileChooser
import org.fst.backup.gui.frame.create.TargetFileChooser
import org.fst.backup.gui.frame.inspect.InspectIncrementFileChooser
import org.fst.backup.gui.frame.restore.RestoreBackupButton
import org.fst.backup.gui.frame.restore.RestoreDirectoryChooser
import org.fst.backup.test.AbstractTest

class TabFactoryTest extends AbstractTest {

	void testChooseTab() {
		def expectedComponentCreatorClasses = [
			BackupDirectoryChooser.class,
			IncrementsList.class,
			InspectIncrementButton.class,
			RestoreButton.class,
		]
		this.verifyUsageOfComponentCreatorsForTab(expectedComponentCreatorClasses, Tab.CHOOSE)
	}

	void testConsoleTab() {
		def expectedComponentCreatorClasses = [ConsolePane.class, ShutdownSystemCheckbox.class]
		this.verifyUsageOfComponentCreatorsForTab(expectedComponentCreatorClasses, Tab.CONSOLE)
	}

	void testCreateTab() {
		def expectedComponentCreatorClasses = [SourceFileChooser.class, TargetFileChooser.class]
		MockFor createBackupButtonMock = new MockFor(CreateBackupButton.class)
		createBackupButtonMock.demand.createComponent(1) {CommonViewModel commonViewModel, SwingBuilder swing, Closure onFinish ->
		}
		createBackupButtonMock.use {
			this.verifyUsageOfComponentCreatorsForTab(expectedComponentCreatorClasses, Tab.CREATE)
		}
	}

	void testInspectTab() {
		def expectedComponentCreatorClasses = [InspectIncrementFileChooser.class]
		this.verifyUsageOfComponentCreatorsForTab(expectedComponentCreatorClasses, Tab.INSPECT)
	}

	void testRestoreTab() {
		def expectedComponentCreatorClasses = [RestoreDirectoryChooser.class]
		MockFor restoreBackupButtonMock = new MockFor(RestoreBackupButton.class)
		restoreBackupButtonMock.demand.createComponent(1) {CommonViewModel commonViewModel, SwingBuilder swing, Closure onFinish ->
		}
		restoreBackupButtonMock.use {
			this.verifyUsageOfComponentCreatorsForTab(expectedComponentCreatorClasses, Tab.RESTORE)
		}
	}

	private void verifyUsageOfComponentCreatorsForTab(List<Class<?>> componentCreatorClasses, Tab tab) {
		CommonViewModel commonViewModel = new CommonViewModel()
		commonViewModel.incrementsListModel = new DefaultListModel()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		this.use(componentCreatorClasses as Queue, { new TabFactory(commonViewModel, new SwingBuilder()).create(tab) })
	}

	private void use(Queue<Class<?>> componentCreatorClasses, Closure createComponent) {
		if (!componentCreatorClasses.empty) {
			Class<?> componentCreatorClass = componentCreatorClasses.poll()
			MockFor componentCreatorMock = mock(componentCreatorClass)
			componentCreatorMock.use {
				this.use(componentCreatorClasses, createComponent)
			}
		} else {
			createComponent()
		}
	}

	private MockFor mock(Class componentCreatorClass) {
		MockFor componentCreatorMock = new MockFor(componentCreatorClass)
		componentCreatorMock.demand.createComponent(1) { CommonViewModel commonViewModel, SwingBuilder swing ->
		}
		return componentCreatorMock
	}
}
