package org.fst.backup.test.unit

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.Application
import org.fst.backup.Main
import org.fst.backup.test.AbstractTest

class MainTest extends AbstractTest {

	void testApplicationInstanceIsCreatedWithArgs() {
		String[] args = ['-s', sourceDir, '-t', targetDir, '-l', 'logs/']
		MockFor application = new MockFor(Application.class, true)
		MockFor dummy =  new MockFor(Application.class)
		application.demand.with {
			Application() { String[] _args ->
				assert args == _args
				return dummy
			}
		}
		application.use { Main.main(args) }
	}
}
