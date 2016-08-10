package org.fst.backup.service

import static org.junit.Assert.*

class PathsToFilesServiceTest extends GroovyTestCase {

	PathsToFilesService service = new PathsToFilesService()

	File root

	void setUp() {
		root = new File(this.getClass().getSimpleName() + '-root/')
		root.mkdir()
	}

	void tearDown() {
		root.deleteDir()
	}

	private void createFileStructureUnderRoot(List<String> paths, root) {
		service.createFileStructureUnderRoot(paths, root)
	}

	void testRootHasNoFilesWhenPathsAreEmpty() {
		def paths = []
		createFileStructureUnderRoot(paths, root)
		assert 0 == root.list().length
	}

	void testRootHasNoFilesWhenBackupIsEmpty() {
		def paths = ['.']
		createFileStructureUnderRoot(paths, root)
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
		createFileStructureUnderRoot(paths, root)
		assertPathsCreated(paths)
	}

	void testManyFilesAreCreated() {
		def paths = ['a0.suf', 'b0.suf', 'c0.suf']
		createFileStructureUnderRoot(paths, root)
		assertPathsCreated(paths)
	}

	void testSimplePathIsCreated() {
		def paths = ['a0/a1/a2.suf']
		createFileStructureUnderRoot(paths, root)
		assertPathsCreated(paths)
	}

	void testForkedPathsAreCreated() {
		def paths = ['a0/a1/a2.suf', 'a0/b1/b2.suf']
		createFileStructureUnderRoot(paths, root)
		assertPathsCreated(paths)
	}

	void testEmptyPathsAreSkipped() {
		def paths = ['', 'a0/a1/a2.suf', '  ', 'b0/b1/b2.suf', '']
		createFileStructureUnderRoot(paths, root)
		assert 2 == root.list().length
		assertPathsCreated(['a0/a1/a2.suf', 'b0/b1/b2.suf'])
	}

	void testFilesAndPathsAreCreated() {
		def paths = ['a0.suf', 'a0/a1/a2.suf', 'b0.suf', 'a0/b1/b2.suf', 'c0.suf']
		createFileStructureUnderRoot(paths, root)
		assertPathsCreated(paths)
	}

	void testWhiteSpaceInPathSegments() {
		def paths = ['a 0/a 1/a 2.suf']
		createFileStructureUnderRoot(paths, root)
		assertPathsCreated(paths)
	}
}
