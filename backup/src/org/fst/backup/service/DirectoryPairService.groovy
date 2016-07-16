package org.fst.backup.service

import groovy.xml.MarkupBuilder

class DirectoryPairService {
	
	final File directoryPairFile
	
	public DirectoryPairService(directoryPairFile) {
		this.directoryPairFile = directoryPairFile
	}
	
	void addDirectoryPair(File sourceDir, File targetDir) {
		if (sourceDir.exists() && targetDir.exists()) {
			if (sourceDir.directory && targetDir.directory) {
				if (directoryPairFile.exists()) {
					def xmlSlurper = new XmlSlurper()
					def directoryPairs = xmlSlurper.parse(directoryPairFile)
					
					directoryPairs.plus(directoryPairs)
				}
				
				prepareDirectoryPairFile()
				addToXml(sourceDir, targetDir)
				
				println xmlWriter.toString()
			} else {
				throw new FileIsNotADirectoryException()
			}
		} else {
			throw new DirectoryNotExistsException()
		}
	}
	
	List<DirectoryPair> loadAllDirectoryPairs() {
		prepareDirectoryPairFile()
		return readXml()
	}
	
	private void addToXml(File sourceDir, File targetDir) {
		XmlParser xmlParser = new XmlParser()
		def xmlDirectoryPairs = xmlParser.parse(directoryPairFile)
		xmlDirectoryPairs.appendNode(this)
		
	}
	
	private Node directoryPairs() {
		
	}
	
	
	
	private void prepareDirectoryPairFile() {
		if (directoryPairFile.createNewFile()) {
			println directoryPairFile.text
			def xmlWriter = new FileWriter(directoryPairFile)
			def xmlMarkup = new MarkupBuilder(xmlWriter)
			xmlMarkup.directoryPairs() {}
			xmlWriter.close()
		}
		
		
	}
	
	private void createXml() {
		def xmlWriter = new FileWriter(directoryPairFile)
		def xmlMarkup = new MarkupBuilder(xmlWriter)
		xmlMarkup.directoryPairs() {
			directoryPair() {
				source(sourceDir.getAbsolutePath())
				target(targetDir.getAbsolutePath())
			}
		}
	}
	
	private List<DirectoryPair> readXml() {
		def xmlParser = new XmlParser()
		println new XmlSlurper().parse(directoryPairFile)
		def xmlDirectoryPairs = xmlParser.parse(directoryPairFile)
		def directoryPairs = []
		xmlDirectoryPairs.children().each {Node it -> directoryPairs.add(new DirectoryPair(sourceDir: it.sourceDir.text(), targetDir: it.targetDir.text())) }
		return directoryPairs
	}

}
