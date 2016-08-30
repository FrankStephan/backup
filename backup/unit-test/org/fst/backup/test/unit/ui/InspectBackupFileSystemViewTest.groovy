package org.fst.backup.test.unit.ui

import static org.junit.Assert.*

import org.fst.backup.test.AbstractTest
import org.fst.backup.ui.frame.inspect.InspectBackupFileSystemView

class InspectBackupFileSystemViewTest extends AbstractTest {

	InspectBackupFileSystemView fsv

	File root

	void setUp() {
		super.setUp()
		root = new File(tmpPath).absoluteFile
		createTestFileStructure()
		fsv = new InspectBackupFileSystemView(new File(tmpPath))
	}

	private createTestFileStructure() {
		FileTreeBuilder ftb = new FileTreeBuilder(root)
		ftb {
			'a0.suf'('')
			a0 {
				a1 { 'a2.suf'('') }
				b1 { 'b2.suf'('') }
			}
			'b0.suf'('')
			'c0.suf'('')
		}
	}

	void testRootParamIsParsedToAbsoluteFile() {
		fsv = new InspectBackupFileSystemView(new File(tmpPath))
		fsv.getRoot() == new File(tmpPath).absoluteFile
	}

	void testHasOnlyOneRoot() {
		assert fsv.roots == [root]
	}

	void testRootIsRoot() {
		assert fsv.getRoots() == [root]
		assert fsv.isRoot(root)
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
