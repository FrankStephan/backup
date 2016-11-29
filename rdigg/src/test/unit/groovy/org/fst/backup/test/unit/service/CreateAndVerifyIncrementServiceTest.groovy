package org.fst.backup.test.unit.service

import static org.junit.Assert.*
import groovy.mock.interceptor.MockFor

import java.nio.file.Paths

import org.fst.backup.model.CommandLineCallback
import org.fst.backup.service.CreateAndVerifyIncrementService
import org.fst.backup.service.CreateIncrementService
import org.fst.backup.service.VerificationService
import org.fst.backup.test.AbstractTest

class CreateAndVerifyIncrementServiceTest extends AbstractTest {

	CommandLineCallback outputCallback = new MockFor(CommandLineCallback.class).proxyInstance()
	CommandLineCallback errorCallback = new MockFor(CommandLineCallback.class).proxyInstance()

	void testServicesAreInvoked() {
		prepareAndExecuteTest()
	}

	void testServicesAreInvokedWithCorrectParams() {
		prepareAndExecuteTest( {File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert this.sourceDir == sourceDir
			assert this.targetDir == targetDir
			assert this.outputCallback == outputCallback
			assert this.errorCallback == errorCallback
		}, { String targetPath, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assert Paths.get(this.targetPath) == Paths.get(targetPath)
			assert this.outputCallback == outputCallback
			assert this.errorCallback == errorCallback
		})
	}
	
	private void prepareAndExecuteTest(Closure assertCreate = null, Closure assertVerify = null) {
		MockFor createIncrementService = new MockFor(CreateIncrementService.class)
		createIncrementService.demand.createIncrement(1) { File sourceDir, File targetDir, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assertCreate?.call(sourceDir, targetDir, outputCallback, errorCallback)
		}
		MockFor verificationService = new MockFor(VerificationService.class)
		verificationService.demand.verifyMirror(1) { String targetPath, CommandLineCallback outputCallback, CommandLineCallback errorCallback ->
			assertVerify?.call(targetPath, outputCallback, errorCallback)
		}
		verificationService.use {
			createIncrementService.use {
				new CreateAndVerifyIncrementService().createAndVerify(sourceDir, targetDir, outputCallback, errorCallback)
			}
		}
	}
}
