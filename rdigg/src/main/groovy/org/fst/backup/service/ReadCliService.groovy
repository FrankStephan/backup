package org.fst.backup.service

import org.fst.backup.model.Configuration

class ReadCliService {

	Configuration read(String[] args) {
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

	private File createFile(String path) {
		File file = new File(path)
		if (file.exists() && file.isDirectory()) {
			if (file.isDirectory()) {
				return file
			} else {
				return null
			}
		}
	}
}
