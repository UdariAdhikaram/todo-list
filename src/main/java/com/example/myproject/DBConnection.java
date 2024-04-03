package com.example.myproject;

import java.sql.*;

/**
 * @author : A.S.M.M.U.P.Adhikaram
 * @Since : 10/24/2023
 * @Time : 5:15 PM
 **/
public class DBConnection {
    public static void main(String[] args) {
        connect();
    }
private  static  DBConnection dbConnection;
    private static Connection connection;

    static {
        connect();
    }

    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/todolist",
                    "root", "");
            System.out.println("Connection successful.");
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public static void disconnect() throws SQLException {
        connection.close();

    }

    public static void executeStatement(String sql) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet;
        resultSet = statement.executeQuery(sql);
        System.out.println(resultSet);
        resultSet.close();
        statement.close();
    }
    public static Connection getConnection(){
        return connection;
    }


}
