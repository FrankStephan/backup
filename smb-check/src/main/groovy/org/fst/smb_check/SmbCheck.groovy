package org.fst.smb_check

import jcifs.smb.NtlmPasswordAuthentication
import jcifs.smb.SmbFile

jcifs.Config.setProperty( 'jcifs.netbios.wins', '192.168.0.2' )
NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, 'admin', '')
SmbFile smbFile = new SmbFile('smb://READYSHARE/all/Media/shuffle-my-music/songs.txt', auth)
InputStream fis = smbFile.getInputStream()

byte[] b = new byte[8192]
int n
while(( n = fis.read( b )) > 0 ) {
	System.out.write( b, 0, n )
}

fis.close()