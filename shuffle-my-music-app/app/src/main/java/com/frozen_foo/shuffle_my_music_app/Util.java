package com.frozen_foo.shuffle_my_music_app;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Frank on 02.08.2017.
 */

public class Util {

	public static String[] toFileNameList(File[] result) {
		return (String[]) CollectionUtils.collect(Arrays.asList(result), new Transformer() {
			@Override
			public Object transform(Object input) {
				return ((File) (input)).getName();
			}
		}).toArray(new String[result.length]);
	}
}
