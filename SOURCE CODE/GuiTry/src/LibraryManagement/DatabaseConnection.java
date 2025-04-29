package LibraryManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Update these with your actual SQL Server credentials
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=LibraryManagement;encrypt=false;trustServerCertificate=true";
    private static final String USERNAME = "ZyraJ"; // TODO: Replace with your SQL Server username
    private static final String PASSWORD = "conchie21"; // TODO: Replace with your SQL Server password

    public static Connection getConnection() throws SQLException {
        try {
            // Ensure SQL Server JDBC driver is available
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server JDBC Driver not found. Please add the JDBC driver to your classpath.", e);
        }
    }
}





