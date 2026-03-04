package SpaceportSystem.UI.views;

import SpaceportSystem.UI.NavigationManager;
import SpaceportSystem.UI.SpaceTheme;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * RoleSelectionView - Main entry screen with 4 role cards
 * Futuristic space-themed design with animations
 */
public class RoleSelectionView {
    
    private final NavigationManager navManager;
    private final BorderPane root;
    
    public RoleSelectionView(NavigationManager navManager) {
        this.navManager = navManager;
        this.root = new BorderPane();
        buildUI();
    }
    
    private void buildUI() {
        root.setStyle(SpaceTheme.getMainBackgroundStyle());
        
        // Header
        VBox header = createHeader();
        root.setTop(header);
        
        // Center - Role cards
        HBox roleCards = createRoleCards();
        root.setCenter(roleCards);
        
        // Footer
        HBox footer = createFooter();
        root.setBottom(footer);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(60, 20, 40, 20));
        
        // Main title
        Label title = new Label("PROTOS SPACEPORT");
        title.setStyle("-fx-font-size: 56px; " +
                      "-fx-font-weight: bold; " +
                      "-fx-text-fill: " + SpaceTheme.ACCENT_PRIMARY + ";" +
                      "-fx-effect: dropshadow(gaussian, rgba(0, 217, 255, 0.8), 30, 0, 0, 0);");
        
        // Subtitle
        Label subtitle = new Label("MISSION CONTROL SYSTEM");
        subtitle.setStyle("-fx-font-size: 18px; " +
                         "-fx-text-fill: " + SpaceTheme.TEXT_SECONDARY + ";" +
                         "-fx-letter-spacing: 3px;");
        
        // Tagline
        Label tagline = new Label("Select your role to access the system");
        tagline.setStyle("-fx-font-size: 14px; " +
                        "-fx-text-fill: " + SpaceTheme.TEXT_MUTED + ";" +
                        "-fx-padding: 20 0 0 0;");
        
        header.getChildren().addAll(title, subtitle, tagline);
        
        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(1500), header);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        
        return header;
    }
    
    private HBox createRoleCards() {
        HBox container = new HBox(40);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));
        
        // Create 4 role cards
        VBox passengerCard = createRoleCard(
            "PASSENGER", 
            "Book interplanetary flights",
            SpaceTheme.ACCENT_PRIMARY,
            "👤",
            () -> navManager.showPassengerLogin()
        );
        
        VBox adminCard = createRoleCard(
            "ADMINISTRATOR",
            "Control panel access",
            SpaceTheme.ACCENT_SECONDARY,
            "⚙️",
            () -> navManager.showAdminDashboard()
        );
        
        VBox pilotCard = createRoleCard(
            "PILOT",
            "Flight operations",
            SpaceTheme.ACCENT_SUCCESS,
            "✈️",
            () -> navManager.showPilotDashboard()
        );
        
        VBox techCard = createRoleCard(
            "TECHNICIAN",
            "Maintenance & repairs",
            SpaceTheme.ACCENT_WARNING,
            "🔧",
            () -> navManager.showTechnicianDashboard()
        );
        
        container.getChildren().addAll(passengerCard, adminCard, pilotCard, techCard);
        
        return container;
    }
    
    private VBox createRoleCard(String role, String description, String accentColor, String icon, Runnable action) {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(250);
        card.setPrefHeight(320);
        card.setPadding(new Insets(30));
        
        String baseStyle = "-fx-background-color: " + SpaceTheme.BG_CARD + ";" +
                          "-fx-background-radius: 20;" +
                          "-fx-border-color: " + accentColor + ";" +
                          "-fx-border-width: 2;" +
                          "-fx-border-radius: 20;" +
                          "-fx-cursor: hand;";
        
        card.setStyle(baseStyle);
        
        // Icon circle
        StackPane iconContainer = new StackPane();
        Circle iconCircle = new Circle(50);
        iconCircle.setStyle("-fx-fill: " + accentColor + ";" +
                           "-fx-effect: dropshadow(gaussian, " + accentColor + ", 20, 0.7, 0, 0);");
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 40px;");
        
        iconContainer.getChildren().addAll(iconCircle, iconLabel);
        
        // Role name
        Label roleLabel = new Label(role);
        roleLabel.setStyle("-fx-font-size: 20px;" +
                          "-fx-font-weight: bold;" +
                          "-fx-text-fill: " + SpaceTheme.TEXT_PRIMARY + ";");
        
        // Description
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 13px;" +
                          "-fx-text-fill: " + SpaceTheme.TEXT_SECONDARY + ";" +
                          "-fx-text-alignment: center;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(200);
        
        // Access button
        Label accessLabel = new Label("ACCESS →");
        accessLabel.setStyle("-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: " + accentColor + ";");
        
        card.getChildren().addAll(iconContainer, roleLabel, descLabel, accessLabel);
        
        // Hover effects
        card.setOnMouseEntered(e -> {
            card.setStyle(baseStyle + 
                         "-fx-background-color: " + SpaceTheme.BG_CARD_HOVER + ";" +
                         "-fx-effect: dropshadow(gaussian, " + accentColor + ", 30, 0.8, 0, 0);");
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle(baseStyle);
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
        
        card.setOnMouseClicked(e -> {
            // Flash animation before navigation
            FadeTransition flash = new FadeTransition(Duration.millis(200), card);
            flash.setFromValue(1.0);
            flash.setToValue(0.5);
            flash.setCycleCount(2);
            flash.setAutoReverse(true);
            flash.setOnFinished(event -> action.run());
            flash.play();
        });
        
        // Initial animation
        FadeTransition fade = new FadeTransition(Duration.millis(800), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(card.getChildren().indexOf(card) * 150));
        fade.play();
        
        return card;
    }
    
    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20));
        
        Label footerText = new Label("© 2025 Protos Spaceport Corporation | Version 2.0");
        footerText.setStyle("-fx-font-size: 12px;" +
                           "-fx-text-fill: " + SpaceTheme.TEXT_MUTED + ";");
        
        footer.getChildren().add(footerText);
        return footer;
    }
    
    public Scene getScene() {
        return new Scene(root, 1400, 900);
    }
}
