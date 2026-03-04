package SpaceportSystem.UI.views;

import SpaceportSystem.uicontroller.SceneManager;
import SpaceportSystem.model.Trip;
import SpaceportSystem.model.Ticket;
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

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PassengerDashboard extends BorderPane {

    private final SceneManager sceneManager;
    private final TableView<Trip> tripTable;
    private final ComboBox<String> classComboBox;

    public PassengerDashboard(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.tripTable = new TableView<>();
        this.classComboBox = new ComboBox<>();
        buildUI();
        loadTrips();
    }

    private void buildUI() {
        setStyle("-fx-background-color: #FFFFFF;");

        // Header
        Label header = new Label("Passenger Dashboard");
        header.setTextFill(Color.web("#FFFFFF"));
        header.setFont(Font.font("Segoe UI", 24));

        Button logoutButton = new Button("Logout");
        styleSecondaryButton(logoutButton);
        logoutButton.setOnAction(e -> {
            sceneManager.setCurrentPassenger(null);
            sceneManager.showPassengerLogin();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerBox = new HBox(20, header, spacer, logoutButton);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #00335A;");

        setTop(headerBox);

        // Center area - trips table
        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(20));
        centerBox.setAlignment(Pos.TOP_CENTER);

        Label tripsLabel = new Label("Available Trips");
        tripsLabel.setFont(Font.font("Segoe UI", 18));
        tripsLabel.setTextFill(Color.web("#333333"));

        configureTripTable();

        // Booking controls
        HBox bookingBox = new HBox(10);
        bookingBox.setAlignment(Pos.CENTER_LEFT);

        Label classLabel = new Label("Seat Class:");
        classLabel.setTextFill(Color.web("#333333"));

        classComboBox.getItems().addAll("ECONOMY", "BUSINESS", "FIRST");
        classComboBox.setPromptText("Select Class");

        Button bookButton = new Button("Book Selected Trip");
        stylePrimaryButton(bookButton);

        bookButton.setOnAction(e -> handleBookTrip());

        bookingBox.getChildren().addAll(classLabel, classComboBox, bookButton);

        centerBox.getChildren().addAll(tripsLabel, tripTable, bookingBox);

        setCenter(centerBox);
    }

    private void configureTripTable() {
        tripTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Trip, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));

        TableColumn<Trip, String> launchDateCol = new TableColumn<>("Launch Date");
        launchDateCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getLaunchDate() == null) {
                return null;
            }
            String formatted = cellData.getValue().getLaunchDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });

        TableColumn<Trip, String> rocketCol = new TableColumn<>("Rocket");
        rocketCol.setCellValueFactory(new PropertyValueFactory<>("rocketName")); // assume property

        TableColumn<Trip, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status")); // assume property

        tripTable.getColumns().addAll(destinationCol, launchDateCol, rocketCol, statusCol);
        tripTable.setPrefHeight(450);
    }

    private void loadTrips() {
        try {
            // Backend requirement from prompt:
            // TripDao.getAllTrips()
            List<Trip> trips = sceneManager.getTripDao().getAllTrips();
            ObservableList<Trip> data = FXCollections.observableArrayList(trips);
            tripTable.setItems(data);
        } catch (Exception ex) {
            showAlert("Error Loading Trips", ex.getMessage());
        }
    }

    private void handleBookTrip() {
        Trip selectedTrip = tripTable.getSelectionModel().getSelectedItem();
        if (selectedTrip == null) {
            showAlert("Selection Required", "Please select a trip to book.");
            return;
        }

        String seatClass = classComboBox.getValue();
        if (seatClass == null || seatClass.isEmpty()) {
            showAlert("Selection Required", "Please select a seat class.");
            return;
        }

        Passenger passenger = sceneManager.getCurrentPassenger();
        if (passenger == null) {
            showAlert("Session Error", "Passenger session expired. Please log in again.");
            sceneManager.showPassengerLogin();
            return;
        }

        try {
            // Backend requirement from prompt:
            // ticketDao.getAvailableTicketForTripAndClass(Trip trip, String seatClass, Passenger passenger)
            Ticket ticket = sceneManager.getTicketDao()
                    .getAvailableTicketForTripAndClass(selectedTrip, seatClass, passenger);

            if (ticket == null) {
                showAlert("No Availability", "No tickets available for selected class on this trip.");
                return;
            }

            // Success: show BookingConfirmationView via SceneManager
            sceneManager.showBookingConfirmation(selectedTrip, ticket);

        } catch (Exception ex) {
            showAlert("Booking Error", ex.getMessage());
        }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
