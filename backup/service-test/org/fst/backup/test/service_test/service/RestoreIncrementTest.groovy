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
		DO_BACKUP.execute()
		LIST_INCREMENTS.execute(null) { increments = it }

		RESTORE_INCREMENT.execute([increments[0]]) {}
		assert subPaths(sourceDir) == subPaths(restoreDir)
	}

	private void clearRestoreDir() {
		restoreDir.listFiles()*.delete()
		restoreDir.listFiles()*.deleteDir()
	}
}
