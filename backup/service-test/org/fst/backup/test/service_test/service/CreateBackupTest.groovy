package org.fst.backup.test.service_test.service

import static org.fst.backup.test.service_test.service.SystemServiceTestStep.*
import static org.junit.Assert.*

class CreateBackupTest extends AbstractServiceTest {

	void test() {
		CREATE_SOME_SOURCE_FILES.execute()

		DO_BACKUP.verify { String cmdLineContent ->
			cmdLineContent.contains('Using rdiff-backup version 1.2.8')
		}
	}
}
