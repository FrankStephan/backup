
package org.fst.backup.test.integration.service


import org.fst.backup.model.Increment
import org.fst.backup.service.CreateAndVerifyIncrementService
import org.fst.backup.service.IncrementFileStructureService
import org.fst.backup.service.ListIncrementsService
import org.fst.backup.service.RestoreIncrementService

enum IntegrationTestStep {

	CREATE_SOME_SOURCE_FILES {
		@Override
		void execute(def params, Closure setResult) {
			FileTreeBuilder ftb = new FileTreeBuilder(sourceDir)
			ftb {
				'a0.suf'('I am a0')
				a0 {
					a1 { 'a2.suf'('I am a2') }
				}
				'b0.suf'('I am b0')
				'c0.suf'('I am c0')
			}
		}
	},

	ADD_ANOTHER_SOURCE_FILE {
		@Override
		void execute(def params, Closure setResult) {
			FileTreeBuilder ftb = new FileTreeBuilder(sourceDir)
			ftb {
				a0 {
					b1 { 'b1.suf'('I am b1') }
				}
			}
		}
	},

	CREATE_INCREMENT {
		@Override
		void execute(def params, Closure setResult) {
			StringBuffer sb = new StringBuffer()
			new CreateAndVerifyIncrementService().createAndVerify(sourceDir, targetDir, sb, sb)
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
			setResult()
		}
	},

	RESTORE_INCREMENT {
		@Override
		void execute(def params, Closure setResult) {
			RestoreIncrementService restoreIncrementService = new RestoreIncrementService()
			Increment increment = params[0]
			restoreIncrementService.restore(increment, restoreDir, {})
			setResult()
		}
	}

	abstract void execute(def params = null, Closure setResult = null)

	static File sourceDir
	static File targetDir
	static File restoreDir

	static void init(File _sourceDir, File _targetDir, File _restoreDir) {
		sourceDir = _sourceDir
		targetDir = _targetDir
		restoreDir = _restoreDir
	}
}
