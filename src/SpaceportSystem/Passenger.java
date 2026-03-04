package SpaceportSystem;

import java.io.Serializable;

public class Passenger extends Person implements Serializable {

	
	
	
	
	
	
	

	public Passenger(int personID, String name, int age,String role ,String contactEmail) {
		super(personID, name, age,role ,contactEmail);
		this.PersonID = personID;
		this.Name = name;
		this.age = age;
		this.contactEmail = contactEmail;
	}

	public Passenger(String name, int age, String contactEmail) {
		super(name, age, contactEmail);
	
		this.Name = name;
		this.age = age;
		this.contactEmail = contactEmail;
	}

	
    

    

	public int getAge() {
		return age;
	}

	public void setPersonID(int PersonID) {
		this.PersonID = PersonID;
	}

   
}