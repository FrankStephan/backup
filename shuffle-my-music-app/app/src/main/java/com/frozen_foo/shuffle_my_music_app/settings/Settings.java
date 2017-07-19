package com.frozen_foo.shuffle_my_music_app.settings;

public class Settings {

	private final String ip;
	private final String username;
	private final String password;
	private final String shuffleMyMusicDir;
	private final String musicDir;

	public Settings(String ip, String username, String password, String shuffleMyMusicDir, String musicDir) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.shuffleMyMusicDir = shuffleMyMusicDir;
		this.musicDir = musicDir;
	}

	public String getIp() {
		return ip;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getShuffleMyMusicDir() { return shuffleMyMusicDir; }

	public String getMusicDir() { return musicDir; }
}
