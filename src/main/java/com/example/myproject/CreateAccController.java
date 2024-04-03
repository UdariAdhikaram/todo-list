package com.example.myproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

/**
 * @author : A.S.M.M.U.P.Adhikaram
 * @Since : 10/22/2023
 * @Time : 10:17 PM
 **/
public class CreateAccController {

    public AnchorPane root1;
    public Label lblID;
    public PasswordField txtConfirmPw;
    public TextField txtUserName;
    public TextField txtEmail;
    public PasswordField txtNewPw;
    public Button btnRegister;
    public Label lblPasswordnotmatch1;
    public Label lblPasswordnotmatch2;

    public void initialize() {
        txtUserName.setDisable(true);
        txtEmail.setDisable(true);
        txtNewPw.setDisable(true);
        txtConfirmPw.setDisable(true);
        btnRegister.setDisable(true);
        lblPasswordnotmatch1.setVisible(false);
        lblPasswordnotmatch2.setVisible(false);
    }

    public void onRegisterButtonClick(ActionEvent actionEvent) throws IOException {

        String newPassword = txtNewPw.getText();
        String confirmPassword = txtConfirmPw.getText();

        if(newPassword.equals(confirmPassword)){
            txtNewPw.setStyle("-fx-border-color: transparent");
            txtConfirmPw.setStyle("-fx-border-color: transparent");
            lblPasswordnotmatch1.setVisible(false);
            lblPasswordnotmatch2.setVisible(false);

            register();
        }
        else {
            txtNewPw.setStyle("-fx-border-color : red");
            txtConfirmPw.setStyle("-fx-border-color: red");

            lblPasswordnotmatch1.setVisible(true);
            lblPasswordnotmatch2.setVisible(true);

            txtNewPw.requestFocus();
        }

    }

    public  void register() {
        String id = lblID.getText();
        String userName = txtUserName.getText();
        String email = txtEmail.getText();
        String password = txtConfirmPw.getText();

        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user values(?,?,?,?)");
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, userName);
            preparedStatement.setObject(3, email);
            preparedStatement.setObject(4, password);

            int i = preparedStatement.executeUpdate();

            if (i != 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Success....").showAndWait();
                Parent parent = FXMLLoader.load(this.getClass().getResource("Login-view.fxml"));
                Scene scene = new Scene(parent);

                Stage primarystage = (Stage) root1.getScene().getWindow();
                primarystage.setScene(scene);
                primarystage.setTitle("Login Form");
                primarystage.centerOnScreen();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Something went wrong....").showAndWait();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void onAddButtonCLick(ActionEvent actionEvent) {
        txtUserName.setDisable(false);
        txtEmail.setDisable(false);
        txtNewPw.setDisable(false);
        txtConfirmPw.setDisable(false);
        btnRegister.setDisable(false);

        autoGenerateID();
    }

    public void autoGenerateID() {
        Connection connection = DBConnection.getConnection();

        try {
            Statement statement;
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select user_id from user order by user_id desc limit 1");// select Query Run

            boolean isExist = resultSet.next();

            if(isExist){
                String oldId = resultSet.getString(1);
                int length = oldId.length();
                String id = oldId.substring(1,length);
                int intId = Integer.parseInt(id);
                intId = intId +1;

                if(intId < 10) {
                    lblID.setText("U00" + intId);
                } else if (intId <100) {
                    lblID.setText("U0" + intId);
                }
                else{
                    lblID.setText("U" + intId);
                }
            }
            else{
                lblID.setText("U001");
            }





        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

}