package org.fst.backup.test.system.service

import static org.fst.backup.test.system.service.SystemServiceTestStep.*
import static org.junit.Assert.*

import org.fst.backup.model.Increment

class IncrementFileStructureTest extends AbstractSystemTest {

	void test() {
		CREATE_SOME_SOURCE_FILES.execute()
		DO_BACKUP.execute()
		List<Increment> increments = LIST_INCREMENTS.execute()

		File root = new File(tmpPath + 'root/')
		root.mkdir()
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
