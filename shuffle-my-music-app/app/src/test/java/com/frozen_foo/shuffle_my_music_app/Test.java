package com.frozen_foo.shuffle_my_music_app;

import com.frozen_foo.shuffle_my_music_2.FavoritesService;
import com.frozen_foo.shuffle_my_music_2.IndexEntry;

import org.apache.commons.io.FileUtils;
import org.junit.After;

import java.io.File;
import java.io.IOException;

/**
 * Created by Frank on 20.09.2017.
 */

public class Test {

	String testPathDir = this.getClass().getSimpleName();

	@org.junit.Test
	public void testFavoritesService() {
		IndexEntry[] indexEntries = new IndexEntry[] {new IndexEntry("song2", "dir/song2")};

		IndexEntry[] indexEntries1 = new FavoritesService().addFavorites(testPathDir, indexEntries);

		IndexEntry[] indexEntries2 = new FavoritesService().loadFavorites(testPathDir);

		assert indexEntries1.equals(indexEntries2);
	}

	@After
	public void after() throws IOException {
		FileUtils.deleteDirectory(new File(testPathDir));
	}
}
