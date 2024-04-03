package com.example.myproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Connection connection = DBConnection.getConnection();

        System.out.println(connection);
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("Login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login Form");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        //DBConnection.connect();
        launch();
        DBConnection.disconnect();
    }
}