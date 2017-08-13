package com.frozen_foo.shuffle_my_music_2

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString



@EqualsAndHashCode
@ToString(includePackage = false)
class IndexEntry {

	String path
	String fileName
}
