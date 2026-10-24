# Feuille
Feuille (Leaf) is a software for fansubbers first and targeting easy development for fansubbers and professionals.
This is a tool that contains more than one tool. In the meantime we can not use developed version, so you can just see the preview version or v0.5 in releases.

The current version (v0.6) is not ready for a release, please use v0.5. If you want to see the latest concept, you need at least JDK 26 (Oracle or others) and the last version of Maven to compile the software and open it. The software is fully in English (sorry for the errors) and partially in French.
### Dependencies:
- FFmpeg and a complete set of dev shared library (see fmp readme)
- FFprobe
- libass with a complete set of dev shared library (see fmp readme)

This three dependencies must be installed in your system (as in Linux with apt) and present in your path (as in Windows).
The seek function of FMP (Free Media Player), a player made by AI which embeds FFmpeg and libass, is deactivated because of a crash of OS by a memory overflow with a freeze.

### How to build FMP:

0/ You need cmake build tools and shared library of FFmpeg and libass, put them at final folder of FMP or install them in your system.<br>
1/ After the cloning of Feuille, go to ```/docs``` and unzip ```fmp.zip```.<br>
2/ In the fresh fmp folder, type: ```cmake -S . -B build -DCMAKE_BUILD_TYPE=Release```.<br>
3/ Then ```cmake --build build```.<br>
4/ Then ```cmake --install build --prefix install```.<br>
5/ In ```/install/lib``` copy files and place them in resources folder ```/src/main/resources/ext_lib/fmp/<os>```.

### How to build Feuille:
1/ Have git, maven and the last version of Java installed and configured in your system.<br>
2/ Clone Feuille repository<br>
3/ In Feuille folder type ```mvn verify```<br>
4/ Enter target subfolder<br>
5/ Launch with the command ```java -jar``` targeting the Feuille shaded version.

### Current development of v0.6:

| Subject | Few functionalities |
|---|---|
| Welcome | Show License, Designed to display Help in HTML (JavaFX) |
| Karaoke | Later (Java Swing) |
| Editor | Time, Preview (with issues), Open Save ASS (Java Swing / JNA / C++) |
| Drawing | Line, Bézier work, but not Spline, delayed (Java Swing) |
| Analysis | Later (Java Swing) |

### Discord EN/FR:

[![Discord](https://github.com/user-attachments/assets/99ec6536-7624-41c1-afd1-7993fc4a1e25)](https://discord.gg/ef8xvA9wsF)
