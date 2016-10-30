package org.fst.backup.test.integration.service

import static org.fst.backup.test.integration.service.IntegrationTestStep.*
import static org.junit.Assert.*

import org.fst.backup.model.Increment

class RestoreIncrementTest extends AbstractIntegrationTest {

	void test() {
		List<Increment> increments
		File root = new File(tmpPath + 'root/')
		root.mkdir()

		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute()
		LIST_INCREMENTS.execute(null) { increments = it }

		RESTORE_INCREMENT.execute([increments[0]]) {}

		assertRestoreDirContainsFilesFromSource()
	}

	private assertRestoreDirContainsFilesFromSource() {
		assert subPaths(sourceDir) == subPaths(restoreDir)
	}
}
