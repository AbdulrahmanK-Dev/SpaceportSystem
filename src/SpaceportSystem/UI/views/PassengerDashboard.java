package SpaceportSystem.UI.views;

import SpaceportSystem.*;
import SpaceportSystem.uicontroller.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * PassengerDashboard - Trip selection and booking interface.
 * Implements the full booking flow from SpaceportTEST.java, but on success
 * navigates to a dedicated BookingConfirmationView instead of showing an alert.
 */
public class PassengerDashboard {
    
    private final SceneManager sceneManager;
    private final Passenger passenger;
    private VBox root;
    
    // Apple-inspired light palette (matches PassengerLoginView)
    private static final String BG_WHITE = "#F7F9FC";
    private static final String CARD_WHITE = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#2D7FF9";
    private static final String BORDER_GREY = "#E1E6EE";
    private static final String TEXT_DARK = "#1F2933";
    private static final String TEXT_MUTED = "#4B5563";
    
    private TableView<Trip> tripTable;
    private ObservableList<Trip> tripList;
    
    public PassengerDashboard(SceneManager sceneManager, Passenger passenger) {
        this.sceneManager = sceneManager;
        this.passenger = passenger;
        buildUI();
        loadAvailableTrips();
    }
    
    private void buildUI() {
        root = new VBox(24);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: " + BG_WHITE + ";");
        
        // Header
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Passenger Dashboard");
        titleLabel.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web(TEXT_DARK));
        
        Label welcomeLabel = new Label("Welcome, " + passenger.getName() + "!");
        welcomeLabel.setFont(Font.font("SF Pro Text", 14));
        welcomeLabel.setTextFill(Color.web(TEXT_MUTED));
        
        Button backBtn = new Button("Logout");
        backBtn.setStyle("-fx-background-color: white; -fx-text-fill: " + TEXT_DARK
                + "; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: " + BORDER_GREY + ";");
        backBtn.setOnAction(e -> sceneManager.navigateToPassengerLogin());
        
        header.getChildren().addAll(titleLabel, welcomeLabel, backBtn);
        
        // Trip Selection Section
        VBox tripSection = createTripSelectionSection();
        
        // Placeholder booking section (not used directly now, confirmation view is separate)
        VBox bookingSection = new VBox();
        bookingSection.setVisible(false);
        
        root.getChildren().addAll(header, tripSection, bookingSection);
    }
    
    private VBox createTripSelectionSection() {
        VBox section = new VBox(18);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: " + CARD_WHITE + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");
        section.setMaxWidth(960);
        
        Label sectionTitle = new Label("Available Trips");
        sectionTitle.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web(TEXT_DARK));
        
        // Trip Table
        tripTable = new TableView<>();
        tripTable.setPrefHeight(320);
        tripTable.setStyle("-fx-background-color: white;");
        
        TableColumn<Trip, String> tripNoCol = new TableColumn<>("Trip No");
        tripNoCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getTripNo())
        );
        tripNoCol.setPrefWidth(120);
        
        TableColumn<Trip, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDestinationLocation())
        );
        destinationCol.setPrefWidth(200);
        
        TableColumn<Trip, String> departureCol = new TableColumn<>("Departure Time");
        departureCol.setCellValueFactory(cellData -> {
            java.util.Date date = cellData.getValue().getDepartureTime();
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return new javafx.beans.property.SimpleStringProperty(sdf.format(date));
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        departureCol.setPrefWidth(200);
        
        TableColumn<Trip, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            tripStatus status = cellData.getValue().getStatus();
            return new javafx.beans.property.SimpleStringProperty(status != null ? status.toString() : "N/A");
        });
        statusCol.setPrefWidth(150);
        
        tripTable.getColumns().addAll(tripNoCol, destinationCol, departureCol, statusCol);
        
        Button selectBtn = new Button("Select Trip");
        selectBtn.setPrefWidth(170);
        selectBtn.setPrefHeight(40);
        selectBtn.setFont(Font.font("SF Pro Text", FontWeight.BOLD, 14));
        selectBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");
        
        selectBtn.setOnAction(e -> {
            Trip selectedTrip = tripTable.getSelectionModel().getSelectedItem();
            if (selectedTrip != null) {
                showClassTierSelection(selectedTrip);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a trip first.");
            }
        });
        
        section.getChildren().addAll(sectionTitle, tripTable, selectBtn);
        return section;
    }
    
    private void showClassTierSelection(Trip selectedTrip) {
        // Class Tier Selection Dialog
        Dialog<String> tierDialog = new Dialog<>();
        tierDialog.setTitle("Class Tier Selection");
        tierDialog.setHeaderText("Select your preferred class tier for Trip " + selectedTrip.getTripNo());
        
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        tierDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        ToggleGroup tierGroup = new ToggleGroup();
        
        RadioButton economyBtn = new RadioButton("ECONOMY - $500 (Standard price)");
        economyBtn.setToggleGroup(tierGroup);
        economyBtn.setSelected(true);
        
        RadioButton businessBtn = new RadioButton("BUSINESS - $1500 (Premium seating)");
        businessBtn.setToggleGroup(tierGroup);
        
        RadioButton firstBtn = new RadioButton("FIRST - $3000 (Luxury/private quarters)");
        firstBtn.setToggleGroup(tierGroup);
        
        content.getChildren().addAll(economyBtn, businessBtn, firstBtn);
        tierDialog.getDialogPane().setContent(content);
        
        tierDialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                RadioButton selected = (RadioButton) tierGroup.getSelectedToggle();
                if (selected == economyBtn) return "ECONOMY";
                if (selected == businessBtn) return "BUSINESS";
                if (selected == firstBtn) return "FIRST";
            }
            return null;
        });
        
        tierDialog.showAndWait().ifPresent(chosenTier -> {
            if (chosenTier != null) {
                processBooking(selectedTrip, chosenTier);
            }
        });
    }
    
    private void processBooking(Trip chosenTrip, String chosenTier) {
        // Booking Logic: on success, go to BookingConfirmationView (no success alert)
        try {
            Ticket availableTicket = sceneManager.getTicketDao().getAvailableTicketForTripAndClass(
                chosenTrip.getTripID(), chosenTier);
            
            if (availableTicket != null) {
                availableTicket.setPassengerID(passenger.getPersonID());
                
                if (sceneManager.getTicketDao().bookTicket(availableTicket)) {
                    // Navigate to confirmation view instead of showing an information alert
                    sceneManager.showBookingConfirmation(chosenTrip, availableTicket);
                    // Optionally refresh list (not strictly necessary if user returns later)
                    loadAvailableTrips();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Booking Failed", 
                        "Error: Failed to update the ticket status.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "No Tickets Available", 
                    "No available tickets for Trip " + chosenTrip.getTripNo() + " in " + chosenTier + " class.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                "CRITICAL ERROR: A database transaction failed during ticket booking.\n" + e.getMessage());
        }
    }
    
    private void loadAvailableTrips() {
        tripList = FXCollections.observableArrayList(
            sceneManager.getTripDao().getAllScheduledTrips()
        );
        
        tripTable.setItems(tripList);
        
        if (tripList.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Trips Available", 
                "No scheduled trips are currently available.");
        }
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
