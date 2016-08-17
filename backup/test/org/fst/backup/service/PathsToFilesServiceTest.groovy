package org.fst.backup.service

import static org.junit.Assert.*

import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractFilesUsingTest

class PathsToFilesServiceTest extends AbstractFilesUsingTest {

	PathsToFilesService service = new PathsToFilesService()

	File root

	void setUp() {
		super.setUp()
		root = new File(this.getClass().getSimpleName() + '-root/')
		root.mkdir()
	}

	void tearDown() {
		root.deleteDir()
	}

	private void createFileStructureFromPaths(List<String> paths, root) {
		service.createFileStructureFromPaths(paths, root)
	}

	void testWithNotExistingRoot() {
		File notExistingRoot = new File(tmpPath + 'NotExisting/')
		shouldFail (DirectoryNotExistsException) { service.createFileStructureFromPaths([], notExistingRoot) }
	}

	void testWithNonDirectoryRoot() {
		File file = new File(tmpPath, 'File.txt')
		file.createNewFile()
		shouldFail (FileIsNotADirectoryException) { service.createFileStructureFromPaths([], file) }
	}

	void testRootHasNoFilesWhenPathsAreEmpty() {
		def paths = []
		createFileStructureFromPaths(paths, root)
		assert 0 == root.list().length
	}

	void testRootHasNoFilesWhenBackupIsEmpty() {
		def paths = ['.']
		createFileStructureFromPaths(paths, root)
		assert 0 == root.list().length
	}

	private void assertPathsCreated(List<String> paths) {
		println '---'
		paths.each {
			String[] segments = it.split('/')
			StringBuilder pathBuilder = new StringBuilder()
			segments.each { String segment ->
				pathBuilder.append(segment)
				pathBuilder.append('/')
				println new File(root, pathBuilder.toString())
				assert new File(root, pathBuilder.toString()).exists()
			}
		}
	}

	void testSingleFileIsCreated() {
		def paths = ['a0.suf']
		createFileStructureFromPaths(paths, root)
		assertPathsCreated(paths)
	}

	void testManyFilesAreCreated() {
		def paths = ['a0.suf', 'b0.suf', 'c0.suf']
		createFileStructureFromPaths(paths, root)
		assertPathsCreated(paths)
	}

	void testSimplePathIsCreated() {
		def paths = ['a0/a1/a2.suf']
		createFileStructureFromPaths(paths, root)
		assertPathsCreated(paths)
	}

	void testForkedPathsAreCreated() {
		def paths = ['a0/a1/a2.suf', 'a0/b1/b2.suf']
		createFileStructureFromPaths(paths, root)
		assertPathsCreated(paths)
	}

	void testEmptyPathsAreSkipped() {
		def paths = ['', 'a0/a1/a2.suf', '  ', 'b0/b1/b2.suf', '']
		createFileStructureFromPaths(paths, root)
		assert 2 == root.list().length
		assertPathsCreated(['a0/a1/a2.suf', 'b0/b1/b2.suf'])
	}

	void testFilesAndPathsAreCreated() {
		def paths = ['a0.suf', 'a0/a1/a2.suf', 'b0.suf', 'a0/b1/b2.suf', 'c0.suf']
		createFileStructureFromPaths(paths, root)
		assertPathsCreated(paths)
	}

	void testWhiteSpaceInPathSegments() {
		def paths = ['a 0/a 1/a 2.suf']
		createFileStructureFromPaths(paths, root)
		assertPathsCreated(paths)
	}
}
