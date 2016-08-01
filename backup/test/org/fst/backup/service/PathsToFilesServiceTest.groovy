package org.fst.backup.service

import static org.junit.Assert.*

import java.nio.file.Files
import java.nio.file.Path
import java.security.AccessController
import java.security.PrivilegedAction

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

	private void createFileStructureUnderRoot(List<String> paths, root) {
		service.createFileStructureUnderRoot(paths as String[], root)
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

	//	void testFileTypeIsConsidered() {
	//		def paths = ['.a0.cpr']
	//		createFileStructureUnderRoot(paths, root)
	//		def a0 = new File(root, '.a0.cpr')
	//		assert a0.exists()
	//		println Files.probeContentType(a0.toPath())
	//		FileTypeDetectors d
	//		ServiceLoader<FileTypeDetector> loader = ServiceLoader.loadInstalled(FileTypeDetector.class)
	//		loader.each { println it }
	//	}


	void testSymbolicLinks() {
		Path target = root.toPath().resolve('targetpath/target.file')
		Path link = root.toPath().resolve('links/link_to_target')

		AccessController.doPrivileged (new PrivilegedAction<Object>() {
					public Object run() {
						Files.createSymbolicLink(link, target)
					}
				}
				)
		createFileStructureUnderRoot(['links/link_to_target'], root)
		assertPathsCreated(['links/link_to_target'])

		//		AccessController.doPrivileged(new PrivilegedAction() {
		//
		//		}


		// https://docs.oracle.com/javase/tutorial/security/tour2/step4.html
		// http://docs.oracle.com/javase/7/docs/technotes/guides/security/PolicyFiles.html
		// Test this for rdiff-backup too
	}
}
