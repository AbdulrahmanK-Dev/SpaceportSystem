package SpaceportSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ticketDAO {

    private static final String INSERT_SQL =
        "INSERT INTO Tickets (TripID, PassengerID, Price, BookingStatus, SeatNo) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
        "SELECT TicketID, TripID, PassengerID, Price, BookingStatus, SeatNo FROM Tickets WHERE TicketID = ?";

    private static final String SELECT_AVAILABLE_TICKET_SQL =
        "SELECT TOP 1 TicketID, TripID, PassengerID, Price, BookingStatus, SeatNo " +
        "FROM Tickets WHERE TripID = ? AND BookingStatus = 'AVAILABLE' ORDER BY SeatNo ASC";

    private static final String UPDATE_BOOKING_SQL =
        "UPDATE Tickets SET PassengerID = ?, BookingStatus = ? WHERE TicketID = ?";

    private static final String UPDATE_FULL_SQL =
        "UPDATE Tickets SET TripID = ?, PassengerID = ?, Price = ?, BookingStatus = ?, SeatNo = ? WHERE TicketID = ?";

    private static final String DELETE_SQL =
        "DELETE FROM Tickets WHERE TicketID = ?";
    
    private static final String SELECT_BY_TRIP_SQL = "SELECT TicketID, TripID, PassengerID, Price, BookingStatus, SeatNo FROM Tickets WHERE TripID = ?";

    
    private static final String INSERT_TICKET_SQL =
    	    "INSERT INTO Tickets (TripFK, SeatNo, Price, ClassTier, PassengerFK) VALUES (?, ?, ?, ?, NULL)";
    
    private static final String SELECT_AVAILABLE_TICKET_BY_TRIP_AND_CLASS_SQL =
    	    "SELECT TicketID, TripFK, SeatNo, Price, ClassTier, PassengerFK " +
    	    "FROM Tickets WHERE TripFK = ? AND ClassTier = ? AND PassengerFK IS NULL";

    // Helper method, private, maps ResultSet to Ticket object
    private Ticket createTicketFromResultSet(ResultSet rs) throws SQLException {
        BookingStatus status = BookingStatus.valueOf(rs.getString("BookingStatus"));

        int passengerID = rs.getInt("PassengerID");
        if (rs.wasNull()) passengerID = 0;

        return new Ticket(
            rs.getInt("TicketID"),
            rs.getInt("TripID"),
            rs.getDouble("Price"),
            status,
            rs.getString("SeatNo"),
            passengerID
        );
    }

    // Public method to get Ticket by ID, calls createTicketFromResultSet internally
    public Ticket getTicketById(int id) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createTicketFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error reading Ticket by ID: " + e.getMessage());
        }

        return null;
    }

    
    
    
    
    
    public List<Ticket> getTicketsByTripId(int tripId) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_TRIP_SQL)) {

            pstmt.setInt(1, tripId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    
                    BookingStatus status = BookingStatus.valueOf(rs.getString("BookingStatus"));

                    int passengerID = rs.getInt("PassengerID");
                    if (rs.wasNull()) {
                        passengerID = 0; // or -1, depending on your convention
                    }

                    Ticket t = new Ticket(
                            rs.getInt("TicketID"),
                            rs.getInt("TripID"),
                            rs.getDouble("Price"),
                            status,
                            rs.getString("SeatNo"),
                            passengerID
                    );
                    tickets.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading tickets for trip: " + e.getMessage());
        }
        return tickets;
    }

    
    
    
    
    


    public Ticket getAvailableTicketForTrip(int tripID) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_AVAILABLE_TICKET_SQL)) {

            pstmt.setInt(1, tripID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createTicketFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error finding available ticket: " + e.getMessage());
        }

        return null;
    }

    public boolean bookTicket(Ticket ticket) throws SQLException {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOKING_SQL)) {

            if (ticket.getPassengerID() > 0)
                pstmt.setInt(1, ticket.getPassengerID());
            else
                pstmt.setNull(1, Types.INTEGER);

            pstmt.setString(2, BookingStatus.CONFIRMED.toString());

            pstmt.setInt(3, ticket.getTicketID());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error during ticket booking: " + e.getMessage());
            throw e;
        }
    }

    

    public boolean updateTicket(Ticket ticket) {
        if (ticket.getTicketID() <= 0) return false;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_FULL_SQL)) {

            pstmt.setInt(1, ticket.getTripID());

            if (ticket.getPassengerID() > 0)
                pstmt.setInt(2, ticket.getPassengerID());
            else
                pstmt.setNull(2, Types.INTEGER);

            pstmt.setDouble(3, ticket.getPrice());
            pstmt.setString(4, ticket.getStatus().toString());
            pstmt.setString(5, ticket.getSeatNo());
            pstmt.setInt(6, ticket.getTicketID());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating Ticket: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTicket(int id) {
        if (id <= 0) return false;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting Ticket: " + e.getMessage());
            return false;
        }
    }
    
    
    
    public Ticket getAvailableTicketForTripAndClass(int tripID, String classTier) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_AVAILABLE_TICKET_BY_TRIP_AND_CLASS_SQL)) {
            
            pstmt.setInt(1, tripID);
            pstmt.setString(2, classTier); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                 
                    return createTicketFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading available ticket by trip and class: " + e.getMessage());
        }
        return null;
    }
    
    

    public void saveTicket(Ticket ticket) throws SQLException {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_TICKET_SQL)) {
            
            pstmt.setInt(1, ticket.getTripID()); 
            
            // --- CRITICAL FIX HERE: SeatNo is a String ---
            pstmt.setString(2, ticket.getSeatNo()); // Use setString() for SeatNo
            
            pstmt.setDouble(3, ticket.getPrice());
            pstmt.setString(4, ticket.getClassTier()); 
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error saving single ticket: " + e.getMessage());
            throw e;
        }
    }
    
}
