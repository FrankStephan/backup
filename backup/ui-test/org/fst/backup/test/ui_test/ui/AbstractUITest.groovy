package org.fst.backup.test.ui_test.ui

import org.fst.backup.test.AbstractTest

abstract class AbstractUITest extends AbstractTest {

	void setUp() {
		super.setUp()
		SystemUITestStep.init(sourceDir, targetDir)
	}
}
