package SpaceportSystem;

import java.io.Serializable;

public class FuelTank implements Serializable{
  public static final double  TankSizeMax = 1000;
  public static final double TankSizeMin = 300;
  private double CurrentFuel;
  private double CarbonFootPrint; // IDK YET WE FIGURE SOMETHING OUT LATER
  //* These attributes will be focused more on fuel and carbon emission each equation needed for these calc was researched carefully//
   private static final double BASE_FUEL = 5.0; // KG/S OF PROPERLLANT CONSUMED BY THE ENGINE
   private double FuelBurnRate;
   private static final double EMISSION_COEFFICIENT = 2.5;  // CO2 kg produced per unit of fuel 
   private double CarbonPerSecond;
   
   long CurrentTime = System.nanoTime(); // This is in NANO second


   FuelTank(double CurrentFuel){
	   this.CurrentFuel = CurrentFuel;
   }

   
   
   
       
  public double getCurrentFuel() {
	  return CurrentFuel;
  }
  
  
  public double FillFuel(double addedFuel, Rocket R) {
	  
	
	   if(CurrentFuel > 1000 || CurrentFuel < 300) {
		   System.out.println("Invalid tank size, setting to MINIMUM ");
		
		   return  TankSizeMin;
	   }
	   else
		   
		 return  CurrentFuel+= addedFuel; 
	   
	//   System.out.println("Fuel capacity for "+ R.getRocketID() +" has been set to " + CurrentFuel + " Liters");
	
  }

  
  
  
  
  public double getCarbonPerSecond(Engine E) { // Carbonprint/s == BASE FUEL * CURRENTHROTTLESETTING DIVIDED BY 100
		if(FuelBurnRate == 0.0 || !(E.getEngineStatus().equals(EngineStatus.OPERATIONAL)) ) {
			System.out.println("Engine is current");
			return 0;
		}
		CarbonPerSecond = BASE_FUEL * (E.getCurrentThrottleSetting()/100);
		
		return CarbonPerSecond;
	}
  
  
	public double getFuelBurnRate(Engine E) { //  =  BASE FUEL * (CURRENT THROTTLE/100) 
	       if(E.getCurrentThrottleSetting() == 0) {
	    	   System.out.println("Thrust is set to zero. No usage of fuel found");
	    	   return 0.0; 
	       }
			FuelBurnRate = BASE_FUEL * (E.getCurrentThrottleSetting()/100);
			
			return FuelBurnRate;
		}
	 
	
	
	public boolean consumeFuel(double burnRate, double timeOn) {  //* ill name the time the engine has been running for to THE WORLD
                                                                          
		                          //* i wanna make a relatime instead of FIXED THE WORLD but i havent figured it out yet 
	    if (burnRate <= 0 || timeOn <= 0)
	        return true;

	    double ConsumedFuel = burnRate * timeOn; // 

	    if (ConsumedFuel > CurrentFuel) {
	        CurrentFuel = 0;
	        System.out.println("FUEL TANK IS NOW EMPLTY!!!!!!!!!!!!!!@!@!1");
	        return false; 
	    }

	    CurrentFuel -= ConsumedFuel;
	    System.out.println( "Remaining Fuel "+CurrentFuel + " Liters");
	    System.out.println( "Consumed Fuel "+ConsumedFuel + " Liters");
	    
	    return true;
	}

		
 	  
}
