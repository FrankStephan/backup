package org.fst.shuffle_my_music



String mediaLibraryPath = 'X:/Media/Musik'
String targetPath  = 'X:/Media/shuffle-my-music'
int numberOfSelectedSongs = 10


//BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
//print 'Shutdown nach Abschluss (j/n)?'
//input = br.readLine()


new Application(mediaLibraryPath, targetPath, numberOfSelectedSongs)

//'cmd /c shutdown.exe -s -t 01'.execute()

