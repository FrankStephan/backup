package org.fst.backup.test.integration.service

import static org.fst.backup.test.integration.service.IntegrationTestStep.*
import static org.junit.Assert.*

import java.nio.file.Files

import org.fst.backup.service.VerificationService

class FailingVerificationTest extends AbstractIntegrationTest {

	void testWithNonEmptyMissingFile() {
		CREATE_SOME_SOURCE_FILES.execute()

		String cmdLineContent
		destroyIncrementBeforeVerification().use {
			CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		}
		assertRDiffCreatesIncrement(cmdLineContent)
		assertRDiffIndicatesVerificationFail(cmdLineContent)
	}

	void testWithEmptyMissingFile() {
		CREATE_SOME_SOURCE_FILES.execute()
		removeFileContent(sourcePath)

		String cmdLineContent
		destroyIncrementBeforeVerification().use {
			CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		}
		assertRDiffCreatesIncrement(cmdLineContent)
		assertRDiffIndicatesVerificationSuccess(cmdLineContent)
		assertRDiffIndicatesVerificationFail(cmdLineContent)
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

	private removeFileContent(String sourcePath) {
		new File(sourcePath, 'a0.suf').text = ''
	}

	private assertRDiffCreatesIncrement(String cmdLineContent) {
		assert cmdLineContent.contains('Using rdiff-backup version 1.2.8')
	}

	private assertRDiffIndicatesVerificationFail(String cmdLineContent) {
		assert cmdLineContent.contains('Could not restore file a0.suf')
	}

	private assertRDiffIndicatesVerificationSuccess(String cmdLineContent) {
		assert cmdLineContent.contains('Every file verified successfully.')
	}
}
