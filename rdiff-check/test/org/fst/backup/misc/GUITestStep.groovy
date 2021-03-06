package org.fst.backup.misc

import java.awt.Component
import java.awt.Container

import javax.swing.Box
import javax.swing.JTabbedPane

import org.fst.backup.gui.Tab;
import org.fst.backup.gui.frame.Frame;
import org.fst.backup.gui.frame.TabsPane;

enum GUITestStep {


	NAVIGATE_TO_TAB {
		@Override
		Object execute(Object params) {
			JTabbedPane tabsPane = componentsMap[TabsPane.NAME]
			tabsPane.setSelectedIndex((params as Tab).ordinal())
			Box createTab = tabsPane.getSelectedComponent()

			// No need to show the frame?

			println createTab
		}
	},


	abstract execute(Object params)

	public static Map componentsMap
	static void init() {
		Frame frame = new Frame()
		frame.show()

		componentsMap = [:]
		putByNameRecursively(componentsMap, frame.window)
	}

	static void putByNameRecursively(Map componentsList, Component component) {
		String key = component.getName()?:''
		componentsList.put(key, component)
		if (component instanceof Container) {
			(((Container) component).components).each { putByNameRecursively(componentsList, it) }
		}
	}
}
