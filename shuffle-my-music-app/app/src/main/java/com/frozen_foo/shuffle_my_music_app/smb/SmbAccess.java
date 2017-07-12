package com.frozen_foo.shuffle_my_music_app.smb;

import jcifs.smb.NtlmPasswordAuthentication;

/**
 * Created by Frank on 12.07.2017.
 */

public class SmbAccess {

	public boolean available() {


		return false;
	}

	private void connect() {
		jcifs.Config.setProperty( "jcifs.netbios.wins", "192.168.1.220" );
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("domain", "username", "password");
	}
}
