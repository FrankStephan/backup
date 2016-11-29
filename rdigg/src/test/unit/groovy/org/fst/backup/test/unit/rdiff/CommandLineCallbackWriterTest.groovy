package org.fst.backup.test.unit.rdiff;

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.rdiff.CommandLineCallbackWriter

class CommandLineCallbackWriterTest extends GroovyTestCase {

	void testCallbackWithPortionOfTheArray() {
		MockFor commandLineCallback = new MockFor(CommandLineCallback.class)
		commandLineCallback.demand.callback(1) {String it ->
			assert '23' == it
		}
		new CommandLineCallbackWriter(commandLineCallback.proxyInstance()).write('01234'.toCharArray(), 2, 2)
	}

}
