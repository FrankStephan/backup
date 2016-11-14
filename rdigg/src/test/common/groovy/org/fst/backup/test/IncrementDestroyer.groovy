package org.fst.backup.test

import java.nio.file.Files

import org.fst.backup.service.VerificationService

class IncrementDestroyer {
	
	static ProxyMetaClass destroyIncrementBeforeVerification(File fileToDelete) {
		ProxyMetaClass verificationServiceProxy = ProxyMetaClass.getInstance(VerificationService.class)
		verificationServiceProxy.interceptor = new Interceptor() {

					@Override
					public Object beforeInvoke(Object object, String methodName,
							Object[] arguments) {

						if (methodName == 'verifyMirror') {
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
