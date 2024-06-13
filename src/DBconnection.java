import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public DBconnection() {
        this("jdbc:mysql://localhost:3306/neo", "root", "admin");
    }

    public DBconnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void verifyDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void open() {
        verifyDriver();

        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}