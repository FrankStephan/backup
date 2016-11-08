package org.fst.backup.test.unit.gui.frame.inspect

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import javax.swing.DefaultListSelectionModel
import javax.swing.JFileChooser
import javax.swing.border.TitledBorder

import org.fst.backup.model.Increment
import org.fst.backup.service.IncrementFileStructureService
import org.fst.backup.test.AbstractTest
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.IncrementListEntry
import org.fst.backup.gui.frame.inspect.InspectIncrementFileChooser
import org.fst.backup.gui.frame.inspect.InspectIncrementFileSystemView

class InspectIncrementFileChooserTest extends AbstractTest {

	InspectIncrementFileChooser componentBuilder = new InspectIncrementFileChooser()
	CommonViewModel commonViewModel = new CommonViewModel()
	MockFor incrementFileStructureService = new MockFor(IncrementFileStructureService.class)
	JFileChooser fc

	void setUp() {
		super.setUp()
		createIncrement()
		commonViewModel.incrementsListSelectionModel = new DefaultListSelectionModel()
		fc = componentBuilder.createComponent(commonViewModel, new SwingBuilder())
	}

	private changeSelectedIncrement(Increment increment) {
		incrementFileStructureService.use {
			commonViewModel.selectedIncrement =  new IncrementListEntry('', increment)
		}
	}

	private void verifyIncrementFileStructureServiceInvocation(Closure assertParams) {
		incrementFileStructureService.demand.createIncrementFileStructure(1) { Increment increment, File root ->
			assertParams?.call(increment, root)
		}
	}

	void testFileChooserUsesInspectBackupFileSystemView() {
		assert InspectIncrementFileSystemView.class == fc.getFileSystemView().getClass()
	}

	void testFileChooserCreatesBackupFileSystemViewWithEmptyRoot() {
		assert []== (fc.getFileSystemView() as InspectIncrementFileSystemView).root.list()
	}

	void testFileChooserRetrievesFileStructureFromCorrectIncrement() {
		verifyIncrementFileStructureServiceInvocation( { Increment _increment, File root ->
			assert increment == _increment
			InspectIncrementFileSystemView fsv = fc.getFileSystemView()
			assert fsv.root == root
		} )
		changeSelectedIncrement(increment)
	}

	void testFileStructureIsCalculatedOnlyOnceIfSelectedIncrementDidNotChange() {
		verifyIncrementFileStructureServiceInvocation( { Increment _increment, File root ->
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
			assert (fc.getFileSystemView() as InspectIncrementFileSystemView).root == root
		} )
		changeSelectedIncrement(increment)
	}

	void testTitledBorderTextIsBlankInitially() {
		verifyIncrementFileStructureServiceInvocation()
		assert ' ' == ((fc.getBorder() as TitledBorder).getTitle())
	}

	void testTitledBorderTextEqualsTargetDir() {
		verifyIncrementFileStructureServiceInvocation()
		changeSelectedIncrement(increment)
		assert targetDir.absolutePath == ((fc.getBorder() as TitledBorder).getTitle())
	}
}
