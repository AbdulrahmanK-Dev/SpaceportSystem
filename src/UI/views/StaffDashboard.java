package UI.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uicontroller.SceneManager;

public class StaffDashboard {

    private SceneManager sceneManager;
    private Button logoutButton;

    public StaffDashboard(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
    }

    public Parent getView() {
        // Top bar with title and logout
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15));
        
        Label title = new Label("STAFF DASHBOARD");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #00ffc8;");
        
        topBar.getChildren().addAll(title, logoutButton);
        HBox.setHgrow(title, javafx.scene.layout.Priority.ALWAYS);
        
        // Main content area
        VBox content = new VBox(20);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.CENTER);
        
        Label welcomeLabel = new Label("Welcome to Staff Dashboard");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        
        Label infoLabel = new Label("This dashboard is a placeholder for staff functionality.\n" +
                                    "Pilot and Technician dashboards will be implemented here.");
        infoLabel.setStyle("-fx-text-fill: #00ffc8;");
        infoLabel.setWrapText(true);
        infoLabel.setAlignment(Pos.CENTER);
        
        content.getChildren().addAll(welcomeLabel, infoLabel);
        
        // Main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1a1a2e;");
        layout.getChildren().addAll(topBar, content);
        
        // Event handlers
        logoutButton.setOnAction(e -> sceneManager.navigateToPassengerLogin());
        
        return layout;
    }
}











