package SpaceportSystem.UI.views;

import SpaceportSystem.uicontroller.SceneManager;
import SpaceportSystem.model.Employee;
import SpaceportSystem.model.Trip;
import SpaceportSystem.model.MaintenanceTask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class StaffDashboard extends BorderPane {

    private final SceneManager sceneManager;

    public StaffDashboard(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        buildUI();
    }

    private void buildUI() {
        setStyle("-fx-background-color: #FFFFFF;");

        // Header
        Label header = new Label("Staff Dashboard");
        header.setTextFill(Color.web("#FFFFFF"));
        header.setFont(Font.font("Segoe UI", 24));

        Button logoutButton = new Button("Logout");
        styleSecondaryButton(logoutButton);
        logoutButton.setOnAction(e -> {
            sceneManager.setCurrentEmployee(null);
            sceneManager.showPassengerLogin();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerBox = new HBox(20, header, spacer, logoutButton);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #00335A;");
        setTop(headerBox);

        Employee staff = sceneManager.getCurrentEmployee();
        if (staff == null) {
            Label error = new Label("No staff member in session.");
            error.setTextFill(Color.web("#333333"));
            error.setFont(Font.font("Segoe UI", 16));
            BorderPane.setAlignment(error, Pos.CENTER);
            setCenter(error);
            return;
        }

        // Backend assumption: Employee.getRole() returns "PILOT" or "TECH"
        String role = staff.getRole();

        if ("PILOT".equalsIgnoreCase(role)) {
            setCenter(buildPilotView(staff));
        } else if ("TECH".equalsIgnoreCase(role) || "TECHNICIAN".equalsIgnoreCase(role)) {
            setCenter(buildTechView(staff));
        } else {
            Label unknown = new Label("Unknown role: " + role);
            unknown.setTextFill(Color.web("#333333"));
            unknown.setFont(Font.font("Segoe UI", 16));
            BorderPane.setAlignment(unknown, Pos.CENTER);
            setCenter(unknown);
        }
    }

    // === Pilot view ===
    private VBox buildPilotView(Employee pilot) {
        VBox box = new VBox(15);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(20));

        Label title = new Label("Assigned Trips (Pilot)");
        title.setFont(Font.font("Segoe UI", 18));
        title.setTextFill(Color.web("#333333"));

        TableView<Trip> tripTable = new TableView<>();
        tripTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Trip, Integer> idCol = new TableColumn<>("Trip ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Trip, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));

        TableColumn<Trip, String> dateCol = new TableColumn<>("Launch Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("launchDateString")); // assume property

        TableColumn<Trip, String> rocketCol = new TableColumn<>("Rocket");
        rocketCol.setCellValueFactory(new PropertyValueFactory<>("rocketName"));

        tripTable.getColumns().addAll(idCol, destinationCol, dateCol, rocketCol);

        try {
            // Backend assumption:
            // EmployeeDao.getTripsForPilot(int pilotId) -> List<Trip>
            List<Trip> trips = sceneManager.getEmployeeDao().getTripsForPilot(pilot.getId());
            ObservableList<Trip> data = FXCollections.observableArrayList(trips);
            tripTable.setItems(data);
        } catch (Exception ex) {
            // Could show a message label if desired
        }

        box.getChildren().addAll(title, tripTable);
        return box;
    }

    // === Technician view ===
    private VBox buildTechView(Employee tech) {
        VBox box = new VBox(15);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(20));

        Label title = new Label("Maintenance Tasks (Technician)");
        title.setFont(Font.font("Segoe UI", 18));
        title.setTextFill(Color.web("#333333"));

        TableView<MaintenanceTask> taskTable = new TableView<>();
        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<MaintenanceTask, Integer> idCol = new TableColumn<>("Task ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<MaintenanceTask, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<MaintenanceTask, String> rocketCol = new TableColumn<>("Rocket");
        rocketCol.setCellValueFactory(new PropertyValueFactory<>("rocketName"));

        TableColumn<MaintenanceTask, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<MaintenanceTask, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button completeButton = new Button("Complete Task");

            {
                stylePrimaryButton(completeButton);
                completeButton.setOnAction(e -> {
                    MaintenanceTask task = getTableView().getItems().get(getIndex());
                    if (task != null) {
                        try {
                            // Backend assumption:
                            // MaintenanceDao.completeTask(int taskId)
                            sceneManager.getMaintenanceDao().completeTask(task.getId());

                            // Refresh
                            List<MaintenanceTask> tasks = sceneManager.getMaintenanceDao()
                                    .getTasksForTechnician(tech.getId());
                            taskTable.setItems(FXCollections.observableArrayList(tasks));
                        } catch (Exception ex) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Error completing task: " + ex.getMessage());
                            alert.showAndWait();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(completeButton);
                }
            }
        });

        taskTable.getColumns().addAll(idCol, descriptionCol, rocketCol, statusCol, actionCol);

        try {
            // Backend assumption:
            // MaintenanceDao.getTasksForTechnician(int techId) -> List<MaintenanceTask>
            List<MaintenanceTask> tasks = sceneManager.getMaintenanceDao()
                    .getTasksForTechnician(tech.getId());
            taskTable.setItems(FXCollections.observableArrayList(tasks));
        } catch (Exception ex) {
            // Could show a message label
        }

        box.getChildren().addAll(title, taskTable);
        return box;
    }

    private void stylePrimaryButton(Button button) {
        button.setStyle(
                "-fx-background-color: #005288;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 12px;" +
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
}
