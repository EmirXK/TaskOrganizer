package com.example.taskorganizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CompletedTasksController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Task> completeTasksTable;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    void aboutInfo(ActionEvent event) {
        Main.showAlert("Version 1.0 made by Miri");
    }

    @FXML
    void closeApp(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void deleteTask(ActionEvent event) throws IOException {
        Task selectedTask = completeTasksTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            Main.showAlert("Please select a task.");
            return;
        }

        String selectedTaskId = selectedTask.getId();
        Scanner scanner = new Scanner(Main.completedTasks);
        StringBuilder file = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            ArrayList<String> splitLine = Main.splitLine(line);
            if (!splitLine.get(0).equals(selectedTaskId)) {
                file.append(line).append("\n");
            }
        }
        scanner.close();

        FileWriter writer = new FileWriter(Main.completedTasks);
        writer.write(file.toString());
        writer.close();
        reloadTasksTable();
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        Stage stage = (Stage)anchorPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("activeTasks.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Active Tasks");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            reloadTasksTable();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void reloadTasksTable() throws FileNotFoundException {
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        Scanner scanner = new Scanner(Main.completedTasks);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("")) break;

            ArrayList<String> splitLine = Main.splitLine(line);
            tasksList.add(
                    new Task(
                            splitLine.get(0),
                            splitLine.get(1),
                            splitLine.get(2)
                    )
            );
        }
        scanner.close();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        completeTasksTable.setItems(tasksList);
    }
}
