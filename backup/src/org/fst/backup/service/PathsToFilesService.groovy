package org.fst.backup.service



class PathsToFilesService {

	int rdiffPathSeparator

	PathsToFilesService(char rdiffPathSeparator) {
		this.rdiffPathSeparator = rdiffPathSeparator as char
	}

	void createFileStructureUnderRoot(String[] paths, File rootDir) {

		List<? extends List<String>> pathsSegements = new ArrayList<? extends List<String>>()
		paths.each { it -> pathsSegements.add(it.split('/') as List) }



		for (path in paths) {
			File f = new File (rootDir, path)
			if (isFile(path)) {
				f.parentFile.mkdirs()
				f.createNewFile()
			} else {
				f.mkdirs()
			}
		}
		//		Files.probeContentType(null)
	}

	private void group(List<? extends List<String>> pathsSegements, File parent) {
		Map<String, ? extends List<String>> pathSegmentsGroupedByFirsSegment = pathsSegements.groupBy({ it.getAt(0) })
		
		pathSegmentsGroupedByFirsSegment.each { 
			File file = new File(parent, it.value.getAt(0))
			// file.mkdir or file.create ??
			List<String> subPathSegments = it.value.subList(1, it.value.size()) 
			group(subPathSegments, file)
			// or break if null or only one value
		}
	}

	//	private boolean isFile(String path) {
	//
	//		String[] segments = path.split('/')
	//		String lastSegement = segments[segments.length - 1]
	//
	//		int numberOfDots = lastSegement.count('.')
	//		switch(numberOfDots) {
	//			case 0:
	//			return false
	//			case 1: lastSegement.indexOf('.') >
	//			return
	//			default:
	//			break
	//		}
	//	}

	private boolean isLevel0(int lastPathSeparatorIndex) {
		return lastPathSeparatorIndex == -1
	}

	private containsDots(String lastSegment) {
	}

	private boolean startsWithDot(int lastPathSeparatorIndex, int lastDotIndex) {
		return !(lastPathSeparatorIndex > -1 && lastDotIndex == lastPathSeparatorIndex + 1)
	}



	private String[] extractSegements(String path) {
		return path.split(rdiffPathSeparator)
	}
}
