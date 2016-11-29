package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor

import org.fst.backup.model.CommandLineCallback
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
	CommandLineCallback outputCallback = new MockFor(CommandLineCallback.class).proxyInstance()
	CommandLineCallback errorCallback = new MockFor(CommandLineCallback.class).proxyInstance()

	@Parameters
	public static Collection params() {
		def verifyIncrement = {String targetPath, def when, CommandLineCallback out, CommandLineCallback err ->
			new VerificationService().verifyIncrement(new Increment(targetPath: targetPath, secondsSinceTheEpoch: when), out, err)
		}

		def verifyMirror = {String targetPath, def when, CommandLineCallback out, CommandLineCallback err ->
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
	void testServiceReturnsAfterStreamsWereFlushed() {
		process.demand.waitForProcessOutput(1) { CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert this.outputCallback == outputCallback
			assert this.errorCallback == errorCallback
		}
		process.demand.exitValue(1) { return 0 }
		rdiffCommands.demand.verify(1) { File targetDir, def when ->
			return process.proxyInstance()
		}
		invokeService()
	}

	@Test
	void testRDiffIsInvokedWithCorrectParams() {
		process.demand.waitForProcessOutput(1) { CommandLineCallback outputCallback, CommandLineCallback errorCallback -> }
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
		process.demand.waitForProcessOutput(1) { CommandLineCallback outputCallback, CommandLineCallback errorCallback -> }
		process.demand.exitValue(1) { return 1 }
		rdiffCommands.demand.verify(1) { File targetDir, def when ->
			return process.proxyInstance()
		}
		assert false == invokeService()
	}

	@Test
	void testReturnTrueIfRDiffSucceeds() {
		process.demand.waitForProcessOutput(1) { CommandLineCallback outputCallback, CommandLineCallback errorCallback -> }
		process.demand.exitValue(1) { return 0 }
		rdiffCommands.demand.verify(1) { File targetDir, def when ->
			return process.proxyInstance()
		}
		assert true == invokeService()
	}

	private boolean invokeService() {
		boolean retVal
		rdiffCommands.use {
			retVal = invokeService(targetPath, when, outputCallback, errorCallback)
		}
		return retVal
	}

	private boolean invokeMirror() {
		boolean retVal
		createIncrement()
		rdiffCommands.use {
			retVal = new VerificationService().verifyMirror(targetPath, outputCallback, errorCallback)
		}
		return retVal
	}
}
