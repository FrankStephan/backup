package com.frozen_foo.shuffle_my_music_2

import java.nio.file.Files
import java.nio.file.Paths



long millis = System.currentTimeMillis()

String mediaLibraryPath = 'C:/Users/Frank/Desktop/Favorite Songs'

InputStream is = Files.newInputStream(Paths.get('C:/Users/Frank/Desktop/Favorite Songs/index.txt'))
List favorites = new ShuffleMyMusicService().randomIndexEntries(Files.newInputStream(Paths.get(mediaLibraryPath, "index.txt")), 36)
new ShuffleMyMusicService().writeFavorites(favorites, Files.newOutputStream(Paths.get('C:/Users/Frank/Desktop/Favorite Songs/favorites.xml')))


//new CreateIndexService().createIndex(Paths.get(mediaLibraryPath))
//
//File time = new File(mediaLibraryPath, 'time.txt')
//time << (((System.currentTimeMillis() - millis) / 60000) + ' min')
//time << System.lineSeparator()

// ('cmd /c shutdown.exe -s -t 10').execute()
