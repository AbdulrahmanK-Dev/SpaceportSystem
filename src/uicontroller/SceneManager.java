package uicontroller;

import SpaceportSystem.*;
import UI.views.AdminDashboard;
import UI.views.PassengerDashboard;
import UI.views.PassengerLoginView;
import UI.views.StaffDashboard;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SceneManager (legacy UI)
 *
 * Used by the non‑module `UI.*` package:
 * - `UI.MainFX`
 * - `UI.views.PassengerLoginView`
 * - `UI.views.PassengerDashboard`
 * - `UI.views.AdminDashboard`
 * - `UI.views.StaffDashboard`
 */
public class SceneManager {

    // Primary stage
    private final Stage primaryStage;

    // DAO references passed from MainFX
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

    // Optional shared context
    private final Spaceport primarySpaceport;

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
                        TechnicianDAO technicianDao) {
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

        // Initialize / cache primary spaceport similarly to the console app
        this.primarySpaceport = spaceportDao.getOrCreatePrimarySpaceport();
        spaceportDao.saveOrUpdateSpaceport(primarySpaceport);
    }

    // ------------------------------------------------------------------
    // Navigation helpers (names must match existing calls)
    // ------------------------------------------------------------------

    /** Initial screen – passenger login / registration. */
    public void navigateToPassengerLogin() {
        PassengerLoginView view = new PassengerLoginView(this);
        Scene scene = new Scene(view.getView(), 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Passenger Login");
        primaryStage.show();
    }

    /** Passenger dashboard after successful login/registration. */
    public void navigateToPassengerDashboard(Passenger passenger) {
        PassengerDashboard view = new PassengerDashboard(this);
        view.setPassenger(passenger);
        Scene scene = new Scene(view.getView(), 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Passenger Dashboard");
        primaryStage.show();
    }

    /** Simple admin dashboard shell for the legacy UI. */
    public void navigateToAdminDashboard() {
        AdminDashboard view = new AdminDashboard(this);
        Scene scene = new Scene(view.getView(), 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.show();
    }

    /** Staff dashboard placeholder (pilot/technician legacy entry point). */
    public void navigateToStaffDashboard() {
        StaffDashboard view = new StaffDashboard(this);
        Scene scene = new Scene(view.getView(), 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Dashboard");
        primaryStage.show();
    }

    // ------------------------------------------------------------------
    // DAO getters – match method names used in the views
    // ------------------------------------------------------------------

    public SpaceportDAO getSpaceportDAO() {
        return spaceportDao;
    }

    public PassengerDAO getPassengerDAO() {
        return passengerDao;
    }

    public RocketDAO getRocketDAO() {
        return rocketDao;
    }

    public LaunchPadDAO getLaunchPadDAO() {
        return launchPadDao;
    }

    public tripDAO getTripDAO() {
        return tripDao;
    }

    public ticketDAO getTicketDAO() {
        return ticketDao;
    }

    public EmployeeDAO getEmployeeDAO() {
        return employeeDao;
    }

    public PartDAO getPartDAO() {
        return partDao;
    }

    public MaintenanceTeamDAO getMaintenanceDAO() {
        return maintenanceDao;
    }

    public TechnicianDAO getTechnicianDAO() {
        return technicianDao;
    }

    public Spaceport getPrimarySpaceport() {
        return primarySpaceport;
    }
}








