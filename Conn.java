package university.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conn {
    Connection connection;
    public Statement statement;

    Conn() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the MySQL database (replace host, port, and database_name with your actual values)
            String url = "jdbc:mysql://localhost:3306/UniversityManagement";
            String username = "Ayush"12;
            String password = "Ayush12345";
            connection = DriverManager.getConnection(url, username, password);

            // Create a statement object
            statement = connection.createStatement();

            System.out.println("Connected to the database successfully with Universitymanagement!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Conn();
    }
}
