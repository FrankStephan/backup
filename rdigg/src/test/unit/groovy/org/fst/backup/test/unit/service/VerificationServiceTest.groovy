package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.model.Increment
import org.fst.backup.model.ProcessStatus
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
	CommandLineCallback outputCallback = new MockFor(CommandLineCallback.class).proxyInstance()
	CommandLineCallback errorCallback = new MockFor(CommandLineCallback.class).proxyInstance()

	@Parameters
	public static Collection params() {
		def verifyIncrement = {String targetPath, def when, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			new VerificationService().verifyIncrement(new Increment(targetPath: targetPath, secondsSinceTheEpoch: when), outputCallback, errorCallback)
		}

		def verifyMirror = {String targetPath, def when, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			new VerificationService().verifyMirror(targetPath, outputCallback, errorCallback)
		}

		return Arrays.asList([verifyIncrement, 1467750198].toArray(), [verifyMirror, 'now'].toArray())
	}

	Closure<Boolean> invoke
	def when

	public VerificationServiceTest(Closure<Boolean> invokeService, def when) {
		this.invoke = invokeService
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
	void testServiceIsInvokedWithCorrectParams() {
		rdiffCommands.demand.verify(1) { File targetDir, def when, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert new File(targetPath) == targetDir
			assert this.when == when
			assert this.outputCallback == outputCallback
			assert this.errorCallback == errorCallback
			return ProcessStatus.SUCCESS
		}
		invokeService()
	}

	@Test
	void testReturnFalseIfRDiffFails() {
		rdiffCommands.demand.verify(1) { File targetDir, def when, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			return ProcessStatus.FAILURE
		}
		assert false == invokeService()
	}

	@Test
	void testReturnTrueIfRDiffSucceeds() {
		rdiffCommands.demand.verify(1) { File targetDir, def when, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			return ProcessStatus.SUCCESS
		}
		assert true == invokeService()
	}

	private boolean invokeService() {
		boolean retVal
		rdiffCommands.use {
			retVal = invoke(targetPath, when, outputCallback, errorCallback)
		}
		return retVal
	}
}
