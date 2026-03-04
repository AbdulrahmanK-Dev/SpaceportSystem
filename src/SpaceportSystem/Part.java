package SpaceportSystem;

import java.io.Serializable;

public class Part implements Serializable {
    
    // 1. Database ID (Primary Key)
    private int partID; 

    private String catalogID;
    
    private String name;
    private int quantity; 

    // Use to create new parts
    public Part(String name, String catalogID, int quantity) {
        this.name = name;
        this.catalogID = catalogID;
        this.quantity = quantity;
    }

    // Loads parts, used by the DAO
    public Part(int partID, String name, String catalogID, int quantity) {
        this.partID = partID;
        this.name = name;
        this.catalogID = catalogID;
        this.quantity = quantity;
    }

   
    public int getPartID() {
        return partID;
    }
    public void setPartID(int partID) {
        this.partID = partID;
    }
    
  
    public String getCatalogID() {
        return catalogID;
    }
    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

 
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}