package SpaceportSystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SpaceportTEST{

    public static void main(String[] args) {

        DBManager DB = new DBManager();
        SpaceportDAO dao = new SpaceportDAO();
        PassengerDAO PassDao = new PassengerDAO();
        RocketDAO RockDao = new RocketDAO();
        LaunchPadDAO PadDao = new LaunchPadDAO();
        tripDAO tripDao = new tripDAO(RockDao,PadDao);
        ticketDAO tickDao = new ticketDAO();
        EmployeeDAO EmployDao = new EmployeeDAO();
        PartDAO PartDao = new PartDAO();
        MaintenanceTeamDAO MaintenanceDao = new MaintenanceTeamDAO(PartDao,RockDao);
        TechnicianDAO TechDao = new TechnicianDAO();
       
        
        
        
        
        
        
        
        
        
        Location testLocation = new Location("Space Station One", 10.0, 5.0, 400000.0);
        Spaceport S = new Spaceport("Protos", testLocation);
     
     
        
        Spaceport PrimarySpaceport = dao.getOrCreatePrimarySpaceport();
        dao.saveOrUpdateSpaceport(PrimarySpaceport);
 
   int spaceportID  =  PrimarySpaceport.getSpaceportID();
   
   
   Adminstrator Admin = new Adminstrator("Abdulrahman","A101","Adminstration",70000,19);
   Technician Tech = new Technician("Seif","T101","Machinary",60000,19);
   Pilot P = new Pilot("Ghazy","PI101","Flight",50000,18,"PILOT12D");
   
   
   
   
        Scanner sc = new Scanner(System.in);
      

        if (S != null) {
            System.out.println("\n Application Initialized ");
            System.out.println("Working on Spaceport: " + S.getName());
            System.out.println("Status: Ready to manage rockets and personnel.");
        } else {
            System.err.println("FATAL: Failed to initialize primary spaceport.");
        }

        System.out.println(S.getLocation());

       // tripDao.updateTrip(new Trip("M101",tripStatus.SCHEDULED,"12/14/2025","Mars L1",));

       // tripDao.updateTrip(new Trip());
 
          // tickDao.saveTicket(ull);
 
        RockDao.updateRocketStatus(6,"IN_MAINTENANCE");
        
        
        System.out.println("Please log in into your account");
        String role = sc.nextLine();

      //  tripDao.saveTrip(new Trip("M102",tripStatus.SCHEDULED,new Date(System.currentTimeMillis() + 86400000),
        //		"Mars L2", 
        	//	RockDao.getRocketById(1).getRocketID(),PadDao.getLaunchPadById(8).getLaunchPadID()  )   );
        //saves the trip in our database persistently
   
     // --- START OF COMPLETE PASSENGER PATH ---
        if (role.equalsIgnoreCase("P") || role.equalsIgnoreCase("Passenger")) {

            if (sc.hasNextLine()) {
                sc.nextLine(); 
            } 
            
            System.out.println("\n--- Passenger Login/Registration ---");
            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            System.out.print("Enter your contact Email (used for account lookup): ");
            String email = sc.nextLine();

            // 1. ATTEMPT LOOKUP BY NEW EMAIL FIELD
            Passenger passenger = PassDao.getPassengerByEmail(email); 

            if (passenger == null) { // IF NO PASSENGER, CREATE A NEW ONE
                System.out.println("No account found with that email. Creating new account.");
                System.out.print("Enter your Age: ");
                int age = sc.nextInt();
                sc.nextLine(); // Consume newline

                // Use the UPDATED constructor including email
                passenger = new Passenger(name, age, email); 
                PassDao.savePassenger(passenger); 

                System.out.println(" New account created successfully! ID: " + passenger.getPersonID());
            } else {
                System.out.println("Welcome back, " + passenger.getName() + " (ID: " + passenger.getPersonID() + ")");
            }

            final int passengerFK = passenger.getPersonID(); // Foreign Key for Tickets

            // --- TRIP SELECTION ---

            System.out.println("\n Hello, " + passenger.getName() + "! Here are the available trips:\n");

            List<Trip> availableTrips = tripDao.getAllScheduledTrips();

            if (availableTrips.isEmpty()) {
                System.out.println(" No scheduled trips are currently available.");
                return;
            }

            for (int i = 0; i < availableTrips.size(); i++) {
                Trip trip = availableTrips.get(i);
                System.out.printf("  Option: %s Trip %s to %s | Departs: %s\n", 
                        (i + 1), trip.getTripNo(), trip.getDestinationLocation(), trip.getDepartureTime());
            }
         

            System.out.print("Choose your preferred trip number: ");
            int tripChoiceIndex = sc.nextInt() - 1; 
            sc.nextLine();

            if (tripChoiceIndex < 0 || tripChoiceIndex >= availableTrips.size()) { 
                System.out.println("Invalid choice. Exiting.");
                return;
            }

            Trip chosenTrip = availableTrips.get(tripChoiceIndex);

            // --- CLASS TIER SELECTION ---

            System.out.println("\n--- Class Tier Selection ---");
            System.out.println("  1. ECONOMY (Standard price)");
            System.out.println("  2. BUSINESS (Premium seating)");
            System.out.println("  3. FIRST (Luxury/private quarters)");
            System.out.print("Choose your class tier (1-3): ");
            int tierChoice = sc.nextInt();
            sc.nextLine();

            String chosenTier;
            switch (tierChoice) {
                case 2: chosenTier = "BUSINESS"; break;
                case 3: chosenTier = "FIRST"; break;
                case 1:
                default: chosenTier = "ECONOMY"; break;
            }

            // --- TICKET BOOKING ---
            
            // Use the NEW DAO method that filters by Class Tier
            Ticket availableTicket = tickDao.getAvailableTicketForTripAndClass(chosenTrip.getTripID(), chosenTier);

            if (availableTicket != null) {
                availableTicket.setPassengerID(passengerFK); 
                
                try { 
                    if (tickDao.bookTicket(availableTicket)) { // bookTicket will update PassengerFK
                        
                        // Add final price calculation/display logic here if needed
                        
                        System.out.println("\n Booking Confirmed!");
                        System.out.println("Trip: " + chosenTrip.getTripNo());
                        System.out.println("Tier: " + availableTicket.getClassTier()); // Display the chosen tier
                        System.out.println("Seat: " + availableTicket.getSeatNo());
                        System.out.println("Price: " + availableTicket.getPrice());

                    } else {
                        System.out.println(" Error: Failed to update the ticket status.");
                    }
                } catch (SQLException e) {
                    
                    System.err.println("CRITICAL ERROR: A database transaction failed during ticket booking.");
                    e.printStackTrace(); 
                    return; 
                }
                
                
            } else {
                System.out.println(" No available tickets for Trip " + chosenTrip.getTripNo() + " in " + chosenTier + " class.");
            }

            System.out.println("\nHappy Spacing! Enjoy your journey!\n");
        }
       
        
        
        else if(role.equalsIgnoreCase("A") || role.equalsIgnoreCase("Admin") ) {
        	
        	int choice = 1;
        	System.out.println("Please enter rocket ID to modify if needed");
        	int rocketID = sc.nextInt();
        	Rocket assignedRocket = RockDao.getRocketById(rocketID);
        	
        	while(choice!=0) {
        	
        		
        		displayAdminMenu();
        		choice = sc.nextInt();
        		
        		
        		processAdminChoice(choice, Admin, S,assignedRocket ,tripDao,RockDao, tickDao, PassDao);
        	}
        	
        	
        	
        
        }
        

    	else if(role.equalsIgnoreCase("P") || role.equalsIgnoreCase("Pilot")) {
    	int choice = 1;
    	System.out.println("Please enter rocket ID to modify if needed");
    		int rockedID = sc.nextInt();
    		Rocket assignedRocket = RockDao.getRocketById(rockedID);
    	    P.setAssignedRocket(assignedRocket);
    	    
    		while(choice != 0) {
    			displayPilotMenu();
    			choice = sc.nextInt();
    			
    			 processPilotChoice(choice,P,S,assignedRocket,RockDao);
    			
    		}
    		
    	}
        
        
        
        
        
    	else if(role.equalsIgnoreCase("T") || role.equalsIgnoreCase("Tech")) {
        	int choice = 1;
   
        		while(choice != 0) {
        			displayTechMenu();
        			choice = sc.nextInt();
        			
        		 processTechChoice(choice,Tech,MaintenanceDao,TechDao,RockDao);
        	
        		}
        		
        	}
        
        
        
        
        
        
        
        
    }

   

    public static void displayAdminMenu() {
        System.out.println("\n--- Administrator Control Panel ---");
        System.out.println("1. Schedule New Trip");
        System.out.println("2. Manage Rocket Status (Fuel/Inspect)");
        System.out.println("3. View Trip Manifest/Roster");
        System.out.println("4. Execute Launch Sequence");
        System.out.println("5. Register a new Rocket");
        System.out.println("0. Log Out");
        System.out.print("Enter command number: ");
    }

  
    
    
    
    
    
    
 // Inside SpaceportTEST.java

    public static void processAdminChoice(
            int choice,
            Adminstrator admin,
            Spaceport S,
            Rocket assignedRocket,
            tripDAO tripDao,
            RocketDAO rocketDao,
            ticketDAO ticketDao,
            PassengerDAO passengerDao) {

        Scanner sc = new Scanner(System.in);

        switch (choice) {

            case 1:
                System.out.println(">> Entering Trip Scheduling Module...");

                if (assignedRocket == null || assignedRocket.getRocketID() == 0) {
                    System.out.println("ERROR: Cannot schedule trip. No rocket assigned or Rocket ID is invalid.");
                    break;
                }

                // --- 1. GATHER INPUTS ---
                System.out.print("Enter Trip Number (e.g., T-01): ");
                String tripNo = sc.nextLine();

                System.out.print("Enter designated destination: ");
                String destination = sc.nextLine();

                System.out.print("Enter Launch Date (String, e.g., 2025-12-30): ");
                String launchDateStr = sc.nextLine();

                System.out.print("Enter Required Crew Amount: ");
                int requiredCrew;
                // Fixing Scanner usage: nextInt() or nextDouble() followed by nextLine() causes issues.
                try {
                    requiredCrew = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number entered for crew count. Aborting trip creation.");
                    break;
                }

                // --- 2. CREATE AND SAVE TRIP ---
                Trip t = new Trip();
                t.setTripNo(tripNo);
                t.setDestinationLocation(destination);
                t.setRequiredCrewCount(requiredCrew);

                // Removed the redundant t.setTripNo("T" + tripNo);
                t.setDepartureTime(new Date()); // Using current date placeholder
                t.setStatus(tripStatus.SCHEDULED);
                t.setRocketID(assignedRocket.getRocketID());
                t.setLaunchPadID(13);

                try {
                    tripDao.saveTrip(t);
                    int newTripID = t.getTripID(); // Assuming DAO populates the ID after saving

                    System.out.println("Trip " + tripNo + " Created Successfully (ID: " + newTripID + ")");

                    // --- 3. AUTOMATIC TICKET GENERATION (REQUIRED CHANGE) ---

                    // **CRITICAL FIX:** Getting capacity from the assigned rocket, not hardcoded '6'
                    int totalSeats = 6;
                    System.out.println("Generating tickets based on Rocket Capacity: " + totalSeats + " seats...");

                    // Define tiered capacity (Example ratios: 70/20/10)
                    int economyCapacity = (int) (totalSeats * 0.7);
                    int businessCapacity = (int) (totalSeats * 0.2);
                    int firstCapacity = totalSeats - economyCapacity - businessCapacity;

                    int seatCounter = 1;

                    // a. ECONOMY TICKETS ($500)
                    for (int i = 0; i < economyCapacity; i++) {
                        
                        Ticket tk = new Ticket(newTripID, String.valueOf(seatCounter), 500.00, "ECONOMY");
                        ticketDao.saveTicket(tk);
                        seatCounter++;
                    }

                    // b. BUSINESS TICKETS ($1500)
                    for (int i = 0; i < businessCapacity; i++) {
                        Ticket tk = new Ticket(newTripID, String.valueOf(seatCounter), 1500.00, "BUSINESS");
                        ticketDao.saveTicket(tk);
                        seatCounter++;
                    }

                    // c. FIRST CLASS TICKETS ($3000)
                    for (int i = 0; i < firstCapacity; i++) {
                        Ticket tk = new Ticket(newTripID, String.valueOf(seatCounter), 3000.00, "FIRST");
                        ticketDao.saveTicket(tk);
                        seatCounter++;
                    }
                    System.out.println("Successfully generated " + (seatCounter - 1) + " tiered tickets.");

                } catch (SQLException e) {
                    System.err.println("DATABASE ERROR during Trip/Ticket creation: " + e.getMessage());
                    e.printStackTrace();
                }

                break;

            case 2:
                // Case 2 logic remains unchanged, but input handling for fuel amount is fixed
                System.out.println("Entering Rocket Management Module...");
                System.out.println("Change status or Fill fuel?");
                String microChoice = sc.nextLine();

                if (assignedRocket == null) {
                    System.out.println("ERROR: No rocket is currently assigned to manage.");
                    break;
                }

                if (microChoice.equalsIgnoreCase("Fill fuel")) {

                    if (assignedRocket.getFuelTank() == null) {
                         assignedRocket.AssignFuelTank(new FuelTank(500000.0));
                    }

                    System.out.print("Enter fuel amount: ");
                    double fillfuel;
                    try {
                        // Fix: Use parseDouble(nextLine()) to handle input stream correctly
                        fillfuel = Double.parseDouble(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number entered for fuel amount. Aborting.");
                        break;
                    }

                    admin.callForRefuel(fillfuel, assignedRocket);
                    assignedRocket.getFuelTank().FillFuel(fillfuel, assignedRocket);

                    rocketDao.updateRocket(assignedRocket);

                    System.out.println("Fuel filled for Rocket " + assignedRocket.getRocketID());
                    System.out.println("Current fuel: " + assignedRocket.getFuelTank().getCurrentFuel() + " L");

                } else {

                    System.out.print("Enter new status (ready/maintenance/midair): ");
                    String StatusChoice = sc.nextLine();

                    if (StatusChoice.equalsIgnoreCase("ready"))
                        admin.ChangeRocketstatus(assignedRocket, RocketStatus.READY);
                    else if (StatusChoice.equalsIgnoreCase("maintenance") || StatusChoice.equalsIgnoreCase("main"))
                        admin.ChangeRocketstatus(assignedRocket, RocketStatus.IN_MAINTENANCE);
                    else if (StatusChoice.equalsIgnoreCase("midair"))
                        admin.ChangeRocketstatus(assignedRocket, RocketStatus.IN_FLIGHT);
                    else
                        System.out.println("Invalid status choice. Status was not changed.");

                    rocketDao.updateRocket(assignedRocket);
                    System.out.println("Rocket status updated to: " + assignedRocket.getStatus());
                }
                break;

            case 3:
                System.out.println(">> Displaying Manifest...");
                System.out.print("Enter Trip ID: ");
                int manifestTripID;
                try {
                    manifestTripID = Integer.parseInt(sc.nextLine()); // Fixed input
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number entered for Trip ID.");
                    break;
                }

                
                List<Ticket> tickets = ticketDao.getTicketsByTripId(manifestTripID);

                if (tickets.isEmpty()) {
                    System.out.println("No tickets created for this trip.");
                    break;
                }

                System.out.println("\nPassengers on Trip " + manifestTripID + ":");
                System.out.println("----------------------------------------");

                boolean hasBookedPassengers = false;
                for (Ticket tk : tickets) {
                    if (tk.getPassengerID() != 0) {
                        hasBookedPassengers = true;
                        Passenger p = passengerDao.getPassengerById(tk.getPassengerID());

                        if (p != null) {
                            System.out.printf("- %s (ID: %d) | Tier: %s | Seat: %s\n",
                                p.getName(), p.getPersonID(), tk.getClassTier(), tk.getSeatNo());
                        } else {
                            System.out.printf("- Passenger ID %d (NOT FOUND) | Tier: %s | Seat: %s\n",
                                tk.getPassengerID(), tk.getClassTier(), tk.getSeatNo());
                        }
                    }
                }

                if (!hasBookedPassengers) {
                    System.out.println("No passengers currently booked, but " + tickets.size() + " tickets are available.");
                }
                System.out.println("----------------------------------------");

                break;

            case 4:
                System.out.println(" Starting Launch Sequence...");

                if (assignedRocket != null && assignedRocket.getStatus() == RocketStatus.READY) {
                    assignedRocket.setStatus(RocketStatus.IN_FLIGHT);
                    rocketDao.updateRocket(assignedRocket);
                    System.out.println("Launch sequence started. Rocket status set to IN_FLIGHT.");
                } else if (assignedRocket == null) {
                    System.out.println("Launch failed: No rocket is currently assigned.");
                } else {
                    System.out.println("Launch failed: Rocket status is " + assignedRocket.getStatus() + ". Must be READY.");
                }

                break;

            case 5:
                System.out.println("Creating a new rocket");
                System.out.print("Enter Registeration ID: ");
                String RegisterationID = sc.nextLine();

                int spaceportFk = S.getSpaceportID();

                Rocket newRocket = new Rocket(spaceportFk, RegisterationID, RocketStatus.AVAILABLE, "S-05", 250000);
                try {
                    rocketDao.saveRocket(newRocket);
                    System.out.println("Rocket " + RegisterationID + " created successfully!");
                } catch (SQLException e) {
                    System.err.println("Error saving new rocket: " + e.getMessage());
                }
                break;

            default:
                System.out.println("Invalid command.");
        }
    }
    
    
    
    public static void displayPilotMenu() {
        System.out.println("\n--- Pilot Cockpit ---");
        System.out.println("1. Start rocket engine");
        System.out.println("2. Fuel Consumption estimation");
        System.out.println("3. Estimate distance");
        System.out.println("4. Adjust throttle");
        System.out.println("5. Execute launch");
        System.out.println("0. Exit");
        System.out.print("Enter command number: ");
    }

    public static void processPilotChoice(
            int choice,
            Pilot p1,
            Spaceport S,
            Rocket assignedRocket,
            RocketDAO RockDao) {

        Scanner sc = new Scanner(System.in);

        switch (choice) {

            case 1:
                System.out.println("Starting engines...");
                p1.startRocketEngines();
                break;

            case 2:
                System.out.println("Enter flight time (hours):");
                int time = sc.nextInt();
                sc.nextLine();
                
                System.out.print("Fuel consumption: ");
                p1.manageFuel(time);
                break;

            case 3:
                // Ensure Navigation Systems exist
                if (S.getNS().isEmpty()) {
                    Location L = new Location(
                            "New delhi space station",
                            28.50,
                            80.40,
                            0.005
                    );

                    S.addNS(new NavigationSystem("Mars L1", 33.20, 75.15, 0.015, L));
                    S.addNS(new NavigationSystem("Mars L2", 42.10, 81.05, 0.006, L));
                    S.addNS(new NavigationSystem("Jupiter Orbit", 45.50, 95.20, 0.025, L));
                    S.addNS(new NavigationSystem("Saturn Ring A", 75.20, 150.45, 0.150, L));
                }

                System.out.println("Available Destinations:");
                for (NavigationSystem ns : S.getNS()) {
                    System.out.println("- " + ns.getDestinationName());
                }

                System.out.println("Enter destination:");
                String destination = sc.nextLine();

                boolean found = false;
                for (NavigationSystem ns : S.getNS()) {
                    if (ns.getDestinationName().equalsIgnoreCase(destination)) {
                        p1.setNavSystem(ns);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Destination not found.");
                    break;
                }

                System.out.println(p1.calculateDistanceToDestination() + " Km");
                break;

            case 4:
                System.out.println("Enter throttle %:");
                double throttle = sc.nextDouble();
                p1.adjustThrottle(throttle);
                System.out.println("Current thrust:" +assignedRocket.getEngine().CalculateCurrentThrust());
               
                System.out.println("Carbon Footprint: "+ assignedRocket.getEngine().getFengine().getCarbonPerSecond(assignedRocket.getEngine()));
                assignedRocket.getEngine().getFengine().getCarbonPerSecond(assignedRocket.getEngine());
                break;

            case 5:
                System.out.println("Initiating launch...");
                assignedRocket.setStatus(RocketStatus.IN_FLIGHT);
                RockDao.updateRocket(assignedRocket);
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }
    
    
    
    
    
    
    
    

  

    public static void displayTechMenu() {
        System.out.println("\n--- Technician Workstation ---");
        System.out.println("1. View Hangar Rockets");
        System.out.println("2. Assign Service to Next Rocket");
        System.out.println("3. View Inventory Parts");
        System.out.println("4. Add Part to Inventory");
        System.out.println("5. Update Part Quantity");
        System.out.println("6. Hire New Technician");
        System.out.println("0. Log Out");
        System.out.print("Enter command number: ");
    }

 
    public static void processTechChoice(int choice, Technician tech, MaintenanceTeamDAO teamDao, TechnicianDAO techDao, RocketDAO rocketDao) {
        
        Scanner sc = new Scanner(System.in);
        
        // --- TESTING ASSUMPTION ---
        // In a real system, you'd load the Technician's team upon login.
        // Ensure the Technician object has a method like getTeamID() or use a placeholder:
        int technicianTeamID = 1; 
        // --------------------------

        try {
            switch (choice) {

                case 1:
                    System.out.println("\n--- Rockets in Maintenance (Team ID: " + technicianTeamID + ") ---");
                    
                    // Correction: Uses the correct object name (rocketDao) and returns List<Rocket>
                    List<Rocket> rockets = rocketDao.getAllRockets();
                   
                    if (rockets.isEmpty()) {
                        System.out.println("No rockets currently in the hangar needing service.");
                    } else {
                        for (Rocket r : rockets) {
                            
                        	if(r.getStatus() == RocketStatus.IN_MAINTENANCE) {
                        	
                            System.out.printf(" %s (%s) - Status: %s (DB ID: %d)%n",  // C programming
                                r.getRegistrationID(), 
                                r.getEngineType(), 
                                r.getStatus().toString(), 
                                r.getRocketID()
     
                            );
                        }
                    }
                    break;
                    }
                case 2:
          
                	List<Rocket> rockets1 = rocketDao.getAllRockets();
                 for(int i=0 ; i < rockets1.size() ; i++) {
                	 if(rockets1.get(i).getStatus() == RocketStatus.AVAILABLE)
                		 System.out.println(rockets1.get(i));  
                 }
                 System.out.println("Enter ID to make assign service");
                 int microchoice = sc.nextInt();
                 rocketDao.updateRocketStatus(microchoice,"IN_MAINTENANCE");
                 
                 
                 break;
                case 3:
                    // Case 3: View Inventory Parts (Global)
                    List<Part> inventory = teamDao.getGlobalInventory();
                     
                    if (inventory.isEmpty()) {
                        System.out.println("The central inventory is empty.");
                    } else {
                        // Assuming this utility method exists in your console class
                        displayInventory(inventory); 
                    }
                    break;

                case 4:
                    // Case 4: Add Part to Inventory
                    sc.nextLine(); // Consume remaining newline before nextLine()
                    System.out.print("Enter part name: ");
                    String partName = sc.nextLine();
                    System.out.print("Enter quantity to ADD: ");
                    int qty = sc.nextInt();
                     sc.nextLine();
                    if (teamDao.addPartToInventory(partName, qty)) {
                        System.out.println("SUCCESS: Added " + qty + " of '" + partName + "' to central inventory.");
                    } else {
                        System.out.println("ERROR: Could not add stock. Check database connection.");
                    }
                    break;

                case 5:
                    // Case 5: Update Part Quantity (Sets stock to a new, specific value)
                    sc.nextLine(); // Consume remaining newline before nextLine()
                    System.out.print("Enter part name: ");
                    String updateName = sc.nextLine();
                    System.out.print("Enter NEW total quantity: ");
                    int newQty = sc.nextInt();
                     
                    if (teamDao.setPartQuantity(updateName, newQty)) {
                        System.out.println("SUCCESS: Set quantity of '" + updateName + "' to " + newQty + ".");
                    } else {
                        System.out.println("ERROR: Could not set quantity. Part may not exist or DB failed.");
                    }
                    break;

                case 6:
                 
                    sc.nextLine(); 
                     
                    System.out.print("Enter new technician name: ");
                    String newTechName = sc.nextLine();
                    System.out.print("Enter Employee ID: ");
                    String empID = sc.nextLine();
                    System.out.print("Enter Department: ");
                    String dept = sc.nextLine();
                    System.out.print("Enter Salary: ");
                    double salary = sc.nextDouble();
                    System.out.print("Enter Age: ");
                    int age = sc.nextInt();

                    Technician newTech = new Technician(newTechName, empID, dept, salary, age);

                    if (techDao.hireNewTechnician(newTech)) {
                        System.out.println("\n--- HIRING SUCCESSFUL ---");
                        System.out.println("New Technician " + newTech.getName() + " hired with ID: " + newTech.getPersonID());
                    } else {
                        System.out.println("Hiring failed. Check input or DB connection.");
                    }
                    break;

                case 0:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        } catch (InputMismatchException e) {
            System.err.println("ERROR: Invalid input type. Please enter a number where requested.");
         
        } catch (SQLException e) {
            System.err.println("DATABASE ERROR: Operation failed: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
 
 
 
 
 
 
 

public static void displayInventory(List<Part> parts) {
  System.out.println("\n--- Central Parts Inventory ---");
  
  
  for (Part p : parts) {
      System.out.printf("%-10d %-30s %-10d %n", 
          p.getPartID(), 
          p.getName(), 
          p.getQuantity()
      );
  }
  System.out.println("--------------------------------------------------");
}
 
 
 
 
 
}
