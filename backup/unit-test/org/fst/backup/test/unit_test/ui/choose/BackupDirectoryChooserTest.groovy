package org.fst.backup.test.unit_test.ui.choose

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import javax.swing.DefaultListModel

import org.fst.backup.model.Increment
import org.fst.backup.service.ListIncrementsService
import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.frame.choose.BackupDirectoryChooser

class BackupDirectoryChooserTest extends AbstractTest {

	CommonViewModel commonViewModel = new CommonViewModel()
	SwingBuilder swing
	MockFor listIncrementService = new MockFor(ListIncrementsService.class)

	private void changeSelectedFile() {
		listIncrementService.use {
			def fc = new BackupDirectoryChooser().createComponent(commonViewModel, swing)
			fc.selectedFile = targetDir
		}
	}

	void setUp() {
		super.setUp()
		commonViewModel.incrementsListModel = new DefaultListModel<>()
		createIncrement()
		def swingMock = new MockFor(SwingBuilder.class)
		swingMock.ignore(~'.*')
		swing = swingMock.proxyInstance()
	}

	void testIncrementListGetsUpdatedWhenFileSelectionChanges() {
		listIncrementService.demand.listIncrements(1) { File _targetDir ->
			assert targetDir == _targetDir
			return [increment]
		}

		changeSelectedFile()

		assert 1 == commonViewModel.incrementsListModel.size
		assert [increment]== commonViewModel.incrementsListModel*.increment
	}

	void testIncrementsListGetsClearedBeforeNewIncrementsAreAdded() {
		Increment increment1 = createIncrement()

		listIncrementService.demand.listIncrements(1) { File _targetDir ->
			return [increment1]
		}
		changeSelectedFile()

		Increment increment2 = createIncrement()
		listIncrementService.demand.listIncrements(1) { File _targetDir ->
			return [increment2]
		}
		changeSelectedFile()
		assert [increment2]== commonViewModel.incrementsListModel*.increment
	}

	void testIncrementsAreOrderedChronologicallyDescending() {
		Increment increment1 = createIncrement()
		Increment increment2 = createIncrement()
		Increment increment3 = createIncrement()
		increment1.secondsSinceTheEpoch = 3
		increment2.secondsSinceTheEpoch = 2
		increment3.secondsSinceTheEpoch = 1

		listIncrementService.demand.listIncrements(1) { File _targetDir ->
			return [increment2, increment1, increment3]
		}
		changeSelectedFile()
		assert [increment1, increment2, increment3]== commonViewModel.incrementsListModel*.increment
	}
}
