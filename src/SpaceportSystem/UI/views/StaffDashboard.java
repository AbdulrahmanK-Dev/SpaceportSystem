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
import java.util.List;

/**
 * StaffDashboard - Staff interface for Pilots and Technicians.
 * Implements functionality from processPilotChoice() and processTechChoice() in SpaceportTEST.java.
 */
public class StaffDashboard {
    
    private final SceneManager sceneManager;
    private final Employee employee;
    private final String role;
    private VBox root;
    
    // Apple-inspired light palette (matches PassengerLoginView)
    private static final String BG_WHITE = "#F7F9FC";
    private static final String CARD_WHITE = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#2D7FF9";
    private static final String BORDER_GREY = "#E1E6EE";
    private static final String TEXT_DARK = "#1F2933";
    private static final String TEXT_MUTED = "#4B5563";
    
    public StaffDashboard(SceneManager sceneManager, Employee employee, String role) {
        this.sceneManager = sceneManager;
        this.employee = employee;
        this.role = role;
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
        
        String roleTitle = role.equalsIgnoreCase("Pilot") ? "Pilot Cockpit" : "Technician Workstation";
        Label titleLabel = new Label(roleTitle);
        titleLabel.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web(TEXT_DARK));
        
        Label welcomeLabel = new Label("Welcome, " + employee.getName() + " (" + role + ")");
        welcomeLabel.setFont(Font.font("SF Pro Text", 14));
        welcomeLabel.setTextFill(Color.web(TEXT_MUTED));
        
        Button backBtn = new Button("Logout");
        backBtn.setStyle("-fx-background-color: white; -fx-text-fill: " + TEXT_DARK
                + "; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: " + BORDER_GREY + ";");
        backBtn.setOnAction(e -> sceneManager.navigateToPassengerLogin());
        
        header.getChildren().addAll(titleLabel, welcomeLabel, backBtn);
        
        // Content based on role
        VBox content;
        if (role.equalsIgnoreCase("Pilot")) {
            content = createPilotView();
        } else {
            content = createTechnicianView();
        }
        
        root.getChildren().addAll(header, content);
    }
    
    // PILOT VIEW (from SpaceportTEST.java processPilotChoice(), lines 541-626)
    private VBox createPilotView() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + CARD_WHITE + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");
        content.setMaxWidth(960);
        
        // Rocket Assignment Section
        HBox rocketSection = new HBox(15);
        rocketSection.setAlignment(Pos.CENTER_LEFT);
        
        Label rocketLabel = new Label("Assigned Rocket ID:");
        rocketLabel.setTextFill(Color.web(TEXT_DARK));
        rocketLabel.setFont(Font.font("SF Pro Text", 12));
        
        TextField rocketIdField = new TextField();
        rocketIdField.setPrefHeight(36);
        rocketIdField.setPrefWidth(150);
        rocketIdField.setPromptText("Enter Rocket ID");
        
        Button assignBtn = new Button("Assign Rocket");
        assignBtn.setPrefHeight(36);
        assignBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        assignBtn.setOnAction(e -> {
            try {
                int rocketID = Integer.parseInt(rocketIdField.getText());
                Rocket rocket = sceneManager.getRocketDao().getRocketById(rocketID);
                if (rocket != null) {
                    showAlert(Alert.AlertType.INFORMATION, "Rocket Assigned", 
                        "Rocket " + rocketID + " assigned successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Invalid Rocket", "Rocket not found.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid Rocket ID.");
            }
        });
        
        rocketSection.getChildren().addAll(rocketLabel, rocketIdField, assignBtn);
        
        // Pilot Controls Grid
        GridPane controlsGrid = new GridPane();
        controlsGrid.setHgap(15);
        controlsGrid.setVgap(15);
        controlsGrid.setPadding(new Insets(20));
        controlsGrid.setStyle("-fx-background-color: " + BG_WHITE + "; -fx-background-radius: 10;");
        
        // Case 1: Start Engine
        Button startEngineBtn = createControlButton("1. Start Rocket Engine");
        startEngineBtn.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Engine Start", "Starting engines...");
            // In real implementation, would call: pilot.startRocketEngines();
        });
        
        // Case 2: Fuel Consumption
        Label fuelTimeLabel = new Label("Flight Time (hours):");
        fuelTimeLabel.setTextFill(Color.web(TEXT_DARK));
        TextField fuelTimeField = new TextField();
        fuelTimeField.setPrefHeight(36);
        Button fuelEstimateBtn = createControlButton("2. Fuel Consumption Estimation");
        fuelEstimateBtn.setOnAction(e -> {
            try {
                int time = Integer.parseInt(fuelTimeField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Fuel Estimation", 
                    "Fuel consumption calculated for " + time + " hours.");
                // In real implementation: pilot.manageFuel(time);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number of hours.");
            }
        });
        
        // Case 3: Estimate Distance
        ComboBox<String> destinationCombo = new ComboBox<>();
        destinationCombo.getItems().addAll("Mars L1", "Mars L2", "Jupiter Orbit", "Saturn Ring A");
        destinationCombo.setPrefWidth(200);
        Button distanceBtn = createControlButton("3. Estimate Distance");
        distanceBtn.setOnAction(e -> {
            String destination = destinationCombo.getValue();
            if (destination != null) {
                // In real implementation: pilot.setNavSystem(ns); double dist = pilot.calculateDistanceToDestination();
                showAlert(Alert.AlertType.INFORMATION, "Distance", 
                    "Distance to " + destination + ": [Calculated distance] Km");
            } else {
                showAlert(Alert.AlertType.WARNING, "No Destination", "Please select a destination.");
            }
        });
        
        // Case 4: Adjust Throttle
        Label throttleLabel = new Label("Throttle %:");
        throttleLabel.setTextFill(Color.web(TEXT_DARK));
        TextField throttleField = new TextField();
        throttleField.setPrefHeight(36);
        Button throttleBtn = createControlButton("4. Adjust Throttle");
        throttleBtn.setOnAction(e -> {
            try {
                double throttle = Double.parseDouble(throttleField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Throttle Adjusted", 
                    "Throttle set to " + throttle + "%");
                // In real implementation: pilot.adjustThrottle(throttle);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid throttle percentage.");
            }
        });
        
        // Case 5: Execute Launch
        Button launchBtn = createControlButton("5. Execute Launch");
        launchBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-background-radius: 10;");
        launchBtn.setOnAction(e -> {
            try {
                int rocketID = Integer.parseInt(rocketIdField.getText());
                Rocket rocket = sceneManager.getRocketDao().getRocketById(rocketID);
                if (rocket != null && rocket.getStatus() == RocketStatus.READY) {
                    rocket.setStatus(RocketStatus.IN_FLIGHT);
                    sceneManager.getRocketDao().updateRocket(rocket);
                    showAlert(Alert.AlertType.INFORMATION, "Launch Sequence", 
                        "Launch sequence started. Rocket status set to IN_FLIGHT.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Launch Failed", 
                        "Rocket must be in READY status to launch.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Rocket", "Please assign a valid rocket first.");
            }
        });
        
        // Add to grid
        controlsGrid.add(startEngineBtn, 0, 0);
        controlsGrid.add(new Label("Flight Time:"), 0, 1);
        controlsGrid.add(fuelTimeField, 1, 1);
        controlsGrid.add(fuelEstimateBtn, 2, 1);
        controlsGrid.add(new Label("Destination:"), 0, 2);
        controlsGrid.add(destinationCombo, 1, 2);
        controlsGrid.add(distanceBtn, 2, 2);
        controlsGrid.add(new Label("Throttle %:"), 0, 3);
        controlsGrid.add(throttleField, 1, 3);
        controlsGrid.add(throttleBtn, 2, 3);
        controlsGrid.add(launchBtn, 0, 4, 3, 1);
        
        content.getChildren().addAll(rocketSection, controlsGrid);
        return content;
    }
    
    // TECHNICIAN VIEW (from SpaceportTEST.java processTechChoice(), lines 651-782)
    private VBox createTechnicianView() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + CARD_WHITE + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");
        content.setMaxWidth(960);
        
        // Tab Pane for Technician functions
        TabPane techTabs = new TabPane();
        techTabs.setStyle("-fx-background-color: transparent;");
        
        // Tab 1: View Hangar Rockets (Case 1)
        Tab hangarTab = createHangarTab();
        
        // Tab 2: Assign Service (Case 2)
        Tab assignServiceTab = createAssignServiceTab();
        
        // Tab 3: View Inventory (Case 3)
        Tab inventoryTab = createInventoryTab();
        
        // Tab 4: Add Part (Case 4)
        Tab addPartTab = createAddPartTab();
        
        // Tab 5: Update Part Quantity (Case 5)
        Tab updatePartTab = createUpdatePartTab();
        
        // Tab 6: Hire Technician (Case 6)
        Tab hireTechTab = createHireTechnicianTab();
        
        techTabs.getTabs().addAll(hangarTab, assignServiceTab, inventoryTab, addPartTab, updatePartTab, hireTechTab);
        
        content.getChildren().add(techTabs);
        return content;
    }
    
    // Case 1: View Hangar Rockets (lines 664-687)
    private Tab createHangarTab() {
        Tab tab = new Tab("Hangar Rockets");
        tab.setClosable(false);
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + CARD_WHITE + ";");
        
        Label title = new Label("Rockets in Maintenance");
        title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(TEXT_DARK));
        
        TableView<Rocket> rocketTable = new TableView<>();
        rocketTable.setPrefHeight(400);
        rocketTable.setStyle("-fx-background-color: white;");
        
        TableColumn<Rocket, String> regCol = new TableColumn<>("Registration ID");
        regCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getRegistrationID())
        );
        regCol.setPrefWidth(150);
        
        TableColumn<Rocket, String> engineCol = new TableColumn<>("Engine Type");
        engineCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getEngineType())
        );
        engineCol.setPrefWidth(150);
        
        TableColumn<Rocket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            RocketStatus status = cellData.getValue().getStatus();
            return new javafx.beans.property.SimpleStringProperty(status != null ? status.toString() : "N/A");
        });
        statusCol.setPrefWidth(150);
        
        TableColumn<Rocket, Integer> idCol = new TableColumn<>("Rocket ID");
        idCol.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getRocketID()).asObject()
        );
        idCol.setPrefWidth(100);
        
        rocketTable.getColumns().addAll(regCol, engineCol, statusCol, idCol);
        
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        refreshBtn.setOnAction(e -> {
            List<Rocket> rockets = sceneManager.getRocketDao().getAllRockets();
            ObservableList<Rocket> maintenanceRockets = FXCollections.observableArrayList();
            for (Rocket r : rockets) {
                if (r.getStatus() == RocketStatus.IN_MAINTENANCE) {
                    maintenanceRockets.add(r);
                }
            }
            rocketTable.setItems(maintenanceRockets);
        });
        
        content.getChildren().addAll(title, rocketTable, refreshBtn);
        tab.setContent(content);
        return tab;
    }
    
    // Case 2: Assign Service (lines 688-700)
    private Tab createAssignServiceTab() {
        Tab tab = new Tab("Assign Service");
        tab.setClosable(false);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + CARD_WHITE + ";");
        
        Label title = new Label("Assign Service to Rocket");
        title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(TEXT_DARK));
        
        TableView<Rocket> availableTable = new TableView<>();
        availableTable.setPrefHeight(300);
        availableTable.setStyle("-fx-background-color: white;");
        
        TableColumn<Rocket, String> regCol = new TableColumn<>("Registration ID");
        regCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getRegistrationID())
        );
        TableColumn<Rocket, Integer> idCol = new TableColumn<>("Rocket ID");
        idCol.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getRocketID()).asObject()
        );
        
        availableTable.getColumns().addAll(regCol, idCol);
        
        Button refreshBtn = new Button("Load Available Rockets");
        refreshBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        refreshBtn.setOnAction(e -> {
            List<Rocket> rockets = sceneManager.getRocketDao().getAllRockets();
            ObservableList<Rocket> availableRockets = FXCollections.observableArrayList();
            for (Rocket r : rockets) {
                if (r.getStatus() == RocketStatus.AVAILABLE) {
                    availableRockets.add(r);
                }
            }
            availableTable.setItems(availableRockets);
        });
        
        Button assignBtn = new Button("Assign to Maintenance");
        assignBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-background-radius: 10;");
        assignBtn.setOnAction(e -> {
            Rocket selected = availableTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                sceneManager.getRocketDao().updateRocketStatus(selected.getRocketID(), "IN_MAINTENANCE");
                showAlert(Alert.AlertType.INFORMATION, "Service Assigned", 
                    "Rocket " + selected.getRocketID() + " assigned to maintenance.");
                refreshBtn.fire();
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a rocket first.");
            }
        });
        
        content.getChildren().addAll(title, refreshBtn, availableTable, assignBtn);
        tab.setContent(content);
        return tab;
    }
    
    // Case 3: View Inventory (lines 701-711)
    private Tab createInventoryTab() {
        Tab tab = new Tab("View Inventory");
        tab.setClosable(false);
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + CARD_WHITE + ";");
        
        Label title = new Label("Central Parts Inventory");
        title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(TEXT_DARK));
        
        TableView<Part> inventoryTable = new TableView<>();
        inventoryTable.setPrefHeight(400);
        inventoryTable.setStyle("-fx-background-color: white;");
        
        TableColumn<Part, Integer> idCol = new TableColumn<>("Part ID");
        idCol.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject()
        );
        idCol.setPrefWidth(100);
        
        TableColumn<Part, String> nameCol = new TableColumn<>("Part Name");
        nameCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getName())
        );
        nameCol.setPrefWidth(300);
        
        TableColumn<Part, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject()
        );
        qtyCol.setPrefWidth(150);
        
        inventoryTable.getColumns().addAll(idCol, nameCol, qtyCol);
        
        Button refreshBtn = new Button("Refresh Inventory");
        refreshBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        refreshBtn.setOnAction(e -> {
            List<Part> inventory = sceneManager.getMaintenanceDao().getGlobalInventory();
            inventoryTable.setItems(FXCollections.observableArrayList(inventory));
        });
        
        content.getChildren().addAll(title, refreshBtn, inventoryTable);
        tab.setContent(content);
        return tab;
    }
    
    // Case 4: Add Part (lines 713-726)
    private Tab createAddPartTab() {
        Tab tab = new Tab("Add Part");
        tab.setClosable(false);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + CARD_WHITE + ";");
        
        Label title = new Label("Add Part to Inventory");
        title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(TEXT_DARK));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: " + BG_WHITE + "; -fx-background-radius: 10;");
        
        TextField partNameField = new TextField();
        partNameField.setPrefHeight(35);
        partNameField.setPromptText("Part Name");
        
        TextField qtyField = new TextField();
        qtyField.setPrefHeight(35);
        qtyField.setPromptText("Quantity to ADD");
        
        form.add(new Label("Part Name:"), 0, 0);
        form.add(partNameField, 1, 0);
        form.add(new Label("Quantity:"), 0, 1);
        form.add(qtyField, 1, 1);
        
        // Style labels
        for (int i = 0; i < form.getChildren().size(); i++) {
            if (form.getChildren().get(i) instanceof Label) {
                Label lbl = (Label) form.getChildren().get(i);
                lbl.setTextFill(Color.web(TEXT_DARK));
            }
        }
        
        Button addBtn = new Button("Add to Inventory");
        addBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        addBtn.setOnAction(e -> {
            try {
                String partName = partNameField.getText().trim();
                int qty = Integer.parseInt(qtyField.getText().trim());
                
                if (sceneManager.getMaintenanceDao().addPartToInventory(partName, qty)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "SUCCESS: Added " + qty + " of '" + partName + "' to central inventory.");
                    partNameField.clear();
                    qtyField.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Could not add stock. Check database connection.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid quantity.");
            }
        });
        
        content.getChildren().addAll(title, form, addBtn);
        tab.setContent(content);
        return tab;
    }
    
    // Case 5: Update Part Quantity (lines 728-741)
    private Tab createUpdatePartTab() {
        Tab tab = new Tab("Update Part");
        tab.setClosable(false);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + CARD_WHITE + ";");
        
        Label title = new Label("Update Part Quantity");
        title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(TEXT_DARK));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: " + BG_WHITE + "; -fx-background-radius: 10;");
        
        TextField partNameField = new TextField();
        partNameField.setPrefHeight(35);
        partNameField.setPromptText("Part Name");
        
        TextField newQtyField = new TextField();
        newQtyField.setPrefHeight(35);
        newQtyField.setPromptText("NEW total quantity");
        
        form.add(new Label("Part Name:"), 0, 0);
        form.add(partNameField, 1, 0);
        form.add(new Label("New Quantity:"), 0, 1);
        form.add(newQtyField, 1, 1);
        
        // Style labels
        for (int i = 0; i < form.getChildren().size(); i++) {
            if (form.getChildren().get(i) instanceof Label) {
                Label lbl = (Label) form.getChildren().get(i);
                lbl.setTextFill(Color.web(TEXT_DARK));
            }
        }
        
        Button updateBtn = new Button("Set Quantity");
        updateBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        updateBtn.setOnAction(e -> {
            try {
                String partName = partNameField.getText().trim();
                int newQty = Integer.parseInt(newQtyField.getText().trim());
                
                if (sceneManager.getMaintenanceDao().setPartQuantity(partName, newQty)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "SUCCESS: Set quantity of '" + partName + "' to " + newQty + ".");
                    partNameField.clear();
                    newQtyField.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", 
                        "Could not set quantity. Part may not exist or DB failed.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid quantity.");
            }
        });
        
        content.getChildren().addAll(title, form, updateBtn);
        tab.setContent(content);
        return tab;
    }
    
    // Case 6: Hire Technician (lines 743-766)
    private Tab createHireTechnicianTab() {
        Tab tab = new Tab("Hire Technician");
        tab.setClosable(false);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: " + CARD_WHITE + ";");
        
        Label title = new Label("Hire New Technician");
        title.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        title.setTextFill(Color.web(TEXT_DARK));
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: " + BG_WHITE + "; -fx-background-radius: 10;");
        
        TextField nameField = new TextField();
        nameField.setPrefHeight(35);
        TextField empIdField = new TextField();
        empIdField.setPrefHeight(35);
        TextField deptField = new TextField();
        deptField.setPrefHeight(35);
        TextField salaryField = new TextField();
        salaryField.setPrefHeight(35);
        TextField ageField = new TextField();
        ageField.setPrefHeight(35);
        
        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Employee ID:"), 0, 1);
        form.add(empIdField, 1, 1);
        form.add(new Label("Department:"), 0, 2);
        form.add(deptField, 1, 2);
        form.add(new Label("Salary:"), 0, 3);
        form.add(salaryField, 1, 3);
        form.add(new Label("Age:"), 0, 4);
        form.add(ageField, 1, 4);
        
        // Style labels
        for (int i = 0; i < form.getChildren().size(); i++) {
            if (form.getChildren().get(i) instanceof Label) {
                Label lbl = (Label) form.getChildren().get(i);
                lbl.setTextFill(Color.web(TEXT_DARK));
            }
        }
        
        Button hireBtn = new Button("Hire Technician");
        hireBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        hireBtn.setOnAction(e -> {
            try {
                Technician newTech = new Technician(
                    nameField.getText(),
                    empIdField.getText(),
                    deptField.getText(),
                    Double.parseDouble(salaryField.getText()),
                    Integer.parseInt(ageField.getText())
                );
                
                if (sceneManager.getTechnicianDao().hireNewTechnician(newTech)) {
                    showAlert(Alert.AlertType.INFORMATION, "Hiring Successful", 
                        "New Technician " + newTech.getName() + " hired with ID: " + newTech.getPersonID());
                    // Clear form
                    nameField.clear();
                    empIdField.clear();
                    deptField.clear();
                    salaryField.clear();
                    ageField.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Hiring Failed", 
                        "Hiring failed. Check input or DB connection.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for salary and age.");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + ex.getMessage());
            }
        });
        
        content.getChildren().addAll(title, form, hireBtn);
        tab.setContent(content);
        return tab;
    }
    
    private Button createControlButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(260);
        btn.setPrefHeight(40);
        btn.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 13));
        btn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        return btn;
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


