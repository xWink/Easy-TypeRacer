package com.wink.etr;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class Racer {

    private static final double range = 0.05; // Variation in time between key presses

    private Thread typer;
    private final Semaphore keyPress;
    private final WebDriver driver;
    private final WebDriverWait shortWait;
    private final WebDriverWait longWait;
    private boolean running;
    private boolean paused;
    private double wpm;

    public Racer(WebDriver driver, double wpm) {
        this.driver = driver;
        this.wpm = wpm;
        keyPress = new Semaphore(1);
        shortWait = new WebDriverWait(driver, 1);
        longWait = new WebDriverWait(driver, 30);
    }

    public void setWpm(double wpm) {
        this.wpm = wpm;
    }

    public void stop() {
        if (!running) {
            return;
        }

        running = false;
        paused = false;
        keyPress.drainPermits();
        keyPress.release();
        if (typer != null) {
            typer.interrupt();
        }
    }

    public void pause() {
        if (running) {
            paused = true;
            keyPress.drainPermits();
        }
    }

    public void race(Runnable callback) {
        if (running) {
            if (paused) {
                paused = false;
                keyPress.release();
            }
            return;
        }

        running = true;

        try {
            // Wait for race page to load
            shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//span[@unselectable='on'])[1]")));
            // Wait for countdown light to load
            shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='lightLabel']")));
        } catch (Exception e) {
            if (callback != null) {
                callback.run();
            }
            return;
        }

        String raceText = getRaceText();
        System.out.println("Race Text: " + raceText);

        CompletableFuture.runAsync(() -> {
            // Wait for text bar to load (green light in race)
            longWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//input[@class='txtInput']")));

            // Type the raceText
            WebElement textBox = driver.findElement(By.xpath("(//input[@class='txtInput'])[1]"));
            typeText(textBox, raceText, callback);
        });
    }

    private String getRaceText() {
        // Wait for page to load
        longWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//span[@unselectable='on']")));

        // Get all raceText elements
        List<WebElement> elements = driver.findElements(By.xpath("//span[@unselectable='on']"));
        if (elements.isEmpty()) {
            System.out.println("No spans found");
            return "";
        }

        // Bypass multi-class raceText storage
        StringBuilder raceText = new StringBuilder();
        for (int i = 0; i < elements.size() - 1; i++) {
            raceText.append(elements.get(i).getText());
        }

        // TypeRacer forgoes a space if the last character of the second last class is not part of a word
        char lastChar = raceText.charAt(raceText.length() - 1);
        if (lastChar != '\'' && lastChar != '$' && lastChar != '('
                && !elements.get(elements.size() - 1).getText().startsWith(",")) {

            raceText.append(" ");
        }

        // Get remaining raceText
        raceText.append(elements.get(elements.size() - 1).getText());
        return raceText.toString();
    }

    // Calculate how long to wait between key inputs
    private int getMsPerChar(String raceText) {
        double numWords = raceText.split(" ").length;
        double numChars = raceText.toCharArray().length;
        double multiplier = 60000;
        if      (wpm >= 390) multiplier = 40000;
        else if (wpm >= 340) multiplier = 43000;
        else if (wpm >= 290) multiplier = 49500;
        else if (wpm >= 240) multiplier = 52000;
        else if (wpm >= 190) multiplier = 56500;
        return (int)(((numWords / numChars) / wpm) * multiplier);
    }

    private void typeText(WebElement textbox, String text, Runnable callback) {
        typer = new Thread(() -> {
            try {
                for (String s : text.split("")) {
                    keyPress.acquire();
                    textbox.sendKeys(s);
                    keyPress.release();
                    int msPerChar = getMsPerChar(text);
                    int delay = new Random().nextInt((int) (msPerChar * range * 2)) + (int) (msPerChar - msPerChar * range);
                    Thread.sleep(delay);
                }

                if (callback != null) {
                    callback.run();
                }

                running = false;
            } catch (InterruptedException ignored) {}
        });

        typer.start();
    }
}
