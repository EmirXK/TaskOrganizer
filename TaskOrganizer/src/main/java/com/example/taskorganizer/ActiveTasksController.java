package com.example.taskorganizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ActiveTasksController implements Initializable {

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TextField nameTextField;

    @FXML
    private TableView<Task> tasksTable;

    @FXML
    void aboutInfo() {
        Main.showAlert("Version 1.0 made by Miri");
    }

    @FXML
    void closeApp() {
        System.exit(0);
    }

    @FXML
    void addNewTask() throws IOException {
        String taskName = nameTextField.getText();
        String taskDescription = descriptionTextField.getText();

        if (issuesDetected(taskName,taskDescription)) {
            // pass
            System.out.println("Issues detected.");
        }
        else {
            String data = Main.getIndex()+"~"+taskName+"~"+taskDescription+"\n";
            FileWriter writer = new FileWriter(Main.activeTasks,true);
            writer.write(data);
            writer.close();
            reloadTaskTable();
            descriptionTextField.setText("");
            nameTextField.setText("");
        }
    }

    @FXML
    void deleteTask() throws IOException {
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            Main.showAlert("Please select a task.");
            return;
        }

        String selectedTaskId = selectedTask.getId();
        Scanner scanner = new Scanner(Main.activeTasks);
        StringBuilder file = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            ArrayList<String> splitLine = Main.splitLine(line);
            if (!splitLine.get(0).equals(selectedTaskId)) {
                file.append(line).append("\n");
            }
        }
        scanner.close();

        FileWriter writer = new FileWriter(Main.activeTasks);
        writer.write(file.toString());
        writer.close();
        reloadTaskTable();
    }

    @FXML
    void updateTask() throws IOException {
        Task selectedTask = tasksTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            Main.showAlert("Please select a task.");
            return;
        }

        String selectedTaskId = selectedTask.getId();

        Scanner scanner = new Scanner(Main.activeTasks);

        StringBuilder file = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            ArrayList<String> splitLine = Main.splitLine(line);
            if (!splitLine.get(0).equals(selectedTaskId)) {
                file.append(line).append("\n");
            }
            else {
                String taskName = nameTextField.getText();
                String taskDescription = descriptionTextField.getText();

                if (taskName.equals("") && taskDescription.equals("")) {
                    Main.showAlert("Please fill out at least one field to update.");
                    return;
                }

                if (taskName.equals("")) {
                    taskName = splitLine.get(1);
                }
                if (taskDescription.equals("")) {
                    taskDescription = splitLine.get(2);
                }

                String updatedLine = splitLine.get(0)+"~"+taskName+"~"+taskDescription+"\n";
                file.append(updatedLine);
            }
        }
        scanner.close();

        FileWriter writer = new FileWriter(Main.activeTasks);
        writer.write(file.toString());
        writer.close();
        reloadTaskTable();
        nameTextField.setText("");
        descriptionTextField.setText("");
    }

    @FXML
    void setAsComplete() throws IOException {
        Task task = tasksTable.getSelectionModel().getSelectedItem();

        if (task == null) {
            Main.showAlert("Please select a task.");
            return;
        }

        FileWriter writer = new FileWriter(Main.completedTasks,true);
        String line = task.getId()+"~"+task.getName()+"~"+task.getDescription()+"\n";
        writer.write(line);
        writer.close();
        deleteTask();
    }

    @FXML
    void viewCompleteTasks(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("completedTasks.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Completed Tasks");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            reloadTaskTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reloadTaskTable() throws IOException {
        ObservableList<Task> tasksList = FXCollections.observableArrayList();
        Scanner scanner = new Scanner(Main.activeTasks);
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

        tasksTable.setItems(tasksList);
    }

    private boolean issuesDetected(String taskName, String taskDescription) {
        if (taskName.equals("") || taskDescription.equals("")) {
            Main.showAlert("Please fill out all the required areas.");
            return true;
        }
        else if (containsLetter(taskName) || containsLetter(taskDescription)) {
            Main.showAlert("Your input cannot use the character \"~\"");
            return true;
        }
        return false;
    }

    private boolean containsLetter(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '~') return true;
        }
        return false;
    }
}
