package SpaceportSystem; 

import java.sql.*;

public class SpaceportDAO {

    // --- SQL STATEMENTS (Includes coordinate columns) ---
    private static final String INSERT_SQL = 
        "INSERT INTO Spaceports (Name, Location, Latitude, Longitude, Altitude) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = 
        "SELECT SpaceportID, Name, Location, Latitude, Longitude, Altitude FROM Spaceports WHERE SpaceportID = ?";
    private static final String SELECT_BY_NAME_SQL = 
        "SELECT SpaceportID, Name, Location, Latitude, Longitude, Altitude FROM Spaceports WHERE Name = ?";
    private static final String UPDATE_SQL = 
        "UPDATE Spaceports SET Name = ?, Location = ?, Latitude = ?, Longitude = ?, Altitude = ? WHERE SpaceportID = ?";
    private static final String DELETE_SQL = 
        "DELETE FROM Spaceports WHERE SpaceportID = ?";


    // --- UTILITY METHOD: Reconstructs Spaceport Object from Result Set ---
    private Spaceport createSpaceportFromResultSet(ResultSet rs) throws SQLException {
        // Reconstruct the full Location object from the result set columns
        Location location = new Location(
            rs.getString("Location"), // LocationName
            rs.getDouble("Latitude"),
            rs.getDouble("Longitude"),
            rs.getDouble("Altitude")
        );
        
        // Use the Spaceport constructor with all retrieved data
        return new Spaceport(
            rs.getInt("SpaceportID"),
            rs.getString("Name"),
            location 
        );
    }



    /**
     * Checks if the primary spaceport exists in the database. 
     * If not, it creates it using the default coordinates. Always returns the single instance.
     */
    public Spaceport getOrCreatePrimarySpaceport() {
        final String PRIMARY_NAME = "Global Space Operations Center";
        // Define the required coordinates for the initial setup
        final Location DEFAULT_LOCATION = new Location(
            "Global HQ", 28.50, -80.40, 0.005 
        );
        
        // 1. Try to read the primary spaceport
        Spaceport primary = getSpaceportByName(PRIMARY_NAME);

        if (primary != null) {
            System.out.println(" Primary Spaceport FOUND! ID: " + primary.getSpaceportID());
            return primary; 
        } else {
            // 2. If not found, create it
            System.out.println(" Primary Spaceport NOT found. Creating a new record...");
            Spaceport newPrimary = new Spaceport(PRIMARY_NAME, DEFAULT_LOCATION);
            
            saveSpaceport(newPrimary); // Uses the CREATE method
            
            System.out.println(" New Primary Spaceport created with ID: " + newPrimary.getSpaceportID());
            return newPrimary;
        }
    }




    // 1. CREATE: Saves a new Spaceport and retrieves its DB ID
    public void saveSpaceport(Spaceport spaceport) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) { 

            // Parameters 1-5
            pstmt.setString(1, spaceport.getName());
            pstmt.setString(2, spaceport.getLocation().getLocationName());
            pstmt.setDouble(3, spaceport.getLocation().getLatitude());
            pstmt.setDouble(4, spaceport.getLocation().getLongitude());
            pstmt.setDouble(5, spaceport.getLocation().getAltitude());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    spaceport.setSpaceportID(generatedKeys.getInt(1)); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving Spaceport: " + e.getMessage());
        }
    }

    // 2. READ by ID
    public Spaceport getSpaceportById(int id) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setInt(1, id); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createSpaceportFromResultSet(rs); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Spaceport by ID: " + e.getMessage());
        }
        return null;
    }

    // 3. READ by Name (Used by Singleton Logic)
    public Spaceport getSpaceportByName(String name) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME_SQL)) {
            
            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createSpaceportFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Spaceport by name: " + e.getMessage());
        }
        return null;
    }
    
    // 4. UPDATE
    public boolean updateSpaceport(Spaceport spaceport) {
        if (spaceport.getSpaceportID() <= 0) return false;
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            // Parameters 1-5 (New values)
            pstmt.setString(1, spaceport.getName());
            pstmt.setString(2, spaceport.getLocation().getLocationName());
            pstmt.setDouble(3, spaceport.getLocation().getLatitude());
            pstmt.setDouble(4, spaceport.getLocation().getLongitude());
            pstmt.setDouble(5, spaceport.getLocation().getAltitude());
            
            // Parameter 6 (WHERE clause ID)
            pstmt.setInt(6, spaceport.getSpaceportID());
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating Spaceport: " + e.getMessage());
            return false;
        }
    }

    // 5. DELETE
    public boolean deleteSpaceport(int id) {
        if (id <= 0) return false;
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setInt(1, id); 
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting Spaceport: " + e.getMessage());
            return false;
        }
    }
    
    
 // Logic you need inside SpaceportDAO:
    public void saveOrUpdateSpaceport(Spaceport sp) {
        if (sp.getSpaceportID() > 0) {
            // Run SQL UPDATE statement (using WHERE ID = ?)
        } else {
            // Run SQL INSERT statement (for brand new object)
        }
    }

 
  
    
    
    
    
    
}