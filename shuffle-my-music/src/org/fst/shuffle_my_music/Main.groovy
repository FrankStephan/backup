package org.fst.shuffle_my_music

import java.nio.file.Path
import java.nio.file.Paths



String mediaLibraryPath = 'X:/Media/Musik'
String targetPath  = 'X:/Media/shuffly-my-music'
int approximateTotalSongCount = 26000
int numberOfSelectedSongs = 50

//new Application(mediaLibraryPath, targetPath, approximateTotalSongCount, numberOfSelectedSongs)


//Everyday I'm Shuffling
//[99, 210, 300, 694, 826, 1295, 2880, 3529, 3532, 4757, 5044, 5088, 5097, 5166, 5301, 5594, 6955, 7243, 7752, 11227, 11374, 11580, 12477, 13531, 13667, 13671, 13695, 14385, 14761, 15189, 15617, 17145, 17254, 17592, 18026, 18192, 18879, 18962, 19109, 20335, 21565, 21813, 22179, 22338, 22559, 22885, 23458, 24710, 24717, 24966]
//[X:\Media\Musik\O.S.T\Jackie Brown\01 - Across 110th Street.MP3, X:\Media\Musik\The Kooks\Konk\01 See the Sun.mp3, X:\Media\Musik\Smashing Pumpkins - Zeitgeist [2007]\02 Smashing Pumpkins - 7 Shades of Black.mp3, X:\Media\Musik\_Compilations Gitarre\FM4 Soundselection 13 [2005]\CD02\03 Attwenger - Dog.mp3, X:\Media\Musik\Elektronik\Binärpilot\Defrag\04 Fuayfsilfm.mp3, X:\Media\Musik\Elektronik\Fenin\Grounded\04 None Of Them (Original Version).mp3, X:\Media\Musik\Adam Green\Adam Green - Friends of Mine [2003]\05 Adam Green - The Prince's Mine.mp3, X:\Media\Musik\Milburn - Well Well Well [2006]\05 Milburn - Lipstick Licking.mp3, X:\Media\Musik\Elektronik\Disco Ensemble - Magic Recoveries\06 - Threat Letter Typewriter.mp3, X:\Media\Musik\Mix_Best\06-Make me bad.mp3, X:\Media\Musik\The Hidden Cameras-Origin Orphan\Miike_Snow-Miike_Snow-2009-pLAN9\06-miike_snow-sans_soleil.mp3, X:\Media\Musik\Red Hot Chili Peppers\Greatest_Hits_\08 - Red Hot Chili Peppers - By The Way - EMG - www.elitemusic.org.mp3, X:\Media\Musik\Korn - Greatest Hits [2004]\08 Korn - Trash.mp3, X:\Media\Musik\QotSA\08 Titelnummer 8.wma, X:\Media\Musik\POP\Zero 7\Zero 7 - Another Late Night [2002]\08 Zero 7 - 93 Til Infinity - Sould Of Mischief.mp3, X:\Media\Musik\Iggy Pop\Preliminaires\09-He's dead _ she's alive.mp3, X:\Media\Musik\Sido\Maske\10 Knast (feat. MOK).mp3, X:\Media\Musik\_Compilations gemsicht\Chillout, Lounge\Kontor_Sunset_Chill_Ibiza_Edition-2CD-2008-MOD\107_the_korgis_-_everybodys_got_to_learn_sometime.mp3, X:\Media\Musik\Mark Ronson - Version [2007]\11  Mark Ronson Ft. Robbie Williams - The Only One I Know.mp3, X:\Media\Musik\Elektronik\Dj Koze\My Music Is Okay\11 - Visit Venus - Big tilt (dj koze remix).mp3, X:\Media\Musik\Elektronik\DJ Babu\DJ Babu - Duck Season Vol. 1\12 - Quasimoto - Microphone Mathematics (Remix).mp3, X:\Media\Musik\The Bloodhound Gang\Use Your Fingers\12 Earlameyer The Butt Pirate.mp3, X:\Media\Musik\Elektronik\The World Domination\Heat\13 - Land of Cockaigne Track 13 - Heat - The World Domination.mp3, X:\Media\Musik\O.S.T\Score - Black Hawk Down [2002]\14 Hans Zimmer - Of The Earth.mp3, X:\Media\Musik\_Compilations Gitarre\Rock Open [2005]\CD01\15 The Rasmus - In The Shadows.mp3, X:\Media\Musik\Hip Hop Krams\The Roots\Phrenology\16 - Thirsty.mp3, X:\Media\Musik\Elektronik\Compilations Electro\The Sound of Summer [2004]\17 Solu Music - Fade.mp3, X:\Media\Musik\_Compilations gemsicht\Chillout, Lounge\Samsung Chillout Session Vol.3 [2005]\26 Ferry Corsten - Holding on.mp3, X:\Media\Musik\Elektronik\Compilations Electro\DJ 3SHA - Elektrotrash - Best Music of MTV Alternative Nation&VIVA Berliner House(R.I.P.)\41 - Studio 45 - Freak it.mp3, X:\Media\Musik\_Compilations gemsicht\Chillout, Lounge\VA-City Lounge 3-4CD-2007\413-gryboy_feat._bart_davenport-genevieve.mp3, X:\Media\Musik\Elektronik\Compilations Electro\DJ 3SHA - Elektrotrash - Best Music of MTV Alternative Nation&VIVA Berliner House(R.I.P.)\44 - Turntablerocker - No melody.mp3, X:\Media\Musik\_Compilations Gitarre\Indie Rock Playlist February [2008]\52 Hard-Fi - I Shall Overcome.mp3, X:\Media\Musik\Elektronik\The Electronic\Alien Produkt - Strategies of Destruction (Master).mp3, X:\Media\Musik\Mix_Best\Children of Bodom - 01 - Follow The Reaper.mp3, X:\Media\Musik\Mix_Best\Disposable heroes.mp3, X:\Media\Musik\ON STAGE\Elvis Presley - Polk Salad Annie.mp3, X:\Media\Musik\_Compilations Gitarre\Indie Rock Playlist #2\Jimmy Eat World - Drugs or me.mp3, X:\Media\Musik\_Compilations gemsicht\Incoming\Kylie Minogue Feat. Mims - In My Arms [Remix].mp3, X:\Media\Musik\128 Indie and Alternative Acoustic Versions [1992-2002]\red hot chili peppers - scar tissue (acoustic).mp3, X:\Media\Musik\Elektronik\Drum'n'Bass\sabian\sabian - mycotoxin.mp3, X:\Media\Musik\Yellow Cab\Yellow Cap Live!\YELLOW CAP - Doesn't Matter (Live).mp3, X:\Media\Musik\Hip Hop Krams\Seeed\Seeed\[Unknown] 10 Song 10.mp3]
//Exception in thread "main" groovy.lang.MissingMethodException: No signature of method: static java.nio.file.Files.write() is applicable for argument types: (sun.nio.fs.WindowsPath, java.lang.String, java.nio.file.StandardOpenOption) values: [X:\Media\shuffly-my-music\songs.txt, [file:///X:/Media/Musik/O.S.T/Jackie%20Brown/01%20-%20Across%20110th%20Street.MP3, file:///X:/Media/Musik/The%20Kooks/Konk/01%20See%20the%20Sun.mp3, file:///X:/Media/Musik/Smashing%20Pumpkins%20-%20Zeitgeist%20%5B2007%5D/02%20Smashing%20Pumpkins%20-%207%20Shades%20of%20Black.mp3, file:///X:/Media/Musik/_Compilations%20Gitarre/FM4%20Soundselection%2013%20%5B2005%5D/CD02/03%20Attwenger%20-%20Dog.mp3, file:///X:/Media/Musik/Elektronik/Binärpilot/Defrag/04%20Fuayfsilfm.mp3, file:///X:/Media/Musik/Elektronik/Fenin/Grounded/04%20None%20Of%20Them%20(Original%20Version).mp3, file:///X:/Media/Musik/Adam%20Green/Adam%20Green%20-%20Friends%20of%20Mine%20%5B2003%5D/05%20Adam%20Green%20-%20The%20Prince's%20Mine.mp3, file:///X:/Media/Musik/Milburn%20-%20Well%20Well%20Well%20%5B2006%5D/05%20Milburn%20-%20Lipstick%20Licking.mp3, file:///X:/Media/Musik/Elektronik/Disco%20Ensemble%20-%20Magic%20Recoveries/06%20-%20Threat%20Letter%20Typewriter.mp3, file:///X:/Media/Musik/Mix_Best/06-Make%20me%20bad.mp3, file:///X:/Media/Musik/The%20Hidden%20Cameras-Origin%20Orphan/Miike_Snow-Miike_Snow-2009-pLAN9/06-miike_snow-sans_soleil.mp3, file:///X:/Media/Musik/Red%20Hot%20Chili%20Peppers/Greatest_Hits_/08%20-%20Red%20Hot%20Chili%20Peppers%20-%20By%20The%20Way%20-%20EMG%20-%20www.elitemusic.org.mp3, file:///X:/Media/Musik/Korn%20-%20Greatest%20Hits%20%5B2004%5D/08%20Korn%20-%20Trash.mp3, file:///X:/Media/Musik/QotSA/08%20Titelnummer%208.wma, file:///X:/Media/Musik/POP/Zero%207/Zero%207%20-%20Another%20Late%20Night%20%5B2002%5D/08%20Zero%207%20-%2093%20Til%20Infinity%20-%20Sould%20Of%20Mischief.mp3, file:///X:/Media/Musik/Iggy%20Pop/Preliminaires/09-He's%20dead%20_%20she's%20alive.mp3, file:///X:/Media/Musik/Sido/Maske/10%20Knast%20(feat.%20MOK).mp3, file:///X:/Media/Musik/_Compilations%20gemsicht/Chillout,%20Lounge/Kontor_Sunset_Chill_Ibiza_Edition-2CD-2008-MOD/107_the_korgis_-_everybodys_got_to_learn_sometime.mp3, file:///X:/Media/Musik/Mark%20Ronson%20-%20Version%20%5B2007%5D/11%20%20Mark%20Ronson%20Ft.%20Robbie%20Williams%20-%20The%20Only%20One%20I%20Know.mp3, file:///X:/Media/Musik/Elektronik/Dj%20Koze/My%20Music%20Is%20Okay/11%20-%20Visit%20Venus%20-%20Big%20tilt%20(dj%20koze%20remix).mp3, file:///X:/Media/Musik/Elektronik/DJ%20Babu/DJ%20Babu%20-%20Duck%20Season%20Vol.%201/12%20-%20Quasimoto%20-%20Microphone%20Mathematics%20(Remix).mp3, file:///X:/Media/Musik/The%20Bloodhound%20Gang/Use%20Your%20Fingers/12%20Earlameyer%20The%20Butt%20Pirate.mp3, file:///X:/Media/Musik/Elektronik/The%20World%20Domination/Heat/13%20-%20Land%20of%20Cockaigne%20Track%2013%20-%20Heat%20-%20The%20World%20Domination.mp3, file:///X:/Media/Musik/O.S.T/Score%20-%20Black%20Hawk%20Down%20%5B2002%5D/14%20Hans%20Zimmer%20-%20Of%20The%20Earth.mp3, file:///X:/Media/Musik/_Compilations%20Gitarre/Rock%20Open%20%5B2005%5D/CD01/15%20The%20Rasmus%20-%20In%20The%20Shadows.mp3, file:///X:/Media/Musik/Hip%20Hop%20Krams/The%20Roots/Phrenology/16%20-%20Thirsty.mp3, file:///X:/Media/Musik/Elektronik/Compilations%20Electro/The%20Sound%20of%20Summer%20%5B2004%5D/17%20Solu%20Music%20-%20Fade.mp3, file:///X:/Media/Musik/_Compilations%20gemsicht/Chillout,%20Lounge/Samsung%20Chillout%20Session%20Vol.3%20%5B2005%5D/26%20Ferry%20Corsten%20-%20Holding%20on.mp3, file:///X:/Media/Musik/Elektronik/Compilations%20Electro/DJ%203SHA%20-%20Elektrotrash%20-%20Best%20Music%20of%20MTV%20Alternative%20Nation&VIVA%20Berliner%20House(R.I.P.)/41%20-%20Studio%2045%20-%20Freak%20it.mp3, file:///X:/Media/Musik/_Compilations%20gemsicht/Chillout,%20Lounge/VA-City%20Lounge%203-4CD-2007/413-gryboy_feat._bart_davenport-genevieve.mp3, file:///X:/Media/Musik/Elektronik/Compilations%20Electro/DJ%203SHA%20-%20Elektrotrash%20-%20Best%20Music%20of%20MTV%20Alternative%20Nation&VIVA%20Berliner%20House(R.I.P.)/44%20-%20Turntablerocker%20-%20No%20melody.mp3, file:///X:/Media/Musik/_Compilations%20Gitarre/Indie%20Rock%20Playlist%20February%20%5B2008%5D/52%20Hard-Fi%20-%20I%20Shall%20Overcome.mp3, file:///X:/Media/Musik/Elektronik/The%20Electronic/Alien%20Produkt%20-%20Strategies%20of%20Destruction%20(Master).mp3, file:///X:/Media/Musik/Mix_Best/Children%20of%20Bodom%20-%2001%20-%20Follow%20The%20Reaper.mp3, file:///X:/Media/Musik/Mix_Best/Disposable%20heroes.mp3, file:///X:/Media/Musik/ON%20STAGE/Elvis%20Presley%20-%20Polk%20Salad%20Annie.mp3, file:///X:/Media/Musik/_Compilations%20Gitarre/Indie%20Rock%20Playlist%20%232/Jimmy%20Eat%20World%20-%20Drugs%20or%20me.mp3, file:///X:/Media/Musik/_Compilations%20gemsicht/Incoming/Kylie%20Minogue%20Feat.%20Mims%20-%20In%20My%20Arms%20%5BRemix%5D.mp3, file:///X:/Media/Musik/128%20Indie%20and%20Alternative%20Acoustic%20Versions%20%5B1992-2002%5D/red%20hot%20chili%20peppers%20-%20scar%20tissue%20(acoustic).mp3, file:///X:/Media/Musik/Elektronik/Drum'n'Bass/sabian/sabian%20-%20mycotoxin.mp3, file:///X:/Media/Musik/Yellow%20Cab/Yellow%20Cap%20Live!/YELLOW%20CAP%20-%20Doesn't%20Matter%20(Live).mp3, file:///X:/Media/Musik/Hip%20Hop%20Krams/Seeed/Seeed/%5BUnknown%5D%2010%20Song%2010.mp3], ...]
//Possible solutions: wait(), write(java.nio.file.Path, [B, [Ljava.nio.file.OpenOption;), write(java.nio.file.Path, java.lang.Iterable, [Ljava.nio.file.OpenOption;), write(java.nio.file.Path, java.lang.Iterable, java.nio.charset.Charset, [Ljava.nio.file.OpenOption;), grep(), find()
//	at groovy.lang.MetaClassImpl.invokeStaticMissingMethod(MetaClassImpl.java:1503)
//	at groovy.lang.MetaClassImpl.invokeStaticMethod(MetaClassImpl.java:1489)
//	at org.codehaus.groovy.runtime.callsite.StaticMetaClassSite.call(StaticMetaClassSite.java:53)
//	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:48)
//	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:113)
//	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:141)
//	at org.fst.shuffle_my_music.DirectoryService.createDirAndIndexList(DirectoryService.groovy:15)
//	at org.fst.shuffle_my_music.DirectoryService$createDirAndIndexList.call(Unknown Source)
//	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:48)
//	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:113)
//	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:133)
//	at org.fst.shuffle_my_music.Application.<init>(Application.groovy:12)
//	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
//	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
//	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
//	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
//	at org.codehaus.groovy.reflection.CachedConstructor.invoke(CachedConstructor.java:83)
//	at org.codehaus.groovy.runtime.callsite.ConstructorSite$ConstructorSiteNoUnwrapNoCoerce.callConstructor(ConstructorSite.java:105)
//	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallConstructor(CallSiteArray.java:60)
//	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:235)
//	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callConstructor(AbstractCallSite.java:271)
//	at org.fst.shuffle_my_music.Main.run(Main.groovy:10)
//	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
//	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//	at java.lang.reflect.Method.invoke(Method.java:498)
//	at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:93)
//	at groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:325)
//	at groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1215)
//	at groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1024)
//	at org.codehaus.groovy.runtime.InvokerHelper.invokePogoMethod(InvokerHelper.java:923)
//	at org.codehaus.groovy.runtime.InvokerHelper.invokeMethod(InvokerHelper.java:906)
//	at org.codehaus.groovy.runtime.InvokerHelper.runScript(InvokerHelper.java:410)
//	at org.codehaus.groovy.runtime.InvokerHelper$runScript.call(Unknown Source)
//	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:48)
//	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:113)
//	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:133)
//	at org.fst.shuffle_my_music.Main.main(Main.groovy)




//List<Path> songs = [
//	'X:/Media/Musik/green Day/1039 Smoothed Out Slappy Hours/03 I Was There.mp3',
//	'X:/Media/Musik/Elektronik/TO80/tbod80/the best of disco 80/036. Stage - Woodoo Dance.mp3',
//	'X:/Media/Musik/My Mother and Father Were Astronauts (2004)/08 - Avatar (remix).mp3',
//	'X:/Media/Musik/green Day/Green Day - American Idiot [2004]/08 Green Day - She\'s a Rebel.mp3',
//	'X:/Media/Musik/Dike/Pottpüree/09 Kreist Es Noch.mp3',
//	'X:/Media/Musik/Hip Hop Krams/Foxy Brown - Broken Silence [2001]/11 Foxy Brown - Bout My Paper Feat Mystikal.mp3',
//	'X:/Media/Musik/Auto/Rammstein-Liebe_Ist_Fuer_Alle_Da_(Special_Edition)-DE-2CD-2009-VOiCE/110_rammstein_-_mehr.mp3',
//	'X:/Media/Musik/P.O.D. - Satellite [2001]/12 P.O.D. - Masterpiece Conspiracy.mp3',
//	'X:/Media/Musik/Elektronik/Compilations Electro/Only For Djs Vol.2  [2006]/14 Serial Crew - Need U (Muttonheads Remix).mp3',
//	'X:/Media/Musik/Hip Hop Krams/Eins Zwo/Gefährliches Halbwissen/Eins zwo - 13.mp3'
//].collect { it ->
//	Paths.get(it)
//}
//

List<Path> songs = [
	'X:/Media/Musik/O.S.T/Jackie Brown/01 - Across 110th Street.mp3',
	'X:/Media/Musik/The Kooks/Konk/01 See the Sun.mp3',
	'X:/Media/Musik/Smashing Pumpkins - Zeitgeist [2007]/02 Smashing Pumpkins - 7 Shades of Black.mp3',
	'X:/Media/Musik/_Compilations Gitarre/FM4 Soundselection 13 [2005]/CD02/03 Attwenger - Dog.mp3',
	'X:/Media/Musik/Elektronik/Binärpilot/Defrag/04 Fuayfsilfm.mp3',
	'X:/Media/Musik/Elektronik/Fenin/Grounded/04 None Of Them (Original Version).mp3',
	'X:/Media/Musik/Adam Green/Adam Green - Friends of Mine [2003]/05 Adam Green - The Prince\'s Mine.mp3',
	'X:/Media/Musik/Milburn - Well Well Well [2006]/05 Milburn - Lipstick Licking.mp3',
	'X:/Media/Musik/Elektronik/Disco Ensemble - Magic Recoveries/06 - Threat Letter Typewriter.mp3',
	'X:/Media/Musik/Mix_Best/06-Make me bad.mp3',
	'X:/Media/Musik/The Hidden Cameras-Origin Orphan/Miike_Snow-Miike_Snow-2009-pLAN9/06-miike_snow-sans_soleil.mp3',
	'X:/Media/Musik/Red Hot Chili Peppers/Greatest_Hits_/08 - Red Hot Chili Peppers - By The Way - EMG - www.elitemusic.org.mp3',
	'X:/Media/Musik/Korn - Greatest Hits [2004]/08 Korn - Trash.mp3',
	'X:/Media/Musik/POP/Zero 7/Zero 7 - Another Late Night [2002]/08 Zero 7 - 93 Til Infinity - Sould Of Mischief.mp3',
	'X:/Media/Musik/Iggy Pop/Preliminaires/09-He\'s dead _ she\'s alive.mp3',
	'X:/Media/Musik/Sido/Maske/10 Knast (feat. MOK).mp3',
	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/Kontor_Sunset_Chill_Ibiza_Edition-2CD-2008-MOD/107_the_korgis_-_everybodys_got_to_learn_sometime.mp3',
	'X:/Media/Musik/Mark Ronson - Version [2007]/11  Mark Ronson Ft. Robbie Williams - The Only One I Know.mp3',
	'X:/Media/Musik/Elektronik/Dj Koze/My Music Is Okay/11 - Visit Venus - Big tilt (dj koze remix).mp3',
	'X:/Media/Musik/Elektronik/DJ Babu/DJ Babu - Duck Season Vol. 1/12 - Quasimoto - Microphone Mathematics (Remix).mp3',
	'X:/Media/Musik/The Bloodhound Gang/Use Your Fingers/12 Earlameyer The Butt Pirate.mp3',
	'X:/Media/Musik/Elektronik/The World Domination/Heat/13 - Land of Cockaigne Track 13 - Heat - The World Domination.mp3',
	'X:/Media/Musik/O.S.T/Score - Black Hawk Down [2002]/14 Hans Zimmer - Of The Earth.mp3',
	'X:/Media/Musik/_Compilations Gitarre/Rock Open [2005]/CD01/15 The Rasmus - In The Shadows.mp3',
	'X:/Media/Musik/Hip Hop Krams/The Roots/Phrenology/16 - Thirsty.mp3',
	'X:/Media/Musik/Elektronik/Compilations Electro/The Sound of Summer [2004]/17 Solu Music - Fade.mp3',
	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/Samsung Chillout Session Vol.3 [2005]/26 Ferry Corsten - Holding on.mp3',
	'X:/Media/Musik/Elektronik/Compilations Electro/DJ 3SHA - Elektrotrash - Best Music of MTV Alternative Nation&VIVA Berliner House(R.I.P.)/41 - Studio 45 - Freak it.mp3',
	'X:/Media/Musik/_Compilations gemsicht/Chillout, Lounge/VA-City Lounge 3-4CD-2007/413-gryboy_feat._bart_davenport-genevieve.mp3',
	'X:/Media/Musik/Elektronik/Compilations Electro/DJ 3SHA - Elektrotrash - Best Music of MTV Alternative Nation&VIVA Berliner House(R.I.P.)/44 - Turntablerocker - No melody.mp3',
	'X:/Media/Musik/_Compilations Gitarre/Indie Rock Playlist February [2008]/52 Hard-Fi - I Shall Overcome.mp3',
	'X:/Media/Musik/Elektronik/The Electronic/Alien Produkt - Strategies of Destruction (Master).mp3',
	'X:/Media/Musik/Mix_Best/Children of Bodom - 01 - Follow The Reaper.mp3',
	'X:/Media/Musik/Mix_Best/Disposable heroes.mp3',
	'X:/Media/Musik/ON STAGE/Elvis Presley - Polk Salad Annie.mp3',
	'X:/Media/Musik/_Compilations Gitarre/Indie Rock Playlist #2/Jimmy Eat World - Drugs or me.mp3',
	'X:/Media/Musik/_Compilations gemsicht/Incoming/Kylie Minogue Feat. Mims - In My Arms [Remix].mp3',
	'X:/Media/Musik/128 Indie and Alternative Acoustic Versions [1992-2002]/red hot chili peppers - scar tissue (acoustic).mp3',
	'X:/Media/Musik/Elektronik/Drum\'n\'Bass/sabian/sabian - mycotoxin.mp3',
	'X:/Media/Musik/Yellow Cab/Yellow Cap Live!/YELLOW CAP - Doesn\'t Matter (Live).mp3',
	'X:/Media/Musik/Hip Hop Krams/Seeed/Seeed/[Unknown] 10 Song 10.mp3'
].collect { it ->
	Paths.get(it)
}

new DirectoryService().createDirAndIndexList(songs, Paths.get(targetPath))


