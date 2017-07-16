package com.frozen_foo.shuffle_my_music_app.settings;

public class Settings {

	private final String ip;
	private final String username;
	private final String password;
	private String shuffleMyMusicDir;

	public Settings(String ip, String username, String password, String shuffleMyMusicDir) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.shuffleMyMusicDir = shuffleMyMusicDir;
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

	public String getShuffleMyMusicDir() {
		return shuffleMyMusicDir;
	}
}
