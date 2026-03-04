package SpaceportSystem.UI;

import SpaceportSystem.*;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * MainFX - Futuristic Space-Themed Spaceport Management System
 * Complete rewrite with modern UI/UX and dark space aesthetics
 */
public class MainFX extends Application {
    
    private SpaceportDAO spaceportDao;
    private PassengerDAO passengerDao;
    private RocketDAO rocketDao;
    private LaunchPadDAO launchPadDao;
    private tripDAO tripDao;
    private ticketDAO ticketDao;
    private EmployeeDAO employeeDao;
    private PartDAO partDao;
    private MaintenanceTeamDAO maintenanceDao;
    private TechnicianDAO technicianDao;
    private Spaceport primarySpaceport;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize all DAOs
        initializeDAOs();
        
        // Create navigation manager
        NavigationManager navManager = new NavigationManager(
            primaryStage,
            spaceportDao,
            passengerDao,
            rocketDao,
            launchPadDao,
            tripDao,
            ticketDao,
            employeeDao,
            partDao,
            maintenanceDao,
            technicianDao,
            primarySpaceport
        );
        
        // Configure stage
        primaryStage.setTitle("PROTOS SPACEPORT SYSTEM");
        primaryStage.setWidth(1400);
        primaryStage.setHeight(900);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        
        // Show role selection screen
        navManager.showRoleSelection();
        
        primaryStage.show();
    }
    
    private void initializeDAOs() {
        DBManager db = new DBManager();
        
        spaceportDao = new SpaceportDAO();
        passengerDao = new PassengerDAO();
        rocketDao = new RocketDAO();
        launchPadDao = new LaunchPadDAO();
        tripDao = new tripDAO(rocketDao, launchPadDao);
        ticketDao = new ticketDAO();
        employeeDao = new EmployeeDAO();
        partDao = new PartDAO();
        maintenanceDao = new MaintenanceTeamDAO(partDao, rocketDao);
        technicianDao = new TechnicianDAO();
        
        // Initialize primary spaceport
        primarySpaceport = spaceportDao.getOrCreatePrimarySpaceport();
        spaceportDao.saveOrUpdateSpaceport(primarySpaceport);
        
        System.out.println("✓ System Initialized - Spaceport: " + primarySpaceport.getName());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
