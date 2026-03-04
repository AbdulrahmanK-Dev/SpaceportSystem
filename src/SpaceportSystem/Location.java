package SpaceportSystem;

import java.io.Serializable;

public class Location implements Serializable{
    private double latitude;
    private double longitude; 
    private String locationName;
    private double altitude; 

  
    public Location() {
    }

    public Location(String locationName, double latitude, double longitude, double altitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
    
    
    public Location(String locationName) {
        this.locationName = locationName;
        
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

	

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}


    @Override
    public String toString() {
        return locationName + ", " + " [" + latitude + ", " + longitude + "]";
    }

    
}