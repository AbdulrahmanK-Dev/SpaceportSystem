package SpaceportSystem;
import java.io.Serializable;


public class Ticket implements Serializable {
    
   
    private int ticketID;       // Primary Key (was String)
    private int tripID;         // Foreign Key to Trips
    private int passengerID;    // Foreign Key to People
    // -----------------
    
    private double price;   // Maps to DB Price (was double)
    private BookingStatus status; // Maps to DB BookingStatus
    private String seatNo;      // Maps to DB SeatNo
    private String ClassTier;
  
   
    public String getClassTier() {
		return ClassTier;
	}
 // Assuming your Ticket class fields include:
 // protected String classTier; 


 // 1. Constructor for NEW Tickets (used by Admin creation and Booking)
 // FIX: Added 'String classTier' argument.
 public Ticket(int tripID, double price, String classTier, String seatNo, int passengerID) {
     this.tripID = tripID;
     this.price = price;
     this.ClassTier = classTier; // NEW FIELD
     this.seatNo = seatNo;
     this.passengerID = passengerID; // Booking immediately links to a passenger
     // Note: Status should probably be set here if the ticket is immediately booked
 }

 // 1b. Constructor for NEW, UNBOOKED Tickets (used by Admin ticket generation)
 // FIX: Added 'String classTier' argument.
 public Ticket(int tripID, String seatNo, double price, String classTier) {
     this.tripID = tripID;
     this.seatNo = seatNo;
     this.price = price;
     this.ClassTier = classTier; // NEW FIELD
     this.passengerID = 0; // Unbooked
     // Note: Status should probably be set to AVAILABLE
 }


 // 2. Constructor for LOADED Tickets (used by TicketDAO READ methods)
 // FIX: Added 'String classTier' argument.
 public Ticket(int ticketID, int tripID, double price, BookingStatus status, String classTier, String seatNo, int passengerID) {
     this.ticketID = ticketID;
     this.tripID = tripID;
     this.price = price;
     this.status = status;
     this.ClassTier = classTier; // NEW FIELD
     this.seatNo = seatNo;
     this.passengerID = passengerID;
 }
    
    
    
  
    

    public int getTicketID() { 
    	return ticketID; 
    	}
    public int getTripID() { 
    	return tripID; 
    	}
    public void setTripID(int tripID) { 
    	this.tripID = tripID; 
    	}
    
    public int getPassengerID() { 
    	return passengerID;
    	}
    public void setPassengerID(int passengerID) {  // DB USE
    	this.passengerID = passengerID;
    	}
    
    public double getPrice() { 
    	return price; 
    	}
   

    public BookingStatus getStatus() { 
    	return status; 
    	}
    public void setStatus(BookingStatus status) { 
    	this.status = status;
    	}
    
  
    
    
    public void confirmBooking() {
       this.status = BookingStatus.CONFIRMED;
    }
    public void cancelBooking() {
       this.status = BookingStatus.CANCELED;
    }

	public void setTicketID(int ticketID) {
		this.ticketID = ticketID;
	}

	public String getSeatNo() {
		
		return seatNo;
				
	}

	
  
    // NOTE: Methods like determinePrice() now belong in a service layer, not the data model.
}