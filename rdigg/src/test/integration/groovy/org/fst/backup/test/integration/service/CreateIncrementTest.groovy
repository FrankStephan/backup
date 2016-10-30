package org.fst.backup.test.integration.service

import static org.fst.backup.test.integration.service.IntegrationTestStep.*
import static org.junit.Assert.*

class CreateIncrementTest extends AbstractIntegrationTest {

	void test() {
		CREATE_SOME_SOURCE_FILES.execute()

		String cmdLineContent
		CREATE_INCREMENT.execute(null) { cmdLineContent = it }
		println cmdLineContent
		assert cmdLineContent.contains('Using rdiff-backup version 1.2.8')
		assertVerificationIsPerformed(cmdLineContent)
	}

	private assertVerificationIsPerformed(String cmdLineContent) {
		assert cmdLineContent.trim().endsWith('Every file verified successfully.')
	}
}
