package org.fst.backup.test.unit.service

import org.fst.backup.service.StringBufferCallback

class StringBufferCallbackTest extends GroovyTestCase {

	void testCommandlineDataIsAppended() {
		def sbc = new StringBufferCallback()
		sbc.callback('1')
		sbc.callback('2')
		sbc.callback('3')
		assert '123' == sbc.toString()
	}
}
