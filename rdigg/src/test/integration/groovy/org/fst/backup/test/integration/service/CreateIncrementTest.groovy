package org.fst.backup.test.integration.service

import static org.fst.backup.test.integration.service.IntegrationTestStep.*
import static org.junit.Assert.*

import org.fst.backup.test.IncrementDestroyer

class CreateIncrementTest extends AbstractIntegrationTest {

	String cmdLineContent

	private void assertRDiffCreatesIncrement() {
		assert cmdLineContent.contains('Using rdiff-backup version 1.2.8')
	}

	private void assertRDiffIndicatesVerificationFail() {
		assert cmdLineContent.contains('Could not restore file a0.suf')
	}

	private void assertRDiffIndicatesVerificationSuccess() {
		assert cmdLineContent.contains('Every file verified successfully.')
	}

	private boolean rdiffProcesses(String fileName) {
		return cmdLineContent.contains('Processing changed file ' + fileName)
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

	void testBackupProcessesOnlyChanges() {
		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		assert rdiffProcesses('.')
		assert rdiffProcesses('a0.suf')
		assert rdiffProcesses('a0/a1')
		assert rdiffProcesses('a0/a1/a2.suf')
		assert rdiffProcesses('b0.suf')
		assert rdiffProcesses('c0.suf')

		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		assert !rdiffProcesses('')
	}

	void testBackupProcessesNewFilesIncludingPath() {
		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()

		ADD_ANOTHER_SOURCE_FILE.execute()
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		assert !rdiffProcesses('a0.suf')
		assert !rdiffProcesses('a0/a1')
		assert !rdiffProcesses('a0/a1/a2.suf')
		assert !rdiffProcesses('b0.suf')
		assert !rdiffProcesses('c0.suf')

		assert rdiffProcesses('.')
		assert rdiffProcesses('a0')
		assert rdiffProcesses('a0/b1')
		assert rdiffProcesses('a0/b1/b1.suf')
	}

	void testBackupProcessesChangedFilesIncludingPath() {
		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()

		new File(sourcePath, 'a0.suf') << 'I have changed'
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		assert !rdiffProcesses('a0/a1')
		assert !rdiffProcesses('a0/a1/a2.suf')
		assert !rdiffProcesses('b0.suf')
		assert !rdiffProcesses('c0.suf')

		assert rdiffProcesses('.')
		assert rdiffProcesses('a0.suf')
	}

	void testBackupProcessesMovedFilesIncludingPath() {
		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()

		new File(sourcePath + 'd0/').mkdir()
		assert new File(sourcePath, 'a0.suf').renameTo(new File(sourcePath + 'd0/', 'a0.suf'))
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		assert !rdiffProcesses('a0/a1')
		assert !rdiffProcesses('a0/a1/a2.suf')
		assert !rdiffProcesses('b0.suf')
		assert !rdiffProcesses('c0.suf')
		assert rdiffProcesses('.')
		assert rdiffProcesses('a0.suf')
		assert rdiffProcesses('d0/a0.suf')
	}

	void testBackupProcessesFileNameChangesIncludingPath() {
		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()

		assert new File(sourcePath, 'a0.suf').renameTo(new File(sourcePath, 'd0.suf'))
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		assert !rdiffProcesses('a0/a1')
		assert !rdiffProcesses('a0/a1/a2.suf')
		assert !rdiffProcesses('b0.suf')
		assert !rdiffProcesses('c0.suf')
		assert rdiffProcesses('.')
		assert rdiffProcesses('a0.suf')
		assert rdiffProcesses('d0.suf')
	}

	void testBackupProcessesDeletedFilesIncludingPath() {
		CREATE_SOME_SOURCE_FILES.execute()
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND.execute()

		assert new File(sourcePath, 'a0.suf').delete()
		CREATE_INCREMENT.execute(null, { cmdLineContent = it })
		assert !rdiffProcesses('a0/a1')
		assert !rdiffProcesses('a0/a1/a2.suf')
		assert !rdiffProcesses('b0.suf')
		assert !rdiffProcesses('c0.suf')
		assert rdiffProcesses('.')
		assert rdiffProcesses('a0.suf')
	}

	private removeFileContent(String sourcePath) {
		new File(sourcePath, 'a0.suf').text = ''
	}

	private ProxyMetaClass destroyIncrementBeforeVerification() {
		return IncrementDestroyer.destroyIncrementBeforeVerification(new File(targetPath, 'a0.suf'))
	}
}
