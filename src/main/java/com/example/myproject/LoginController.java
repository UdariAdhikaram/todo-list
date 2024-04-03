package com.example.myproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    public AnchorPane root;
    public PasswordField password;
    public TextField username;

    public  static String enteredID;
    public static String enteredUserName;

    public void initialize(){
        username.requestFocus();
    }
    public void onCreateMouseClick(MouseEvent mouseEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("CreateAcc.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage primarystage = (Stage) root.getScene().getWindow();
        primarystage.setScene(scene);
        primarystage.setTitle("Create New Account");
        primarystage.centerOnScreen();

    }

    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException {

        String UserName = username.getText();
        String PassWord = password.getText();

        Connection connection = DBConnection.getConnection();


        try{

            PreparedStatement preparedStatement = connection.prepareStatement("select *from user where user_name = ? and password = ?");
            preparedStatement.setObject(1,UserName);
            preparedStatement.setObject(2,PassWord);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                enteredID = resultSet.getString(1);
                enteredUserName = UserName;

                FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("Welcome-view.fxml"));
                Scene scene = new Scene(fxmlLoader.<Parent>load());

                Stage primarystage = (Stage) root.getScene().getWindow();
                primarystage.setScene(scene);
                primarystage.setTitle("Welcome to To Do List");
                primarystage.centerOnScreen();
            }
            else {
                new Alert(Alert.AlertType.ERROR,"Invalid UserName or Password").showAndWait();
                username.clear();
                password.clear();

                username.requestFocus();
            }
        }
        catch(SQLException e){
                e.printStackTrace();
        }

    }
}
