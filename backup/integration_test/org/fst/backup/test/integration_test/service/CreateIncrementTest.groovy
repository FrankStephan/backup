package org.fst.backup.test.integration_test.service

import static org.fst.backup.test.integration_test.service.ServiceTestStep.*
import static org.junit.Assert.*

class CreateIncrementTest extends AbstractServiceTest {

	void test() {
		CREATE_SOME_SOURCE_FILES.execute()

		String cmdLineContent
		CREATE_INCREMENT.execute(null) { cmdLineContent = it }
		println cmdLineContent
		assertRdiffIsExecuted(cmdLineContent)
		assertVerificationIsPerformed(cmdLineContent)
	}

	private assertRdiffIsExecuted(String cmdLineContent) {
		assert cmdLineContent.contains('Using rdiff-backup version 1.2.8')
	}

	private assertVerificationIsPerformed(String cmdLineContent) {
		assert cmdLineContent.trim().endsWith('Every file verified successfully.')
	}
}
