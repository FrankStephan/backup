package org.fst.shuffle_my_music



String mediaLibraryPath = 'X:/Media/Musik'
String targetPath  = 'X:/Media/shuffly-my-music'
int numberOfSelectedSongs = 10

new Application(mediaLibraryPath, targetPath, numberOfSelectedSongs)

'cmd /c shutdown.exe -s -t 01'.execute()

