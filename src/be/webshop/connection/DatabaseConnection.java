package be.webshop.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    //private String url = "jdbc:mysql://dt5.ehb.be:3306/2425PROGPROJWT01";
    //private String username = "2425PROGPROJWT01";
    //private String password = "yDHw4Xu";
    private String url;
    private String username;
    private String password;

//    private DatabaseConnection() throws SQLException {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            this.connection = DriverManager.getConnection(url, username, password);
//        } catch (ClassNotFoundException ex) {
//            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
//        }
//    }

    private DatabaseConnection() throws SQLException {
        try {
            // Laad db.properties
            Properties props = new Properties();
            InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties");

            if (input == null) {
                throw new RuntimeException("Kan db.properties niet vinden!");
            }

            props.load(input);

            // Haal waarden op uit properties-bestand
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");

            // Laad JDBC-driver en maak verbinding
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            System.out.println("Database Connection Creation Failed: " + ex.getMessage());
            throw new SQLException(ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }

        return instance;
    }
}
