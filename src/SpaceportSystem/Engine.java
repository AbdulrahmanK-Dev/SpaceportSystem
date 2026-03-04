	package SpaceportSystem;

import java.io.Serializable;

public class Engine implements Serializable{
	
	private String engineID = "SS Nightmare";
    private static final double maxThrust = 500000; // MAXIMUM FORCE NEEDED TO PUSH THE ROCKET in newton
    private static final String propellantType = "Methane"; // SUBSTANCE THAT IS BURNED TO GENERATE THRUST WE USE  METHANE AS IT IS CLEANER. SDG CLEAN ENERGY
    private static final int ReuseabilityLimit = 1000; // i think 
    private int ActivateCount = 0; // number of times the engine has been activated
    private double currentThrottleSetting; // 0.0 to 100.0 percentage of thrust used
    private boolean isOperational; 
    private EngineStatus Status;
    private FuelTank Fengine = new FuelTank(400);
    
    
    public Engine(){
    }
    
    public Engine(String engineID, EngineStatus Status, double currenThrottleSetting, boolean isOperational, int reuseLimit) {
        this.engineID = engineID;
        this.Status = EngineStatus.POWERED_OFF;
        this.currentThrottleSetting = 0;
        this.isOperational = false;
    }

    
  
   
	public String getEngineID() {
		return engineID;
	}

	public void setEngineID(String engineID) {
		this.engineID = engineID;
	}

	public double getMaxThrust() {
		return maxThrust;
	}

	
	public FuelTank getFengine() {
		return Fengine;
	}

	public String getPropellantType() {
		return propellantType;
	}

	public double getCurrentThrottleSetting() {
		return currentThrottleSetting;
	}
	
	
	

	public void setCurrentThrottleSetting(double currentThrottleSetting) {
		if(currentThrottleSetting < 0 || currentThrottleSetting > 100) {
         System.out.println("Throttle input must be between 0 to 100 percent");
         return;
		}
		this.currentThrottleSetting = currentThrottleSetting;
	}

	public boolean isOperational() {
		return isOperational;
	}


    public int getReuseabilityLimit() {
		return ReuseabilityLimit;
	}

	

	public EngineStatus getEngineStatus() {
		return Status;
	}
	

	public int getActivateCount() {
		return ActivateCount;
	}
	

	
	public double CalculateCurrentThrust() { //ACTUAL THRUST USED, UNLIKE MAX THRUST 
    	return maxThrust * (currentThrottleSetting/100.00); // Current thrust = Max thrust / percentage of throttle used.
         }
	
	

    
    public void ChangeThrottleSetting(double TargetThrottle) { // The throttle targeted for change
    	
    	if(TargetThrottle > 100 || TargetThrottle < 30) {
    		System.out.println("Throttle has to be between 30 to 100");
    		return;
    	}
    	
    	if( !(isOperational) ) {
    		System.out.println("Engine isnt operational!");
    		return;
    	}
    	
    	
    	this.currentThrottleSetting = TargetThrottle;
    	System.out.println("Engine " + getEngineID() + " throttled to " + TargetThrottle + "%");
    	
        }
    
    
     
    
    
    public void StartEngine() {
    	
    	if(ActivateCount >= ReuseabilityLimit) {
    		System.out.println("ENGINE HAS SURPASSED THE LIMIT OF ACTIVATION SETTING STATUS TO MAINETNANCE CONTACTING TECHNICIAN PERSONNEL");
    		Status = EngineStatus.IN_MAINTENANCE;
    		  return;
    	}
    	
    	System.out.println("ENGINE HAS BEEN ACTIVATED SUCCESFULLY");
    	Status = EngineStatus.OPERATIONAL;
        isOperational = true;
    	ActivateCount++;
    }
    
    public void ShutdownEngine() {
    	
    	if(Status == EngineStatus.IN_MAINTENANCE) {
    		System.out.println("CANNOT SHUTDOWN AS ENGINE IS IN MAINTENANCE");
    	      return;
    	}
    	
    System.out.println("ENGINE HAS BEEN SHUT OFF SUCCESFULLY");
     Status = EngineStatus.POWERED_OFF;
     isOperational = false;
    }    
    
    
    public void MaintenanceEngine() {
        System.out.println("ENGINE HAS BEEN PUT TO MAINTENANCE");
     Status = EngineStatus.IN_MAINTENANCE;
     isOperational = false;
    }    
    
    
    
    
    
    
    
    

    
}
