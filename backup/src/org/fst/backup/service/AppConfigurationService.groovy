package org.fst.backup.service

class AppConfigurationService {
	
	String getDirectoryPairsFilePath() {
		return System.getProperty('user.dir') + '\\directoryPairs.xml'
	}
}
