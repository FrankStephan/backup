package org.fst.backup.service

import org.fst.backup.model.Increment
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException

class IncrementFileStructureService {

	ListPathsFromIncrementService listPathsFromIncrementService = new ListPathsFromIncrementService()
	PathsToFilesService pathsToFilesService = new PathsToFilesService()

	void createIncrementFileStructure(Increment increment, File root) {

		if (root.exists()) {
			if (root.isDirectory()) {
				def pathsList = listPathsFromIncrementService.listPathsFromIncrement(increment)
				pathsToFilesService.createFileStructureFromPaths(pathsList, root)
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
}
