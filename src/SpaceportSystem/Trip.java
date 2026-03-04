package SpaceportSystem;
import java.io.Serializable;
import java.util.Date; // Assuming LaunchDate is a java.util.Date or similar

public class Trip implements Serializable {

   
    private int tripID;           // Primary Key
    private int rocketID;         // Foreign Key
    private int launchPadID;      // Foreign Key
    // -----------------

    private String tripNo;        // Maps to DB TripNo (was String TripID in DB structure)
    private tripStatus status;    // Maps to DB Status (Enum)
    private Date departureTime;   // Maps to DB DepartureTime 
    private String destinationLocation; 
    private int requiredCrewCount; 
    public int getRequiredCrewCount() {
		return requiredCrewCount;
	}

	public void setRequiredCrewCount(int requiredCrewCount) {
		this.requiredCrewCount = requiredCrewCount;
	}

	private Rocket assignedRocket;
    private LaunchPad assignedLaunchPad;

    
    
    // ADMIN USE/ USE TO CREATE SOME TRIPS
    public Trip(String tripNo, tripStatus status, Date departureTime, String destinationLocation, int rocketID, int launchPadID) {
        this.tripID = tripID;
        this.tripNo = tripNo;
        this.status = status;
        this.departureTime = departureTime;
        this.destinationLocation = destinationLocation;
        this.rocketID = rocketID;
        this.launchPadID = launchPadID;
    }
 
    // 2. Constructor for LOADED Trips NEVER USE IT TO DECLARE TRIPS
    public Trip(int tripID,String tripNo, tripStatus status, Date departureTime, String destinationLocation, int rocketID, int launchPadID) {
        this.tripID = tripID;
        this.tripNo = tripNo;
        this.status = status;
        this.departureTime = departureTime;
        this.destinationLocation = destinationLocation;
        this.rocketID = rocketID;
        this.launchPadID = launchPadID;
    }
    
    
    public Trip() {
    }
 

    public int getTripID() { 
    	return tripID;
    	}
    public void setTripID(int tripID) {
    	this.tripID = tripID; 
    	}
    
    public int getRocketID() {
    	return rocketID; 
    	}
    public void setRocketID(int rocketID) { 
    	this.rocketID = rocketID; 
    	}
    
    public int getLaunchPadID() {
    	return launchPadID; 
    	}
    public void setLaunchPadID(int launchPadID) { 
    	this.launchPadID = launchPadID; 
    	}

    public String getTripNo() {
    	return tripNo; 
    	}
    public void setTripNo(String tripNo) { 
    	this.tripNo = tripNo; 
    	}

    public tripStatus getStatus() {
    	return status; 
    	}
    public void setStatus(tripStatus status) { 
    	this.status = status;
    	}

    public Date getDepartureTime() { 
    	return departureTime;
    	}

    public void setDepartureTime(Date departureTime) {
    	this.departureTime = departureTime; 
    	} 
    
    public String getDestinationLocation() {
    	return destinationLocation; 
    	}
    public void setDestinationLocation(String destinationLocation) {
    	this.destinationLocation = destinationLocation; 
    	}

	public Rocket getAssignedRocket() {
		return assignedRocket;
	}

	public void setAssignedRocket(Rocket assignedRocket) {
		this.assignedRocket = assignedRocket;
	}

	public LaunchPad getAssignedLaunchPad() {
		return assignedLaunchPad;
	}

	public void setAssignedLaunchPad(LaunchPad assignedLaunchPad) {
		this.assignedLaunchPad = assignedLaunchPad;
	}
    
    
    

    
}