package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.Increment
import org.fst.backup.service.IncrementFileStructureService
import org.fst.backup.service.ListPathsFromIncrementService
import org.fst.backup.service.PathsToFilesService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest

class IncrementFileStructureServiceTest extends AbstractTest {
	private File root
	def pathsToFilesService
	def listPathsFromIncrementService

	void setUp() {
		super.setUp()
		root = new File(tmpPath + 'root/')
		root.mkdir()
	}

	void testWithNotExistingRoot() {
		IncrementFileStructureService service = new IncrementFileStructureService()
		File notExistingRoot = new File(tmpPath + 'NotExisting/')
		shouldFail (DirectoryNotExistsException) { service.createIncrementFileStructure(createIncrement(), notExistingRoot) }
	}

	void testWithNonDirectoryRoot() {
		IncrementFileStructureService service = new IncrementFileStructureService()
		File file = new File(tmpPath, 'File.txt') << 'I am a real file'
		shouldFail (FileIsNotADirectoryException) { service.createIncrementFileStructure(createIncrement(), file) }
	}

	void testServicePassesExceptions1() {
		mockUsedServices({ throw new SomeException1() }, { throw new SomeException2() })
		listPathsFromIncrementService.use {
			IncrementFileStructureService service = new IncrementFileStructureService()
			shouldFail (SomeException1) { service.createIncrementFileStructure(createIncrement(), root) }
		}
	}

	void testServicePassesExceptions2() {
		mockUsedServices({ return new ArrayList<String>() }, { throw new SomeException2() } )
		pathsToFilesService.use {
			listPathsFromIncrementService.use {
				IncrementFileStructureService service = new IncrementFileStructureService()
				shouldFail (SomeException2) { service.createIncrementFileStructure(createIncrement(), root) }
			}
		}
	}

	void testServiceIsInvokedWithCorrectParams1() {
		Increment increment = createIncrement()
		mockUsedServices({ assert increment == it }, { })
		pathsToFilesService.use {
			listPathsFromIncrementService.use {
				IncrementFileStructureService service = new IncrementFileStructureService()
				service.createIncrementFileStructure(increment, root)
			}
		}
	}

	void testServiceIsInvokedWithCorrectParams2() {
		List<String> paths = createPaths()
		mockUsedServices({ return paths }, {
			assert paths == it[0]
			assert root == it[1]
		})
		pathsToFilesService.use {
			listPathsFromIncrementService.use {
				IncrementFileStructureService service = new IncrementFileStructureService()
				service.createIncrementFileStructure(createIncrement(), root)
			}
		}
	}

	private List<String> createPaths() {
		return ['a0/a1', 'a0/b1.suf']
	}

	private void mockUsedServices(Closure<List<String>> returnPaths, Closure createFileStructure) {
		listPathsFromIncrementService = new MockFor(ListPathsFromIncrementService.class)
		listPathsFromIncrementService.demand.listPathsFromIncrement(1) { Increment it -> returnPaths(it) }

		pathsToFilesService = new MockFor(PathsToFilesService.class)
		pathsToFilesService.demand.createFileStructureFromPaths(1) { List<String> pathsList, File rootDir -> createFileStructure([pathsList, rootDir]) }
	}

	class SomeException1 extends Exception {}
	class SomeException2 extends Exception {}
}
