package SpaceportSystem.UI.views;

import SpaceportSystem.*;
import SpaceportSystem.uicontroller.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * AdminDashboard - Complete administrator control panel with 3 tabs.
 * Implements ALL admin functionality from SpaceportTEST.java processAdminChoice() method.
 */
public class AdminDashboard {
    
    private final SceneManager sceneManager;
    private VBox root;
    private TabPane tabPane;
    
    // Apple-inspired light palette (matches PassengerLoginView)
    private static final String BG_WHITE = "#F7F9FC";
    private static final String CARD_WHITE = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#2D7FF9";
    private static final String BORDER_GREY = "#E1E6EE";
    private static final String TEXT_DARK = "#1F2933";
    private static final String TEXT_MUTED = "#4B5563";
    
    public AdminDashboard(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        buildUI();
    }
    
    private void buildUI() {
        root = new VBox(24);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: " + BG_WHITE + ";");
        
        // Header
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Administrator Control Panel");
        titleLabel.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web(TEXT_DARK));
        
        Button backBtn = new Button("Logout");
        backBtn.setStyle("-fx-background-color: white; -fx-text-fill: " + TEXT_DARK
                + "; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: " + BORDER_GREY + ";");
        backBtn.setOnAction(e -> sceneManager.navigateToPassengerLogin());
        
        header.getChildren().addAll(titleLabel, backBtn);
        
        // Tab Pane
        tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");
        
        // Tab 1: Schedule Trip (Admin Case 1)
        Tab scheduleTab = createScheduleTripTab();
        
        // Tab 2: Manage Rocket (Admin Case 2)
        Tab manageRocketTab = createManageRocketTab();
        
        // Tab 3: View Manifest (Admin Case 3)
        Tab manifestTab = createViewManifestTab();
        
        tabPane.getTabs().addAll(scheduleTab, manageRocketTab, manifestTab);
        
        root.getChildren().addAll(header, tabPane);
    }
    
    // TAB 1: Schedule Trip (from SpaceportTEST.java case 1, lines 304-391)
    private Tab createScheduleTripTab() {
        Tab tab = new Tab("Schedule Trip");
        tab.setClosable(false);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: " + CARD_WHITE + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");
        
        Label title = new Label("Schedule New Trip");
        title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(TEXT_DARK));
        
        // Form fields
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: " + BG_WHITE + "; -fx-background-radius: 10;");
        
        TextField tripNoField = new TextField();
        tripNoField.setPrefHeight(36);
        tripNoField.setPromptText("e.g., T-01");
        
        TextField destinationField = new TextField();
        destinationField.setPrefHeight(36);
        destinationField.setPromptText("e.g., Mars L1");
        
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefHeight(35);
        
        TextField crewField = new TextField();
        crewField.setPrefHeight(36);
        crewField.setPromptText("Required Crew Amount");
        
        TextField rocketIdField = new TextField();
        rocketIdField.setPrefHeight(36);
        rocketIdField.setPromptText("Rocket ID");
        
        TextField launchPadIdField = new TextField();
        launchPadIdField.setPrefHeight(36);
        launchPadIdField.setText("13"); // Default as in SpaceportTEST.java line 342
        
        form.add(new Label("Trip Number:"), 0, 0);
        form.add(tripNoField, 1, 0);
        form.add(new Label("Destination:"), 0, 1);
        form.add(destinationField, 1, 1);
        form.add(new Label("Launch Date:"), 0, 2);
        form.add(datePicker, 1, 2);
        form.add(new Label("Required Crew:"), 0, 3);
        form.add(crewField, 1, 3);
        form.add(new Label("Rocket ID:"), 0, 4);
        form.add(rocketIdField, 1, 4);
        form.add(new Label("Launch Pad ID:"), 0, 5);
        form.add(launchPadIdField, 1, 5);
        
        // Style labels
        for (int i = 0; i < form.getChildren().size(); i++) {
            if (form.getChildren().get(i) instanceof Label) {
                Label lbl = (Label) form.getChildren().get(i);
                lbl.setTextFill(Color.web(TEXT_DARK));
                lbl.setFont(Font.font("SF Pro Text", 12));
            }
        }
        
        Button createBtn = new Button("Create Trip & Generate Tickets");
        createBtn.setPrefWidth(320);
        createBtn.setPrefHeight(44);
        createBtn.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 14));
        createBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        
        createBtn.setOnAction(e -> {
            try {
                // Validate inputs
                if (tripNoField.getText().isEmpty() || destinationField.getText().isEmpty() ||
                    rocketIdField.getText().isEmpty() || crewField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please fill all required fields.");
                    return;
                }
                
                int rocketID = Integer.parseInt(rocketIdField.getText());
                Rocket assignedRocket = sceneManager.getRocketDao().getRocketById(rocketID);
                
                if (assignedRocket == null || assignedRocket.getRocketID() == 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Rocket", 
                        "Cannot schedule trip. No rocket assigned or Rocket ID is invalid.");
                    return;
                }
                
                // Create Trip object (lines 333-342)
                Trip trip = new Trip();
                trip.setTripNo(tripNoField.getText());
                trip.setDestinationLocation(destinationField.getText());
                trip.setRequiredCrewCount(Integer.parseInt(crewField.getText()));
                trip.setDepartureTime(datePicker.getValue() != null ? 
                    java.sql.Date.valueOf(datePicker.getValue()) : new Date());
                trip.setStatus(tripStatus.SCHEDULED);
                trip.setRocketID(rocketID);
                trip.setLaunchPadID(Integer.parseInt(launchPadIdField.getText()));
                
                // Save trip (line 345)
                sceneManager.getTripDao().saveTrip(trip);
                int newTripID = trip.getTripID();
                
                // Generate tickets (lines 350-384)
                int totalSeats = 6; // As in SpaceportTEST.java line 353
                int economyCapacity = (int) (totalSeats * 0.7);
                int businessCapacity = (int) (totalSeats * 0.2);
                int firstCapacity = totalSeats - economyCapacity - businessCapacity;
                
                int seatCounter = 1;
                
                // Economy tickets
                for (int i = 0; i < economyCapacity; i++) {
                    Ticket tk = new Ticket(newTripID, String.valueOf(seatCounter), 500.00, "ECONOMY");
                    sceneManager.getTicketDao().saveTicket(tk);
                    seatCounter++;
                }
                
                // Business tickets
                for (int i = 0; i < businessCapacity; i++) {
                    Ticket tk = new Ticket(newTripID, String.valueOf(seatCounter), 1500.00, "BUSINESS");
                    sceneManager.getTicketDao().saveTicket(tk);
                    seatCounter++;
                }
                
                // First class tickets
                for (int i = 0; i < firstCapacity; i++) {
                    Ticket tk = new Ticket(newTripID, String.valueOf(seatCounter), 3000.00, "FIRST");
                    sceneManager.getTicketDao().saveTicket(tk);
                    seatCounter++;
                }
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Trip " + trip.getTripNo() + " Created Successfully (ID: " + newTripID + ")\n" +
                    "Successfully generated " + (seatCounter - 1) + " tiered tickets.");
                
                // Clear form
                tripNoField.clear();
                destinationField.clear();
                datePicker.setValue(null);
                crewField.clear();
                rocketIdField.clear();
                
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for Rocket ID and Crew Count.");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", 
                    "DATABASE ERROR during Trip/Ticket creation: " + ex.getMessage());
            }
        });
        
        content.getChildren().addAll(title, form, createBtn);
        tab.setContent(content);
        return tab;
    }
    
    // TAB 2: Manage Rocket (from SpaceportTEST.java case 2, lines 393-445)
    private Tab createManageRocketTab() {
        Tab tab = new Tab("Manage Rocket");
        tab.setClosable(false);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: " + CARD_WHITE + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");
        
        Label title = new Label("Rocket Management");
        title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(TEXT_DARK));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: " + BG_WHITE + "; -fx-background-radius: 10;");
        
        TextField rocketIdField = new TextField();
        rocketIdField.setPrefHeight(36);
        rocketIdField.setPromptText("Rocket ID");
        
        ToggleGroup actionGroup = new ToggleGroup();
        RadioButton fillFuelBtn = new RadioButton("Fill Fuel");
        fillFuelBtn.setToggleGroup(actionGroup);
        fillFuelBtn.setSelected(true);
        RadioButton changeStatusBtn = new RadioButton("Change Status");
        changeStatusBtn.setToggleGroup(actionGroup);
        
        TextField fuelAmountField = new TextField();
        fuelAmountField.setPrefHeight(36);
        fuelAmountField.setPromptText("Fuel Amount (Liters)");
        fuelAmountField.setVisible(true);
        
        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("READY", "IN_MAINTENANCE", "AVAILABLE", "IN_FLIGHT");
        statusCombo.setValue("READY");
        statusCombo.setVisible(false);
        
        fillFuelBtn.setOnAction(e -> {
            fuelAmountField.setVisible(true);
            statusCombo.setVisible(false);
        });
        
        changeStatusBtn.setOnAction(e -> {
            fuelAmountField.setVisible(false);
            statusCombo.setVisible(true);
        });
        
        form.add(new Label("Rocket ID:"), 0, 0);
        form.add(rocketIdField, 1, 0);
        form.add(new Label("Action:"), 0, 1);
        HBox actionBox = new HBox(10);
        actionBox.getChildren().addAll(fillFuelBtn, changeStatusBtn);
        form.add(actionBox, 1, 1);
        form.add(new Label("Fuel Amount:"), 0, 2);
        form.add(fuelAmountField, 1, 2);
        form.add(new Label("New Status:"), 0, 3);
        form.add(statusCombo, 1, 3);
        
        // Style labels
        for (int i = 0; i < form.getChildren().size(); i++) {
            if (form.getChildren().get(i) instanceof Label) {
                Label lbl = (Label) form.getChildren().get(i);
                lbl.setTextFill(Color.web(TEXT_DARK));
                lbl.setFont(Font.font("SF Pro Text", 12));
            }
        }
        
        fillFuelBtn.setTextFill(Color.web(TEXT_DARK));
        changeStatusBtn.setTextFill(Color.web(TEXT_DARK));
        
        Button executeBtn = new Button("Execute");
        executeBtn.setPrefWidth(220);
        executeBtn.setPrefHeight(40);
        executeBtn.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 14));
        executeBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        
        executeBtn.setOnAction(e -> {
            try {
                int rocketID = Integer.parseInt(rocketIdField.getText());
                Rocket rocket = sceneManager.getRocketDao().getRocketById(rocketID);
                
                if (rocket == null) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Rocket", "No rocket found with ID: " + rocketID);
                    return;
                }
                
                Adminstrator admin = new Adminstrator("Admin", "A101", "Administration", 70000, 25);
                
                if (fillFuelBtn.isSelected()) {
                    // Fill fuel logic (lines 404-427)
                    double fillAmount = Double.parseDouble(fuelAmountField.getText());
                    
                    if (rocket.getFuelTank() == null) {
                        rocket.AssignFuelTank(new FuelTank(500000.0));
                    }
                    
                    admin.callForRefuel(fillAmount, rocket);
                    rocket.getFuelTank().FillFuel(fillAmount, rocket);
                    sceneManager.getRocketDao().updateRocket(rocket);
                    
                    showAlert(Alert.AlertType.INFORMATION, "Fuel Filled", 
                        "Fuel filled for Rocket " + rocketID + "\n" +
                        "Current fuel: " + rocket.getFuelTank().getCurrentFuel() + " L");
                } else {
                    // Change status logic (lines 428-444)
                    String statusStr = statusCombo.getValue();
                    RocketStatus newStatus = RocketStatus.valueOf(statusStr);
                    
                    admin.ChangeRocketstatus(rocket, newStatus);
                    sceneManager.getRocketDao().updateRocket(rocket);
                    
                    showAlert(Alert.AlertType.INFORMATION, "Status Updated", 
                        "Rocket status updated to: " + rocket.getStatus());
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers.");
            }
        });
        
        content.getChildren().addAll(title, form, executeBtn);
        tab.setContent(content);
        return tab;
    }
    
    // TAB 3: View Manifest (from SpaceportTEST.java case 3, lines 447-490)
    private Tab createViewManifestTab() {
        Tab tab = new Tab("View Manifest");
        tab.setClosable(false);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color: " + CARD_WHITE + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");
        
        Label title = new Label("Trip Manifest / Roster");
        title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(TEXT_DARK));
        
        HBox inputBox = new HBox(15);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        
        Label tripIdLabel = new Label("Trip ID:");
        tripIdLabel.setTextFill(Color.web(TEXT_DARK));
        tripIdLabel.setFont(Font.font("SF Pro Text", 12));
        
        TextField tripIdField = new TextField();
        tripIdField.setPrefHeight(36);
        tripIdField.setPrefWidth(150);
        tripIdField.setPromptText("Enter Trip ID");
        
        Button loadBtn = new Button("Load Manifest");
        loadBtn.setPrefHeight(36);
        loadBtn.setPrefWidth(160);
        loadBtn.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 13));
        loadBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        
        TableView<ManifestEntry> manifestTable = new TableView<>();
        manifestTable.setPrefHeight(400);
        manifestTable.setStyle("-fx-background-color: white;");
        
        TableColumn<ManifestEntry, String> nameCol = new TableColumn<>("Passenger Name");
        nameCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getName())
        );
        nameCol.setPrefWidth(200);
        
        TableColumn<ManifestEntry, Integer> idCol = new TableColumn<>("Passenger ID");
        idCol.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getPassengerId()).asObject()
        );
        idCol.setPrefWidth(120);
        
        TableColumn<ManifestEntry, String> tierCol = new TableColumn<>("Class Tier");
        tierCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getTier())
        );
        tierCol.setPrefWidth(120);
        
        TableColumn<ManifestEntry, String> seatCol = new TableColumn<>("Seat No");
        seatCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getSeatNo())
        );
        seatCol.setPrefWidth(100);
        
        manifestTable.getColumns().addAll(nameCol, idCol, tierCol, seatCol);
        
        loadBtn.setOnAction(e -> {
            try {
                int tripID = Integer.parseInt(tripIdField.getText());
                
                // Get tickets for trip (line 459)
                List<Ticket> tickets = sceneManager.getTicketDao().getTicketsByTripId(tripID);
                
                if (tickets.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "No Tickets", "No tickets created for this trip.");
                    manifestTable.getItems().clear();
                    return;
                }
                
                // Build manifest entries (lines 469-483)
                ObservableList<ManifestEntry> manifestEntries = FXCollections.observableArrayList();
                
                for (Ticket tk : tickets) {
                    if (tk.getPassengerID() != 0) {
                        Passenger p = sceneManager.getPassengerDao().getPassengerById(tk.getPassengerID());
                        if (p != null) {
                            manifestEntries.add(new ManifestEntry(
                                p.getName(),
                                p.getPersonID(),
                                tk.getClassTier(),
                                tk.getSeatNo()
                            ));
                        }
                    }
                }
                
                manifestTable.setItems(manifestEntries);
                
                if (manifestEntries.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "No Bookings", 
                        "No passengers currently booked, but " + tickets.size() + " tickets are available.");
                }
                
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid Trip ID.");
            }
        });
        
        inputBox.getChildren().addAll(tripIdLabel, tripIdField, loadBtn);
        content.getChildren().addAll(title, inputBox, manifestTable);
        tab.setContent(content);
        return tab;
    }
    
    // Helper class for manifest table
    public static class ManifestEntry {
        private String name;
        private int passengerId;
        private String tier;
        private String seatNo;
        
        public ManifestEntry(String name, int passengerId, String tier, String seatNo) {
            this.name = name;
            this.passengerId = passengerId;
            this.tier = tier;
            this.seatNo = seatNo;
        }
        
        public String getName() { return name; }
        public int getPassengerId() { return passengerId; }
        public String getTier() { return tier; }
        public String getSeatNo() { return seatNo; }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public VBox getRoot() {
        return root;
    }
}


