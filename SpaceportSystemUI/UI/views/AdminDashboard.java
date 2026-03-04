package SpaceportSystem.UI.views;

import SpaceportSystem.uicontroller.SceneManager;
import SpaceportSystem.model.Rocket;
import SpaceportSystem.model.Passenger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminDashboard extends BorderPane {

    private final SceneManager sceneManager;

    public AdminDashboard(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        buildUI();
    }

    private void buildUI() {
        setStyle("-fx-background-color: #FFFFFF;");

        // Header
        Label header = new Label("Admin Dashboard");
        header.setTextFill(Color.web("#FFFFFF"));
        header.setFont(Font.font("Segoe UI", 24));

        Button logoutButton = new Button("Logout");
        styleSecondaryButton(logoutButton);
        logoutButton.setOnAction(e -> {
            sceneManager.setCurrentEmployee(null);
            sceneManager.showPassengerLogin();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerBox = new HBox(20, header, spacer, logoutButton);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #00335A;");
        setTop(headerBox);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createScheduleTripTab(),
                createManageRocketsTab(),
                createManifestTab()
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        setCenter(tabPane);
    }

    // === Tab 1: Schedule Trip ===
    private Tab createScheduleTripTab() {
        Tab tab = new Tab("Schedule Trip");

        GridPane form = new GridPane();
        form.setPadding(new Insets(20));
        form.setVgap(10);
        form.setHgap(10);

        Label destinationLabel = new Label("Destination:");
        TextField destinationField = new TextField();

        Label dateLabel = new Label("Launch Date/Time (yyyy-MM-dd HH:mm):");
        TextField dateField = new TextField();

        Label rocketIdLabel = new Label("Rocket ID:");
        TextField rocketIdField = new TextField();

        Label pilotIdLabel = new Label("Pilot ID:");
        TextField pilotIdField = new TextField();

        Button scheduleButton = new Button("Schedule Trip");
        stylePrimaryButton(scheduleButton);

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#333333"));

        scheduleButton.setOnAction(e -> {
            String destination = destinationField.getText().trim();
            String dateText = dateField.getText().trim();
            String rocketIdText = rocketIdField.getText().trim();
            String pilotIdText = pilotIdField.getText().trim();

            if (destination.isEmpty() || dateText.isEmpty()
                    || rocketIdText.isEmpty() || pilotIdText.isEmpty()) {
                messageLabel.setText("All fields are required.");
                return;
            }

            try {
                LocalDateTime launchDate = LocalDateTime.parse(
                        dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                int rocketId = Integer.parseInt(rocketIdText);
                int pilotId = Integer.parseInt(pilotIdText);

                // Backend assumption:
                // TripDao.scheduleTrip(String destination, LocalDateTime date, int rocketId, int pilotId)
                sceneManager.getTripDao().scheduleTrip(destination, launchDate, rocketId, pilotId);

                messageLabel.setText("Trip scheduled successfully.");
                destinationField.clear();
                dateField.clear();
                rocketIdField.clear();
                pilotIdField.clear();
            } catch (Exception ex) {
                messageLabel.setText("Error scheduling trip: " + ex.getMessage());
            }
        });

        form.add(destinationLabel, 0, 0);
        form.add(destinationField, 1, 0);
        form.add(dateLabel, 0, 1);
        form.add(dateField, 1, 1);
        form.add(rocketIdLabel, 0, 2);
        form.add(rocketIdField, 1, 2);
        form.add(pilotIdLabel, 0, 3);
        form.add(pilotIdField, 1, 3);
        form.add(scheduleButton, 1, 4);
        form.add(messageLabel, 1, 5);

        tab.setContent(form);
        return tab;
    }

    // === Tab 2: Manage Rockets ===
    private Tab createManageRocketsTab() {
        Tab tab = new Tab("Manage Rockets");

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));

        TableView<Rocket> rocketTable = new TableView<>();
        rocketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Rocket, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Rocket, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Rocket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Rocket, Integer> fuelCol = new TableColumn<>("Fuel %");
        fuelCol.setCellValueFactory(new PropertyValueFactory<>("fuelLevel"));

        rocketTable.getColumns().addAll(idCol, nameCol, statusCol, fuelCol);

        // Load rockets
        try {
            // Backend assumption: RocketDao.getAllRockets()
            List<Rocket> rockets = sceneManager.getRocketDao().getAllRockets();
            ObservableList<Rocket> data = FXCollections.observableArrayList(rockets);
            rocketTable.setItems(data);
        } catch (Exception ex) {
            // Optionally show in a label or dialog
        }

        // Update controls
        HBox updateBox = new HBox(10);
        updateBox.setAlignment(Pos.CENTER_LEFT);
        updateBox.setPadding(new Insets(10, 0, 0, 0));

        Label statusLabel = new Label("New Status:");
        TextField statusField = new TextField();

        Label fuelLabel = new Label("Fuel %:");
        TextField fuelField = new TextField();

        Button updateButton = new Button("Update Selected Rocket");
        stylePrimaryButton(updateButton);

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#333333"));

        updateButton.setOnAction(e -> {
            Rocket selected = rocketTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                messageLabel.setText("Please select a rocket.");
                return;
            }

            String newStatus = statusField.getText().trim();
            String fuelText = fuelField.getText().trim();

            if (newStatus.isEmpty() && fuelText.isEmpty()) {
                messageLabel.setText("Enter a new status and/or fuel level.");
                return;
            }

            try {
                Integer newFuel = fuelText.isEmpty() ? null : Integer.parseInt(fuelText);

                // Backend assumption:
                // RocketDao.updateRocket(int rocketId, String status, Integer fuelLevel)
                sceneManager.getRocketDao().updateRocket(selected.getId(), newStatus, newFuel);

                // Refresh
                List<Rocket> rockets = sceneManager.getRocketDao().getAllRockets();
                rocketTable.setItems(FXCollections.observableArrayList(rockets));

                messageLabel.setText("Rocket updated.");
                statusField.clear();
                fuelField.clear();
            } catch (NumberFormatException nfe) {
                messageLabel.setText("Fuel must be numeric.");
            } catch (Exception ex) {
                messageLabel.setText("Error updating rocket: " + ex.getMessage());
            }
        });

        updateBox.getChildren().addAll(statusLabel, statusField, fuelLabel, fuelField, updateButton);

        VBox centerBox = new VBox(rocketTable, updateBox, messageLabel);
        centerBox.setSpacing(10);

        pane.setCenter(centerBox);

        tab.setContent(pane);
        return tab;
    }

    // === Tab 3: Manifest ===
    private Tab createManifestTab() {
        Tab tab = new Tab("Manifest");

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(20));

        HBox topBox = new HBox(10);
        topBox.setAlignment(Pos.CENTER_LEFT);

        Label tripIdLabel = new Label("Trip ID:");
        TextField tripIdField = new TextField();
        Button loadButton = new Button("Load Manifest");
        stylePrimaryButton(loadButton);

        topBox.getChildren().addAll(tripIdLabel, tripIdField, loadButton);

        TableView<Passenger> manifestTable = new TableView<>();
        manifestTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Passenger, Integer> idCol = new TableColumn<>("Passenger ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Passenger, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Passenger, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        manifestTable.getColumns().addAll(idCol, nameCol, ageCol);

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#333333"));

        loadButton.setOnAction(e -> {
            String tripIdText = tripIdField.getText().trim();
            if (tripIdText.isEmpty()) {
                messageLabel.setText("Please enter a trip ID.");
                return;
            }

            try {
                int tripId = Integer.parseInt(tripIdText);
                // Backend assumption:
                // TripDao.getManifest(int tripId) -> List<Passenger>
                List<Passenger> passengers = sceneManager.getTripDao().getManifest(tripId);
                manifestTable.setItems(FXCollections.observableArrayList(passengers));
                messageLabel.setText("Loaded " + passengers.size() + " passengers.");
            } catch (NumberFormatException nfe) {
                messageLabel.setText("Trip ID must be numeric.");
            } catch (Exception ex) {
                messageLabel.setText("Error loading manifest: " + ex.getMessage());
            }
        });

        VBox centerBox = new VBox(10, manifestTable, messageLabel);

        pane.setTop(topBox);
        pane.setCenter(centerBox);

        tab.setContent(pane);
        return tab;
    }

    private void stylePrimaryButton(Button button) {
        button.setStyle(
                "-fx-background-color: #005288;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-background-radius: 4;");
    }

    private void styleSecondaryButton(Button button) {
        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #FFFFFF;" +
                "-fx-text-fill: #FFFFFF;" +
                "-fx-font-size: 13px;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-background-radius: 4;" +
                "-fx-border-radius: 4;");
    }
}
