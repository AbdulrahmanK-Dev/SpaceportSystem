package SpaceportSystem.UI.views;

import SpaceportSystem.*;
import SpaceportSystem.UI.NavigationManager;
import SpaceportSystem.UI.SpaceTheme;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

/**
 * PassengerLoginView - Login/Registration screen for passengers
 */
public class PassengerLoginView {
    
    private final NavigationManager navManager;
    private final BorderPane root;
    
    private TextField nameField;
    private TextField emailField;
    private TextField ageField;
    private Label messageLabel;
    
    public PassengerLoginView(NavigationManager navManager) {
        this.navManager = navManager;
        this.root = new BorderPane();
        buildUI();
    }
    
    private void buildUI() {
        root.setStyle(SpaceTheme.getMainBackgroundStyle());
        
        // Top bar with back button
        HBox topBar = createTopBar();
        root.setTop(topBar);
        
        // Center - Login form
        VBox centerContent = createLoginForm();
        root.setCenter(centerContent);
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Button backButton = new Button("← BACK");
        backButton.setStyle(SpaceTheme.getButtonSecondaryStyle());
        backButton.setOnAction(e -> navManager.showRoleSelection());
        
        Label title = new Label("PASSENGER ACCESS");
        title.setStyle("-fx-font-size: 20px;" +
                      "-fx-font-weight: bold;" +
                      "-fx-text-fill: " + SpaceTheme.ACCENT_PRIMARY + ";");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        topBar.getChildren().addAll(backButton, spacer, title, spacer, new Label());
        return topBar;
    }
    
    private VBox createLoginForm() {
        VBox container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));
        
        // Login card
        VBox loginCard = new VBox(25);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(50));
        loginCard.setMaxWidth(500);
        loginCard.setStyle(SpaceTheme.getCardStyle());
        
        // Title
        Label cardTitle = new Label("PASSENGER LOGIN");
        cardTitle.setStyle("-fx-font-size: 32px;" +
                          "-fx-font-weight: bold;" +
                          "-fx-text-fill: " + SpaceTheme.TEXT_PRIMARY + ";");
        
        Label subtitle = new Label("Enter your credentials or create a new account");
        subtitle.setStyle(SpaceTheme.getSubtitleStyle());
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(400);
        subtitle.setAlignment(Pos.CENTER);
        
        // Name field
        VBox nameBox = createFormField("FULL NAME", "Enter your full name");
        nameField = (TextField) nameBox.getChildren().get(1);
        
        // Email field
        VBox emailBox = createFormField("CONTACT EMAIL", "Enter your email address");
        emailField = (TextField) emailBox.getChildren().get(1);
        
        // Age field (initially hidden)
        VBox ageBox = createFormField("AGE", "Enter your age (new users only)");
        ageField = (TextField) ageBox.getChildren().get(1);
        ageBox.setVisible(false);
        ageBox.setManaged(false);
        
        // Message label
        messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 13px;" +
                             "-fx-text-fill: " + SpaceTheme.ACCENT_SUCCESS + ";" +
                             "-fx-padding: 10;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        
        // Email field listener to check if user exists
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                Passenger existing = navManager.getPassengerDao().getPassengerByEmail(newVal.trim());
                if (existing == null) {
                    ageBox.setVisible(true);
                    ageBox.setManaged(true);
                    messageLabel.setText("✓ New user detected. Please provide your age.");
                    messageLabel.setStyle("-fx-font-size: 13px;" +
                                         "-fx-text-fill: " + SpaceTheme.ACCENT_PRIMARY + ";");
                } else {
                    ageBox.setVisible(false);
                    ageBox.setManaged(false);
                    messageLabel.setText("✓ Welcome back, " + existing.getName() + "!");
                    messageLabel.setStyle("-fx-font-size: 13px;" +
                                         "-fx-text-fill: " + SpaceTheme.ACCENT_SUCCESS + ";");
                }
            } else {
                ageBox.setVisible(false);
                ageBox.setManaged(false);
                messageLabel.setText("");
            }
        });
        
        // Login button
        Button loginButton = new Button("PROCEED TO DASHBOARD");
        loginButton.setStyle(SpaceTheme.getButtonPrimaryStyle() +
                            "-fx-pref-width: 300;" +
                            "-fx-pref-height: 50;");
        loginButton.setOnAction(e -> handleLogin());
        
        // Hover effect
        loginButton.setOnMouseEntered(e -> 
            loginButton.setStyle(SpaceTheme.getButtonPrimaryStyle() +
                                "-fx-pref-width: 300;" +
                                "-fx-pref-height: 50;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 217, 255, 0.8), 25, 0, 0, 0);"));
        loginButton.setOnMouseExited(e -> 
            loginButton.setStyle(SpaceTheme.getButtonPrimaryStyle() +
                                "-fx-pref-width: 300;" +
                                "-fx-pref-height: 50;"));
        
        loginCard.getChildren().addAll(
            cardTitle,
            subtitle,
            nameBox,
            emailBox,
            ageBox,
            messageLabel,
            loginButton
        );
        
        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(800), loginCard);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        
        container.getChildren().add(loginCard);
        return container;
    }
    
    private VBox createFormField(String labelText, String promptText) {
        VBox fieldBox = new VBox(8);
        
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 12px;" +
                      "-fx-font-weight: bold;" +
                      "-fx-text-fill: " + SpaceTheme.TEXT_SECONDARY + ";" +
                      "-fx-letter-spacing: 1px;");
        
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setPrefHeight(45);
        field.setStyle(SpaceTheme.getTextFieldStyle() +
                      "-fx-font-size: 15px;");
        
        // Focus effect
        field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                field.setStyle(SpaceTheme.getTextFieldStyle() +
                              "-fx-font-size: 15px;" +
                              "-fx-border-color: " + SpaceTheme.ACCENT_PRIMARY + ";" +
                              "-fx-border-width: 2;");
            } else {
                field.setStyle(SpaceTheme.getTextFieldStyle() +
                              "-fx-font-size: 15px;");
            }
        });
        
        fieldBox.getChildren().addAll(label, field);
        return fieldBox;
    }
    
    private void handleLogin() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String ageText = ageField.getText().trim();
        
        // Validation
        if (name.isEmpty() || email.isEmpty()) {
            showError("Please enter both name and email.");
            return;
        }
        
        try {
            Passenger passenger = navManager.getPassengerDao().getPassengerByEmail(email);
            
            if (passenger == null) {
                // New user - require age
                if (ageText.isEmpty()) {
                    showError("Please enter your age to create a new account.");
                    return;
                }
                
                try {
                    int age = Integer.parseInt(ageText);
                    passenger = new Passenger(name, age, email);
                    navManager.getPassengerDao().savePassenger(passenger);
                    
                    showSuccess("Account created successfully! ID: " + passenger.getPersonID());
                    
                    // Navigate after a brief delay
                    javafx.application.Platform.runLater(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {}
                        navManager.showPassengerDashboard(passenger);
                    });
                    
                } catch (NumberFormatException e) {
                    showError("Please enter a valid age.");
                }
            } else {
                // Existing user
                navManager.showPassengerDashboard(passenger);
            }
            
        } catch (Exception e) {
            showError("Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showError(String message) {
        messageLabel.setText("✗ " + message);
        messageLabel.setStyle("-fx-font-size: 13px;" +
                             "-fx-text-fill: " + SpaceTheme.ACCENT_DANGER + ";");
    }
    
    private void showSuccess(String message) {
        messageLabel.setText("✓ " + message);
        messageLabel.setStyle("-fx-font-size: 13px;" +
                             "-fx-text-fill: " + SpaceTheme.ACCENT_SUCCESS + ";");
    }
    
    public Scene getScene() {
        return new Scene(root, 1400, 900);
    }
}
