package SpaceportSystem;

import java.io.Serializable;

public enum BookingStatus implements Serializable{
    
	CONFIRMED,
	CANCELED,
	AVAILABLE,
	TAKEN,
	PENDING;
	
	
}
