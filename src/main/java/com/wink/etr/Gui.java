package com.wink.etr;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class Gui extends Application {

    private static final double initialWpm = 125;

    private Button stopButton;
    private Button playButton;
    private Button pauseButton;
    private Controller controller;

    public static void show() {
        launch();
    }

    @Override
    public void start(Stage stage) {
        controller = new Controller(initialWpm, this);
        VBox root = new VBox(25);
        root.setId("root");
        root.getChildren().addAll(getFirstRow(), getSecondRow(), getThirdRow());

        Scene scene = new Scene(root, 530, 245);
        scene.getStylesheets().add("styles.css");

        stage.setScene(scene);
        stage.setTitle("Easy TypeRacer");
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> controller.quit());
        stage.show();
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    private HBox getFirstRow() {
        return new HBox(getSpacer(), getPlayButtonLabel(), getSpacer());
    }

    private HBox getSecondRow() {
        stopButton = getSimpleButton("stop.png", actionEvent -> controller.stop());
        stopButton.setDisable(true);
        playButton = getSimpleButton("play.png", actionEvent -> controller.run());
        pauseButton = getSimpleButton("pause.png", actionEvent -> controller.pause());
        pauseButton.setDisable(true);
        return new HBox(25, getSpacer(), stopButton, playButton, pauseButton, getSpacer());
    }

    private HBox getThirdRow() {
        Label wpmLabel = new Label((int) initialWpm + " WPM");
        wpmLabel.setId("wpmLabel");
        HBox hBox = new HBox(12, getSpacer(), getWpmSlider(wpmLabel), wpmLabel, getSpacer());
        hBox.setFillHeight(true);
        hBox.setAlignment(Pos.CENTER_LEFT);
        return hBox;
    }

    private Label getPlayButtonLabel() {
        Label label = new Label("Press PLAY when race countdown starts");
        label.setPadding(new Insets(12, 0, 0, 0));
        label.setId("playLabel");
        return label;
    }

    private Button getSimpleButton(String fileName, EventHandler<ActionEvent> handler) {
        ImageView imageView = getImageView(fileName);
        Button button = new Button("", imageView);
        button.setOnAction(handler);
        return button;
    }

    private ImageView getImageView(String fileName) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        InputStream stream;
        if ((stream = loader.getResourceAsStream(fileName)) != null) {
            ImageView imageView = new ImageView(new Image(stream));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            return imageView;
        }
        return null;
    }

    private Slider getWpmSlider(Label wpmLabel) {
        Slider slider = new Slider(30, 400, initialWpm);
        slider.valueProperty().addListener((observableValue, oldNum, newNum) -> {
            controller.setWpm(newNum.doubleValue());
            wpmLabel.setText((int) newNum.doubleValue() + " WPM");
        });
        HBox.setHgrow(slider, Priority.ALWAYS);
        return slider;
    }

    private HBox getSpacer() {
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
}
