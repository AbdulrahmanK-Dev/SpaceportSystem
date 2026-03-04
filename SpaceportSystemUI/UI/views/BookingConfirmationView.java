package SpaceportSystem.UI.views;

import SpaceportSystem.uicontroller.SceneManager;
import SpaceportSystem.model.Trip;
import SpaceportSystem.model.Ticket;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.format.DateTimeFormatter;

public class BookingConfirmationView extends BorderPane {

    private final SceneManager sceneManager;
    private final Trip trip;
    private final Ticket ticket;

    public BookingConfirmationView(SceneManager sceneManager, Trip trip, Ticket ticket) {
        this.sceneManager = sceneManager;
        this.trip = trip;
        this.ticket = ticket;
        buildUI();
    }

    private void buildUI() {
        setStyle("-fx-background-color: #FFFFFF;");

        // Header
        Label header = new Label("Booking Confirmed!");
        header.setTextFill(Color.web("#FFFFFF"));
        header.setFont(Font.font("Segoe UI", 24));

        HBox headerBox = new HBox(header);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #00335A;");
        setTop(headerBox);

        // Center content
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));

        Label checkmark = new Label("\u2713");
        checkmark.setTextFill(Color.web("#28a745"));
        checkmark.setFont(Font.font("Segoe UI", 72));

        Label congrats = new Label("Your spaceflight booking has been confirmed.");
        congrats.setTextFill(Color.web("#333333"));
        congrats.setFont(Font.font("Segoe UI", 18));

        VBox detailsBox = new VBox(8);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        detailsBox.setPadding(new Insets(20));
        detailsBox.setMaxWidth(400);
        detailsBox.setStyle(
                "-fx-background-color: #F7F9FB;" +
                "-fx-border-color: #E0E6ED;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;");

        Label destination = infoLabel("Destination", trip.getDestination());
        String dateStr = trip.getLaunchDate() != null
                ? trip.getLaunchDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : "N/A";
        Label launchDate = infoLabel("Launch Date", dateStr);

        Label rocketName = infoLabel("Rocket", trip.getRocketName()); // assume getter
        Label ticketId = infoLabel("Ticket ID", String.valueOf(ticket.getId())); // assume getId()
        Label seatClass = infoLabel("Seat Class", ticket.getSeatClass()); // assume getSeatClass()

        detailsBox.getChildren().addAll(destination, launchDate, rocketName, ticketId, seatClass);

        Button backButton = new Button("Back to Dashboard");
        backButton.setFont(Font.font("Segoe UI", 14));
        backButton.setTextFill(Color.WHITE);
        backButton.setStyle(
                "-fx-background-color: #005288;" +
                "-fx-background-radius: 4;");
        backButton.setOnAction(e -> sceneManager.showPassengerDashboard());

        centerBox.getChildren().addAll(checkmark, congrats, detailsBox, backButton);

        setCenter(centerBox);
    }

    private Label infoLabel(String label, String value) {
        Label l = new Label(label + ": " + value);
        l.setTextFill(Color.web("#333333"));
        l.setFont(Font.font("Segoe UI", 14));
        return l;
    }
}
