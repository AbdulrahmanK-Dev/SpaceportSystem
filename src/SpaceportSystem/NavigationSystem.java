package SpaceportSystem;

import java.io.Serializable;
 class NavigationSystem implements Serializable{
	 

    private double latitude;   
    private double longitude;  
    private double altitude;   
    private double Currentlatitude;   
    private double Currentlongitude;  
    private double Currentaltitude;   
    
    private Location Location;

    private String country;
    private String waypointName;  


    public NavigationSystem() { 
    }

    public NavigationSystem(String waypointName, double latitude, double longitude, double altitude,Location Location) {
                                               
        this.waypointName = waypointName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.Location = Location;
    }

   
    public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public Location getLocation() {
		return Location;
	}

	public void setLocation(Location location) {
		Location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWaypointName() {
		return waypointName;
	}

	public void setWaypointName(String waypointName) {
		this.waypointName = waypointName;
	}

	public double distanceTo() {
	
        double latDelta =  this.latitude - Location.getLatitude();
        double lonDelta =  this.longitude - Location.getLongitude();
       
 
        return Math.sqrt(latDelta * latDelta + lonDelta * lonDelta) * 111.0; // rad to km
    }
	
	
	
   
	

  
   

	

	@Override
    public String toString() {
        return waypointName + ", " + country + " [" + latitude + ", " + longitude + "]";
    }

	public String getDestinationName() {
		return waypointName;
	}


	
}