package org.fst.backup.test.system.service

import static org.junit.Assert.*

import org.fst.backup.ui.Tab

class FrameTest extends AbstractSystemTest {

	public void test() {

		UITestStep.init()
		UITestStep.NAVIGATE_TO_TAB.execute(Tab.CREATE)

		//		componentsMap.each { println it }
		//
		//		JFileChooser backupDirectoryChooser = componentsMap[BackupDirectoryChooser.NAME]
		//		backupDirectoryChooser.setSelectedFile(targetDir)
		//
		//		JList incrementsList = componentsMap[IncrementsList.NAME]
		//		incrementsList.ensureIndexIsVisible(0)
		//		assert null != incrementsList.indexToLocation(0)
	}

	void testRemoveFilesLoadedFromLastIncrement () {
		fail()
	}
}
