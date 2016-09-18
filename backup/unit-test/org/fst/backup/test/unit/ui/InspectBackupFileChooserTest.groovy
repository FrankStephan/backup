package org.fst.backup.test.unit.ui

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import javax.swing.DefaultListSelectionModel
import javax.swing.JFileChooser

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
	MockFor incrementFileStructureService = new MockFor(IncrementFileStructureService.class)
	JFileChooser fc

	void setUp() {
		super.setUp()
		createIncrement()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		fc = componentBuilder.createComponent(commonViewModel)
	}

	private changeSelectedIncrement(Increment increment) {
		incrementFileStructureService.use {
			commonViewModel.selectedIncrement =  new IncrementListEntry('', increment)
		}
	}

	private void verifyIncrementFileStructureServiceInvocation(Closure assertParams) {
		incrementFileStructureService.demand.createIncrementFileStructure(1) { Increment increment, File root -> assertParams?.call(increment, root) }
	}

	void testFileChooserUsesInspectBackupFileSystemView() {
		assert InspectBackupFileSystemView.class == fc.getFileSystemView().getClass()
	}

	void testFileChooserCreatesBackupFileSystemViewWithEmptyRoot() {
		assert []== (fc.getFileSystemView() as InspectBackupFileSystemView).root.list()
	}

	void testFileChooserRetrievesFileStructureFromCorrectIncrement() {
		verifyIncrementFileStructureServiceInvocation( {Increment _increment, File root ->
			assert increment == _increment
			InspectBackupFileSystemView fsv = fc.getFileSystemView()
			assert fsv.root == root
		} )
		changeSelectedIncrement(increment)
	}

	void testFileStructureIsCalculatedOnlyOnceIfSelectedIncrementDidNotChange() {
		verifyIncrementFileStructureServiceInvocation( {Increment _increment, File root ->
			changeSelectedIncrement(increment)
			changeSelectedIncrement(increment)
		} )
	}

	void testFileStructureIsCalculatedAgainIfSelectedIncrementDidChange() {
		verifyIncrementFileStructureServiceInvocation()
		changeSelectedIncrement(increment)
		verifyIncrementFileStructureServiceInvocation()
		changeSelectedIncrement(createIncrement())
	}

	void testFileStructureIsAlwaysAddedToAnEmptyRoot() {
		verifyIncrementFileStructureServiceInvocation ( { Increment _increment, File root ->
			assert []== root.list()
			File child = new File(root, 'Child.txt') << 'Child'
			assert [child.name]== root.list()
		} )
		changeSelectedIncrement(increment)
		verifyIncrementFileStructureServiceInvocation ( { Increment _increment, File root ->
			assert []== root.list()
		} )
		changeSelectedIncrement(createIncrement())
	}

	void testFileSystemViewRootIsUsedAsParentOfCalculatedFileStructure() {
		verifyIncrementFileStructureServiceInvocation ( { Increment _increment, File root ->
			assert (fc.getFileSystemView() as InspectBackupFileSystemView).root == root
		} )
		changeSelectedIncrement(increment)
	}
}
