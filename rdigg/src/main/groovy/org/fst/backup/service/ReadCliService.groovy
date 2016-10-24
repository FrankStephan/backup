package org.fst.backup.service

import org.fst.backup.model.Configuration
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException



class ReadCliService {

	Configuration read(String[] args) throws DirectoryNotExistsException, FileIsNotADirectoryException {
		CliBuilder cli = new CliBuilder()
		cli.s('Default source directory', args: 1, argName: 'source directory')
		cli.t('Default target directory', args: 1, argName: 'target directory')

		def options = cli.parse(args)
		String sourcePath = options.s
		String targetPath = options.t

		Configuration configuration = new Configuration()
		configuration.defaultSourceDir = createFile(sourcePath)
		configuration.defaultTargetDir = createFile(targetPath)
		return configuration
	}

	File createFile(String path) {
		File file = new File(path)
		if (file.exists()) {
			if (file.isDirectory()) {
				return file
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
}
