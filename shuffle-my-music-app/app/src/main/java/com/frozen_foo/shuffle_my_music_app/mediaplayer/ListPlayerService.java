package com.frozen_foo.shuffle_my_music_app.mediaplayer;

import android.content.Context;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;


/**
 * Created by Frank on 27.03.2018.
 */

public class ListPlayerService extends MediaBrowserService {
	private MediaSession mMediaSession;
	private PlaybackState.Builder mStateBuilder;
	private final Context context;

	public ListPlayerService(final Context context) {
		this.context = context;
	}

	/*
	https://developer.android.com/guide/topics/media/mediaplayer.html#mpandservices
	https://developer.android.com/guide/topics/media-apps/audio-app/building-a-mediabrowserservice.html

	 <service android:name=".MyMediaBrowserServiceCompat"
          android:label="@string/service_name" >
     <intent-filter>
         <action android:name="android.media.browse.MediaBrowserService" />
     </intent-filter>
 </service>

https://developer.android.com/reference/android/support/v4/media/MediaBrowserCompat.html

	 */

	@Nullable
	@Override
	public BrowserRoot onGetRoot(@NonNull final String clientPackageName, final int clientUid,
								 @Nullable final Bundle rootHints) {
		return null;
	}

	@Override
	public void onLoadChildren(@NonNull final String parentId,
							   @NonNull final Result<List<MediaBrowser.MediaItem>> result) {

	}



	@Override
	public void onCreate() {
		super.onCreate();

		// Create a MediaSessionCompat
		mMediaSession = new MediaSession(context, getClass().getName());

		// Enable callbacks from MediaButtons and TransportControls
		mMediaSession.setFlags(
				MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
						MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

		// Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
		mStateBuilder = new PlaybackState.Builder()
				.setActions(
						PlaybackState.ACTION_PLAY |
								PlaybackState.ACTION_PLAY_PAUSE);
		mMediaSession.setPlaybackState(mStateBuilder.build());

		// MySessionCallback() has methods that handle callbacks from a media controller
		mMediaSession.setCallback(new SessionCallback());

		// Set the session's token so that client activities can communicate with it.
		setSessionToken(mMediaSession.getSessionToken());
	}
}
