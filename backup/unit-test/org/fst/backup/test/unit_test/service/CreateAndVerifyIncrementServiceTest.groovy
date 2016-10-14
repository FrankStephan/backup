package org.fst.backup.test.unit_test.service

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
		MockFor createIncrementService = new MockFor(CreateIncrementService.class)
		createIncrementService.demand.createIncrement(1) {File sourceDir, File targetDir, Closure commandLineCallback ->}
		MockFor verificationService = new MockFor(VerificationService.class)
		verificationService.demand.verifyMirror(1) {String targetPath, Appendable cmdOut, Appendable cmdErr -> }
		verificationService.use {
			createIncrementService.use {
				new CreateAndVerifyIncrementService().createAndVerify(sourceDir, targetDir, cmdOut, cmdErr)
			}
		}
	}

	void testServicesAreInvokedWithCorrectParams() {
		MockFor createIncrementService = new MockFor(CreateIncrementService.class)
		createIncrementService.demand.createIncrement(1) {File sourceDir, File targetDir, Closure commandLineCallback ->
			assert this.sourceDir == sourceDir
			assert this.targetDir == targetDir
		}
		MockFor verificationService = new MockFor(VerificationService.class)
		verificationService.demand.verifyMirror(1) {String targetPath, Appendable cmdOut, Appendable cmdErr ->
			assert Paths.get(this.targetPath) == Paths.get(targetPath)
			assert this.cmdOut == cmdOut
			assert this.cmdErr == cmdErr
		}
		verificationService.use {
			createIncrementService.use {
				new CreateAndVerifyIncrementService().createAndVerify(sourceDir, targetDir, cmdOut, cmdErr)
			}
		}
	}

	void testCmdLineOutputsGetConcatenated() {
		String out = 'out'
		MockFor outMock = new MockFor(Appendable.class)
		outMock.demand.append(1) {CharSequence it ->
			assert out == it
		}
		cmdOut = outMock.proxyInstance()

		MockFor createIncrementService = new MockFor(CreateIncrementService.class)
		createIncrementService.demand.createIncrement(1) {File sourceDir, File targetDir, Closure commandLineCallback ->
			commandLineCallback(out)
		}
		MockFor verificationService = new MockFor(VerificationService.class)
		verificationService.demand.verifyMirror(1) {String targetPath, Appendable cmdOut, Appendable cmdErr ->
			assert this.cmdOut == cmdOut
		}
		verificationService.use {
			createIncrementService.use {
				new CreateAndVerifyIncrementService().createAndVerify(sourceDir, targetDir, cmdOut, cmdErr)
			}
		}
	}
}
