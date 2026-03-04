package SpaceportSystem.UI;

import SpaceportSystem.uicontroller.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.initialize(primaryStage);
        SceneManager.getInstance().showPassengerLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
