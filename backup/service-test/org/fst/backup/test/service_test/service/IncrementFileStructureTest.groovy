package org.fst.backup.test.service_test.service

import static org.fst.backup.test.service_test.service.ServiceTestStep.*
import static org.junit.Assert.*

import org.fst.backup.model.Increment

class IncrementFileStructureTest extends AbstractServiceTest {

	void test() {
		List<Increment> increments
		File root = new File(tmpPath + 'root/')
		root.mkdir()

		CREATE_SOME_SOURCE_FILES.execute()
		DO_BACKUP.execute()
		LIST_INCREMENTS.execute(null, {it -> increments = it })
		GET_INCREMENT_FILE_STRUCTURE.verify([increments[0], root]) {
			assert subPaths(root) == subPaths(sourceDir)
		}
	}

	private List<String> subPaths(File parent) {
		List<String> subPaths = []
		parent.eachFileRecurse {File file ->
			String path = file.toPath().toString()
			String subPath = path.replace(parent.toString(), '')
			subPaths.add(subPath)
		}
		return subPaths
	}
}