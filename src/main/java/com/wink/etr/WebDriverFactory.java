package com.wink.etr;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverFactory {

    private static String chromeProfilePath = null;

    public static WebDriver create() {
        WebDriver driver;
        if (chromeProfilePath != null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("user-data-dir=" + chromeProfilePath);
            driver = new ChromeDriver(options);
        } else {
            driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        return driver;
    }

    public static void setChromeProfilePath(String string) {
        chromeProfilePath = string;
    }
}
