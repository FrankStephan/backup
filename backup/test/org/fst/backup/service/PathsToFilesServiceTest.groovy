package org.fst.backup.service

import static org.junit.Assert.*

class PathsToFilesServiceTest extends GroovyTestCase {

	PathsToFilesService service = new PathsToFilesService('\\' as char)

	File root

	void setUp() {
		root = new File(this.getClass().getSimpleName() + '-root/')
		root.mkdir()
	}

	void tearDown() {
		root.deleteDir()
	}
	
	void testRootHasNoFilesWhenPathsAreEmpty() {
		def paths = [] as String[]
		service.createFileStructureUnderRoot(paths, root)
		assert 0 == root.list().length
	}

	void testRootHasNoFilesWhenBackupIsEmpty() {
		def paths = ['.'] as String[]
		service.createFileStructureUnderRoot(paths, root)
		assert 0 == root.list().length
	}
	
	void testEmptyPathsAreSkipped() {
		def paths = ['a0/a1/a2.suf', '  ', 'b0/b1/b2.suf', ''] as String[]
		service.createFileStructureUnderRoot(paths, root)
		assert 2 == root.list().length
		assert root.list()[0].endsWith('a2.suf')
		assert root.list()[1].endsWith('b2.suf')
	}
	
	


	/*
	 * Simple heuristics to decide whether a path is a file or a directory.
	 * We have to guess it here, since we do not work on the backup files themselves.
	 * We rather use only the paths inside the backup directory to avoid the need to
	 * really restore the files at the point where we actually just want to know what
	 * is inside the backup.
	 */

	void testLastPathSegmentIsFileIfDotIsNotTheFirstChar() {
		String pathString = 'a0.suf'
		def paths = [pathString] as String[]
		service.createFileStructureUnderRoot(paths, root)
		File a1 = new File(root, pathString)
		assert a1.exists()
		assert a1.isFile()
	}

	void testLastPathSegmentIsFileIfDotIsNotTheFirstChar2() {
		String pathString = 'a0/a1/a2.suf'
		def paths = [pathString] as String[]
		service.createFileStructureUnderRoot(paths, root)
		File a1 = new File(root, pathString)
		assert a1.exists()
		assert a1.isFile()
	}

	void testDirectoriesMayContainDotsAtTheBeginning() {
		String pathString = '.a0'
		def paths = [pathString] as String[]
		service.createFileStructureUnderRoot(paths, root)
		File a1 = new File(root, pathString)
		assert a1.exists()
		assert a1.isDirectory()
	}

	void testDirectoriesMayContainDotsAtTheBeginning2() {
		String pathString = 'a0/a1/.a2'
		def paths = [pathString] as String[]
		service.createFileStructureUnderRoot(paths, root)
		File a1 = new File(root, pathString)
		assert a1.exists()
		assert a1.isDirectory()
	}

	void testSymbolicLinks() {
		fail()
	}
}
