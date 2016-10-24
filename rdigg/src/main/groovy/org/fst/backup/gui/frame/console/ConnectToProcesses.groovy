package org.fst.backup.gui.frame.console

import java.awt.Color

import org.fst.backup.rdiff.RDiffCommandExecutor
import org.fst.backup.gui.CommonViewModel
import org.fst.backup.gui.DocumentWriter

class ConnectToProcesses {

	void interceptRDiff() {

		ProxyMetaClass proxyMetaClass = ProxyMetaClass.getInstance(RDiffCommandExecutor.class)
		proxyMetaClass.setInterceptor(interceptor)
	}

	class ExecutorInterceptor implements Interceptor {

		CommonViewModel commonViewModel

		@Override
		public Object afterInvoke(Object arg0, String arg1, Object[] arg2,
				Object result) {
			Process process = result
			DocumentWriter docWriter = new DocumentWriter(document: commonViewModel.consoleDocument, textColor: Color.BLUE)
			DocumentWriter errWriter = new DocumentWriter(document: commonViewModel.consoleDocument, textColor: Color.RED)
			process.waitForProcessOutput(docWriter, errWriter)
			// --> Problem: Blocks the actual method. Use consumeXY...()
			return process
		}

		@Override
		public Object beforeInvoke(Object arg0, String arg1, Object[] arg2) {
			// TODO Auto-generated method stub
			return null
		}

		@Override
		public boolean doInvoke() {
			return true
		}
	}
}
