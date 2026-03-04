package SpaceportSystem.UI.views;

import SpaceportSystem.*;
import SpaceportSystem.UI.NavigationManager;
import SpaceportSystem.UI.SpaceTheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * BookingConfirmationView - Success screen after booking
 * TODO: Implement full functionality
 */
public class BookingConfirmationView {
    
    private final NavigationManager navManager;
    private final Trip trip;
    private final Ticket ticket;
    private final BorderPane root;
    
    public BookingConfirmationView(NavigationManager navManager, Trip trip, Ticket ticket) {
        this.navManager = navManager;
        this.trip = trip;
        this.ticket = ticket;
        this.root = new BorderPane();
        buildUI();
    }
    
    private void buildUI() {
        root.setStyle(SpaceTheme.getMainBackgroundStyle());
        
        // Resolve current passenger from NavigationManager
        Passenger passenger = navManager.getCurrentPassenger();
        
        // --- Top Bar ---
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Label heading = new Label("BOOKING CONFIRMED");
        heading.setStyle("-fx-font-size: 24px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: " + SpaceTheme.ACCENT_PRIMARY + ";");
        
        Button backButton = new Button("BACK TO DASHBOARD");
        backButton.setStyle(SpaceTheme.getButtonSecondaryStyle());
        backButton.setOnAction(e -> {
            if (passenger != null) {
                navManager.showPassengerDashboard(passenger);
            } else {
                navManager.showPassengerLogin();
            }
        });
        
        topBar.getChildren().addAll(heading, backButton);
        BorderPane.setMargin(topBar, new Insets(10, 20, 0, 20));
        root.setTop(topBar);
        
        // --- Center Card ---
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));
        
        // Big checkmark + title
        Label checkIcon = new Label("✓");
        checkIcon.setStyle("-fx-font-size: 64px;"
                + "-fx-text-fill: " + SpaceTheme.ACCENT_SUCCESS + ";");
        
        Label title = new Label("Your interplanetary journey is booked!");
        title.setStyle("-fx-font-size: 28px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: " + SpaceTheme.TEXT_PRIMARY + ";");
        
        String passengerName = (passenger != null) ? passenger.getName() : "Passenger";
        String tier = (ticket != null) ? ticket.getClassTier() : "N/A";
        String seat = (ticket != null) ? ticket.getSeatNo() : "N/A";
        String tripNo = (trip != null) ? trip.getTripNo() : "N/A";
        String destination = (trip != null) ? trip.getDestinationLocation() : "Unknown Destination";
        
        // Optional check: confirm this ticket is assigned to the logged-in passenger
        boolean assignedToCurrent =
                passenger != null
                        && ticket != null
                        && ticket.getPassengerID() == passenger.getPersonID();
        
        Label summary = new Label(
                "Passenger: " + passengerName + "\n" +
                "Trip: " + tripNo + " → " + destination + "\n" +
                "Class Tier: " + tier + "\n" +
                "Seat: " + seat
        );
        summary.setStyle("-fx-font-size: 16px;"
                + "-fx-text-fill: " + SpaceTheme.TEXT_SECONDARY + ";");
        summary.setWrapText(true);
        summary.setMaxWidth(500);
        
        Label assignmentLabel = new Label();
        assignmentLabel.setWrapText(true);
        assignmentLabel.setMaxWidth(500);
        if (assignedToCurrent) {
            assignmentLabel.setText("✓ This ticket is correctly assigned to your profile.");
            assignmentLabel.setStyle("-fx-font-size: 14px;"
                    + "-fx-text-fill: " + SpaceTheme.ACCENT_SUCCESS + ";");
        } else {
            assignmentLabel.setText("⚠ Could not verify ticket ownership from the current session.");
            assignmentLabel.setStyle("-fx-font-size: 14px;"
                    + "-fx-text-fill: " + SpaceTheme.ACCENT_WARNING + ";");
        }
        
        centerBox.getChildren().addAll(checkIcon, title, summary, assignmentLabel);
        root.setCenter(centerBox);
    }
    
    public Scene getScene() {
        return new Scene(root, 1400, 900);
    }
}
