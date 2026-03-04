package SpaceportSystem;



import java.util.Date;  

import java.io.Serializable;


public class Employee extends Person implements Serializable{



 protected String EmployeeID;
 protected String Department;
 protected Double Salary;




// DATABASE USE ONLY!!
public Employee(String name, int personID, String EmployeeID, String Department, double Salary, int age) {
    
    super(personID, name, age); 
    
    this.EmployeeID = EmployeeID;
    this.Department = Department;
    this.Salary = Salary;
}

// USE THIS TO CREATE AN EMPLOYEE CHOOSE THE ROLE USING THE EmployeeDAO saveEmployee!
public Employee(String name, String EmployeeID, String Department, double Salary, int age) {
    
 
    super(name, age);
    this.EmployeeID = EmployeeID;
    this.Department = Department;
    this.Salary = Salary;
}





public int getAge() {
	return age;
}

public void setPersonID(int PersonID) {
	this.PersonID = PersonID;
}


public String getEmployeeID() {

return EmployeeID;

}


public String getDepartment() {

return Department;

}



public void setDepartment(String department) {

Department = department;

}





public Double getSalary() {

return Salary;

}





public void setSalary(Double salary) {

Salary = salary;

}





@Override 

public boolean equals(Object ob) {

if(this == ob)

return true;

if(ob == null)

return false;


if( !(ob instanceof Employee) ) // iS this an employee?

       return false;


Employee e = (Employee)ob;


if( !(e.Department.equals(getDepartment())) )

return false;


if( !(e.EmployeeID.equals(getEmployeeID()) ))

return false;

if(e.Salary != Salary)

return false;




return true;

}






}