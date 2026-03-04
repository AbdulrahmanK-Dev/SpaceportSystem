package SpaceportSystem.UI.views;

import SpaceportSystem.uicontroller.SceneManager;
import SpaceportSystem.model.Passenger;
import SpaceportSystem.model.Employee;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class PassengerLoginView extends BorderPane {

    private final SceneManager sceneManager;

    public PassengerLoginView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        buildUI();
    }

    private void buildUI() {
        setStyle("-fx-background-color: #FFFFFF;");

        // Header
        Label header = new Label("Spaceport System");
        header.setTextFill(Color.web("#FFFFFF"));
        header.setFont(Font.font("Segoe UI", 26));

        HBox headerBox = new HBox(header);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #00335A;"); // dark navy
        setTop(headerBox);

        // Center content
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        Label title = new Label("Welcome to the Spaceport");
        title.setFont(Font.font("Segoe UI", 24));
        title.setTextFill(Color.web("#333333"));

        Label subtitle = new Label("Please select your role to continue");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.web("#666666"));

        // Passenger login controls
        VBox passengerBox = new VBox(10);
        passengerBox.setAlignment(Pos.CENTER_LEFT);

        Label passengerLabel = new Label("Passenger Login");
        passengerLabel.setFont(Font.font("Segoe UI", 18));
        passengerLabel.setTextFill(Color.web("#333333"));

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Button passengerButton = new Button("PASSENGER");
        stylePrimaryButton(passengerButton);
        passengerButton.setMaxWidth(Double.MAX_VALUE);

        // Age dialog is requested if passenger is new
        passengerButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                showAlert("Validation Error", "Please enter your email.");
                return;
            }

            // Backend assumptions:
            // - PassDao has: Passenger findByEmail(String email)
            // - PassDao has: Passenger registerPassenger(String email, int age)
            try {
                Passenger existing = sceneManager.getPassDao().findByEmail(email);
                if (existing == null) {
                    TextInputDialog ageDialog = new TextInputDialog();
                    ageDialog.setTitle("New Passenger");
                    ageDialog.setHeaderText("New passenger detected");
                    ageDialog.setContentText("Please enter your age:");
                    ageDialog.getEditor().setPromptText("Age");

                    ageDialog.showAndWait().ifPresent(ageStr -> {
                        try {
                            int age = Integer.parseInt(ageStr.trim());
                            Passenger created = sceneManager.getPassDao().registerPassenger(email, age);
                            sceneManager.setCurrentPassenger(created);
                            sceneManager.showPassengerDashboard();
                        } catch (NumberFormatException ex) {
                            showAlert("Invalid Age", "Please enter a valid numeric age.");
                        }
                    });
                } else {
                    sceneManager.setCurrentPassenger(existing);
                    sceneManager.showPassengerDashboard();
                }
            } catch (Exception ex) {
                showAlert("Login Error", ex.getMessage());
            }
        });

        passengerBox.getChildren().addAll(passengerLabel, emailField, passengerButton);

        // Admin login
        VBox adminBox = new VBox(10);
        adminBox.setAlignment(Pos.CENTER_LEFT);

        Label adminLabel = new Label("Admin Login");
        adminLabel.setFont(Font.font("Segoe UI", 18));
        adminLabel.setTextFill(Color.web("#333333"));

        TextField adminIdField = new TextField();
        adminIdField.setPromptText("Admin ID");

        PasswordField adminPasswordField = new PasswordField();
        adminPasswordField.setPromptText("Password");

        Button adminButton = new Button("ADMIN");
        stylePrimaryButton(adminButton);
        adminButton.setMaxWidth(Double.MAX_VALUE);

        adminButton.setOnAction(e -> {
            String idText = adminIdField.getText().trim();
            String password = adminPasswordField.getText();
            if (idText.isEmpty() || password.isEmpty()) {
                showAlert("Validation Error", "Please enter admin ID and password.");
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                // Backend assumption:
                // - EmployeeDao has: Employee authenticateAdmin(int id, String password)
                Employee admin = sceneManager.getEmployeeDao().authenticateAdmin(id, password);
                if (admin == null) {
                    showAlert("Login Failed", "Invalid admin credentials.");
                } else {
                    sceneManager.setCurrentEmployee(admin);
                    sceneManager.showAdminDashboard();
                }
            } catch (NumberFormatException ex) {
                showAlert("Validation Error", "Admin ID must be numeric.");
            } catch (Exception ex) {
                showAlert("Login Error", ex.getMessage());
            }
        });

        adminBox.getChildren().addAll(adminLabel, adminIdField, adminPasswordField, adminButton);

        // Staff login
        VBox staffBox = new VBox(10);
        staffBox.setAlignment(Pos.CENTER_LEFT);

        Label staffLabel = new Label("Staff Login");
        staffLabel.setFont(Font.font("Segoe UI", 18));
        staffLabel.setTextFill(Color.web("#333333"));

        TextField staffIdField = new TextField();
        staffIdField.setPromptText("Staff ID");

        PasswordField staffPasswordField = new PasswordField();
        staffPasswordField.setPromptText("Password");

        Button staffButton = new Button("STAFF");
        stylePrimaryButton(staffButton);
        staffButton.setMaxWidth(Double.MAX_VALUE);

        staffButton.setOnAction(e -> {
            String idText = staffIdField.getText().trim();
            String password = staffPasswordField.getText();
            if (idText.isEmpty() || password.isEmpty()) {
                showAlert("Validation Error", "Please enter staff ID and password.");
                return;
            }

            try {
                int id = Integer.parseInt(idText);
                // Backend assumption:
                // - EmployeeDao has: Employee authenticateStaff(int id, String password)
                Employee staff = sceneManager.getEmployeeDao().authenticateStaff(id, password);
                if (staff == null) {
                    showAlert("Login Failed", "Invalid staff credentials.");
                } else {
                    sceneManager.setCurrentEmployee(staff);
                    sceneManager.showStaffDashboard();
                }
            } catch (NumberFormatException ex) {
                showAlert("Validation Error", "Staff ID must be numeric.");
            } catch (Exception ex) {
                showAlert("Login Error", ex.getMessage());
            }
        });

        staffBox.getChildren().addAll(staffLabel, staffIdField, staffPasswordField, staffButton);

        // Layout columns
        HBox rolesBox = new HBox(40, passengerBox, adminBox, staffBox);
        rolesBox.setAlignment(Pos.CENTER);
        rolesBox.setPadding(new Insets(20));

        content.getChildren().addAll(title, subtitle, rolesBox);

        setCenter(content);
    }

    private void stylePrimaryButton(Button button) {
        button.setStyle(
                "-fx-background-color: #005288;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-background-radius: 4;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
