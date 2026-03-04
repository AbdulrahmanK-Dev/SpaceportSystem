package SpaceportSystem;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;



public class Spaceport implements Serializable{
	
	private int spaceportID;
	private String name;
	private Location location = new Location("Spaceport 1", 28.50, 80.40, 0.005);
    private ArrayList<NavigationSystem> NS = new ArrayList<>();
	
	
	
	
	
	public Spaceport(String name, Location location) {
	    this.name = name;
	    this.location = location;
	}

	
	public Spaceport(int spaceportID, String name, Location location) {
	    this.spaceportID = spaceportID; // CRUCIAL: Set the ID from the database
	    this.name = name;
	    this.location = location;
	}
	
	
	
	
	
	public int getSpaceportID() {
        return spaceportID;
    }

    public void setSpaceportID(int spaceportID) {
        this.spaceportID = spaceportID; // CRUCIAL: Used after INSERT
    }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public ArrayList<NavigationSystem> getNS() {
		return NS;
	}


	
	
	
	
    public void addNS(NavigationSystem Navigation) {
    NS.add(Navigation);
    }


     public void RemoveNS(NavigationSystem Navigation) {
	 NS.remove(Navigation);
    }
	
     public void PrintNS(int i) {
	
	 System.out.println(	NS.get(i));	
    }


public void DisplayAllRoutes() {
	
	System.out.println("Available Routes:");
	for(int i = 0 ; i < NS.size() ; i++) {

		System.out.println(	NS.get(i));			
}

}



public void saveToFile(String filename) {
 try (ObjectOutputStream oos = new ObjectOutputStream(
      new FileOutputStream(filename))) {
     
     oos.writeObject(this); 
     System.out.println(" System data saved to " + filename);
     
 } catch (IOException e) {
     System.err.println("❌ Error saving system data: " + e.getMessage());
 }
}





}
	
