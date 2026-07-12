package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Humne URL ke end mein SSL errors se bachne ke liye parameters add kiye hain
    private static final String URL = "jdbc:mysql://localhost:3306/clinic_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = "root"; 

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Hurray! Connected successfully!");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver missing!");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Connection failed!");
                e.printStackTrace();
            }
        }
        return connection;
    }
}