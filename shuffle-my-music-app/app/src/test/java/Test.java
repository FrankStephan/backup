import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;

import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

	public static final String TEST_PATH = "test";

	@Before
	public void before() {
		File path = new File(TEST_PATH);
		path.mkdir();
	}

	@After
	public void after() {
		File path = new File(TEST_PATH);
		if (path.exists()) {
			path.deleteOnExit();
		}
	}

	@org.junit.Test
	public void testCreateAndLoadSongFile() {
		ShuffleMyMusicService smms        = new ShuffleMyMusicService();
		IndexEntry            indexEntry1 = new IndexEntry("1.mp3", "a/1.mp3");
		IndexEntry            indexEntry2 = new IndexEntry("2.mp3", "a/2.mp3");
		smms.createSongsFile(TEST_PATH, Arrays.asList(indexEntry1, indexEntry2));


		List<IndexEntry> indexEntries = smms.loadSongsFile(TEST_PATH);
		System.out.println(indexEntries);


	}

	@org.junit.Test
	public void testCreateAndLoadFavorites() {
		ShuffleMyMusicService smms = new ShuffleMyMusicService();

		IndexEntry indexEntry1 = new IndexEntry("1.mp3", "a/1.mp3");
		IndexEntry indexEntry2 = new IndexEntry("2.mp3", "a/2.mp3");
		List<IndexEntry> newFavorites = Arrays.asList(indexEntry1, indexEntry2);
		smms.saveFavorites(TEST_PATH, newFavorites, false);

		assert newFavorites.equals(smms.loadFavorites("test"));

	}

	@org.junit.Test
	public void testJoin() {
		ShuffleMyMusicService smms = new ShuffleMyMusicService();
		IndexEntry indexEntry1 = new IndexEntry("1.mp3", "a/1.mp3");
		IndexEntry indexEntry2 = new IndexEntry("2.mp3", "a/2.mp3");
		List<IndexEntry> favorites = Arrays.asList(indexEntry1, indexEntry2);
		assert smms.join(new ArrayList<IndexEntry>(), new ArrayList<IndexEntry>()).isEmpty();

	}

}
