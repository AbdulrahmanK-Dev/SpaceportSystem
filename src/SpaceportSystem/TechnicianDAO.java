package SpaceportSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TechnicianDAO {

 
    public Technician getTechnicianByName(String name) throws SQLException {
     
        String sql = "SELECT * FROM dbo.People WHERE Name = ? AND Role = 'Technician'";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, name);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    
                    return new Technician(
                        rs.getString("Name"), 
                        rs.getInt("PersonID"), 
                        rs.getString("EmployeeID"), // Column must exist in dbo.People
                        rs.getString("Department"), // Column must exist
                        rs.getDouble("Salary"),     // Column must exist
                        rs.getInt("Age")
                    );
                }
            }
        }
        return null; 
    }

    
    public boolean hireNewTechnician(Technician tech) throws SQLException {
      
        String sql = "INSERT INTO dbo.People (Name, Age, Role, EmployeeID, Department, Salary) " +
                     "VALUES (?, ?, 'Technician', ?, ?, ?)";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, tech.getName());
            ps.setInt(2, tech.getAge());
            ps.setString(3, tech.getEmployeeID());
            ps.setString(4, tech.getDepartment());
            ps.setDouble(5, tech.getSalary());

            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
      
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tech.setPersonID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }
}