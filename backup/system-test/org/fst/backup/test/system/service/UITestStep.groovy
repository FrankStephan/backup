package org.fst.backup.test.system.service

import java.awt.Component
import java.awt.Container

import javax.swing.Box
import javax.swing.JTabbedPane

import org.fst.backup.ui.Tab;
import org.fst.backup.ui.frame.Frame;
import org.fst.backup.ui.frame.TabsPane;

enum UITestStep {


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
