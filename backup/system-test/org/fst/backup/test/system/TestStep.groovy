
package org.fst.backup.test.system

import org.fst.backup.service.CreateBackupService
import org.fst.backup.service.IncrementFileStructureService
import org.fst.backup.service.ListIncrementsService

enum TestStep {

	CREATE_SOME_SOURCE_FILES {
		@Override
		Object execute(Object params) {
			FileTreeBuilder ftb = new FileTreeBuilder(sourceDir)
			ftb {
				'a0.suf'('')
				a0 {
					a1 { 'a2.suf'('') }
				}
				'b0.suf'('')
				'c0.suf'('')
			}
		}
	},

	ADD_ANOTHER_SOURCE_FILE {
		@Override
		Object execute(Object params) {
			FileTreeBuilder ftb = new FileTreeBuilder(sourceDir)
			ftb {
				a0 {
					b1 { 'b2.suf'('') }
				}
			}
		}
	},

	DO_BACKUP {
		@Override
		Object execute(Object params) {
			StringBuilder sb = new StringBuilder()
			new CreateBackupService().createBackup(sourceDir, targetDir, {
				sb.append(it).append(System.lineSeparator())
			})
			return sb.toString().trim()
		}
	},

	WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND {
		@Override
		Object execute(Object params) {
			Thread.sleep(1020L)
		}
	},

	LIST_INCREMENTS {
		@Override
		Object execute(Object params) {
			return new ListIncrementsService().listIncrements(targetDir)
		}
	},

	GET_INCREMENT_FILE_STRUCTURE {

		@Override
		Object execute(Object params) {
			IncrementFileStructureService incrementFileStructureService = new IncrementFileStructureService()
			incrementFileStructureService.createIncrementFileStructure(params[0], params[1])
		}
	}


	abstract execute(Object params = null)

	public void verify(Object params = null, Closure verifier) {
		verifier(execute(params))
	}

	static File sourceDir
	static File targetDir

	static void init(File _sourceDir, File _targetDir) {
		sourceDir = _sourceDir
		targetDir = _targetDir
	}
}
