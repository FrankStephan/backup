package org.fst.backup.test.service_test.service

import static org.fst.backup.test.service_test.service.ServiceTestStep.*
import static org.junit.Assert.*

import org.fst.backup.model.Increment

class RestoreIncrementTest extends AbstractServiceTest {

	void test() {
		List<Increment> increments
		File root = new File(tmpPath + 'root/')
		root.mkdir()

		CREATE_SOME_SOURCE_FILES.execute()
		def subPaths1 = subPaths(sourceDir)
		DO_BACKUP.execute()
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()
		ADD_ANOTHER_SOURCE_FILE.execute()
		def subPaths2 = subPaths(sourceDir)
		DO_BACKUP.execute()
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()
		LIST_INCREMENTS.execute(null) { increments = it }

		RESTORE_INCREMENT.execute([increments[1]]) {
			assert subPaths2 == subPaths(restoreDir)
		}

		clearRestoreDir()

		RESTORE_INCREMENT.execute([increments[0]]) {
			assert subPaths1 == subPaths(restoreDir)
		}
	}

	private void clearRestoreDir() {
		restoreDir.listFiles()*.delete()
		restoreDir.listFiles()*.deleteDir()
	}
}
