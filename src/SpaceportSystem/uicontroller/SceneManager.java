package SpaceportSystem.uicontroller;

import SpaceportSystem.*;
import SpaceportSystem.UI.views.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Central navigation and dependency manager for the JavaFX UI.
 * Holds references to the primary Stage, DAOs, and current user/session state.
 */
public class SceneManager {

    // --- Core JavaFX ---
    private final Stage primaryStage;

    // --- Backend DAOs (assumed to exist as per instructions) ---
    private final PassengerDAO passengerDAO;
    private final tripDAO tripDAO;
    private final ticketDAO ticketDAO;
    private final RocketDAO rocketDAO;
    private final EmployeeDAO employeeDAO;
    private final MaintenanceTeamDAO maintenanceDAO;

    // --- Session State ---
    private Passenger currentPassenger;
    private Employee currentEmployee; // Could be Pilot, Technician, Adminstrator subclass

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize DAOs – these map to your existing backend classes
        this.passengerDAO = new PassengerDAO();
        this.rocketDAO = new RocketDAO();
        this.tripDAO = new tripDAO(rocketDAO, new LaunchPadDAO());
        this.ticketDAO = new ticketDAO();
        this.employeeDAO = new EmployeeDAO();
        this.maintenanceDAO = new MaintenanceTeamDAO(new PartDAO(), rocketDAO);

        configureStage();
    }

    private void configureStage() {
        primaryStage.setTitle("Spaceport System - Clean Aerospace UI");
    }

    // --- Accessors for DAOs & Session State ---

    public PassengerDAO getPassengerDAO() {
        return passengerDAO;
    }

    public tripDAO getTripDAO() {
        return tripDAO;
    }

    public ticketDAO getTicketDAO() {
        return ticketDAO;
    }

    public RocketDAO getRocketDAO() {
        return rocketDAO;
    }

    public EmployeeDAO getEmployeeDAO() {
        return employeeDAO;
    }

    public MaintenanceTeamDAO getMaintenanceDAO() {
        return maintenanceDAO;
    }

    public Passenger getCurrentPassenger() {
        return currentPassenger;
    }

    public void setCurrentPassenger(Passenger currentPassenger) {
        this.currentPassenger = currentPassenger;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    // --- Navigation Helpers ---

    private void setScene(Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * Initial screen
     */
    public void showPassengerLogin() {
        PassengerLoginView root = new PassengerLoginView(this);
        Scene scene = new Scene(root, 900, 600);
        setScene(scene);
    }

    /**
     * Show the passenger dashboard after successful login/registration.
     */
    public void showPassengerDashboard(Passenger passenger) {
        this.currentPassenger = passenger;
        PassengerDashboard root = new PassengerDashboard(this, passenger);
        Scene scene = new Scene(root, 1000, 650);
        setScene(scene);
    }

    /**
     * Show the booking confirmation view using the chosen Trip and Ticket.
     */
    public void showBookingConfirmation(Trip trip, Ticket ticket) {
        BookingConfirmationView root = new BookingConfirmationView(this, trip, ticket);
        Scene scene = new Scene(root, 800, 500);
        setScene(scene);
    }

    /**
     * Show the admin dashboard.
     */
    public void showAdminDashboard(Adminstrator admin) {
        this.currentEmployee = admin;
        AdminDashboard root = new AdminDashboard(this, admin);
        Scene scene = new Scene(root, 1100, 700);
        setScene(scene);
    }

    /**
     * Show the staff dashboard (Pilot or Technician).
     */
    public void showStaffDashboard(Employee employee) {
        this.currentEmployee = employee;
        StaffDashboard root = new StaffDashboard(this, employee);
        Scene scene = new Scene(root, 1000, 650);
        setScene(scene);
    }
}

package SpaceportSystem.uicontroller;

import SpaceportSystem.*;
import SpaceportSystem.UI.views.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SceneManager handles navigation between all views and maintains DAO references.
 * All DAOs are initialized once in MainFX and passed here for persistence.
 */
public class SceneManager {
    
    private final Stage primaryStage;
    
    // All DAO references (initialized once in MainFX)
    private final SpaceportDAO spaceportDao;
    private final PassengerDAO passengerDao;
    private final RocketDAO rocketDao;
    private final LaunchPadDAO launchPadDao;
    private final tripDAO tripDao;
    private final ticketDAO ticketDao;
    private final EmployeeDAO employeeDao;
    private final PartDAO partDao;
    private final MaintenanceTeamDAO maintenanceDao;
    private final TechnicianDAO technicianDao;
    
    // Primary Spaceport instance
    private final Spaceport primarySpaceport;
    
    // Current user context (set after login)
    private Passenger currentPassenger;
    private Employee currentEmployee;
    private String currentEmployeeRole; // "Pilot" or "Technician"
    
    public SceneManager(Stage primaryStage,
                        SpaceportDAO spaceportDao,
                        PassengerDAO passengerDao,
                        RocketDAO rocketDao,
                        LaunchPadDAO launchPadDao,
                        tripDAO tripDao,
                        ticketDAO ticketDao,
                        EmployeeDAO employeeDao,
                        PartDAO partDao,
                        MaintenanceTeamDAO maintenanceDao,
                        TechnicianDAO technicianDao,
                        Spaceport primarySpaceport) {
        this.primaryStage = primaryStage;
        this.spaceportDao = spaceportDao;
        this.passengerDao = passengerDao;
        this.rocketDao = rocketDao;
        this.launchPadDao = launchPadDao;
        this.tripDao = tripDao;
        this.ticketDao = ticketDao;
        this.employeeDao = employeeDao;
        this.partDao = partDao;
        this.maintenanceDao = maintenanceDao;
        this.technicianDao = technicianDao;
        
        // Primary spaceport passed from MainFX to avoid double init
        this.primarySpaceport = primarySpaceport;
    }
    
    // Navigation methods
    public void navigateToPassengerLogin() {
        PassengerLoginView view = new PassengerLoginView(this);
        primaryStage.setScene(new Scene(view.getRoot(), 900, 700));
    }
    
    public void navigateToPassengerDashboard(Passenger passenger) {
        this.currentPassenger = passenger;
        PassengerDashboard view = new PassengerDashboard(this, passenger);
        primaryStage.setScene(new Scene(view.getRoot(), 1200, 800));
    }
    
    public void navigateToAdminDashboard() {
        AdminDashboard view = new AdminDashboard(this);
        primaryStage.setScene(new Scene(view.getRoot(), 1200, 800));
    }
    
    public void navigateToStaffDashboard(Employee employee, String role) {
        this.currentEmployee = employee;
        this.currentEmployeeRole = role;
        StaffDashboard view = new StaffDashboard(this, employee, role);
        primaryStage.setScene(new Scene(view.getRoot(), 1200, 800));
    }

    /**
     * Show booking confirmation screen instead of an alert.
     */
    public void showBookingConfirmation(Trip trip, Ticket ticket) {
        BookingConfirmationView view = new BookingConfirmationView(this, trip, ticket);
        primaryStage.setScene(new Scene(view.getRoot(), 900, 700));
    }
    
    // Getters for DAOs
    public SpaceportDAO getSpaceportDao() { return spaceportDao; }
    public PassengerDAO getPassengerDao() { return passengerDao; }
    public RocketDAO getRocketDao() { return rocketDao; }
    public LaunchPadDAO getLaunchPadDao() { return launchPadDao; }
    public tripDAO getTripDao() { return tripDao; }
    public ticketDAO getTicketDao() { return ticketDao; }
    public EmployeeDAO getEmployeeDao() { return employeeDao; }
    public PartDAO getPartDao() { return partDao; }
    public MaintenanceTeamDAO getMaintenanceDao() { return maintenanceDao; }
    public TechnicianDAO getTechnicianDao() { return technicianDao; }
    
    // Getters for context
    public Spaceport getPrimarySpaceport() { return primarySpaceport; }
    public Passenger getCurrentPassenger() { return currentPassenger; }
    public Employee getCurrentEmployee() { return currentEmployee; }
    public String getCurrentEmployeeRole() { return currentEmployeeRole; }
}
