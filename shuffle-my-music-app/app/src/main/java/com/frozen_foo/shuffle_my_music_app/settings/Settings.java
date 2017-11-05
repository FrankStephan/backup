package com.frozen_foo.shuffle_my_music_app.settings;

public class Settings {

	private final String ip;
	private final String username;
	private final String password;
	private final String localDir;
	private final String remoteDir;

	public Settings(String ip, String username, String password, String localDir, String remoteDir) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.localDir = localDir;
		this.remoteDir = remoteDir;
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

	public String getLocalDir() {
		return localDir;
	}

	public String getRemoteDir() {
		return remoteDir;
	}
}
