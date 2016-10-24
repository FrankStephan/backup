package org.fst.backup.test.integration.service

import static org.fst.backup.test.integration.service.ServiceTestStep.*
import static org.junit.Assert.*

import org.fst.backup.model.Increment

class IncrementFileStructureTest extends AbstractServiceTest {

	void test() {
		List<Increment> increments
		File root = new File(tmpPath + 'root/')
		root.mkdir()

		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute()
		LIST_INCREMENTS.execute(null) { increments = it }
		GET_INCREMENT_FILE_STRUCTURE.execute([increments[0], root]) {}
		assert subPaths(root) == subPaths(sourceDir)
	}
}
