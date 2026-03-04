package UI.views;

import SpaceportSystem.Passenger;
import SpaceportSystem.Ticket;
import SpaceportSystem.Trip;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uicontroller.SceneManager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PassengerDashboard {

    private SceneManager sceneManager;
    private Passenger passenger;
    private TableView<Trip> tripTable;
    private ObservableList<Trip> tripList;
    
    private Button economyButton;
    private Button businessButton;
    private Button firstButton;
    private Button logoutButton;
    private Label welcomeLabel;
    private Label statusLabel;

    public PassengerDashboard(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.tripList = FXCollections.observableArrayList();
        
        // Initialize buttons
        economyButton = new Button("Book ECONOMY ($500)");
        businessButton = new Button("Book BUSINESS ($1500)");
        firstButton = new Button("Book FIRST ($3000)");
        logoutButton = new Button("Logout");
        
        economyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        businessButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        firstButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        
        welcomeLabel = new Label();
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #00ffc8;");
        
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: #00ffc8;");
    }
    
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
        if (passenger != null) {
            welcomeLabel.setText("Welcome, " + passenger.getName() + "!");
            loadTrips();
        }
    }

    public Parent getView() {
        // Create TableView for trips
        tripTable = new TableView<>();
        tripTable.setItems(tripList);
        tripTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Define columns
        TableColumn<Trip, String> tripNoCol = new TableColumn<>("Trip No");
        tripNoCol.setCellValueFactory(new PropertyValueFactory<>("tripNo"));
        
        TableColumn<Trip, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destinationLocation"));
        
        TableColumn<Trip, String> departureCol = new TableColumn<>("Departure Time");
        departureCol.setCellValueFactory(cellData -> {
            Trip trip = cellData.getValue();
            if (trip.getDepartureTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return new javafx.beans.property.SimpleStringProperty(sdf.format(trip.getDepartureTime()));
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        
        TableColumn<Trip, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            Trip trip = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(trip.getStatus().toString());
        });
        
        tripTable.getColumns().addAll(tripNoCol, destinationCol, departureCol, statusCol);
        
        // Set column widths
        tripNoCol.setPrefWidth(150);
        destinationCol.setPrefWidth(250);
        departureCol.setPrefWidth(200);
        statusCol.setPrefWidth(150);
        
        // Booking buttons layout
        HBox bookingButtons = new HBox(15);
        bookingButtons.setAlignment(Pos.CENTER);
        bookingButtons.setPadding(new Insets(20));
        bookingButtons.getChildren().addAll(economyButton, businessButton, firstButton);
        
        // Top bar with welcome and logout
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15));
        topBar.getChildren().addAll(welcomeLabel, logoutButton);
        HBox.setHgrow(welcomeLabel, javafx.scene.layout.Priority.ALWAYS);
        
        // Main layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1a1a2e;");
        
        Label title = new Label("AVAILABLE TRIPS");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #00ffc8;");
        
        layout.getChildren().addAll(
            topBar,
            title,
            statusLabel,
            tripTable,
            bookingButtons
        );
        
        // Event handlers
        economyButton.setOnAction(e -> bookTicket("ECONOMY", 500.00));
        businessButton.setOnAction(e -> bookTicket("BUSINESS", 1500.00));
        firstButton.setOnAction(e -> bookTicket("FIRST", 3000.00));
        logoutButton.setOnAction(e -> sceneManager.navigateToPassengerLogin());
        
        return layout;
    }
    
    private void loadTrips() {
        tripList.clear();
        List<Trip> trips = sceneManager.getTripDAO().getAllScheduledTrips();
        tripList.addAll(trips);
        
        if (trips.isEmpty()) {
            statusLabel.setText("No scheduled trips are currently available.");
            statusLabel.setStyle("-fx-text-fill: #ff6347;");
        } else {
            statusLabel.setText("Select a trip and choose your class tier to book.");
            statusLabel.setStyle("-fx-text-fill: #00ffc8;");
        }
    }
    
    private void bookTicket(String classTier, double expectedPrice) {
        Trip selectedTrip = tripTable.getSelectionModel().getSelectedItem();
        
        if (selectedTrip == null) {
            showAlert(Alert.AlertType.WARNING, "No Trip Selected", 
                     "Please select a trip from the table before booking.");
            return;
        }
        
        if (passenger == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passenger information not available.");
            return;
        }
        
        try {
            // Get available ticket for selected trip and class tier
            Ticket availableTicket = sceneManager.getTicketDAO().getAvailableTicketForTripAndClass(
                selectedTrip.getTripID(), classTier);
            
            if (availableTicket == null) {
                showAlert(Alert.AlertType.INFORMATION, "No Tickets Available",
                         "No available tickets for Trip " + selectedTrip.getTripNo() + 
                         " in " + classTier + " class.");
                return;
            }
            
            // Set passenger ID and book the ticket
            availableTicket.setPassengerID(passenger.getPersonID());
            
            if (sceneManager.getTicketDAO().bookTicket(availableTicket)) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Booking Confirmed!");
                successAlert.setHeaderText("Success");
                successAlert.setContentText(
                    "Trip: " + selectedTrip.getTripNo() + "\n" +
                    "Tier: " + availableTicket.getClassTier() + "\n" +
                    "Seat: " + availableTicket.getSeatNo() + "\n" +
                    "Price: $" + String.format("%.2f", availableTicket.getPrice())
                );
                successAlert.showAndWait();
                
                statusLabel.setText("Booking successful! You can book another trip.");
                statusLabel.setStyle("-fx-text-fill: #4CAF50;");
                
            } else {
                showAlert(Alert.AlertType.ERROR, "Booking Failed",
                         "Failed to update the ticket status. Please try again.");
            }
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                     "A database error occurred during ticket booking: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}











