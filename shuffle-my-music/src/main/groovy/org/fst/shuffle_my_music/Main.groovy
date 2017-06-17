package org.fst.shuffle_my_music

import org.fst.shuffle_my_music.v2.ApplicationV2


String mediaLibraryPath = 'X:/Media/Musik'
String targetPath  = 'X:/Media/shuffly-my-music'
int numberOfSelectedSongs = 10

new ApplicationV2(mediaLibraryPath, targetPath, numberOfSelectedSongs)

'cmd /c shutdown.exe -s -t 01'.execute()

