package SpaceportSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class tripDAO { 

    // First case of dependancy injection!!!
    private final RocketDAO rocketDAO;
    private final LaunchPadDAO launchPadDAO;
    
    public tripDAO(RocketDAO rocketDAO, LaunchPadDAO launchPadDAO) {
        this.rocketDAO = rocketDAO;
        this.launchPadDAO = launchPadDAO;
    }

 // --- SQL STATEMENTS ---
    private static final String INSERT_SQL = 
        "INSERT INTO Trips (TripNo, Status, DepartureTime, DestinationLocation, LaunchPadID, RocketID) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = 
        "SELECT TripID, TripNo, Status, DepartureTime, DestinationLocation, LaunchPadID, RocketID FROM Trips WHERE TripID = ?";
    private static final String SELECT_ALL_SCHEDULED_SQL = 
        "SELECT TripID, TripNo, Status, DepartureTime, DestinationLocation, LaunchPadID, RocketID FROM Trips WHERE Status = 'SCHEDULED' OR Status = 'BOARDING' ORDER BY DepartureTime ASC";
    private static final String UPDATE_SQL = 
        "UPDATE Trips SET TripNo = ?, Status = ?, DepartureTime = ?, DestinationLocation = ?, LaunchPadID = ?, RocketID = ? WHERE TripID = ?";
    private static final String DELETE_SQL = 
        "DELETE FROM Trips WHERE TripID = ?";


    private Trip createTripFromResultSet(ResultSet rs) throws SQLException {
        // Use proper enum capitalization if necessary (assuming your enum is tripStatus)
        tripStatus status = tripStatus.valueOf(rs.getString("Status"));
        // java.sql.Timestamp converts directly to java.util.Date
        Date departureTime = new Date(rs.getTimestamp("DepartureTime").getTime());

        int rocketID = rs.getInt("RocketID");
        int launchPadID = rs.getInt("LaunchPadID");

        // 1. Create the base Trip object
        Trip trip = new Trip(
            rs.getInt("TripID"),
            rs.getString("TripNo"),
            status,
            departureTime,
            rs.getString("DestinationLocation"),
            rocketID,     
            launchPadID
        );

        if (rocketID > 0) {
            trip.setAssignedRocket(rocketDAO.getRocketById(rocketID));
        }
        if (launchPadID > 0) {
            trip.setAssignedLaunchPad(launchPadDAO.getLaunchPadById(launchPadID));
        }
        
        return trip;
    }

    
    
    
    
    
    
    
    
    
    
    // 1. CREATE: Saves a new Trip and retrieves its DB ID (Admin Use)
    public void saveTrip(Trip trip) {
        // Assume DBConnectionManager is the correct class name
        try (Connection conn = DBManager.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) { 

            // Parameters 1-6
            pstmt.setString(1, trip.getTripNo());
            pstmt.setString(2, trip.getStatus().toString());
            pstmt.setTimestamp(3, new Timestamp(trip.getDepartureTime().getTime())); 
            pstmt.setString(4, trip.getDestinationLocation());
            pstmt.setInt(5, trip.getLaunchPadID()); 
            pstmt.setInt(6, trip.getRocketID());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trip.setTripID(generatedKeys.getInt(1)); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving Trip: " + e.getMessage());
        }
    }

    // 2. READ by ID (Admin Use)
    public Trip getTripById(int id) {
        try (Connection conn = DBManager.getConnection(); // Assume DBConnectionManager
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setInt(1, id); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createTripFromResultSet(rs); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Trip by ID: " + e.getMessage());
        }
        return null;
    }

    // 3. READ ALL SCHEDULED (Passenger Use)
    public List<Trip> getAllScheduledTrips() {
        List<Trip> trips = new ArrayList<>();
        try (Connection conn = DBManager.getConnection(); // Assume DBConnectionManager
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SCHEDULED_SQL);
             ResultSet rs = pstmt.executeQuery()) { 

            while (rs.next()) {
                trips.add(createTripFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error reading all scheduled trips: " + e.getMessage());
        }
        return trips;
    }
    
    // 4. UPDATE (Admin Use)
    public boolean updateTrip(Trip trip) {
        if (trip.getTripID() <= 0) return false;
        
        try (Connection conn = DBManager.getConnection(); // Assume DBConnectionManager
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            pstmt.setString(1, trip.getTripNo());
            pstmt.setString(2, trip.getStatus().toString());
            pstmt.setTimestamp(3, new Timestamp(trip.getDepartureTime().getTime()));
            pstmt.setString(4, trip.getDestinationLocation());
            pstmt.setInt(5, trip.getLaunchPadID());
            pstmt.setInt(6, trip.getRocketID());
            pstmt.setInt(7, trip.getTripID());
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating Trip: " + e.getMessage());
            return false;
        }
    }

    // 5. DELETE (Admin Use)
    public boolean deleteTrip(int id) {
        if (id <= 0) return false;
        
        try (Connection conn = DBManager.getConnection(); // Assume DBConnectionManager
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setInt(1, id); 
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting Trip: " + e.getMessage());
            return false;
        }
    }
}