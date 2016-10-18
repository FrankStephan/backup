package org.fst.backup.test.integration_test.ui;

import static org.junit.Assert.*;
import static org.fst.backup.test.integration_test.ui.UITestStep.*

import org.junit.Test;

class CreateIncrementAndCheckConsoleTest extends AbstractUITest {

	void testSuccessfulCreation() {
		CREATE_SOME_SOURCE_FILES.execute()
		
		CREATE_INCREMENT.execute()
		
		
	}

}
