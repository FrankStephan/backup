package org.fst.backup.model

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

@Builder(builderStrategy=SimpleStrategy)
class Increment {

	long secondsSinceTheEpoch
	String targetPath
}
