package SpaceportSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MaintenanceTeamDAO {
   
    
    
    private final PartDAO partDao;
    private final RocketDAO rocketDao; // Dependency

    public MaintenanceTeamDAO(PartDAO partDao, RocketDAO rocketDao) {
        this.partDao = partDao;
        this.rocketDao = rocketDao; // Initialize RocketDAO
    }

  
    public boolean sendRocketToMaintenance(int rocketID, int teamID) {
        
     
        if (!rocketDao.updateRocketStatus(rocketID, "NEEDS_SERVICE")) {
             System.err.println("Failed to update Rocket status.");
             return false;
        }

        // --- Step 2: Insert Log Entry ---
        String insertLogSql = "INSERT INTO dbo.MaintenanceLog (RocketFK, TeamFK, ServiceStatus) VALUES (?, ?, 'Awaiting Service')";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement psLog = conn.prepareStatement(insertLogSql)) {
            
            psLog.setInt(1, rocketID);
            psLog.setInt(2, teamID);
            
            if (psLog.executeUpdate() == 0) {
                // rollback logic omitted for now
                System.err.println("Failed to create maintenance log entry.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Database Error during log creation: " + e.getMessage());
            return false;
        }
    }
    
    

    public String assignServiceToNextRocket() throws SQLException {
   
        return "Service assignment logic executed (Placeholder).";
    }

   
    public List<Part> getGlobalInventory() {
        return partDao.getAllParts();
    }
    
    // Case 4: Add Part to Inventory (Adds to existing stock, or creates new part)
    public boolean addPartToInventory(String partName, int quantity) {
   
        return partDao.updatePartInventory(partName, quantity, false); 
    }

    // Case 5: Update Part Quantity (Sets the quantity to a new specific total)
    public boolean setPartQuantity(String partName, int newQuantity) {
        // Calls the PartDAO method to SET the quantity (isUpdate = true)
        return partDao.updatePartInventory(partName, newQuantity, true);
    }
    
    
  
public String assignServiceToNextRocket(int teamID, int technicianPersonID) throws SQLException {
        
        // We need a Connection here since this method performs database write operations
        // (unlike the PartDAO methods which delegate to the PartDAO's methods which handle connections).
        
        // 1. Find the next 'Awaiting Service' log entry for this team
        String selectSql = "SELECT TOP 1 LogID, RocketFK FROM dbo.MaintenanceLog " +
                           "WHERE TeamFK = ? AND ServiceStatus = 'Awaiting Service'";
        
        // 2. Update the status and assign the technician
        String updateSql = "UPDATE dbo.MaintenanceLog " +
                           "SET ServiceStatus = 'In Progress', AssignedTechnicianFK = ? " +
                           "WHERE LogID = ?";
        
        try (Connection conn = DBManager.getConnection(); // Get connection from DBManager
             PreparedStatement psSelect = conn.prepareStatement(selectSql);
             PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
            
            psSelect.setInt(1, teamID);
            
            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    int logID = rs.getInt("LogID");
                    int rocketFK = rs.getInt("RocketFK");
                    
                    psUpdate.setInt(1, technicianPersonID);
                    psUpdate.setInt(2, logID);
                    
                    if (psUpdate.executeUpdate() > 0) {
                        return "SUCCESS: Assigned Technician (ID: " + technicianPersonID + 
                               ") to Rocket (Log ID: " + logID + ") and set status to 'In Progress'.";
                    }
                } else {
                    return "No rockets are currently awaiting service in this hangar (Team ID: " + teamID + ").";
                }
            }
        }
        return "ERROR: Failed to assign service due to an internal database issue.";
    }
}