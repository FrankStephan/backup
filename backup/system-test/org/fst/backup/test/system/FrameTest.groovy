package org.fst.backup.test.system

import static org.fst.backup.test.system.TestStep.*
import static org.junit.Assert.*

import java.awt.Component
import java.awt.Container

import javax.swing.JFileChooser
import javax.swing.JList

import org.fst.backup.ui.BackupDirectoryChooser
import org.fst.backup.ui.Frame
import org.fst.backup.ui.IncrementsList

class FrameTest extends AbstractSystemTest {

	public void test() {

		Map componentsMap = [:]

		Frame frame = new Frame()
		frame.show()

		// TODO: Backup per UI erzeugen

		putByNameRecursively(componentsMap, frame.window)
		componentsMap.each { println it }

		JFileChooser backupDirectoryChooser = componentsMap[BackupDirectoryChooser.NAME]
		CREATE_SOME_SOURCE_FILES.execute()
		DO_BACKUP.execute()
		backupDirectoryChooser.setSelectedFile(targetDir)

		JList incrementsList = componentsMap[IncrementsList.NAME]
		incrementsList.ensureIndexIsVisible(0)
		assert null != incrementsList.indexToLocation(0)
	}

	private void putByNameRecursively(Map componentsList, Component component) {
		String key = component.getName()?:''
		componentsList.put(key, component)
		if (component instanceof Container) {
			(((Container) component).components).each { putByNameRecursively(componentsList, it) }
		}
	}
}
