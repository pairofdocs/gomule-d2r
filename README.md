## Gomule-d2r
GoMule enabled for D2R


#### Original GoMule App
http://gomule.sourceforge.net/ all credits go to Gohanman, Randall, Silospen, collaborators, ...

Original usage docs: https://managedway.dl.sourceforge.net/project/gomule/gomule/R0.31/GoMuleDocs.pdf


#### Install
1. Install Java Runtime Environment [JRE (Windows x64 exe)](https://www.oracle.com/java/technologies/javase-jre8-downloads.html#license-lightbox) following the [instructions](https://docs.oracle.com/goldengate/1212/gg-winux/GDRAD/java.htm#BGBFJHAB)
2. Download the repo from [here](https://github.com/pairofdocs/gomule-d2r/archive/refs/heads/main.zip) and unzip it to a convenient location
3. Double click the app at `gomule-d2r-main/gomule/GoMule.jar`


#### Pics
- *Main Char*  
![Main](./img/gomule_lamboresu_sockitem.png)

- *Gems moved to the gems mule, runes to the runes mule and uniques to the uniques mule*  
![Gems, runes, uniques](https://i.imgur.com/1De14PT.png)

- *Moving&#160;multiple&#160;items*  
![Multi items](./img/move_runes_multiclick.gif)


#### Test Save Files
- [Sorc](./savefiles/LamboResu.d2s), [mule](./savefiles/testtt.d2s)


#### Flavie Grail Report
- [Full report](https://pairofdocs.github.io/gomule-d2r/GoMuleReport.html)

- Report Summary  
![Report Summary](./img/gomule_flavie_reportend.png) 


#### In Progress, Debugging
- Merc item reading and saving
- Update the data files to v1.14 (test anni and essences)


#### Fixed
- Moving items between chars ([pic](https://github.com/pairofdocs/gomule-d2r/pull/1#issuecomment-846635551))


#### Backup Save Files
WARNING: Back up important save files when using GoMule!

GoMule automatically creates backup directories in `GoMule.backup/` each `week` by default.
This can be changed to each `day`, `month` or `never`. 
Click on Preferences when the app is open to change this setting.


#### Building and Developing
- To build the GoMule app install JDK (either Java SE 11 (LTS) or Java SE 16) from [here](https://www.oracle.com/java/technologies/javase-downloads.html).
- Install [ant](https://mirrors.gigenet.com/apache//ant/binaries/apache-ant-1.10.10-bin.zip) from [here](https://ant.apache.org/bindownload.cgi) following the [instructions](https://ant.apache.org/manual/install.html). Go through the `Setup` section and set up `ANT_HOME`, `JAVA_HOME` and add `ANT_HOME/bin` to your path
- In a terminal go to the directory  `gomule-d2r/gomule/` and run `ant Jar-BuildAllOptimezed`. This builds GoMule.jar
- Debuggin with breakpoints using an IDE also lets you test the app (but doesn't create the java archive GoMule.jar)


#### Credits
- http://gomule.sourceforge.net/ for the original app
- https://github.com/d07RiV/d07riv.github.io for the save file converter and cracking the item Huffman encoding
- https://github.com/dschu012/d2s for the save file converter and editor
- https://user.xmission.com/~trevin/DiabloIIv1.09_File_Format.shtml for extensive details on the d2s file format
