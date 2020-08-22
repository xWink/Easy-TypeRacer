package com.wink.etr;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Controller {

    private Gui gui;
    private Racer racer;
    private WebDriver driver;

    public Controller(double wpm, Gui gui) {
        this.gui = gui;
        driver = new ChromeDriver();
        driver.get("https://play.typeracer.com/");
        racer = new Racer(driver, wpm);
    }

    public void run() {
        gui.getStopButton().setDisable(false);
        gui.getPlayButton().setDisable(true);
        gui.getPauseButton().setDisable(false);
        racer.race(() -> {
            gui.getStopButton().setDisable(true);
            gui.getPlayButton().setDisable(false);
            gui.getPauseButton().setDisable(true);
        });
    }

    public void stop() {
        gui.getStopButton().setDisable(true);
        gui.getPlayButton().setDisable(false);
        gui.getPauseButton().setDisable(true);
        racer.stop();
    }

    public void pause() {
        gui.getStopButton().setDisable(false);
        gui.getPlayButton().setDisable(false);
        gui.getPauseButton().setDisable(true);
        racer.pause();
    }

    public void quit() {
        racer.stop();
        driver.quit();
        System.exit(0);
    }

    public void setWpm(double wpm) {
        racer.setWpm(wpm);
    }
}
