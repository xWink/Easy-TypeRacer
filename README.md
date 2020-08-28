# Easy TypeRacer

The fastest bot to bypass anticheat on https://play.typeracer.com/

![Example of App Running](https://github.com/xWink/Easy-TypeRacer/blob/master/demo/typeracer_demo.png)

### Prerequisites

* Java 11 installed and on system path
  - Windows: https://java.tutorials24x7.com/blog/how-to-install-java-11-on-windows
  - Linux: https://www.javahelps.com/2017/09/install-oracle-jdk-9-on-linux.html
  - Mac: https://installvirtual.com/install-openjdk-11-mac-using-brew/

* Chromedriver downloaded
  1. Check your Chrome version: https://www.whatismybrowser.com/detect/what-version-of-chrome-do-i-have
  2. Download driver for your version: https://chromedriver.chromium.org/downloads
  3. Unzip the downloaded package and keep track of the location of chromedriver.exe

### Setup

1. Download EasyTypeRacer.jar from the [latest release](https://github.com/xWink/Easy-TypeRacer/releases/tag/v1.0).
2. On terminal, execute the jar with `java -jar <path/to/EasyTypeRacer.jar> <path/to/chromedriver.exe> [path/to/chrome/profile/]`
    - The Chrome Profile is optional and will allow you to use your personal Google Chrome settings (including adblocker and other plugins)
    - Example: `java -jar C:/Downloads/EasyTypeRacer.jar C:/Downloads/chromedriver_win32/chromedriver.exe "C:/Users/Wink/AppData/Local/Google/Chrome/User Data"`

A Google Chrome window should open to TypeRacer's website, alongside the application window.

### Play
1. Navigate to any race page ("Enter a typing race", "Practice", or "Race your friends").
2. Press the play button during the countdown.
3. You can pause and resume using the pause and play buttons (you can pause before the race has started to make the bot wait).
4. If you press stop, the bot will completely stop playing in the current race. You can go to another race to play again.
