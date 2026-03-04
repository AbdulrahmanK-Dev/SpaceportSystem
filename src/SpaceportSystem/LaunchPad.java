package SpaceportSystem;

import java.io.Serializable;

public class LaunchPad implements Serializable {

    private int launchPadID;
    private String padNo;
    private PadStatus status;
    private int assignedRocketID;
    private int spaceportID;

    private Trip assignedTrip;
    private Rocket assignedRocket;

    // Constructor for NEW LaunchPads
    public LaunchPad(String padNo, PadStatus status, int spaceportID) {
        this.padNo = padNo;
        this.status = status;
        this.spaceportID = spaceportID;
        this.assignedRocketID = 0;
    }

    // Constructor for LOADED LaunchPads
    public LaunchPad(int launchPadID, String padNo, PadStatus status, int assignedRocketID, int spaceportID) {
        this.launchPadID = launchPadID;
        this.padNo = padNo;
        this.status = status;
        this.assignedRocketID = assignedRocketID;
        this.spaceportID = spaceportID;
    }

    // Getters & Setters
    public int getLaunchPadID() { 
    	return launchPadID;
        }
    public void setLaunchPadID(int launchPadID) { 
    	this.launchPadID = launchPadID;
    	}

    public String getPadNo() { return padNo; 
        }
    
   
    public void setPadNo(String padNo) { this.padNo = padNo; 
        }

    public PadStatus getStatus() { 
    	return status;
        }
    public void setStatus(PadStatus status) { 
    	this.status = status; 
    	}

    public int getAssignedRocketID() { 
    	return assignedRocketID; 
    	}
    public int getSpaceportID() { 
    	return spaceportID;
    	}

    public Trip getAssignedTrip() {
    	return assignedTrip; 
    	}
    public void setAssignedTrip(Trip assignedTrip) { 
    	this.assignedTrip = assignedTrip; 
    	}

    public Rocket getAssignedRocket() {
    	return assignedRocket; 
    	}

    public void setAssignedRocket(Rocket assignedRocket) {
        this.assignedRocket = assignedRocket;
        if (assignedRocket != null) {
            this.assignedRocketID = assignedRocket.getRocketID();
        } else {
            this.assignedRocketID = 0;
        }
    }

  
    public boolean PrepareForLaunch(Rocket R, FuelTank F) {

        if (this.status == PadStatus.IN_USE) {
            System.out.println("Pad already in use!");
            return false;
        }

        if (this.status == PadStatus.UNDER_MAINTENANCE) {
            System.out.println("Pad under maintenance!");
            return false;
        }

        if (R.getStatus() == RocketStatus.IN_MAINTENANCE) {
            System.out.println("Rocket under maintenance. Cannot launch.");
            return false;
        }

        if (F.getCurrentFuel() < F.TankSizeMin) {
            System.out.println("Fuel below minimum limit!");
            return false;
        }

        return true;
    }

    public void ClearPad() {
        this.status = PadStatus.AVAILABLE;
        this.assignedRocket = null;
        this.assignedRocketID = 0;
    }

    public void ChangeStatus(PadStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LaunchPad [PadNo=" + padNo + ", Status=" + status +
                ", RocketID=" + assignedRocketID + ", SpaceportID=" + spaceportID + "]";
    }
}
