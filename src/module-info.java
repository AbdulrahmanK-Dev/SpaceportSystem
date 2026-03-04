/**
 * SpaceportSystem Module Configuration
 */
module SpaceportSystem {
    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    exports SpaceportSystem;
    exports SpaceportSystem.UI;
    exports SpaceportSystem.UI.views;
    
    opens SpaceportSystem to javafx.base;
    opens SpaceportSystem.UI to javafx.fxml;
    opens SpaceportSystem.UI.views to javafx.fxml;
}
