package org.fst.backup.test.service_test.service

import static org.fst.backup.test.service_test.service.ServiceTestStep.*
import static org.junit.Assert.*

class CreateBackupTest extends AbstractServiceTest {

	void test() {
		CREATE_SOME_SOURCE_FILES.execute()

		DO_BACKUP.execute(null) { String cmdLineContent ->
			assert cmdLineContent.contains('Using rdiff-backup version 1.2.8')
		}
	}
}
