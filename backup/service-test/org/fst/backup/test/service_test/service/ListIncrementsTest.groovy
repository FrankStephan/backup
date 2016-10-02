package org.fst.backup.test.service_test.service

import static org.fst.backup.test.service_test.service.ServiceTestStep.*
import static org.junit.Assert.*

import org.fst.backup.model.Increment

class ListIncrementsTest extends AbstractServiceTest {

	public void test() {
		CREATE_SOME_SOURCE_FILES.execute()
		DO_BACKUP.execute()
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()
		ADD_ANOTHER_SOURCE_FILE.execute()
		DO_BACKUP.execute()
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()

		LIST_INCREMENTS.execute(null) { List<Increment> increments ->
			assert 2 == increments.size()
		}
	}
}
