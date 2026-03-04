package SpaceportSystem;

import java.io.Serializable;
// Note: java.util.Date is imported but unused; I'll keep the import for completeness
import java.util.Date;

public abstract class Person implements Serializable {
    
    protected String Name;
    protected int PersonID;
    protected String Dateofbirth;
    protected int age;
    protected String contactEmail;

    

    // 1. DB Constructor - FULL ARGS (For loading existing data) - [Your Original]
    public Person(int personID, String name, int age, String role, String contactEmail) {
        this.PersonID = personID; 
        this.Name = name;
        this.age = age;
        this.contactEmail = contactEmail;
    }

    // 2. NEW Constructor - MINIMAL ARGS (The one subclasses were likely calling to create new data)
    // FIX: ADDED THIS ONE: Person(String, int)
    public Person(String name, int age) {
        this.PersonID = 0; // ID will be set by DAO
        this.Name = name;
        this.age = age;
        this.contactEmail = null; // Set to null if not provided
    }
    
    // 3. NEW Constructor - MINIMAL ARGS with ID (The one subclasses were likely calling to load existing data)
    // FIX: ADDED THIS ONE: Person(int, String, int)
    public Person(int personID, String name, int age) {
        this.PersonID = personID;
        this.Name = name;
        this.age = age;
        this.contactEmail = null; // Set to null if not provided
    }

    // 4. Creation Constructor - FULL ARGS (For creating new data with email) - [Your Original]
    public Person(String name, int age, String contactEmail) {
        this.PersonID = 0; 
        this.Name = name;
        this.age = age;
        this.contactEmail = contactEmail;
    }


    // --- GETTERS AND SETTERS ---

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPersonID() {
        return PersonID;
    }
    
    public void setPersonID(int personID) {
        this.PersonID = personID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getDateofbirth() {
        return Dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        Dateofbirth = dateofbirth;
    }

    // --- UTILITY METHODS ---

    @Override
    public String toString() {
        return " Name: " + Name + "\n PersonID: " + PersonID + "\n Dateofbirth: " + Dateofbirth + "\n ContactEmail: " + contactEmail
                + "\n Age: " + age;
    }

    @Override
    public boolean equals(Object ob) {
        
        if(this == ob)
            return true;
        
        if(ob == null)
            return false;
        
        if( !(ob instanceof Person) )
            return false;
        
        Person p = (Person)ob;
        
  
        if(p.age != age)
            return false;
        if( !(p.Name).equals(getName()) )
            return false;
        if( !(p.PersonID == getPersonID() ) )
            return false;
        if( p.Dateofbirth != Dateofbirth) // Potential bug here, should use p.Dateofbirth.equals(Dateofbirth)
            return false;
            
        return true;
    }   
}