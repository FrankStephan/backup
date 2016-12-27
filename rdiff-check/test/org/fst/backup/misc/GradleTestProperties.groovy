package org.fst.backup.misc

import java.lang.reflect.Field
import java.lang.reflect.Modifier

// For integration tests to a (simulated) remote backup dir
// See RDiffCheck
class GradleTestProperties {

	private String remoteTestFolder


	public GradleTestProperties() {
		
	}

	public void read() {
		File propertiesFile = new File(gradleUserHome() + '/gradle.properties')
		Properties properties = new Properties()
		propertiesFile.withInputStream { properties.load(it) }

		Field[] fields = getClass().getDeclaredFields()

		fields.each {Field it ->
			if (!Modifier.isStatic(it.getModifiers())) {
				String value = properties.get(it.name)
				it.setAccessible(true)
				it.set(this, value)
			}
		}
	}

	private String gradleUserHome() {
		String gradleUserHome = System.getenv('GRADLE_USER_HOME')
		if (gradleUserHome != null) {
			return gradleUserHome
		} else {
			return System.getProperty('user.home') + '/.gradle'
		}
	}

	public static void main(String[] args) {
		GradleTestProperties gtp = new GradleTestProperties()
		gtp.read()
		println gtp.remoteTestFolder
	}
}
