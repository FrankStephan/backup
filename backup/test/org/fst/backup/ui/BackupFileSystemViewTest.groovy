package org.fst.backup.ui

import static org.junit.Assert.*

import org.fst.backup.test.AbstractFilesUsingTest

class BackupFileSystemViewTest extends AbstractFilesUsingTest {

	InspectBackupFileSystemView fsv

	File root

	void setUp() {
		super.setUp()
		root = new File(tmpPath)
		createTestFileStructure()
		fsv = new InspectBackupFileSystemView(root)
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

	void testHasOnlyOneRoot() {
		fsv.roots == [root]
	}

	void testNoFileCreationIsPossible() {
		assertNull(fsv.createFileObject('somePath'))
	}

	void testNoFileCreationIsPossible2() {
		assertNull(fsv.createFileObject(root, 'someFile'))
	}

	void testNoDirCreationIsPossible() {
		assertNull(fsv.createNewFolder(root))
	}

	void testGetChild() {
		fail()
	}

	void testGetFiles() {
		fail()
	}

	void testRootIsDefaultDirectory() {
		assert root == fsv.getDefaultDirectory()
	}

	void testHomeDirIsNull() {
		assertNull(fsv.getHomeDirectory())
	}
}
