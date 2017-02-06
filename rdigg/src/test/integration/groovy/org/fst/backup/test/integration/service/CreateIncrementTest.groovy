package org.fst.backup.test.integration.service

import static org.fst.backup.test.integration.service.IntegrationTestStep.*
import static org.junit.Assert.*

import java.nio.file.Files

import org.fst.backup.service.VerificationService
import org.fst.backup.test.IncrementDestroyer;

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
	
	void testBackupOnlyConsidersChangedFiles() {
		fail()
	}

	private removeFileContent(String sourcePath) {
		new File(sourcePath, 'a0.suf').text = ''
	}

	private ProxyMetaClass destroyIncrementBeforeVerification() {
		return IncrementDestroyer.destroyIncrementBeforeVerification(new File(targetPath, 'a0.suf'))
	}
}
