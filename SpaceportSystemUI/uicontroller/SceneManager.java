package SpaceportSystem.uicontroller;

import SpaceportSystem.UI.views.PassengerLoginView;
import SpaceportSystem.UI.views.PassengerDashboard;
import SpaceportSystem.UI.views.BookingConfirmationView;
import SpaceportSystem.UI.views.AdminDashboard;
import SpaceportSystem.UI.views.StaffDashboard;

import javafx.scene.Scene;
import javafx.stage.Stage;

// Assume these backend classes exist with appropriate APIs.
import SpaceportSystem.dao.PassDao;
import SpaceportSystem.dao.TripDao;
import SpaceportSystem.dao.TicketDao;
import SpaceportSystem.dao.RocketDao;
import SpaceportSystem.dao.EmployeeDao;
import SpaceportSystem.dao.MaintenanceDao;

import SpaceportSystem.model.Passenger;
import SpaceportSystem.model.Trip;
import SpaceportSystem.model.Ticket;
import SpaceportSystem.model.Employee;

public class SceneManager {

    private static SceneManager instance;

    private final Stage primaryStage;

    private final PassDao passDao;
    private final TripDao tripDao;
    private final TicketDao ticketDao;
    private final RocketDao rocketDao;
    private final EmployeeDao employeeDao;
    private final MaintenanceDao maintenanceDao;

    private Passenger currentPassenger;
    private Employee currentEmployee;   // For admin or staff

    private SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize DAOs (assume default constructors exist)
        this.passDao = new PassDao();
        this.tripDao = new TripDao();
        this.ticketDao = new TicketDao();
        this.rocketDao = new RocketDao();
        this.employeeDao = new EmployeeDao();
        this.maintenanceDao = new MaintenanceDao();

        this.primaryStage.setTitle("Spaceport System");
    }

    public static void initialize(Stage primaryStage) {
        if (instance == null) {
            instance = new SceneManager(primaryStage);
        }
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SceneManager not initialized. Call initialize() first.");
        }
        return instance;
    }

    // ===== Public accessors for DAOs & state =====
    public PassDao getPassDao() {
        return passDao;
    }

    public TripDao getTripDao() {
        return tripDao;
    }

    public TicketDao getTicketDao() {
        return ticketDao;
    }

    public RocketDao getRocketDao() {
        return rocketDao;
    }

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public MaintenanceDao getMaintenanceDao() {
        return maintenanceDao;
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

    // ===== Scene navigation methods =====

    public void showPassengerLogin() {
        PassengerLoginView root = new PassengerLoginView(this);
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showPassengerDashboard() {
        if (currentPassenger == null) {
            showPassengerLogin();
            return;
        }
        PassengerDashboard root = new PassengerDashboard(this);
        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showBookingConfirmation(Trip trip, Ticket ticket) {
        BookingConfirmationView root = new BookingConfirmationView(this, trip, ticket);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showAdminDashboard() {
        AdminDashboard root = new AdminDashboard(this);
        Scene scene = new Scene(root, 1200, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showStaffDashboard() {
        StaffDashboard root = new StaffDashboard(this);
        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
