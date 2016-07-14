package org.fst.backup.rdiff.test

import groovy.util.GroovyTestCase

abstract class AbstractRDiffTest extends GroovyTestCase {
	
	String tmpPath
	String sourePath
	String targetPath
	
	void setUp() {
		super.setUp()
		tmpPath = getClass().getSimpleName() + '-tmp/'
		sourePath = tmpPath + 'source/'
		targetPath = tmpPath + 'target/'
		new File(tmpPath).mkdir()
		new File(sourePath).mkdir()
		new File(targetPath).mkdir()
	}
	
	void tearDown() {
		super.tearDown()
		new File(tmpPath).deleteDir()
	}

}
