package SpaceportSystem;

import java.io.Serializable;

public class Technician extends Employee implements Serializable{

   Rocket assignedRocket;

   
   
   // DB USE
	public Technician(String name, int personID, String EmployeeID, String Department, double Salary, int age) {
		super(name, personID, EmployeeID, Department, Salary, age);
		
		this.Name = name;
	
	}

	
	
	
	// Constructor for CREATING A NEW Technician 
	public Technician(String name, String EmployeeID, String Department, double Salary, int age) {
	    // Calls the Employee constructor that creates a new Person (ID = 0)
	    super(name, EmployeeID, Department, Salary, age);
	}

	
	
	
	
	
	
	/*public Technician(String name) {
		this(Name, "General Technician");
	} */

	public String getName() {
		return Name;
	}

	public void workOn(Rocket assignedRocket) {
		System.out.println("Crew member " + Name + " is now working on Rocket " + assignedRocket.getRocketID() );
	}

	@Override
	public String toString() {
		return Name;
	}
}

