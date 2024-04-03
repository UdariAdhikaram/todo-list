package com.example.myproject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

/**
 * @author : A.S.M.M.U.P.Adhikaram
 * @Since : 10/22/2023
 * @Time : 10:17 PM
 **/
public class WelcomeController {
    public Pane taskbar;
    public Label lblId;
    public Label lblTitle;
    public AnchorPane root;
    public Pane subroot;
    public TextField txtToDo;
    public ListView<TodoTM> lsttodos;
    public Button btnDetele;
    public Button btnUpdate;
    public TextField txtselectedTodo;

    String id;


    public void initialize() {
        lblTitle.setText("Hello " + LoginController.enteredUserName + " Welcome to To-Do List");
        lblId.setText(LoginController.enteredID);
        subroot.setVisible(false);

        btnUpdate.setDisable(true);
        btnDetele.setDisable(true);
        txtselectedTodo.setDisable(true);

        loadList();

        lsttodos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoTM>() {
            @Override
            public void changed(ObservableValue<? extends TodoTM> observableValue, TodoTM oldValue, TodoTM newValue) {
                btnUpdate.setDisable(false);
                btnDetele.setDisable(false);
                txtselectedTodo.setDisable(false);
                subroot.setVisible(false);

                TodoTM selectedItem = lsttodos.getSelectionModel().getSelectedItem();

                if(selectedItem == null){
                    return;
                }
                txtselectedTodo.setText(selectedItem.getDescription());

                id = selectedItem.getId();
            }
        });
    }

    public void onAddNewToDoButtonClick(ActionEvent actionEvent) throws IOException {
        subroot.setVisible(true);

        btnUpdate.setDisable(true);
        btnDetele.setDisable(true);
        txtselectedTodo.setDisable(true);

       lsttodos.getSelectionModel().clearSelection();
    }

    public void onLogoutButtonClick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to Log Out?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("Login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage primarystage = (Stage) root.getScene().getWindow();
            primarystage.setScene(scene);
            primarystage.setTitle("Login Form");
            primarystage.centerOnScreen();

        }
    }

    public void onAddToListButtonClick(ActionEvent actionEvent) {
        String id = autoGenerateID();
        String description = txtToDo.getText();
        String user_id = lblId.getText();

        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement("insert into todo values (?,?,?)");

            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, description);
            preparedStatement.setObject(3, user_id);

            int i = preparedStatement.executeUpdate();
            System.out.println(i);

            txtToDo.clear();
            subroot.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadList();
    }

    public String autoGenerateID(){
            Connection connection = DBConnection.getConnection();

            try {
                Statement statement;
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select id from todo order by id desc limit 1");// select Query Run
                boolean isExist = resultSet.next();
                if(isExist){
                    String oldId = resultSet.getString(1);
                    int length = oldId.length();
                    String id = oldId.substring(1,length);
                    int intId = Integer.parseInt(id);
                    intId = intId +1;

                    if(intId < 10) {
                        return "T00" + intId;
                    } else if (intId <100) {
                        return "T0" + intId;
                    }
                    else{return "T" + intId;
                    }
                }
                else {
                    return "T001";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void loadList(){
            ObservableList<TodoTM> todos = lsttodos.getItems();
            todos.clear();

            Connection connection = DBConnection.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select *from todo where user_id = ?");
                preparedStatement.setObject(1,LoginController.enteredID);// or lblID.gettext()
                ResultSet resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                   String id = resultSet.getString(1);
                   String description = resultSet.getString(2);
                   String user_id = resultSet.getString(3);

                   TodoTM object = new TodoTM(id, description, user_id);
                   todos.add(object);
               }
            lsttodos.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    public void onDeleteButtonClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this todo?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES)){
            Connection connection = DBConnection.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from todo where id = ?");
                preparedStatement.setObject(1, id);
                preparedStatement.executeUpdate();
                loadList();;
                txtselectedTodo.clear();;
                btnDetele.setDisable(true);
                btnUpdate.setDisable(true);
                txtselectedTodo.setDisable(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void onUpdateButtonClick(ActionEvent actionEvent) {

            Connection connection = DBConnection.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("update todo set description = ? where id = ?");
                preparedStatement.setObject(1,txtselectedTodo.getText());
                preparedStatement.setObject(2,id);
                preparedStatement.executeUpdate();
                loadList();;
                txtselectedTodo.clear();;
                btnDetele.setDisable(true);
                btnUpdate.setDisable(true);
                txtselectedTodo.setDisable(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
