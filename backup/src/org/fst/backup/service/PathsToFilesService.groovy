package org.fst.backup.service

import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException



class PathsToFilesService {

	void createFileStructureFromPaths(List<String> pathsList, File rootDir) {
		if (rootDir.exists()) {
			if (rootDir.isDirectory()) {
				List<? extends List<String>> paths = new ArrayList<? extends List<String>>()
				pathsList.each { it ->
					paths.add(it.split('/') as List)
				}

				createFileStructureRecursively(paths, rootDir)
				//		Files.probeContentType(null)
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}

	private void createFileStructureRecursively(List<? extends List<String>> paths, File parent) {

		List<? extends List<String>> pathsNotYetCreated = removeCompletelyCreatedPaths(paths)

		if (containsSubpaths(pathsNotYetCreated)) {
			createDir(parent)

			Map<String, List<? extends List<String>>> pathsGroupedByFirstSegment = groupByFirstSegment(pathsNotYetCreated)

			pathsGroupedByFirstSegment.each {
				String firstSegment = it.key
				File firstSegmentFile = new File(parent, firstSegment)
				List<? extends List<String>> pathsInGroup = it.value
				List<? extends List<String>> subPathsInGroup = removeFirstSegmentFromEachPath(pathsInGroup)
				createFileStructureRecursively(subPathsInGroup, firstSegmentFile)
			}
		} else {
			createFileOrDir(parent)
		}
	}

	private boolean containsSubpaths(List<? extends List<String>> pathsNotYetCreated) {
		return !pathsNotYetCreated.isEmpty()
	}

	private void createDir(File parent) {
		parent.mkdir()
	}

	private Map<String, List<? extends List<String>>> groupByFirstSegment(List<? extends List<String>> paths) {
		return paths.groupBy({ firstSegment(it) })
	}

	private String firstSegment(List<String> pathSegments) {
		return pathSegments.get(0)
	}

	private List<? extends List<String>> removeFirstSegmentFromEachPath(List<? extends List<String>> pathsGroup) {
		pathsGroup*.remove(0)
		return pathsGroup
	}

	private List<? extends List<String>> removeCompletelyCreatedPaths(List<? extends List<String>> paths) {
		return paths.findAll { !it.isEmpty() }
	}

	private createFileOrDir(File parent) {
		parent.createNewFile()
	}
}
