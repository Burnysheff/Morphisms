package com.example.morphims;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stageActs.*;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stageActs stageActs = new stageActs(stage);
        Scene scene = new Scene(stageActs);
        stage.setScene(scene);
        stage.setTitle("JavaFX Application");
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.show();
        stageActs.entranceWindow();
    }
}