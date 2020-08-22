package com.wink.etr;

public class Launcher {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Expected argument: <path to chromedriver.exe>");
            System.exit(-1);
        }

        System.setProperty("webdriver.chrome.driver", args[0]);

        try {
            Gui.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
