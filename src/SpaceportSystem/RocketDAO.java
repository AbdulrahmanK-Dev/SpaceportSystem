package SpaceportSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RocketDAO {

    // --- SQL STATEMENTS ---
    private static final String INSERT_SQL =
        "INSERT INTO Rockets (RegistrationID, RocketStatus, EngineType, MaxPayload, Capacity, CurrentSpaceportID) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
        "SELECT RocketID, RegistrationID, RocketStatus, EngineType, MaxPayload, Capacity, CurrentSpaceportID FROM Rockets WHERE RocketID = ?";
    private static final String SELECT_ALL_AVAILABLE_SQL =
        "SELECT RocketID, RegistrationID, RocketStatus, EngineType, MaxPayload, Capacity, CurrentSpaceportID FROM Rockets WHERE RocketStatus = 'AVAILABLE' OR RocketStatus = 'READY'";
    // Select all rockets, regardless of status
    private static final String SELECT_ALL_SQL = 
        "SELECT RocketID, RegistrationID, RocketStatus, EngineType, MaxPayload, Capacity, CurrentSpaceportID FROM Rockets";
    private static final String UPDATE_SQL =
        "UPDATE Rockets SET RegistrationID = ?, RocketStatus = ?, EngineType = ?, MaxPayload = ?, Capacity = ?, CurrentSpaceportID = ? WHERE RocketID = ?";
    private static final String DELETE_SQL =
        "DELETE FROM Rockets WHERE RocketID = ?";


    private Rocket createRocketFromResultSet(ResultSet rs) throws SQLException {
        RocketStatus status = RocketStatus.valueOf(rs.getString("RocketStatus"));
        
        // 1. Create the Rocket object using the LOADED constructor
        Rocket loadedRocket = new Rocket(
            rs.getInt("RocketID"),
            rs.getInt("CurrentSpaceportID"),
            rs.getString("RegistrationID"),
            status,
            rs.getString("EngineType"),
            rs.getDouble("MaxPayload"),
            rs.getInt("Capacity")
        );
        

        loadedRocket.setEngines(new Engine());         // Default Engine
        loadedRocket.AssignFuelTank(new FuelTank(800)); // Default FuelTank
        
        
        return loadedRocket;
    }


    // 1. CREATE: Saves a new Rocket and retrieves its DB ID
    public void saveRocket(Rocket rocket) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) { 

            // Parameters 1-6
            pstmt.setString(1, rocket.getRegistrationID());
            pstmt.setString(2, rocket.getStatus().toString());
            pstmt.setString(3, rocket.getEngineType());
            pstmt.setDouble(4, rocket.getMaxPayload());
            pstmt.setInt(5, rocket.getPassengerCapacity()); // Uses Capacity column
            pstmt.setInt(6, rocket.getSpaceportID());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rocket.setRocketID(generatedKeys.getInt(1)); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving Rocket: " + e.getMessage());
        }
    }

    // 2. READ by ID (Admin/Trip use to verify Rocket existence)
    public Rocket getRocketById(int id) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setInt(1, id); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createRocketFromResultSet(rs); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Rocket by ID: " + e.getMessage());
        }
        return null;
    }

    // 3. READ ALL AVAILABLE (For Admin/Trip creation)
    public List<Rocket> getAllAvailableRockets() {
        List<Rocket> rockets = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_AVAILABLE_SQL);
             ResultSet rs = pstmt.executeQuery()) { 

            while (rs.next()) {
                rockets.add(createRocketFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error reading all available rockets: " + e.getMessage());
        }
        return rockets;
    }
    
    // READ ALL ROCKETS (Regardless of Status)
    public List<Rocket> getAllRockets() {
        List<Rocket> rockets = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pstmt.executeQuery()) { 

            while (rs.next()) {
                rockets.add(createRocketFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error reading all rockets: " + e.getMessage());
        }
        return rockets;
    }
    
    // 4. UPDATE 
    public boolean updateRocket(Rocket rocket) {
        if (rocket.getRocketID() <= 0) return false;
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            pstmt.setString(1, rocket.getRegistrationID());
            pstmt.setString(2, rocket.getStatus().toString());
            pstmt.setString(3, rocket.getEngineType());
            pstmt.setDouble(4, rocket.getMaxPayload());
            pstmt.setInt(5, rocket.getPassengerCapacity());
            pstmt.setInt(6, rocket.getSpaceportID());
            pstmt.setInt(7, rocket.getRocketID()); // WHERE clause
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating Rocket: " + e.getMessage());
            return false;
        }
    }

    // 5. DELETE
    public boolean deleteRocket(int id) {
        if (id <= 0) return false;
        
        try (Connection conn = DBManager.getConnection(); // Fixed connection reference
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setInt(1, id); 
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting Rocket: " + e.getMessage());
            return false;
        }
    }
    

    public List<Rocket> getRocketsInMaintenanceByTeam(int teamID) {
        List<Rocket> rocketsInService = new ArrayList<>();
        

        String sql = "SELECT R.* FROM dbo.Rockets R " +
                     "JOIN dbo.MaintenanceLog L ON R.RocketID = L.RocketFK " +
                     "WHERE L.TeamFK = ? AND L.ServiceStatus IN ('Awaiting Service', 'In Progress')";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teamID);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Reuses your existing createRocketFromResultSet helper method
                    rocketsInService.add(createRocketFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading rockets in maintenance: " + e.getMessage());
        }
        return rocketsInService;
    }
    
    
    public boolean updateRocketStatus(int rocketID, String newStatus) {
        String sql = "UPDATE dbo.Rockets SET RocketStatus = ? WHERE RocketID = ?";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, rocketID);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating Rocket status: " + e.getMessage());
            return false;
        }
    }
}