package com.example.taskorganizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    public static File loginInfo = new File("src/main/resources/com/example/taskorganizer/loginInfo.txt");

    public static File activeTasks = new File("src/main/resources/com/example/taskorganizer/activeTasks.txt");

    public static File completedTasks = new File("src/main/resources/com/example/taskorganizer/completedTasks.txt");

    public static File taskIndex = new File("src/main/resources/com/example/taskorganizer/taskIndex.txt");


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Task Organizer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static ArrayList<String> splitLine(String line) {
        ArrayList<String> output = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '~') {
                output.add(temp.toString());
                temp = new StringBuilder();
            }
            else {
                temp.append(line.charAt(i));
            }
        }
        output.add(temp.toString());
        return output;
    }

    public static String getIndex() throws IOException {
        Scanner scanner = new Scanner(taskIndex);
        String index = scanner.nextLine().trim();
        scanner.close();
        FileWriter writer = new FileWriter(taskIndex);
        writer.write(Integer.toString(Integer.parseInt(index)+1));
        writer.close();
        return index;
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
