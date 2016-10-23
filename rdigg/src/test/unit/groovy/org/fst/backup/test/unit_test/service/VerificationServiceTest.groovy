package org.fst.backup.test.unit_test.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor

import org.fst.backup.model.Increment
import org.fst.backup.rdiff.RDiffCommands
import org.fst.backup.service.VerificationService
import org.fst.backup.service.exception.DirectoryNotExistsException
import org.fst.backup.service.exception.FileIsNotADirectoryException
import org.fst.backup.test.AbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized.class)
class VerificationServiceTest extends AbstractTest {

	MockFor rdiffCommands = new MockFor(RDiffCommands.class)
	StubFor process = new StubFor(Process.class)
	Appendable cmdOut = new MockFor(Appendable.class).proxyInstance()
	Appendable cmdErr = new MockFor(Appendable.class).proxyInstance()

	@Parameters
	public static Collection params() {
		def verifyIncrement = {String targetPath, def when, Appendable out, Appendable err ->
			new VerificationService().verifyIncrement(new Increment(targetPath: targetPath, secondsSinceTheEpoch: when), out, err)
		}

		def verifyMirror = {String targetPath, def when, Appendable out, Appendable err ->
			new VerificationService().verifyMirror(targetPath, out, err)
		}

		return Arrays.asList([verifyIncrement, 1467750198].toArray(), [verifyMirror, 'now'].toArray())
	}

	Closure invokeService
	def when

	public VerificationServiceTest(Closure invokeService, def when) {
		this.invokeService = invokeService
		this.when = when
	}

	@Override
	@Before
	public void setUp() {
		super.setUp()
	}

	@Override
	@After
	public void tearDown() {
		super.tearDown()
	}

	@Test
	void testNotExisitingTargetDirIsDenied() {
		targetDir = new File(tmpPath + 'NE2/')
		targetPath = targetDir.absolutePath
		shouldFail (DirectoryNotExistsException) { invokeService() }
	}

	@Test
	void testNonDirectoryTargetFileIsDenied() {
		targetDir = new File (tmpPath, 'File1.txt') << 'Content'
		targetPath = targetDir.absolutePath
		shouldFail (FileIsNotADirectoryException) { invokeService() }
	}

	@Test
	void testServiceReturnsAfterOutIsWrittenToAppenables() {
		process.demand.waitForProcessOutput(1) { Appendable cmdOut, Appendable cmdErr ->
			assert this.cmdOut == cmdOut
		}
		process.demand.exitValue(1) { return 0 }
		rdiffCommands.demand.verify(1) { File targetDir, def when ->
			return process.proxyInstance()
		}
		invokeService()
	}

	@Test
	void testServiceReturnsAfterErrIsWrittenToAppenables() {
		process.demand.waitForProcessOutput(1) { Appendable cmdOut, Appendable cmdErr ->
			assert this.cmdErr == cmdErr
		}
		process.demand.exitValue(1) { return 1 }
		rdiffCommands.demand.verify(1) { File targetDir, def when ->
			return process.proxyInstance()
		}
		invokeService()
	}

	@Test
	void testRDiffIsInvokedWithCorrectParams() {
		process.demand.waitForProcessOutput(1) { Appendable cmdOut, Appendable cmdErr -> }
		process.demand.exitValue(1) { return 0 }
		rdiffCommands.demand.verify(1) { File targetDir, def when ->
			assert new File(targetPath) == targetDir
			assert this.when == when
			return process.proxyInstance()
		}
		invokeService()
	}

	@Test
	void testReturnFalseIfRDiffFails() {
		process.demand.waitForProcessOutput(1) { Appendable cmdOut, Appendable cmdErr -> }
		process.demand.exitValue(1) { return 1 }
		rdiffCommands.demand.verify(1) { File targetDir, def when ->
			return process.proxyInstance()
		}
		assert false == invokeService()
	}

	@Test
	void testReturnTrueIfRDiffSucceeds() {
		process.demand.waitForProcessOutput(1) { Appendable cmdOut, Appendable cmdErr -> }
		process.demand.exitValue(1) { return 0 }
		rdiffCommands.demand.verify(1) { File targetDir, def when ->
			return process.proxyInstance()
		}
		assert true == invokeService()
	}

	private boolean invokeService() {
		boolean retVal
		rdiffCommands.use {
			retVal = invokeService(targetPath, when, cmdOut, cmdErr)
		}
		return retVal
	}

	private boolean invokeMirror() {
		boolean retVal
		createIncrement()
		rdiffCommands.use {
			retVal = new VerificationService().verifyMirror(targetPath, cmdOut, cmdErr)
		}
		return retVal
	}
}
