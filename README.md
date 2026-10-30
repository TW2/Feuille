# Feuille
Feuille (Leaf) is a software for fansubbers first and targeting easy development for fansubbers and professionals.
It is the continuation of AssFxMaker, its big features are a karaoke editor (v0.5+) and an ASS drawing tool (v0.5+) and v0.6 comes with a whole ASS editor system (like substationalpha or aegisub for the references but different).
The current version (v0.6) is not ready for a release, please use v0.5. If you want to see the latest concept, you need at least JDK 26 (Oracle or others) and the last version of Maven to compile the software and open it. The software is fully in English (sorry for the errors) and partially in French.

### Availability:
- Windows -> Release later. It maybe works on WSL2 targeting Ubuntu and following installation like in Ubuntu. 
- Linux -> Software works and you can use libass_jna.so and you must install ffmpeg and libass in your system. I develop it using Ubuntu 26.04 so you can test it with ease. Release later.
- Mac -> Someone have a mac, I don't have a mac. MacOs breaks on VM with me and new versions. Release later if possible.

### Dependencies:
- FFmpeg
- FFprobe
- libass (libass_jna.c)
- GStreamer 1.x

### CMake:
To build ```libass_jna``` (so/dll/dylib) you must have cmake. libass must be installed in your compile system (cmake) or in your system like in linux.
Go to ```docs``` to get ```ass-native.zip``` and after extract, rename the folder that contains ```libass_jna.c``` to ```native```.
You are now ready for compile. Type this ```cmake -S native -B native/build && cmake --build native/build``` in a terminal while viewing the ```native``` folder in your folders list.
Place the resulting ```libass_jna``` (so/dll/dylib) in the ```/settings/dependencies/<os-name>```. ```<os-name>``` is your system name in lowercase (```linux```, ```mac```, ```windows```).

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
| Editor | Time, Preview, Open Save ASS (Java Swing / JNA / C) |
| Drawing | Line, Bézier work, but not Spline, delayed (Java Swing) |
| Analysis | Later (Java Swing) |

### Discord EN/FR:

[![Discord](https://github.com/user-attachments/assets/99ec6536-7624-41c1-afd1-7993fc4a1e25)](https://discord.gg/ef8xvA9wsF)
