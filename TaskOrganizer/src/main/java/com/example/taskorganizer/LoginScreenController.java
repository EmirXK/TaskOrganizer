package com.example.taskorganizer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class LoginScreenController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void login(ActionEvent event) throws IOException {
        if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
            Main.showAlert("Please fill username and password areas.");
        }
        else {
            Scanner scanner = new Scanner(Main.loginInfo);
            String username = scanner.nextLine();
            String password = scanner.nextLine();
            scanner.close();
            if (usernameField.getText().equals(username) && passwordField.getText().equals(password)) {
                // we're logged in
                System.out.println("Login successful");

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("activeTasks.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle("Active Tasks");
                stage.show();
            }
            else {
                Main.showAlert("Incorrect username or password.");
            }
        }
    }

}
