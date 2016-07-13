package org.fst.backup.service

import groovy.xml.MarkupBuilder

class FolderPairService {
	
	void store(File sourceDir, File targetDir) {
		def xmlWriter = new StringWriter()
		def xmlMarkup = new MarkupBuilder(xmlWriter)
//		http://groovy-lang.org/processing-xml.html
	}

}
