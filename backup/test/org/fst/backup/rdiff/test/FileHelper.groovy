package org.fst.backup.rdiff.test

class FileHelper {
	
	File createFile(String dirName, String fileName) {
		File dir = new File(dirName);
		dir.mkdirs();
		return new File(dir, fileName)
	}

}
