package UI.views;

import SpaceportSystem.Passenger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uicontroller.SceneManager;

public class PassengerLoginView {

    // --- UI Component Declarations ---
    private TextField nameField;
    private TextField emailField;
    private TextField ageField;
    private Label messageLabel;
    private Button continueButton;
    private Button backButton;
    
    // Role selection buttons
    private Button passengerButton;
    private Button adminButton;
    private Button staffButton;
    
    private SceneManager sceneManager;

    public PassengerLoginView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        
        // Initialize fields
        nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setMinWidth(250);

        emailField = new TextField();
        emailField.setPromptText("Enter your contact email");
        emailField.setMinWidth(250);

        ageField = new TextField();
        ageField.setPromptText("Enter your age (for new users)");
        ageField.setDisable(true);
        ageField.setMinWidth(250);

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #ff6347; -fx-font-weight: bold;");

        continueButton = new Button("Continue to Trips");
        backButton = new Button("Back");
        
        continueButton.setMinWidth(250);
        backButton.setMinWidth(250);
        
        // Role selection buttons
        passengerButton = new Button("Passenger");
        adminButton = new Button("Admin");
        staffButton = new Button("Staff");
        
        passengerButton.setMinWidth(120);
        adminButton.setMinWidth(120);
        staffButton.setMinWidth(120);
    }

    public Parent getView() {
        // Role selection header
        HBox roleHeader = new HBox(15);
        roleHeader.setAlignment(Pos.TOP_CENTER);
        roleHeader.setPadding(new Insets(20));
        roleHeader.getChildren().addAll(passengerButton, adminButton, staffButton);
        
        // Set button styles
        passengerButton.setStyle("-fx-background-color: #00ffc8; -fx-text-fill: #000; -fx-font-weight: bold;");
        adminButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");
        staffButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");
        
        // Input Grid Setup
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(15);
        inputGrid.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label("Full Name:");
        nameLabel.setStyle("-fx-text-fill: white;");
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-text-fill: white;");
        Label ageLabel = new Label("Age:");
        ageLabel.setStyle("-fx-text-fill: white;");
        
        inputGrid.addRow(0, nameLabel, nameField);
        inputGrid.addRow(1, emailLabel, emailField);
        inputGrid.addRow(2, ageLabel, ageField);

        // Layout Container
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: #1a1a2e;");

        Label title = new Label("PASSENGER LOGIN / REGISTRATION");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #00ffc8;");

        layout.getChildren().addAll(
            roleHeader,
            title,
            messageLabel, 
            inputGrid,
            new VBox(10, continueButton, backButton)
        );
        
        // Event Handlers
        continueButton.setOnAction(e -> handleContinue());
        backButton.setOnAction(e -> handleBack());
        
        // Role button handlers
        passengerButton.setOnAction(e -> {
            // Already on passenger login, just highlight
            passengerButton.setStyle("-fx-background-color: #00ffc8; -fx-text-fill: #000; -fx-font-weight: bold;");
            adminButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");
            staffButton.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");
        });
        
        adminButton.setOnAction(e -> sceneManager.navigateToAdminDashboard());
        staffButton.setOnAction(e -> sceneManager.navigateToStaffDashboard());
        
        // Email field listener - enable age field if email not found
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                Passenger existing = sceneManager.getPassengerDAO().getPassengerByEmail(newValue.trim());
                if (existing == null) {
                    ageField.setDisable(false);
                    messageLabel.setText("New user detected. Please enter your age.");
                    messageLabel.setStyle("-fx-text-fill: #00ffc8;");
                } else {
                    ageField.setDisable(true);
                    ageField.clear();
                    messageLabel.setText("Welcome back, " + existing.getName() + "!");
                    messageLabel.setStyle("-fx-text-fill: #00ffc8;");
                }
            } else {
                ageField.setDisable(true);
                messageLabel.setText("");
            }
        });

        return layout;
    }
    
    private void handleContinue() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String ageText = ageField.getText().trim();
        
        if (name.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Please enter both name and email.");
            messageLabel.setStyle("-fx-text-fill: #ff6347;");
            return;
        }
        
        // Check if passenger exists
        Passenger passenger = sceneManager.getPassengerDAO().getPassengerByEmail(email);
        
        if (passenger == null) {
            // New user - require age
            if (ageText.isEmpty()) {
                messageLabel.setText("Please enter your age to create a new account.");
                messageLabel.setStyle("-fx-text-fill: #ff6347;");
                return;
            }
            
            try {
                int age = Integer.parseInt(ageText);
                passenger = new Passenger(name, age, email);
                sceneManager.getPassengerDAO().savePassenger(passenger);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Created");
                alert.setHeaderText("Success!");
                alert.setContentText("New account created successfully! ID: " + passenger.getPersonID());
                alert.showAndWait();
                
            } catch (NumberFormatException e) {
                messageLabel.setText("Please enter a valid age.");
                messageLabel.setStyle("-fx-text-fill: #ff6347;");
                return;
            }
        } else {
            // Existing user - update name if changed
            if (!passenger.getName().equals(name)) {
                passenger.setName(name);
                sceneManager.getPassengerDAO().updatePassenger(passenger);
            }
        }
        
        // Navigate to dashboard
        sceneManager.navigateToPassengerDashboard(passenger);
    }
    
    private void handleBack() {
        // Could navigate to a role selection screen, but for now just clear fields
        nameField.clear();
        emailField.clear();
        ageField.clear();
        messageLabel.setText("");
    }

    // Getters for testing/debugging
    public TextField getNameField() { return nameField; }
    public TextField getEmailField() { return emailField; }
    public TextField getAgeField() { return ageField; }
    public Label getMessageLabel() { return messageLabel; }
}
