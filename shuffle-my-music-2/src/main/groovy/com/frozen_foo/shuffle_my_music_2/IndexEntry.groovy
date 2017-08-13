package com.frozen_foo.shuffle_my_music_2

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor



@EqualsAndHashCode
@ToString(includePackage = false)
@TupleConstructor
class IndexEntry {

	String fileName
	String path
}
