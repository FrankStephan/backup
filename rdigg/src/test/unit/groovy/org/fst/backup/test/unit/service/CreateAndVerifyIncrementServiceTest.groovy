package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import java.nio.file.Paths

import org.fst.backup.service.CreateAndVerifyIncrementService
import org.fst.backup.service.CreateIncrementService
import org.fst.backup.service.VerificationService
import org.fst.backup.test.AbstractTest

class CreateAndVerifyIncrementServiceTest extends AbstractTest {

	Appendable cmdOut = new MockFor(Appendable.class).proxyInstance()
	Appendable cmdErr = new MockFor(Appendable.class).proxyInstance()

	void testServicesAreInvoked() {
		prepareAndExecuteTest()
	}

	void testServicesAreInvokedWithCorrectParams() {
		prepareAndExecuteTest( {File sourceDir, File targetDir, Closure commandLineCallback ->
			assert this.sourceDir == sourceDir
			assert this.targetDir == targetDir
		}, { String targetPath, Appendable cmdOut, Appendable cmdErr ->
			assert Paths.get(this.targetPath) == Paths.get(targetPath)
			assert this.cmdOut == cmdOut
			assert this.cmdErr == cmdErr
		})
	}

	void testCmdLineCallbackIsAddedToAppenable() {
		MockFor outMock = new MockFor(Appendable.class)
		outMock.demand.append(1) { CharSequence cs -> }
		cmdOut = outMock.proxyInstance()

		prepareAndExecuteTest({ File sourceDir, File targetDir, Closure commandLineCallback ->
			commandLineCallback('out')
		})
	}

	void testLineSeparatorsAreAddedAfterEachCommandLineCallback() {
		fail()
	}

	private void prepareAndExecuteTest(Closure assertCreate = null, Closure assertVerify = null) {
		MockFor createIncrementService = new MockFor(CreateIncrementService.class)
		createIncrementService.demand.createIncrement(1) { File sourceDir, File targetDir, Closure commandLineCallback ->
			assertCreate?.call(sourceDir, targetDir, commandLineCallback)
		}
		MockFor verificationService = new MockFor(VerificationService.class)
		verificationService.demand.verifyMirror(1) { String targetPath, Appendable cmdOut, Appendable cmdErr ->
			assertVerify?.call(targetPath, cmdOut, cmdErr)
		}
		verificationService.use {
			createIncrementService.use {
				new CreateAndVerifyIncrementService().createAndVerify(sourceDir, targetDir, cmdOut, cmdErr)
			}
		}
	}
}
