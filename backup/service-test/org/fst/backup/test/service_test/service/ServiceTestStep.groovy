
package org.fst.backup.test.service_test.service

import org.fst.backup.service.CreateBackupService
import org.fst.backup.service.IncrementFileStructureService
import org.fst.backup.service.ListIncrementsService

enum ServiceTestStep {

	CREATE_SOME_SOURCE_FILES {
		@Override
		void execute(def params, Closure setResult) {
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
		void execute(def params, Closure setResult) {
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
		void execute(def params, Closure setResult) {
			StringBuilder sb = new StringBuilder()
			new CreateBackupService().createBackup(sourceDir, targetDir, {
				sb.append(it).append(System.lineSeparator())
			})
			setResult?.call(sb.toString().trim())
		}
	},

	WAIT_BECAUSE_RDIFF_CAN_DO_ONLY_ONE_BACKUP_PER_SECOND {
		@Override
		void execute(def params, Closure setResult) {
			Thread.sleep(1020L)
		}
	},

	LIST_INCREMENTS {
		@Override
		void execute(def params, Closure setResult) {
			def listIncrements = new ListIncrementsService().listIncrements(targetDir)
			setResult(listIncrements)
		}
	},

	GET_INCREMENT_FILE_STRUCTURE {

		@Override
		void execute(def params, Closure setResult) {
			IncrementFileStructureService incrementFileStructureService = new IncrementFileStructureService()
			incrementFileStructureService.createIncrementFileStructure(params[0], params[1])
		}
	}


	abstract void execute(def params = null, Closure setResult = null)

	public void verify(Object params = null, Closure verifier) {
		def result
		Closure setResult = {it -> result = it}
		def executionResult = execute(params, setResult)
		verifier(result)
	}

	static File sourceDir
	static File targetDir

	static void init(File _sourceDir, File _targetDir) {
		sourceDir = _sourceDir
		targetDir = _targetDir
	}
}
