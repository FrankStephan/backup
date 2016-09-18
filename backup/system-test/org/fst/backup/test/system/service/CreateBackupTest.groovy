package org.fst.backup.test.system.service

import static org.fst.backup.test.system.service.SystemServiceTestStep.*
import static org.junit.Assert.*

class CreateBackupTest extends AbstractSystemTest {

	void test() {
		CREATE_SOME_SOURCE_FILES.execute()

		DO_BACKUP.verify { String cmdLineContent ->
			cmdLineContent.contains('Using rdiff-backup version 1.2.8')
		}
	}
}
