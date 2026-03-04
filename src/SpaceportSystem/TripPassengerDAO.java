package SpaceportSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// USED TO GET THE PASSENGER AND THE TRIP AS A WHOLE ADMIN PURPOSE
public class TripPassengerDAO {

    private static final String SELECT_PASSENGERS_BY_TRIP_SQL =
        "SELECT p.PersonID, p.Name, p.Age FROM TripPassengers tp " +
        "JOIN People p ON tp.PersonID = p.PersonID " +
        "WHERE tp.TripID = ?";

    public List<Passenger> getPassengersForTrip(int tripID) {
        List<Passenger> passengers = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_PASSENGERS_BY_TRIP_SQL)) {

            pstmt.setInt(1, tripID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Passenger p = new Passenger(
                        rs.getInt("PersonID"),
                        rs.getString("Name"),
                        rs.getInt("Age")
                    );
                    passengers.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading passengers for trip: " + e.getMessage());
        }
        return passengers;
    }

    // OPTIONAL: add methods to add/remove passengers from TripPassengers table
}
