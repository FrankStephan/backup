package org.fst.shuffle_my_music

import org.fst.shuffle_my_music.v1.ApplicationV1;





String mediaLibraryPath = 'X:/Media/Musik'
String targetPath  = 'X:/Media/shuffly-my-music'
int approximateTotalSongCount = 26000
int numberOfSelectedSongs = 50

new ApplicationV1(mediaLibraryPath, targetPath, approximateTotalSongCount, numberOfSelectedSongs)


//List<Path> songs = [
//	'X:/Media/Musik/Lost Prophets/Liberation Transmission/01 - Everyday Combat.mp3',
//	'X:/Media/Musik/O.S.T/Score - The 5th Element [2002]/01 Eric Serra - Little Light of Love.mp3',
//	'X:/Media/Musik/The Spin Doctors - Pocket Full Of Kryptonite [1993]/02 Spin Doctors - What Time Is It.mp3',
//	'X:/Media/Musik/Panic! At The Disco/A Fever You Can\'t Sweat Out/02 The Only Difference Between Martyrdom and Suicide is Press Coverage.mp3',
//	'X:/Media/Musik/Monster Magnet/Greatest Hits (2003)/CD1/03 - Dopes To Infinity.mp3',
//	'X:/Media/Musik/Hip Hop Krams/Aaliyah - I Care 4 U [2003]/03 Aaliyah - One In A Million.mp3',
//	'X:/Media/Musik/Creme De La Creme/Porno Funk/03 Creme De La Creme.mp3',
//	'X:/Media/Musik/Gorillaz/Gorillaz/03 Tomorrow Comes Today.mp3',
//	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/Sinners Lounge - The Erotic Sessions [2006]/CD 2/03. Minus 8 feat. Billie - Sometimes [Compost original edit].mp3',
//	'X:/Media/Musik/Crystal Method/Legion of Boom/05 I know its you.mp3',
//	'X:/Media/Musik/POP/Simian/We Are Your Friends/06 Big Black Gun.mp3',
//	'X:/Media/Musik/_Compilations gemsicht/DANCEHALL/07___REVOLUTION__LIVE_AT_RE.mp3',
//	'X:/Media/Musik/Adam Green/Adam Green - Gemstones [2005]/08 Adam Green - Emily.mp3',
//	'X:/Media/Musik/Elektronik/Chemical Brothers/Chemical Brothers - 1997 - Dig Your Own Hole/09 - Chemical Brother - Lost In The K-Hole - Dig Your Own Ho.mp3',
//	'X:/Media/Musik/Foo Fighters/Foo Fighters/09 For All The Cows.mp3',
//	'X:/Media/Musik/_Compilations Gitarre/Indie Rock Playlist February [2008]/106 Sons & Daughters - The Bell.mp3',
//	'X:/Media/Musik/CSS-Donkey-2008-DV8/11-css-air_painter.mp3',
//	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/VA-Klassik_Lounge_Nightflight_Vol.2-2CD-2007-NOiR/110-vector_lovers-tokyo_glitterati-noir.mp3',
//	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/Kontor_Sunset_Chill_Ibiza_Edition-2CD-2008-MOD/112_nova_june_-_fade_away.mp3',
//	'X:/Media/Musik/POP/Telépopmusik/Telépopmusik - Angel milk/13 - Tuesday.mp3',
//	'X:/Media/Musik/Wolfmother/Cosmic Egg/15 - Phoenix.mp3',
//	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/MTV Hacienda [2005]/15 afro medusa - pasilda.mp3',
//	'X:/Media/Musik/_Compilations Gitarre/Indie Rock Playlist January [2008]/15 The Marbles - Dracula.mp3',
//	'X:/Media/Musik/Elektronik/Compilations Electro/Only For Djs Vol.2  [2006]/16 Mister T & Demon Ritchie Feat. Prince Charles - Just U & I.mp3',
//	'X:/Media/Musik/Elektronik/Moloko/17-Moloko [On My Horsey].mp3',
//	'X:/Media/Musik/_Compilations Gitarre/Indie Rock Playlist May [2007]/18 Brakes - Cease And Desist.mp3',
//	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/Late Night Moods [2004]/206  Diana Ross - Lover Man (Oh Where Can You Be).mp3',
//	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/The Best Chillout Ever/CD 2/22 - Radiohead - Street Spirit (Fade Out).mp3',
//	'X:/Media/Musik/Fünf Sterne Deluxe/Sillium/23 DJ Coolmann vs DJ Toni.mp3',
//	'X:/Media/Musik/_Compilations Gitarre/Indie Rock Playlist January [2007]/49 You, Me, And Everyone We Know - I\'d Be More Interested If You Were Already Spoken For....mp3',
//	'X:/Media/Musik/Elektronik/Compilations Electro/DJ 3SHA - Elektrotrash - Best Music of MTV Alternative Nation&VIVA Berliner House(R.I.P.)/65 - Air -  Kelly Watch The Stars.mp3',
//	'X:/Media/Musik/_Compilations Gitarre/Indie Rock Playlist January [2007]/67 Meg & Dia - Monster.mp3',
//	'X:/Media/Musik/Elektronik/Chemical Brothers/Chemical Brothers - 2002 - Come With Us/Chemical Brothers - 10 - The Test - Come With Us.mp3',
//	'X:/Media/Musik/_Compilations gemsicht/Incoming/Chrissi D - Don\'t You Feel [Klaas Remix].mp3',
//	'X:/Media/Musik/Keith Jarret/Keith Jarrett The Köln Concert/Keith Jarrett - Köln Concert - Part I.mp3',
//	'X:/Media/Musik/_Compilations gemsicht/Incoming/Madonna Feat. Justin Timberlake - 4 Minutes.mp3',
//	'X:/Media/Musik/POP/Manu Chao/Manu Chao - La Radiolina [2007]/Manu Chao - El Hoyo.mp3',
//	'X:/Media/Musik/Kid Rock/The History of Rock/My Oedipus Complex feat. Twist.mp3',
//	'X:/Media/Musik/Die Toten Hosen/Opel-Gang.mp3',
//	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/Science Fiction Jazz - Vol. 3 - eac-256kbps - by Shankar/various artists - science fiction jazz volume three - 05 - waking up - nicolette.mp3'
//].collect { it ->
//	Paths.get(it)
//}
//
//new DirectoryService().createDirAndIndexList(songs, Paths.get(targetPath))
//'cmd /c shutdown.exe -s -t 01'.execute()

