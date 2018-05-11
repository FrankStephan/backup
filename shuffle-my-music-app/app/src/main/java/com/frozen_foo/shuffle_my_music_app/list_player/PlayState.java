package com.frozen_foo.shuffle_my_music_app.list_player;

/**
 * Created by Frank on 17.04.2018.
 */

public class PlayState {

	public int songIndex;
	public int position;

	public PlayState(final int songIndex, final int position) {
		this.songIndex = songIndex;
		this.position = position;
	}
}
