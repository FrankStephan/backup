package org.fst.backup.test.system

import static org.junit.Assert.*

import org.fst.backup.service.CreateBackupService
import org.fst.backup.service.ListIncrementsService
import org.fst.backup.test.unit.AbstractFilesUsingTest

class CreateBackupTest extends AbstractFilesUsingTest {

	CreateBackupService createBackupService = new CreateBackupService()
	ListIncrementsService listIcrementsService = new ListIncrementsService()

	public void test() {
		FileTreeBuilder ftb = new FileTreeBuilder(sourceDir)
		createSomeFilesInSourceFolder(ftb)
		doBackup()

		addAnotherFileInSourceFolder(ftb)
		doBackup()

		listIcrementsService.listIncrements(targetDir)
	}

	private void createSomeFilesInSourceFolder(FileTreeBuilder ftb) {
		ftb {
			'a0.suf'('')
			a0 {
				a1 { 'a2.suf'('') }
			}
			'b0.suf'('')
			'c0.suf'('')
		}
	}

	private void addAnotherFileInSourceFolder(FileTreeBuilder ftb) {
		ftb {
			a0 {
				b1 { 'b2.suf'('') }
			}
		}
	}

	private void doBackup() {
		createBackupService.createBackup(sourceDir, targetDir)
	}
}
