package org.fst.backup.test.unit.gui.frame

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import javax.swing.DefaultSingleSelectionModel
import javax.swing.JTabbedPane

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.Tab
import org.fst.backup.gui.frame.TabFactory
import org.fst.backup.gui.frame.TabsPane
import org.fst.backup.test.AbstractTest

class TabsPaneTest extends AbstractTest {

	private JTabbedPane tabbedPane

	void testCreatesTabsAccordingToTabOrder() {
		MockFor tabFactory = new MockFor(TabFactory.class)
		int n=Tab.values().length
		int i=0
		tabFactory.demand.create(n) { Tab tab ->
			assert i == tab.ordinal()
			i++
		}

		tabFactory.use {
			tabbedPane = new TabsPane().createComponent(new CommonViewModel(), new SwingBuilder())
		}
	}

	void testTabsModel() {
		MockFor tabFactory = new MockFor(TabFactory.class)
		int n=Tab.values().length
		tabFactory.demand.create(n) { Tab tab ->}

		CommonViewModel commonViewModel = new CommonViewModel()
		commonViewModel.tabsModel == new DefaultSingleSelectionModel()
		tabFactory.use {
			tabbedPane = new TabsPane().createComponent(commonViewModel, new SwingBuilder())
		}

		assert commonViewModel.tabsModel == tabbedPane.model
	}
}
