package SpaceportSystem;

import java.sql.*;
import java.util.Date; // For the Date type

public class EmployeeDAO {

    // --- SQL STATEMENTS ---
    // Note: We MUST include the Role field when inserting into People.
    private static final String INSERT_PEOPLE_SQL = 
        "INSERT INTO People (Name, Age, Role) VALUES (?, ?, ?)";
        
    private static final String INSERT_EMPLOYEE_DETAILS_SQL = 
        "INSERT INTO EmployeeDetails (PersonID, JobTitle, Salary, EmployeeID, Department) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_EMPLOYEE_SQL = 
        "SELECT p.PersonID, p.Name, p.Age, p.Role, e.EmployeeID, e.Department, e.Salary, e.JobTitle " +
        "FROM People p " +
        "INNER JOIN EmployeeDetails e ON p.PersonID = e.PersonID " +
        "WHERE p.PersonID = ?";


    // --- 1. CREATE: Saves an Employee across two tables (Transaction required!) ---
    public void saveEmployee(Employee employee, String role) throws SQLException {
        Connection conn = null;
        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_PEOPLE_SQL, Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setString(1, employee.getName());
                pstmt.setInt(2, employee.getAge());
                pstmt.setString(3, role); 

                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setPersonID(generatedKeys.getInt(1)); // Critical: Update Java object
                    } else {
                        throw new SQLException("Failed to get PersonID, no rows affected.");
                    }
                }
            }

           
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_EMPLOYEE_DETAILS_SQL)) {
                
                pstmt.setInt(1, employee.getPersonID()); // Foreign Key!
                

                pstmt.setString(2, role); 
                pstmt.setDouble(3, employee.getSalary());
                pstmt.setString(4, employee.getEmployeeID());
                pstmt.setString(5, employee.getDepartment());
                
                pstmt.executeUpdate();
            }

            conn.commit(); 
        } catch (SQLException e) {
            System.err.println("Error saving Employee/Technician: " + e.getMessage());
            if (conn != null) conn.rollback(); // Rollback all changes
            throw e; 
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }


    // --- 2. READ: Retrieves an Employee by PersonID, joining both tables ---
    /**
     * Reads a generic Employee object by their PersonID. 
     * NOTE: This method returns a generic Employee. Child DAOs (like TechnicianDAO) 
     * should call this and then cast/validate the type.
     */
    public Employee getEmployeeById(int personID) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_EMPLOYEE_SQL)) {
            
            pstmt.setInt(1, personID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Extract data from the joined tables
                    String name = rs.getString("Name");
                    int age = rs.getInt("Age");
                    String employeeID = rs.getString("EmployeeID");
                    String department = rs.getString("Department");
                    double salary = rs.getDouble("Salary");
               
                    return new Employee(
                        name,
                        personID,
                        employeeID,
                        department,
                        salary,
                        age
             
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Employee by ID: " + e.getMessage());
        }
        return null;
    }
    
    
    
    
    
    
    private static final String UPDATE_PEOPLE_SQL = 
        "UPDATE People SET Name = ?, Age = ?, Role = ? WHERE PersonID = ?";
        
    private static final String UPDATE_EMPLOYEE_DETAILS_SQL = 
        "UPDATE EmployeeDetails SET JobTitle = ?, Salary = ?, EmployeeID = ?, Department = ? WHERE PersonID = ?";


    public boolean updateEmployee(Employee employee, String role) throws SQLException {
        if (employee.getPersonID() <= 0) {
            System.err.println("Error: Cannot update employee without a valid PersonID.");
            return false;
        }

        Connection conn = null;
        boolean success = false;
        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // A. UPDATE the People table
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_PEOPLE_SQL)) {
                pstmt.setString(1, employee.getName());
                pstmt.setInt(2, employee.getAge());
                pstmt.setString(3, role); // Update the Role if necessary
                pstmt.setInt(4, employee.getPersonID());
                pstmt.executeUpdate();
            }

            // B. UPDATE the EmployeeDetails table
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_EMPLOYEE_DETAILS_SQL)) {
                pstmt.setString(1, role); // Assuming Role is the JobTitle
                pstmt.setDouble(2, employee.getSalary());
                pstmt.setString(3, employee.getEmployeeID());
                pstmt.setString(4, employee.getDepartment());
                pstmt.setInt(5, employee.getPersonID()); // WHERE clause
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                }
            }

            conn.commit(); // End Transaction successfully

        } catch (SQLException e) {
            System.err.println("Error updating Employee: " + e.getMessage());
            if (conn != null) conn.rollback(); // Rollback all changes
            throw e; 
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return success;
    }
    
    
    
    private static final String DELETE_EMPLOYEE_DETAILS_SQL = 
        "DELETE FROM EmployeeDetails WHERE PersonID = ?";
    private static final String DELETE_PEOPLE_SQL = 
        "DELETE FROM People WHERE PersonID = ?";


    public boolean deleteEmployee(int personID) throws SQLException {
        if (personID <= 0) return false;

        Connection conn = null;
        boolean success = false;
        try {
            conn = DBManager.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // A. DELETE from EmployeeDetails (CHILD table - MUST GO FIRST)
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_EMPLOYEE_DETAILS_SQL)) {
                pstmt.setInt(1, personID);
                pstmt.executeUpdate(); // It's okay if 0 rows are affected (e.g., if it was only a Passenger)
            }

            // B. DELETE from People (PARENT table - MUST GO SECOND)
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_PEOPLE_SQL)) {
                pstmt.setInt(1, personID);
                if (pstmt.executeUpdate() > 0) {
                    success = true;
                }
            }

            conn.commit(); 

        } catch (SQLException e) {
            System.err.println("Error deleting Employee (Rolling back changes): " + e.getMessage());
            if (conn != null) conn.rollback(); 
            throw e; 
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return success;
    }
    
    
    
    
    
    
    
    
    
    
    
    // UPDATE and DELETE methods would also require transactional logic 
    // to update/delete rows in both People and EmployeeDetails.
}