package org.fst.backup.test.unit.gui.frame;

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.swing.SwingBuilder

import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.frame.Frame
import org.fst.backup.gui.frame.TabsPane
import org.fst.backup.test.AbstractTest

class FrameTest extends AbstractTest {

	void testTabsPaneIsInvoked() {
		SwingBuilder swing = new SwingBuilder()
		CommonViewModel commonViewModel = new CommonViewModel()
		MockFor tabsPane = new MockFor(TabsPane.class)
		tabsPane.demand.createComponent(1) {CommonViewModel _commonViewModel, SwingBuilder _swing ->
			assert commonViewModel == _commonViewModel
			assert _swing == swing
		}
		tabsPane.use {
			new Frame().createComponent(commonViewModel, swing)
		}
	}

}
