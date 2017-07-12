package com.frozen_foo.shuffle_my_music_app.settings;

public class Settings {

	private final String ip;
	private final String username;
	private final String password;

	public Settings(String ip, String username, String password) {
		this.ip = ip;
		this.username = username;
		this.password = password;
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
}
