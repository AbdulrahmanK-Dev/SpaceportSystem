package SpaceportSystem.UI;

import SpaceportSystem.*;
import SpaceportSystem.UI.views.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * NavigationManager - Handles all navigation between views
 */
public class NavigationManager {
    
    private final Stage stage;
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
    private final Spaceport primarySpaceport;
    
    // Current user context
    private Passenger currentPassenger;
    private Employee currentEmployee;
    
    public NavigationManager(
            Stage stage,
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
        
        this.stage = stage;
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
        this.primarySpaceport = primarySpaceport;
    }
    
    // Navigation methods
    
    public void showRoleSelection() {
        RoleSelectionView view = new RoleSelectionView(this);
        setScene(view.getScene(), "PROTOS SPACEPORT - ROLE SELECTION");
    }
    
    public void showPassengerLogin() {
        PassengerLoginView view = new PassengerLoginView(this);
        setScene(view.getScene(), "PASSENGER LOGIN");
    }
    
    public void showPassengerDashboard(Passenger passenger) {
        this.currentPassenger = passenger;
        PassengerDashboardView view = new PassengerDashboardView(this, passenger);
        setScene(view.getScene(), "PASSENGER DASHBOARD - " + passenger.getName());
    }
    
    public void showBookingConfirmation(Trip trip, Ticket ticket) {
        BookingConfirmationView view = new BookingConfirmationView(this, trip, ticket);
        setScene(view.getScene(), "BOOKING CONFIRMED");
    }
    
    public void showAdminDashboard() {
        AdminDashboardView view = new AdminDashboardView(this);
        setScene(view.getScene(), "ADMINISTRATOR CONTROL PANEL");
    }
    
    public void showPilotDashboard() {
        PilotDashboardView view = new PilotDashboardView(this);
        setScene(view.getScene(), "PILOT COCKPIT");
    }
    
    public void showTechnicianDashboard() {
        TechnicianDashboardView view = new TechnicianDashboardView(this);
        setScene(view.getScene(), "TECHNICIAN WORKSTATION");
    }
    
    // Helper method to set scene
    private void setScene(Scene scene, String title) {
        stage.setScene(scene);
        stage.setTitle(title);
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
    public Spaceport getPrimarySpaceport() { return primarySpaceport; }
    
    // Getters for current user
    public Passenger getCurrentPassenger() { return currentPassenger; }
    public Employee getCurrentEmployee() { return currentEmployee; }
}
