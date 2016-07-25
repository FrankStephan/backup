package org.fst.backup.ui

import java.nio.file.Paths

import javax.swing.filechooser.FileSystemView

import org.fst.backup.rdiff.test.AbstractFileSystemTest


class TestFileSystemView extends AbstractFileSystemTest {



	void test() {

		tmpPath

		String pathsString = new File('test/org/fst/backup/ui/', 'example-paths.txt').text
		int i=0
		pathsString.eachLine {
			println ((i++) + ': ' + it)

			String[] pathSegments = it.split('/')
		}

		def lines = pathsString.readLines()

		File root = new File(tmpPath)
		println root.absolutePath

		File f1 = Paths.get(root.absolutePath, lines[1]).toFile()
		File f2 = Paths.get(root.absolutePath, lines[2]).toFile()
		File f3 = Paths.get(root.absolutePath, lines[3]).toFile()
		f1.mkdirs()
		f2.mkdirs()
		f3.mkdirs()
		println f1.directory
		println f2.directory
		println f3.directory
		println f1
		println f2
		println f3
		println f1.list()



		FileTreeBuilder fileTreeBuilder = new FileTreeBuilder()

		createTree(lines)
	}

	void testPathsToTree() {
		String pathsString = new File('test/org/fst/backup/ui/', 'example-paths.txt').text
	}


	private void createTree(List paths) {


		ArrayList<? extends List<String>> matrix = new ArrayList<? extends List<String>>(paths.size())
		int maxSegments = 0
		for (String path in paths) {
			String[] pathSegments = path.split('/')
			matrix.add(Arrays.asList(pathSegments))
			maxSegments = Math.max(maxSegments, pathSegments.length)
		}
		http://stackoverflow.com/questions/22986506/groovy-2d-arrays
		println matrix

		// Wenn ein Knoten nur genau einmal existiert ist es eine Datei
		// Kommt er mehrmals vor, ist es ein Verzeichnis
	}





	class SimulatedFileSystemView extends FileSystemView {




		@Override
		public File createNewFolder(File containingDir) throws IOException {
			// TODO Auto-generated method stub
			return null
		}
	}
}
