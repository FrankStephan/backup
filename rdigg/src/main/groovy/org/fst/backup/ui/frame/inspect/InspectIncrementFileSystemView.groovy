package org.fst.backup.ui.frame.inspect

import javax.swing.filechooser.FileSystemView

class InspectIncrementFileSystemView extends FileSystemView {

	File root

	InspectIncrementFileSystemView(File root) {
		this.root = root.absoluteFile
	}

	@Override
	File[] getRoots() {
		return [root] as File[]
	}

	@Override
	File getDefaultDirectory() {
		return root
	}

	@Override
	File getHomeDirectory() {
		return root
	}

	@Override
	File getParentDirectory(File dir) {
		if (root == dir) {
			return null
		} else {
			return super.getParentDirectory(dir)
		}
	}

	@Override
	File createNewFolder(File containingDir) throws IOException {
		return null
	}
}
