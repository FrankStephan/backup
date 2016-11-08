package org.fst.backup.test.unit.gui.frame.inspect

import static org.junit.Assert.*

import org.fst.backup.test.AbstractTest
import org.fst.backup.gui.frame.inspect.InspectIncrementFileSystemView

class InspectIncrementFileSystemViewTest extends AbstractTest {

	InspectIncrementFileSystemView fsv

	File root

	void setUp() {
		super.setUp()
		root = new File(tmpPath).absoluteFile
		fsv = new InspectIncrementFileSystemView(new File(tmpPath))
	}

	void testRootParamIsParsedToAbsoluteFile() {
		fsv = new InspectIncrementFileSystemView(new File(tmpPath))
		fsv.getRoot() == new File(tmpPath).absoluteFile
	}

	void testHasOnlyOneRoot() {
		assert fsv.roots == [root]
	}

	void testRootIsRoot() {
		assert fsv.getRoots() == [root]
		assert fsv.isRoot(root)
	}

	void testParentIsReturned() {
		File child = new File(root, 'Child.txt') << 'Child'
		assert root == fsv.getParentDirectory(child)
	}

	void testCannotGoUpToParentOfRoot() {
		assert null == fsv.getParentDirectory(root)
	}

	void testFolderCreationIsNotPossible() {
		String[] children = root.list()
		assert null == fsv.createNewFolder(root)
		assert children == root.list()
	}

	void testDefaultDirectoryIsRoot() {
		assert root == fsv.getDefaultDirectory()
	}

	void testHomeDirIsRoot() {
		assert root == fsv.getHomeDirectory()
	}
}
