package team_nine.course_scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/team_nine/course_scheduler/MainWindow.fxml"));
        Parent root = loader.load();
        stage.setTitle("Main Window");
        stage.setScene(new Scene(root));
        stage.show();
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/team_nine/course_scheduler/MainWindow.fxml"));
        Parent root = loader.load();
        System.out.println("FXML loaded successfully!");*/


    }

    public static void main(String[] args) {
        launch();
    }
}