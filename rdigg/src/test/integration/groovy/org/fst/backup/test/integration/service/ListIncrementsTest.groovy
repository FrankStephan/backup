package org.fst.backup.test.integration.service

import static org.fst.backup.test.integration.service.IntegrationTestStep.*
import static org.junit.Assert.*

import org.fst.backup.model.Increment

class ListIncrementsTest extends AbstractIntegrationTest {

	public void test() {
		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute()
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()
		ADD_ANOTHER_SOURCE_FILE.execute()
		CREATE_INCREMENT.execute()
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()

		List<Increment> increments
		LIST_INCREMENTS.execute(null) { increments = it }
		assert 2 == increments.size()
	}
}
