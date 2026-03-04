package UI;

import SpaceportSystem.*;
import javafx.application.Application;
import javafx.stage.Stage;
import uicontroller.SceneManager;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize all DAOs exactly as in SpaceportTEST.java
        DBManager DB = new DBManager();
        SpaceportDAO dao = new SpaceportDAO();
        PassengerDAO PassDao = new PassengerDAO();
        RocketDAO RockDao = new RocketDAO();
        LaunchPadDAO PadDao = new LaunchPadDAO();
        tripDAO tripDao = new tripDAO(RockDao, PadDao);
        ticketDAO tickDao = new ticketDAO();
        EmployeeDAO EmployDao = new EmployeeDAO();
        PartDAO PartDao = new PartDAO();
        MaintenanceTeamDAO MaintenanceDao = new MaintenanceTeamDAO(PartDao, RockDao);
        TechnicianDAO TechDao = new TechnicianDAO();
        
        // Initialize primary spaceport (as in SpaceportTEST)
        Spaceport PrimarySpaceport = dao.getOrCreatePrimarySpaceport();
        dao.saveOrUpdateSpaceport(PrimarySpaceport);
        
        // Create SceneManager with all DAOs
        SceneManager sceneManager = new SceneManager(
            primaryStage,
            dao,
            PassDao,
            RockDao,
            PadDao,
            tripDao,
            tickDao,
            EmployDao,
            PartDao,
            MaintenanceDao,
            TechDao
        );
        
        // Set stage properties
        primaryStage.setTitle("Spaceport System");
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);
        
        // Navigate to initial view (Passenger Login)
        sceneManager.navigateToPassengerLogin();
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
