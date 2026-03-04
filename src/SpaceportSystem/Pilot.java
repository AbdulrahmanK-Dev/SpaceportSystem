package SpaceportSystem;

import java.io.Serializable;

   

public class Pilot extends Employee implements Serializable{

    private String PilotLicenseID;
    private Rocket assignedRocket;
  
    
    private int assignedRocketID;
    private int assignedTripID;
    private int assignedLaunchPadID;
    private int assignedNavSystemID;
    
    public static boolean isEngineOn = false; 
    private NavigationSystem navSystem;
    
    
    
    
    // --- 1. CONSTRUCTOR FOR LOADING FROM DATABASE (Includes PersonID) ---
    public Pilot(String name, int personID, 
                 String employeeID, String department, double salary, int age,
                 String pilotLicenseID, int flightHours, 
                 int assignedRocketID, int assignedTripID, int assignedLaunchPadID, int assignedNavSystemID) {

        super(name, personID, employeeID, department, salary, age); 
        
        this.PilotLicenseID = pilotLicenseID;
        this.assignedRocketID = assignedRocketID;
        this.assignedTripID = assignedTripID;
        this.assignedLaunchPadID = assignedLaunchPadID;
        this.assignedNavSystemID = assignedNavSystemID;
    }

    //  CONSTRUCTOR FOR CREATING A NEW PILOT 
    public Pilot(String name, 
                 String employeeID, String department, double salary, int age,
                 String pilotLicenseID) {

        super(name, employeeID, department, salary, age);
        
        this.PilotLicenseID = pilotLicenseID;
        this.assignedRocketID = 0;
        this.assignedTripID = 0;
        this.assignedLaunchPadID = 0;
        this.assignedNavSystemID = 0;
    }

    // ROCKET GETTER (Lazy Load)
    public Rocket getAssignedRocket() {
        if (this.assignedRocketID <= 0) return null;
        try {
            // ASSUMPTION: RocketDAO can be instantiated or accessed
            RocketDAO dao = new RocketDAO(); 
            return dao.getRocketById(this.assignedRocketID);
        } catch (Exception e) {
            System.err.println("Error loading Assigned Rocket: " + e.getMessage());
            return null;
        }
    }

 

    // LAUNCH PAD GETTER (Lazy Load)
    public LaunchPad getAssignedLaunchPad() {
        if (this.assignedLaunchPadID <= 0) return null;
        try {
            // ASSUMPTION: LaunchPadDAO can be instantiated or accessed
            LaunchPadDAO dao = new LaunchPadDAO(); 
            return dao.getLaunchPadById(this.assignedLaunchPadID);
        } catch (Exception e) {
            System.err.println("Error loading Assigned LaunchPad: " + e.getMessage());
            return null;
        }
    }
    


    public void startRocketEngines() {
    
    	
    
        if (assignedRocket == null) {
            System.out.println("No rocket assigned to pilot");
            return;
        }


        Engine E = assignedRocket.getEngine();
        if (E == null) {
            System.out.println("Rocket has no engine assigned!");
            return;
        }

        System.out.println("Pilot " + getName() + " starting engine...");
        E.StartEngine();
    }


    public void shutdownEngines() {

        Rocket rocket = getAssignedRocket();
        
        if (rocket == null) {
            System.out.println("No rocket assigned to shut down");
            return;
        }

        rocket.getEngine().ShutdownEngine();
        System.out.println("Pilot " + getName() + " has shut down the engines");
    }

    public double calculateDistanceToDestination() {
  
       
        if (navSystem == null) {
            System.out.println("Pilot has no navigation system assigned");
            return 0.0;
        }

        return navSystem.distanceTo();
    } 

 
   

	public int getAssignedNav() {
		return assignedNavSystemID;
	}
	
	public void setNavSystem(NavigationSystem navSystem) {
		this.navSystem = navSystem;
	}

	public void manageFuel(double timeHours) {
      
        
        if (assignedRocket == null) {
            System.out.println("Pilot cannot manage fuel no assigned rocket.");
            return;
        }

        FuelTank F = assignedRocket.getFuelTank();
        Engine E = assignedRocket.getEngine();

        if (F == null || E == null) {
            System.out.println("Missing fuel tank or engine.");
            return ;
        }
        double burnRate = F.getFuelBurnRate(E);
        F.consumeFuel(burnRate, timeHours);
        
       System.out.println("Current Burn Rate: "+ F.getFuelBurnRate(E) );
    }
    
	
	public void setAssignedRocket(Rocket assignedRocket) {
		this.assignedRocket = assignedRocket;
	}
	
	
	
	
	
	public void adjustThrottle(double throttle) {
	assignedRocket.getEngines().ChangeThrottleSetting(throttle);
	}

}