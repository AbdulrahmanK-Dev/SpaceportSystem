package SpaceportSystem;

import java.sql.*;

public class PassengerDAO {

    // --- SQL STATEMENTS ---
    // Note: All SELECT statements must include the ContactEmail column now.

    private static final String SELECT_BY_ID_SQL = 
        "SELECT PersonID, Name, Age, Role, ContactEmail FROM People WHERE PersonID = ? AND Role = 'Passenger'"; // FIXED: Added ContactEmail
    private static final String UPDATE_SQL = 
        "UPDATE People SET Name = ?, Age = ? WHERE PersonID = ?";
    private static final String DELETE_SQL = 
        "DELETE FROM People WHERE PersonID = ?";

    private static final String INSERT_SQL =
        "INSERT INTO People (Name, Age, Role, ContactEmail) VALUES (?, ?, 'Passenger', ?)";
    private static final String SELECT_BY_NAME_SQL =
        "SELECT PersonID, Name, Age, Role, ContactEmail FROM People WHERE Name = ? AND Role = 'Passenger'";
    private static final String SELECT_BY_EMAIL_SQL =
        "SELECT PersonID, Name, Age, Role, ContactEmail FROM People WHERE ContactEmail = ? AND Role = 'Passenger'";


    private Passenger createPassengerFromResultSet(ResultSet rs) throws SQLException {
        
        // Ensure the Passenger constructor can handle the role and contactEmail fields
        return new Passenger(
            rs.getInt("PersonID"),
            rs.getString("Name"),
            rs.getInt("Age"),
            rs.getString("Role"),
            rs.getString("ContactEmail")
        );
    }


    /**
     * Retrieves a Passenger by name (deprecated for login, but useful for lookups).
     */
    public Passenger getPassengerByName(String name) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME_SQL)) {
            
            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createPassengerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Passenger by name: " + e.getMessage());
        }
        return null;
    }
    
    // -------------------------------------------------------------------
    // --- CORE CRUD OPERATIONS ---
    // -------------------------------------------------------------------

    /**
     * Saves a new Passenger to the database and retrieves the generated PersonID.
     * FIX: Added executeUpdate, getGeneratedKeys, and robust error logging.
     */
    public void savePassenger(Passenger passenger) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Set Parameters
            pstmt.setString(1, passenger.getName());
            pstmt.setInt(2, passenger.getAge());
            pstmt.setString(3, passenger.getContactEmail());

            // 2. EXECUTE THE INSERT!
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // 3. RETRIEVE THE GENERATED ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // SUCCESS: Set the Passenger object's ID with the DB value
                        passenger.setPersonID(generatedKeys.getInt(1));
                    } else {
                        System.err.println("WARNING: Insert was successful, but could not retrieve the generated ID.");
                    }
                }
            }
        } catch (SQLException e) {
            // 4. LOG THE FULL ERROR! This will stop silent failures.
            System.err.println("FATAL ERROR: Failed to save Passenger to DB. Check SQL and NOT NULL constraints.");
            e.printStackTrace(); 
        }
    }

    /**
     * Reads a Passenger by their database ID.
     */
    public Passenger getPassengerById(int id) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setInt(1, id); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createPassengerFromResultSet(rs); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Passenger by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Updates an existing Passenger's name and age.
     */
    public boolean updatePassenger(Passenger passenger) {
        if (passenger.getPersonID() <= 0) return false;
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            pstmt.setString(1, passenger.getName());
            pstmt.setInt(2, passenger.getAge());
            pstmt.setInt(3, passenger.getPersonID());
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating Passenger: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a Passenger by their ID.
     */
    public boolean deletePassenger(int id) {
        if (id <= 0) return false;
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setInt(1, id); 
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting Passenger: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a Passenger by their contact email (used for login/lookup).
     */
    public Passenger getPassengerByEmail(String email) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_EMAIL_SQL)) {
            
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createPassengerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Passenger by Email: " + e.getMessage());
        }
        return null;
    }
}