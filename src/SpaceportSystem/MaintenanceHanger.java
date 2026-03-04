package SpaceportSystem;

import java.io.Serializable;
import java.util.ArrayList;

public class MaintenanceHanger implements Serializable{
    private String hangerID;
    private ArrayList<Technician> crew;
    private ArrayList<Rocket> housedRockets; 

    public MaintenanceHanger(String hangerID) {
        this.hangerID = hangerID;
        this.crew = new ArrayList<>();
        this.housedRockets = new ArrayList<>();
        System.out.println("Maintenance Hangar " + hangerID + " established.");
    }


    public String getHangarID() {
        return hangerID;
    }

    public void setHangarID(String hangarID) {
        this.hangerID = hangarID;
    }

    public ArrayList<Technician> getCrew() {
        return crew;
    }

    public void setCrew(ArrayList<Technician> crew) {
        this.crew = crew;
    }

 

    public ArrayList<Rocket> getHousedRockets() { 
        return housedRockets;
    }

    public void setHousedRockets(ArrayList<Rocket> housedRockets) {
        this.housedRockets = housedRockets;
    }

   
    public void addRocket(Rocket rocket) { 
        housedRockets.add(rocket);
       
        System.out.println("Rocket " + rocket.getRocketID() + " is now housed in Hangar " + hangerID + 
                           ". Total rockets: " + housedRockets.size());
    }
    
   
    public String assignServiceToNextRocket() {
        
        if (housedRockets.isEmpty()) {
            return "No rockets are currently housed in this hangar.";
        }
      
        Technician leadTech;
        
        if (crew.isEmpty()) {
            return "No maintenance crew members are currently assigned to this hangar.";
        } else {
          
            
       //we need to loop through all the techs and then its done	
        	leadTech = crew.get(0); 
        }

        
        Rocket rocketToService = housedRockets.remove(0);
      
        
        return leadTech.getName() + " is now beginning service on Rocket " + 
               rocketToService.getRocketID() + ". Rockets remaining in hangar: " + housedRockets.size();
    }
    
   
    public void hireCrew(Technician member) {
        crew.add(member);
        System.out.println(member.getName() + " has been assigned to Hangar " + hangerID);
    }
}