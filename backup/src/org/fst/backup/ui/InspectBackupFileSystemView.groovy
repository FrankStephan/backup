package org.fst.backup.ui

import javax.swing.Icon
import javax.swing.filechooser.FileSystemView

class InspectBackupFileSystemView extends FileSystemView {

	File root

	InspectBackupFileSystemView(File root) {
		this.root = root
	}

	public Boolean isTraversable(File f) {
		return Boolean.valueOf(isFileSystemRoot(f) || f.isDirectory())
	}

	public boolean isDrive(File dir) {
		return isFileSystemRoot(dir)
	}

	@Override
	public File[] getRoots() {
		return [root] as File[]
	}

	@Override
	public boolean isFileSystemRoot(File dir) {
		return dir == root
	}

	@Override
	public File getDefaultDirectory() {
		return root
	}

	@Override
	public File getHomeDirectory() {
		return root
	}

	@Override
	public File createNewFolder(File containingDir) throws IOException {
		return null
	}
}
