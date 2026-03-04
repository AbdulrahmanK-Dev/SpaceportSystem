package SpaceportSystem;

import java.io.Serializable;

public class BookingManager implements Serializable{

   BookingManager(){
   }
  
  
  /*public void Book(Passenger P, Ticket T) {
	  if(P.assignTicket(T)) { 
		  T.setAssociatedPassenger(P);
	  }
  }*/
  
  
  /*public void ChangeBook(Passenger P, Passenger P2 , Ticket T) {
	  if(P.AssignTicket(T)) {  // 
		  T.setAssociatedPassenger(P);
	  }
	  P2.setPassengerTicket(null);
  }*/
  
  
  public void CancelBook(Passenger P) {
 
	  if(P.getPassengerTicket() == null ) {
		  System.out.println("No ticket is assigned to the passenger");  //no tickets
	    return;
	  }
		 
	  
	  
	   P.setPassengerTicket(null);
	  System.out.println("Ticked cancelled successfully");
	 
  
  
  
}
  
}
