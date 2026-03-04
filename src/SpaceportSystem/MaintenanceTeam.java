package SpaceportSystem; // Assuming your package is SpaceportSystem now

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceTeam implements Serializable {

    // --- Persistence Fields (Maps to MaintenanceTeams table) ---
    private int teamID;           // Maps to MaintenanceTeams.TeamID (Primary Key)
    private String teamName;      // Maps to MaintenanceTeams.TeamName
    private Technician leadTech;  // Stores the full Technician object for the lead
    
    // --- Composition Field ---
    private List<Technician> members = new ArrayList<>(); // Stores all members

    // 1. Constructor for CREATING A NEW TEAM (ID is 0)
    public MaintenanceTeam(String teamName) {
        this.teamID = 0; // Will be set by DAO after save
        this.teamName = teamName;
    }
    
    // 2. Constructor for LOADING FROM DATABASE (Used by DAO)
    // The DAO sets the leadTech and members lists separately after initial construction.
    public MaintenanceTeam(int teamID, String teamName) {
        this.teamID = teamID;
        this.teamName = teamName;
    }

    // --- Management Methods (Keep) ---
    
    public void addMember(Technician tech) {
        if (!this.members.contains(tech)) {
             this.members.add(tech);
        }
        // Logic for setting leadTech is now done explicitly or via a dedicated setter
    }
    
    public void setLeadTech(Technician leadTech) {
        this.leadTech = leadTech;
        // Ensure the lead is also in the member list
        if (leadTech != null && !this.members.contains(leadTech)) {
            this.members.add(leadTech);
        }
    }
    
    public void removeMember(Technician tech) {
        this.members.remove(tech);
        if (tech.equals(this.leadTech)) {
            this.leadTech = null; // Remove lead if the member is removed
        }
    }
    
    // --- Getters for DAO and Application ---
    
    public int getTeamID() {
        return teamID;
    }
    
    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }
    
    public String getTeamName() {
        return teamName;
    }

    // CRITICAL: DAO uses this to save the LeadTechnicianID to the DB
    public int getLeadTechnicianID() {
        return (leadTech != null) ? leadTech.getPersonID() : 0;
    }
    
    public Technician getLeadTech() {
        return leadTech;
    }

    // CRITICAL: DAO uses this list to save members to the Team_Members table
    public List<Technician> getMembers() {
        return members;
    }

    // --- Utility Methods ---
    
    // Original startWork logic (requires your Rocket class)
    
    public void startWork(Rocket rocket) {
        Technician lead = getLeadTech();
        if (lead != null) {
            System.out.println(lead.getName() + " is overseeing maintenance tasks for " + teamName + ".");
        } 
        
        for (Technician tech : members) {
            if (!tech.equals(lead)) {
                // tech.performTask(rocket, "Basic Systems Check"); 
            }
        }
    }
    
}