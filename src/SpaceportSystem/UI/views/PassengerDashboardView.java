package SpaceportSystem.UI.views;

import SpaceportSystem.*;
import SpaceportSystem.UI.NavigationManager;
import SpaceportSystem.UI.SpaceTheme;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * PassengerDashboardView - Trip browsing and booking
 * TODO: Implement full functionality
 */
public class PassengerDashboardView {
    
    private final NavigationManager navManager;
    private final Passenger passenger;
    private final BorderPane root;
    
    public PassengerDashboardView(NavigationManager navManager, Passenger passenger) {
        this.navManager = navManager;
        this.passenger = passenger;
        this.root = new BorderPane();
        buildUI();
    }
    
    private void buildUI() {
        root.setStyle(SpaceTheme.getMainBackgroundStyle());
        // TODO: Build passenger dashboard UI
    }
    
    public Scene getScene() {
        return new Scene(root, 1400, 900);
    }
}
