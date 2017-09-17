package com.frozen_foo.shuffle_my_music_2

import groovy.xml.MarkupBuilder

class FavoritesService {

    IndexEntry[] addFavorites(String targetDirPath, IndexEntry[] newFavorites) {
        File favoritesFile = createOrGetFile(targetDirPath)
        List<IndexEntry> favorites = calculateResultingFavorites(favoritesFile, newFavorites)
        writeFavoritesToFile(favoritesFile, favorites)
        return favorites
    }

    private File createOrGetFile(String targetDirPath) {
        File favoritesFile = new File(targetDirPath, 'favorites.xml').absoluteFile
        if (!favoritesFile.exists()) {
            new File(targetDirPath).mkdirs()
            favoritesFile.createNewFile()
        }
        return favoritesFile
    }

    private List<IndexEntry> calculateResultingFavorites(File favoritesFile, IndexEntry[] newFavorites) {
        List<IndexEntry> favorites = readFavoritesFromFile(favoritesFile) as List
        newFavorites.each { IndexEntry it ->
            favorites << it
        }
        removeDuplicates(favorites)
    }

    private List<IndexEntry> removeDuplicates(List<IndexEntry> favorites) {
        favorites.unique()
    }

    private String writeFavoritesToFile(File favoritesFile, List<IndexEntry> favorites) {
        favoritesFile.withWriter('UTF-8') { favoritesFileWriter ->
            new MarkupBuilder(favoritesFileWriter).favorites(timestamp: new Date().getDateTimeString()) {
                for (IndexEntry favorite : favorites) {
                    song(title: favorite.fileName, favorite.path)
                }
            }
        }
    }

    IndexEntry[] loadFavorites(String targetDirPath) {
        File favoritesFile = createOrGetFile(targetDirPath)
        return readFavoritesFromFile(favoritesFile)
    }

    private Object readFavoritesFromFile(File favoritesFile) {
        String xmlString = favoritesFile.getText('UTF-8')
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
