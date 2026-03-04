
package SpaceportSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBManager {

 
    private static final String SERVER_NAME = "localhost\\SQLEXPRESS";
    private static final int PORT = 1433;
    private static final String DATABASE_NAME = "SpaceportDataCenter"; 
 

    
    private static final String DB_URL = 
        "jdbc:sqlserver://" + SERVER_NAME + 
        ":" + PORT + 
        ";databaseName=" + DATABASE_NAME + 
        ";integratedSecurity=true;trustServerCertificate=true;";
    
    /**
     * 
     * @return 
     * @throws SQLException if the connection fails.
     */
    public static Connection getConnection() throws SQLException {
        // Since we are using integratedSecurity, we only need the URL.
        return DriverManager.getConnection(DB_URL);
    }
    

    /**
     * Test method to verify the connection is successful.
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println(" Connection to SQL Server successful!");
                System.out.println("Database Product Name: " + conn.getMetaData().getDatabaseProductName());
            } else {
                System.out.println(" Failed to establish connection.");
            }
        } catch (SQLException e) {
            System.err.println(" Database Connection Error: " + e.getMessage());
            System.err.println("Please check your DB_URL components and ensure the database exists.");
            e.printStackTrace();
        }
    }
}