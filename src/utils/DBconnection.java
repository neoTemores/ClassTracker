package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    // Default values:
    private static final String URL = "jdbc:mysql://localhost:3306/neo";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Instance values:
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public DBconnection() {
        this(URL, USERNAME, PASSWORD);
    }

    public DBconnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void verifyDriver() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            Utils.showTempMsg(e.toString());
        }
    }

    public void open() {
        verifyDriver();

        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void close() {
        try {
            this.connection.close();
        } catch (Exception e) {
            Utils.showTempMsg(e.toString());
        }
    }

}