package org.fst.backup.test.unit.ui

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import javax.swing.DefaultListSelectionModel

import org.fst.backup.model.Increment
import org.fst.backup.service.IncrementFileStructureService
import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.CommonViewModel
import org.fst.backup.ui.IncrementListEntry
import org.fst.backup.ui.frame.inspect.InspectBackupFileChooser
import org.fst.backup.ui.frame.inspect.InspectBackupFileSystemView

class InspectBackupFileChooserTest extends AbstractTest {

	InspectBackupFileChooser componentBuilder = new InspectBackupFileChooser()
	CommonViewModel commonViewModel = new CommonViewModel()

	void setUp() {
		super.setUp()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
	}

	void testFileChooserIsInitiallyEmpty() {
		def fc = componentBuilder.createComponent(commonViewModel)
		assert 0 == fc.currentDirectory.list().length
	}

	void testFileChooserUsesInspectBackupFileSystemView() {
		def fc = componentBuilder.createComponent(commonViewModel)
		assert InspectBackupFileSystemView.class == fc.getFileSystemView().getClass()
	}

	void testFileChooserRetrievesFileStructureFromCorrectIncrement() {
		def fc = componentBuilder.createComponent(commonViewModel)
		Increment increment = new Increment()

		def listIncrementsService = new MockFor(IncrementFileStructureService.class)
		listIncrementsService.demand.createIncrementFileStructure(1) { Increment _increment, File root ->
			assert increment == _increment
			InspectBackupFileSystemView fsv = fc.getFileSystemView()
			assert fsv.root == root
		}
		listIncrementsService.use {
			commonViewModel.selectedIncrement =  new IncrementListEntry('', increment)
		}
	}

	void testFileChooserContentsChangeIfANewIncrementIsSelected() {
		def fc = componentBuilder.createComponent(commonViewModel)
		Increment increment1 = new Increment()
		Increment increment2 = new Increment()
		def listIncrementsService = new MockFor(IncrementFileStructureService.class)
		File root1
		listIncrementsService.demand.createIncrementFileStructure(2) { Increment _increment, File root ->
			if (null == root1) {
				root1 = root
			} else {
				assert root != root1
			}
		}
		listIncrementsService.use {
			commonViewModel.selectedIncrement =  new IncrementListEntry('', increment1)
		}
		listIncrementsService.use {
			commonViewModel.selectedIncrement =  new IncrementListEntry('', increment2)
		}
	}
}
