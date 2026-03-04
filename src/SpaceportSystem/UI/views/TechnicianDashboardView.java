package SpaceportSystem.UI.views;

import SpaceportSystem.UI.NavigationManager;
import SpaceportSystem.UI.SpaceTheme;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * TechnicianDashboardView - Technician workstation interface
 * TODO: Implement full functionality
 */
public class TechnicianDashboardView {
    
    private final NavigationManager navManager;
    private final BorderPane root;
    
    public TechnicianDashboardView(NavigationManager navManager) {
        this.navManager = navManager;
        this.root = new BorderPane();
        buildUI();
    }
    
    private void buildUI() {
        root.setStyle(SpaceTheme.getMainBackgroundStyle());
        // TODO: Build technician dashboard UI
    }
    
    public Scene getScene() {
        return new Scene(root, 1400, 900);
    }
}
