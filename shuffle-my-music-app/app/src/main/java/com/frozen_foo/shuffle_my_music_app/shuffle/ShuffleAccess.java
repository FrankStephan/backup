package com.frozen_foo.shuffle_my_music_app.shuffle;

import android.content.Context;
import android.support.annotation.NonNull;

import com.frozen_foo.shuffle_my_music_2.IndexEntry;
import com.frozen_foo.shuffle_my_music_2.ShuffleMyMusicService;
import com.frozen_foo.shuffle_my_music_app.io.local.LocalDirectoryAccess;
import com.frozen_foo.shuffle_my_music_app.io.remote.RemoteDirectoryAccess;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

/**
 * Created by Frank on 13.10.2017.
 */

public class ShuffleAccess {

	public void cleanLocalData() throws IOException {
		new LocalDirectoryAccess().cleanLocalDir();
	}

	public InputStream loadIndexFromRemote(Context context) throws IOException, CertificateException,
			NoSuchAlgorithmException, InvalidKeyException, UnrecoverableEntryException,
			InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchProviderException, KeyStoreException {
		return new RemoteDirectoryAccess().indexStream(context);
	}

	public List<IndexEntry> shuffleIndexEntries(InputStream indexStream, int numberOfSongs) {
		return new ShuffleMyMusicService().randomIndexEntries(indexStream, numberOfSongs);
	}

	public void createLocalIndex(List<IndexEntry> shuffledIndexEntries) throws IOException {
		String localDirPath = localSongsDirPath();
		new ShuffleMyMusicService().createSongsFile(localDirPath, shuffledIndexEntries);
	}


	public List<IndexEntry> getLocalIndex() {
		String localDirPath = localSongsDirPath();
		return new ShuffleMyMusicService().loadSongsFile(localDirPath);
	}

	public void copySongFromRemoteToLocal(Context context, IndexEntry indexEntry) throws IOException,
			CertificateException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException,
			UnrecoverableEntryException, NoSuchProviderException, InvalidKeyException, KeyStoreException {
		InputStream remoteSongStream = new RemoteDirectoryAccess().songStream(context, indexEntry.getPath());
		new LocalDirectoryAccess().copyToLocal(remoteSongStream, indexEntry.getFileName());
	}

	public List<IndexEntry> markAsFavorites(List<IndexEntry> indexEntries) {
		String localDirPath = localSongsDirPath();
		return new ShuffleMyMusicService().addFavorites(localDirPath, indexEntries);
	}

	public void addFavoritesToLocalCollection() {
		String           localDirPath = new LocalDirectoryAccess().localDir().getPath();
		List<IndexEntry> favorites    = new ShuffleMyMusicService().loadFavorites(localDirPath);
		List<IndexEntry> newFavorites =
				new ShuffleMyMusicService().loadFavorites(localSongsDirPath());
		List<IndexEntry> resultingFavorites = new ShuffleMyMusicService().join(newFavorites, favorites);
		new ShuffleMyMusicService().addFavorites(localDirPath, resultingFavorites);
	}



	public void backupFavoritesCollectionToRemote() {

	}

	public File resolveLocalSong(IndexEntry indexEntry) {
		return new File(new LocalDirectoryAccess().localSongsDir(), indexEntry.getFileName());
	}



	@NonNull
	private String localSongsDirPath() {
		return new LocalDirectoryAccess().localSongsDir().getPath();
	}
}
