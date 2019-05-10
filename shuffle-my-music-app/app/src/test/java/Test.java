import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.crypto.Cryptifier;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class Test {



	@org.junit.Test
	public void testLib() {
		ShuffleMyMusicService smms = new ShuffleMyMusicService();
		IndexEntry indexEntry1 = new IndexEntry("1.mp3", "a/1.mp3");
		IndexEntry indexEntry2 = new IndexEntry("2.mp3", "a/2.mp3");
		smms.createSongsFile("/test", Arrays.asList(indexEntry1, indexEntry2));


		List<IndexEntry> indexEntries = smms.loadSongsFile("/test");
		System.out.println(indexEntries);


	}

	@org.junit.Test
	public void testCryptifier() throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
			NoSuchProviderException, InvalidAlgorithmParameterException, IOException, UnrecoverableEntryException,
			NoSuchPaddingException, InvalidKeyException {
		Cryptifier cryptifier = new Cryptifier();
		String     encoded = cryptifier.encrypt("123");
		String     decoded    = cryptifier.decrypt(encoded);
		assert "123".equals(decoded);
	}

}
