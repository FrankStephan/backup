package com.frozen_foo.shuffle_my_music_2

import groovy.xml.MarkupBuilder

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


class FavoritesService {


    IndexEntry[] addFavorites(String targetDirPath, IndexEntry[] newFavorites) {
        Path favoritesFile = createOrGetFile(targetDirPath)
        List<IndexEntry> favorites = calculateResultingFavorites(favoritesFile, newFavorites)
        writeFavoritesToFile(favoritesFile, favorites)
        return favorites
    }

    private Path createOrGetFile(String targetDirPath) {
        Path targetDir = Paths.get(targetDirPath).toAbsolutePath()
        Path favoritesFile = targetDir.resolve('favorites.xml')
        if (!Files.exists(favoritesFile)) {
            Files.createDirectories(targetDir)
            Files.createFile(favoritesFile)
        }
        return favoritesFile
    }

    private List<IndexEntry> calculateResultingFavorites( Path favoritesFile, IndexEntry[] newFavorites) {
        List<IndexEntry> favorites = readFavoritesFromFile(favoritesFile) as List
        newFavorites.each { IndexEntry it ->
            favorites << it
        }
        removeDuplicates(favorites)
    }

    private List<IndexEntry> removeDuplicates(List<IndexEntry> favorites) {
        favorites.unique()
    }

    private String writeFavoritesToFile(Path favoritesFile, List<IndexEntry> favorites) {
        favoritesFile.withWriter('UTF-8') { favoritesFileWriter ->
            new MarkupBuilder(favoritesFileWriter).favorites(timestamp: new Date().getDateTimeString()) {
                for (IndexEntry favorite : favorites) {
                    song(title: favorite.fileName, favorite.path)
                }
            }
        }
    }

    IndexEntry[] loadFavorites(String targetDirPath) {
        Path favoritesFile = createOrGetFile(targetDirPath)
        return readFavoritesFromFile(favoritesFile)

    }

    private Object readFavoritesFromFile(Path favoritesFile) {
        String xmlString = favoritesFile.toFile().getText('UTF-8')
        if (!xmlString.isEmpty()) {
            def favoritesXml = new XmlSlurper().parseText(xmlString)

            IndexEntry[] favorites = new IndexEntry[favoritesXml.song.size()]
            favoritesXml.song.eachWithIndex { def song, int i ->
                favorites[i] = new IndexEntry(fileName: song.@title, path: song)
            }
            return favorites
        } else {
            return []
        }
    }

}
