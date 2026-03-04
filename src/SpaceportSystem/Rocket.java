package SpaceportSystem;
import java.io.Serializable;
import java.util.ArrayList;


public class Rocket implements Serializable {
	
	
	private int rocketID;    // Primary Key (Maps to DB RocketID)
    private String registrationID;    // NEW: Maps to DB RegistrationID (Unique Code)
    private String engineType;        // NEW: Maps to DB EngineType
    private double maxPayload;        // NEW: Maps to DB MaxPayload
   
	private int spaceportID;    // Foreign Key (Maps to DB CurrentSpaceportID)


	private LaunchPad AssingedLaunchPad;
	private String ModelName; 
	private RocketStatus Status;
	private int PassengerCapacity = 6; 
	private FuelTank Fuel;
    private Engine Engines;
	private LifeSupportSystem LFS;	
	private MaintenanceLog ML;
	private NavigationSystem Distanation;
	
    
	// USE THIS TO DECLARE NEW ROCKETS

	public Rocket(int spaceportID, String registrationID, RocketStatus Status, String engineType, double maxPayload){
		this.spaceportID = spaceportID; 
        this.registrationID = registrationID;
		this.ModelName = registrationID;     
		this.Status = Status;
		this.engineType = engineType;       
        this.maxPayload = maxPayload;       
  
	}
	


	
	 // 2. Constructor for LOADED Rockets (used by RocketDAO after reading from DB).
	 
	public Rocket(int rocketID, int spaceportID, String registrationID, RocketStatus Status, String engineType, double maxPayload, int capacity) {
	    this.rocketID = rocketID;
	    this.spaceportID = spaceportID;
	    this.registrationID = registrationID;
	    this.ModelName = registrationID;
	    this.Status = Status;
	    this.engineType = engineType;
	    this.maxPayload = maxPayload;
	    
	    // CRITICAL FIX: Ensure PassengerCapacity is set from the DB value
	    this.PassengerCapacity = capacity; 
	}
	

	
	
	
	public int getRocketID() {
		return rocketID; 
		}
	public void setRocketID(int rocketID) { 
		this.rocketID = rocketID; 
		}

	public int getSpaceportID() {
		return spaceportID; 
		}
	/*public void setSpaceportID(int spaceportID) {
		this.spaceportID = spaceportID; 
		}*/

    public String getRegistrationID() {
    	return registrationID; 
    	}
    public void setRegistrationID(String registrationID) {
    	this.registrationID = registrationID; 
    	}
    
    public String getEngineType() {
    	return engineType; 
    	}
    public void setEngineType(String engineType) {
    	this.engineType = engineType; 
    	}

    
    public double getMaxPayload() {
    	return maxPayload; 
    	}
    public void setMaxPayload(double maxPayload) {
    	this.maxPayload = maxPayload; 
    	}

	public String getModelName() {
		return ModelName; 
		}

	public Engine getEngine() {
		return Engines;
	}

	public void setEngines(Engine engines) {
		Engines = engines;
	    }

	public RocketStatus getStatus() {
		return Status; 
		}
	public void setStatus(RocketStatus rocketStatus) { 
		Status = rocketStatus; 
		}

	public int getPassengerCapacity() { 
		return PassengerCapacity;
		}
	

	
	public FuelTank getFuelTank() {
		return Fuel; 
		}
	public void AssignFuelTank(FuelTank fuelTank) {
		Fuel = fuelTank; 
		}

	public Engine getEngines() {
		return Engines; 
		}
	

	public LaunchPad getAssingedLaunchPad() {
		return AssingedLaunchPad; 
		}
	public void setAssingedLaunchPad(LaunchPad assingedLaunchPad) {
		AssingedLaunchPad = assingedLaunchPad;
		}


	public double distanceToLocation(NavigationSystem Distance) {
	 return Distance.distanceTo();
	}
	
	
	
	
	
	
	
	
	
	
    public void setPassengerCapacity(int passengerCapacity) {
		PassengerCapacity = passengerCapacity;
	}




	// Overriding toString for easy debugging
    @Override
    public String toString() {
        return "Rocket [ID=" + rocketID + ", RegID=" + registrationID + ", Status=" + Status 
                + ", Payload=" + maxPayload + ", SpaceportID=" + spaceportID + "]";
    }




	
}