package SpaceportSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaunchPadDAO {
    
    // --- SQL STATEMENTS ---
    private static final String INSERT_SQL = 
        "INSERT INTO LaunchPads (PadNo, Status, AssignedRocketID, SpaceportID) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = 
        "SELECT LaunchPadID, PadNo, Status, AssignedRocketID, SpaceportID FROM LaunchPads WHERE LaunchPadID = ?";
    private static final String SELECT_ALL_SQL = 
        "SELECT LaunchPadID, PadNo, Status, AssignedRocketID, SpaceportID FROM LaunchPads";
    private static final String UPDATE_SQL = 
        "UPDATE LaunchPads SET PadNo = ?, Status = ?, AssignedRocketID = ?, SpaceportID = ? WHERE LaunchPadID = ?";
    private static final String DELETE_SQL = 
        "DELETE FROM LaunchPads WHERE LaunchPadID = ?";
    
    // --- UTILITY: Reconstructs LaunchPad Object from Result Set ---
    private LaunchPad createLaunchPadFromResultSet(ResultSet rs) throws SQLException {
        PadStatus status = PadStatus.valueOf(rs.getString("Status"));
        
        // AssignedRocketID is nullable (INT), so we check for null/0
        int assignedRocketID = rs.getInt("AssignedRocketID");
        if (rs.wasNull()) { assignedRocketID = 0; } 
        
        return new LaunchPad(
            rs.getInt("LaunchPadID"),
            rs.getString("PadNo"),
            status,
            assignedRocketID,
            rs.getInt("SpaceportID")
        );
    }
    
    // -------------------------------------------------------------------
    // --- CRUD OPERATIONS ---
    // -------------------------------------------------------------------

    // 1. CREATE
    public void saveLaunchPad(LaunchPad pad) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) { 

            pstmt.setString(1, pad.getPadNo());
            pstmt.setString(2, pad.getStatus().toString());
            // AssignedRocketID is often 0 or NULL initially
            pstmt.setInt(3, pad.getAssignedRocketID()); 
            pstmt.setInt(4, pad.getSpaceportID());

            
            if (pad.getAssignedRocketID() > 0) {
                pstmt.setInt(3, pad.getAssignedRocketID());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER); // Send NULL instead of 0
            }
            
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pad.setLaunchPadID(generatedKeys.getInt(1)); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving LaunchPad: " + e.getMessage());
        }
    }

    // 2. READ by ID (Used to get an FK or verify existence)
    public LaunchPad getLaunchPadById(int id) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setInt(1, id); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createLaunchPadFromResultSet(rs); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading LaunchPad by ID: " + e.getMessage());
        }
        return null;
    }

    // 3. READ ALL 
    public List<LaunchPad> getAllLaunchPads() {
        List<LaunchPad> pads = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pstmt.executeQuery()) { 

            while (rs.next()) {
                pads.add(createLaunchPadFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error reading all LaunchPads: " + e.getMessage());
        }
        return pads;
    }
    
    // 4. UPDATE (Used for changing status or assigning a Rocket)
    public boolean updateLaunchPad(LaunchPad pad) {
        if (pad.getLaunchPadID() <= 0) return false;
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            pstmt.setString(1, pad.getPadNo());
            pstmt.setString(2, pad.getStatus().toString());
            pstmt.setInt(3, pad.getAssignedRocketID());
            pstmt.setInt(4, pad.getSpaceportID());
            pstmt.setInt(5, pad.getLaunchPadID()); // WHERE clause
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating LaunchPad: " + e.getMessage());
            return false;
        }
    }
    
    // 5. DELETE
    public boolean deleteLaunchPad(int id) {
        if (id <= 0) return false;
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setInt(1, id); 
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting LaunchPad: " + e.getMessage());
            return false;
        }
    }
}