package be.webshop.connection;

import be.webshop.util.DatabaseUtils;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        //test connectie DB
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            if (conn != null) {
                System.out.println("Verbinding succesvol!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
