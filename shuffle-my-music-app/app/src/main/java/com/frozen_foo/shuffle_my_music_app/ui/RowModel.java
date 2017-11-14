package com.frozen_foo.shuffle_my_music_app.ui;

/**
 * Created by Frank on 04.07.2017.
 */

public class RowModel {

	private String label;
	private String path;
	private boolean copying;
	private boolean favorite;
	private boolean playing;
	private String duration;

	public RowModel(String label, String path, boolean favorite) {
		this.label = label;
		this.path = path;
		this.favorite = favorite;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isCopying() {
		return copying;
	}

	public void setCopying(final boolean copying) {
		this.copying = copying;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(final boolean favorite) {
		this.favorite = favorite;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(final boolean playing) {
		this.playing = playing;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(final String duration) {
		this.duration = duration;
	}
}
