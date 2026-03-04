package SpaceportSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartDAO {

    private static final String INSERT_SQL =
        "INSERT INTO Parts (Name, CatalogID, CurrentQuantity) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
        "SELECT PartID, Name, CatalogID, CurrentQuantity FROM Parts WHERE PartID = ?";
    private static final String SELECT_ALL_SQL =
        "SELECT PartID, Name, CatalogID, CurrentQuantity FROM Parts";
    private static final String UPDATE_SQL =
        "UPDATE Parts SET Name = ?, CatalogID = ?, CurrentQuantity = ? WHERE PartID = ?";
    private static final String DELETE_SQL =
        "DELETE FROM Parts WHERE PartID = ?";


    private Part createPartFromResultSet(ResultSet rs) throws SQLException {
        return new Part(
            rs.getInt("PartID"),
            rs.getString("Name"),
            rs.getString("CatalogID"),
            rs.getInt("CurrentQuantity")
        );
    }

    // 1. CREATE: Saves a new Part and retrieves its DB ID
    public void savePart(Part part) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, part.getName());
            pstmt.setString(2, part.getCatalogID());
            pstmt.setInt(3, part.getQuantity());

            pstmt.executeUpdate();

            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    part.setPartID(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            // Changed to print to err to see the failure
            System.err.println("Error saving Part: " + e.getMessage()); 
        }
    }

    
    public Part getPartById(int id) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createPartFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Part by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Part> getAllParts() {
        List<Part> parts = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                parts.add(createPartFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error reading all Parts: " + e.getMessage());
        }
        return parts;
    }

    public boolean updatePart(Part part) {
        if (part.getPartID() <= 0) return false;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            pstmt.setString(1, part.getName());
            pstmt.setString(2, part.getCatalogID());
            pstmt.setInt(3, part.getQuantity());
            pstmt.setInt(4, part.getPartID());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating Part: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePart(int id) {
        if (id <= 0) return false;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting Part: " + e.getMessage());
            return false;
        }
    }
    
    public boolean addStock(int partId, int addedQuantity) {
        Part part = getPartById(partId);
        if (part == null) return false;
        
        part.setQuantity(part.getQuantity() + addedQuantity);
        return updatePart(part);
    }

    public boolean removeStock(int partId, int removedQuantity) {
        Part part = getPartById(partId);
        if (part == null || part.getQuantity() < removedQuantity) return false;
        
        part.setQuantity(part.getQuantity() - removedQuantity);
        return updatePart(part);
    }
    
    
    public Part getPartByName(String name) {
        String sql = "SELECT PartID, Name, CatalogID, CurrentQuantity FROM Parts WHERE Name = ?";
        try (Connection conn = DBManager.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createPartFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading Part by Name: " + e.getMessage());
        }
        return null;
    }

    // FIX: Corrected logic for handling INSERT vs. UPDATE based on existence and intention (isUpdate)
    public boolean updatePartInventory(String partName, int quantityChange, boolean isUpdate) {
        Part existingPart = getPartByName(partName);

        if (existingPart == null) {
            // Case 4 (Add Stock) and Part doesn't exist: INSERT a new part with the provided quantity.
            if (!isUpdate) { // !isUpdate means this is an 'addStock' operation (Case 4)
                // Assuming you have a constructor: public Part(String name, String catalogId, int quantity)
                Part newPart = new Part(partName, "N/A", quantityChange); 
                savePart(newPart);
                return true;
            } else {
                // Case 5 (Set Quantity) and Part doesn't exist: Fail gracefully.
                return false; 
            }

        } else {
            // Part exists -> Update its quantity
            int finalQuantity;
            if (isUpdate) {
                // Case 5: Set to the exact new quantity
                finalQuantity = quantityChange;
            } else {
                // Case 4: Add the new quantity to the existing quantity
                finalQuantity = existingPart.getQuantity() + quantityChange;
            }
            
            // Prevent negative stock
            if (finalQuantity < 0) {
                 System.err.println("Stock update failed: Resulting quantity cannot be negative.");
                 return false;
            }

            existingPart.setQuantity(finalQuantity);
            return updatePart(existingPart);
        }
    }
}