package UI.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Lightweight role selection mock view (legacy). Kept for reference but updated
 * to the light blue/white aesthetic so it can be reused without dark styling.
 * Not wired to navigation; SceneManager drives real flows.
 */
public class RoleSelectView {

    // Light palette
    private static final String BG_WHITE = "#F7F9FC";
    private static final String CARD_WHITE = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#2D7FF9";
    private static final String BORDER_GREY = "#E1E6EE";
    private static final String TEXT_DARK = "#1F2933";

    public Parent getView() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_WHITE + ";");

        // Top-right role buttons
        HBox topRightButtons = new HBox(12);
        topRightButtons.setPadding(new Insets(18));
        topRightButtons.setAlignment(Pos.TOP_RIGHT);

        Button adminBtn = buildPillButton("Admin Dashboard");
        Button pilotBtn = buildPillButton("Pilot Dashboard");
        topRightButtons.getChildren().addAll(adminBtn, pilotBtn);
        root.setTop(topRightButtons);

        // Center card
        VBox loginCard = new VBox(16);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(26));
        loginCard.setStyle("-fx-background-color: " + CARD_WHITE + ";"
                + "-fx-background-radius: 14;"
                + "-fx-border-radius: 14;"
                + "-fx-border-color: " + BORDER_GREY + ";");

        Label welcomeLabel = new Label("Welcome to Protos Spaceport");
        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: 700; -fx-text-fill: " + TEXT_DARK + ";");

        Label subLabel = new Label("Login as Passenger to book your interplanetary journey!");
        subLabel.setWrapText(true);
        subLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #4B5563;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setPrefHeight(38);
        nameField.setStyle("-fx-background-color: #F8FAFB; -fx-border-color: " + BORDER_GREY + "; -fx-background-radius: 8; -fx-border-radius: 8;");

        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(200);
        loginBtn.setPrefHeight(40);
        loginBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 10;");

        loginCard.getChildren().addAll(welcomeLabel, subLabel, nameField, loginBtn);

        StackPane center = new StackPane(loginCard);
        center.setPadding(new Insets(40));
        root.setCenter(center);

        // Placeholder actions
        loginBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter your name to continue.").showAndWait();
            } else {
                System.out.println("Passenger logged in: " + name);
            }
        });
        adminBtn.setOnAction(e -> System.out.println("Go to Admin Dashboard"));
        pilotBtn.setOnAction(e -> System.out.println("Go to Pilot Dashboard"));

        return root;
    }

    private Button buildPillButton(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(34);
        btn.setStyle("-fx-background-color: white; -fx-text-fill: " + TEXT_DARK + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: transparent;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: " + TEXT_DARK + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";"));
        return btn;
    }
}
