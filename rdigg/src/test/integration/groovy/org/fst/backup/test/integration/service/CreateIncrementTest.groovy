package org.fst.backup.test.integration.service

import static org.fst.backup.test.integration.service.IntegrationTestStep.*
import static org.junit.Assert.*

import java.nio.file.Files

import org.fst.backup.service.VerificationService

class CreateIncrementTest extends AbstractIntegrationTest {

	String cmdLineContent

	private assertRDiffCreatesIncrement() {
		assert cmdLineContent.contains('Using rdiff-backup version 1.2.8')
	}

	private assertRDiffIndicatesVerificationFail() {
		assert cmdLineContent.contains('Could not restore file a0.suf')
	}

	private assertRDiffIndicatesVerificationSuccess() {
		assert cmdLineContent.contains('Every file verified successfully.')
	}

	void testSuccessfulBackup() {
		CREATE_SOME_SOURCE_FILES.execute()

		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		assertRDiffCreatesIncrement()
		assertRDiffIndicatesVerificationSuccess()
	}

	void testWithNonEmptyMissingFile() {
		CREATE_SOME_SOURCE_FILES.execute()

		destroyIncrementBeforeVerification().use {
			CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		}
		assertRDiffCreatesIncrement()
		assertRDiffIndicatesVerificationFail()
	}

	void testWithEmptyMissingFile() {
		CREATE_SOME_SOURCE_FILES.execute()
		removeFileContent(sourcePath)

		destroyIncrementBeforeVerification().use {
			CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		}
		assertRDiffCreatesIncrement()
		assertRDiffIndicatesVerificationSuccess()
		assertRDiffIndicatesVerificationFail()
	}

	private removeFileContent(String sourcePath) {
		new File(sourcePath, 'a0.suf').text = ''
	}

	private ProxyMetaClass destroyIncrementBeforeVerification() {
		ProxyMetaClass verificationServiceProxy = ProxyMetaClass.getInstance(VerificationService.class)
		verificationServiceProxy.interceptor = new Interceptor() {

					@Override
					public Object beforeInvoke(Object object, String methodName,
							Object[] arguments) {

						if (methodName == 'verifyMirror') {
							File fileToDelete = new File(targetPath, 'a0.suf')
							assert fileToDelete.exists()
							Files.delete(fileToDelete.toPath())
							assert !fileToDelete.exists()
						}
						return null
					}

					@Override
					public Object afterInvoke(Object object, String methodName,
							Object[] arguments, Object result) {
						return result
					}

					@Override
					public boolean doInvoke() {
						return true
					}
				}
		return verificationServiceProxy
	}
}
