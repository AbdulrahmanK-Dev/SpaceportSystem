package UI.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uicontroller.SceneManager;

/**
 * Legacy AdminDashboard (non-module) updated to match the light blue/white theme.
 * This is a simplified shell; core logic lives in SpaceportSystem.UI.views.AdminDashboard.
 */
public class AdminDashboard {

    // Shared light palette
    private static final String BG_WHITE = "#F7F9FC";
    private static final String CARD_WHITE = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#2D7FF9";
    private static final String BORDER_GREY = "#E1E6EE";
    private static final String TEXT_DARK = "#1F2933";
    private static final String TEXT_MUTED = "#4B5563";

    private final SceneManager sceneManager;
    private final TabPane tabPane;
    private final Tab scheduleTripTab;
    private final Tab manageRocketTab;
    private final Tab viewManifestTab;
    private final Button logoutButton;

    public AdminDashboard(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.tabPane = new TabPane();

        scheduleTripTab = new Tab("Schedule Trip");
        manageRocketTab = new Tab("Manage Rocket");
        viewManifestTab = new Tab("View Manifest");

        scheduleTripTab.setClosable(false);
        manageRocketTab.setClosable(false);
        viewManifestTab.setClosable(false);

        logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: white; -fx-text-fill: " + TEXT_DARK
                + "; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: " + BORDER_GREY + ";");
    }

    public Parent getView() {
        VBox scheduleTripContent = createScheduleTripTab();
        scheduleTripTab.setContent(scheduleTripContent);

        VBox manageRocketContent = createManageRocketTab();
        manageRocketTab.setContent(manageRocketContent);

        VBox viewManifestContent = createViewManifestTab();
        viewManifestTab.setContent(viewManifestContent);

        tabPane.getTabs().addAll(scheduleTripTab, manageRocketTab, viewManifestTab);
        tabPane.setStyle("-fx-background-color: transparent;");

        HBox topBar = new HBox(16);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16));

        Label title = new Label("Administrator Control Panel");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_DARK + ";");

        topBar.getChildren().addAll(title, logoutButton);
        HBox.setHgrow(title, javafx.scene.layout.Priority.ALWAYS);

        VBox layout = new VBox(12);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: " + BG_WHITE + ";");
        layout.getChildren().addAll(topBar, tabPane);

        logoutButton.setOnAction(e -> sceneManager.navigateToPassengerLogin());
        return layout;
    }

    private VBox createScheduleTripTab() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);
        content.setStyle("-fx-background-color: " + CARD_WHITE + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");

        Label header = new Label("Schedule New Trip");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_DARK + ";");

        Label infoLabel = new Label("This legacy view is a placeholder; main implementation lives in the SpaceportSystem module.");
        infoLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        infoLabel.setWrapText(true);

        content.getChildren().addAll(header, infoLabel);
        return content;
    }

    private VBox createManageRocketTab() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);
        content.setStyle("-fx-background-color: " + CARD_WHITE + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");

        Label header = new Label("Manage Rocket Status");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_DARK + ";");

        Label infoLabel = new Label("Use the primary Admin dashboard in the SpaceportSystem UI for full functionality.");
        infoLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        infoLabel.setWrapText(true);

        content.getChildren().addAll(header, infoLabel);
        return content;
    }

    private VBox createViewManifestTab() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);
        content.setStyle("-fx-background-color: " + CARD_WHITE + "; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: " + BORDER_GREY + ";");

        Label header = new Label("View Trip Manifest");
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_DARK + ";");

        Label infoLabel = new Label("Trip manifest management is implemented in the SpaceportSystem AdminDashboard.");
        infoLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        infoLabel.setWrapText(true);

        content.getChildren().addAll(header, infoLabel);
        return content;
    }
}



