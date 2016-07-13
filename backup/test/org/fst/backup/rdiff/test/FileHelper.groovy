package org.fst.backup.rdiff.test

class FileHelper {
	
	File createFile(String dirName, String fileName) {
		File dir = new File(dirName);
		dir.mkdirs();
		File file = new File(dir, fileName)
		file.createNewFile()
		file.write('Content')
		return file
	}

}
