package com.frozen_foo.shuffle_my_music_2

import java.nio.file.Paths

import com.frozen_foo.shuffle_my_music_2.impl.CreateIndexService;

long millis = System.currentTimeMillis()

String mediaLibraryPath = 'X:/Media/Musik'
new CreateIndexService().createIndex(Paths.get(mediaLibraryPath))

File time = new File(mediaLibraryPath, 'time.txt')
time << ((System.currentTimeMillis() - millis) / 1000)
time << System.lineSeparator()

('cmd /c shutdown.exe -s -t 10').execute()
