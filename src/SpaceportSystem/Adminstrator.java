package SpaceportSystem;
import java.io.Serializable;

import java.util.Date;

public class Adminstrator extends Employee implements Serializable{
  
	private Spaceport S;
	private FuelTank AdminFuel = new FuelTank(400);
	
	public Adminstrator(String name, int personID,String EmployeeID, String Department, double Salary, Spaceport S,int age) {
		super(name, personID, EmployeeID,Department, Salary, age);
		this.EmployeeID = EmployeeID;
		this.Department = Department;
		this.Salary = Salary;
	    this.S = S;
	    
	}
	
	
	
	public Adminstrator(String name, String EmployeeID, String Department, double Salary, int age) {
	    
	    super(name, EmployeeID, Department, Salary, age);
	   this.EmployeeID = EmployeeID;
	   this.Department = Department;
	   this.Salary = Salary;
	   this.age = age;
	}
	
	

	
	public void AssignRocketToLaunchpad(LaunchPad LP,Rocket assignedRocket) {
		LP.setAssignedRocket(assignedRocket);
		assignedRocket.setAssingedLaunchPad(LP);
		LP.ChangeStatus(PadStatus.IN_USE);
	}
	
	public void ChangeRocketstatus(Rocket assignedRocket, RocketStatus Status) {
		
		assignedRocket.setStatus(Status);
	}
	
	public void ChangePadStatus(LaunchPad LP, PadStatus Status) {
		LP.ChangeStatus(Status);
	}
	    
	
	public void scheduleMaintenance(Rocket assignedRocket, String Date) {
		assignedRocket.setStatus(RocketStatus.IN_MAINTENANCE);
	}
	
	
	public void callForRefuel(double amount,Rocket assignedRocket) {
		AdminFuel.FillFuel(amount, assignedRocket);
	} 
	
	
	
	
	
	
}
