
package cedarwood;

import cedarwood.CedarWoodSystem;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
// Clock feature imports: used to display a live real-time clock in the GUI 
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;


public class HelloApplication extends Application {
    
    
// Singleton controller instance used to access the main business logic of the system
    private CedarWoodSystem cedarSystem = CedarWoodSystem.getInstance();
// Top section controls: area selection, date view, and statistics display
    private TableView<Accommodation> table;
    private ComboBox<AreaType> comboArea;
    private DatePicker dpViewDate;
    private TextField txtBreakfastStats, txtCleaningStats;
// Left panel controls: room maintenance and accommodation information
    private ChoiceBox<CleaningStatus> choiceCleaningStatus;
    private TextField txtInfoType, txtInfoNumber, txtInfoAccommodates, txtInfoPrice;
// Right panel controls: guest check-in form
    private TextField txtFName, txtLName, txtPhone, txtGuests, txtNights;
    private DatePicker dpCheckInDate;
    private CheckBox chkBreakfast;
// Bottom status label used to show success and error messages
    private Label lblStatusMessage;
    
    
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cedar Woods Accommodation System");
        // Create a label to display the live clock on the top-right of the window
        Label lblClock = new Label();
        // Style the clock label so the time and date are clearly visible
        lblClock.setAlignment(Pos.CENTER_RIGHT);
        lblClock.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        // Start the live clock so the label updates every second
        startClock(lblClock);
       
        /*HBox topBox = new HBox(15);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10));

        comboArea = new ComboBox<>();
        comboArea.getItems().addAll(AreaType.values());
        comboArea.setValue(AreaType.Hilltop);

        dpViewDate = new DatePicker(LocalDate.now());
        dpViewDate.setEditable(false);
        dpViewDate.setPrefWidth(120);
        
         Region spacer = new Region();
         HBox.setHgrow(spacer, Priority.ALWAYS);
         
        topBox.getChildren().addAll(new Label("Area:"), comboArea, new Label("Select Date to View:"), dpViewDate,new Label("Clock:"),lblClock);

        HBox statsBox = new HBox(20);*/
        comboArea = new ComboBox<>();
        comboArea.getItems().addAll(AreaType.values());
        comboArea.setValue(AreaType.Hilltop);

        dpViewDate = new DatePicker(LocalDate.now());
        dpViewDate.setEditable(false);
        dpViewDate.setPrefWidth(120);

        HBox leftBox = new HBox(15);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.getChildren().addAll(
        new Label("Area:"), comboArea,
        new Label("Select Date to View:"), dpViewDate
     );
        // Right-side container for the clock display
        HBox rightBox = new HBox(5);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.getChildren().addAll(lblClock);
       // Top layout: area/date controls on the left, live clock on the right
       BorderPane topBox = new BorderPane();
       topBox.setPadding(new Insets(10));
       topBox.setLeft(leftBox);
       topBox.setRight(rightBox);
// Statistics section showing breakfasts for the selected date and current dirty rooms
       HBox statsBox = new HBox(20);
       
        statsBox.setPadding(new Insets(5, 10, 15, 10));
        txtBreakfastStats = new TextField("0");
        txtBreakfastStats.setEditable(false);
        txtBreakfastStats.setPrefWidth(50);

        txtCleaningStats = new TextField("0");
        txtCleaningStats.setEditable(false);
        txtCleaningStats.setPrefWidth(50);

        statsBox.getChildren().addAll(new Label("Breakfasts for Selected Date:"), txtBreakfastStats, new Label("Current Rooms Require Cleaning:"), txtCleaningStats);

        VBox headerArea = new VBox(topBox, statsBox);

        setupTable();

        HBox bottomBox = new HBox(20);
        bottomBox.setPadding(new Insets(15, 10, 10, 10));
// Left panel contains maintenance controls and selected room details
        VBox leftPanel = new VBox(10);
        leftPanel.setPrefWidth(350);

        choiceCleaningStatus = new ChoiceBox<>();
        choiceCleaningStatus.getItems().addAll(CleaningStatus.values());

        GridPane infoGrid = new GridPane();
        infoGrid.setVgap(8);
        infoGrid.setHgap(10);
        txtInfoType = new TextField(); txtInfoType.setEditable(false);
        txtInfoNumber = new TextField(); txtInfoNumber.setEditable(false);
        txtInfoAccommodates = new TextField(); txtInfoAccommodates.setEditable(false);
        txtInfoPrice = new TextField(); txtInfoPrice.setEditable(false);

        infoGrid.add(new Label("Accomm. Type:"), 0, 0); infoGrid.add(txtInfoType, 1, 0);
        infoGrid.add(new Label("Accomm. Number:"), 0, 1); infoGrid.add(txtInfoNumber, 1, 1);
        infoGrid.add(new Label("Accommodates:"), 0, 2); infoGrid.add(txtInfoAccommodates, 1, 2);
        infoGrid.add(new Label("Price Per Night (£):"), 0, 3); infoGrid.add(txtInfoPrice, 1, 3);

        leftPanel.getChildren().addAll(new Label("Room Maintenance"), new HBox(10, new Label("Cleaning Status:"), choiceCleaningStatus), new Separator(), new Label("Accommodation Info"), infoGrid);
// Right panel contains the reception form for guest check-in and check-out
        VBox rightPanel = new VBox(10);
        rightPanel.setPrefWidth(400);

        GridPane recGrid = new GridPane();
        recGrid.setVgap(8);
        recGrid.setHgap(10);
        txtFName = new TextField();
        txtLName = new TextField();
        txtPhone = new TextField();
        txtGuests = new TextField();
        txtNights = new TextField();

        dpCheckInDate = new DatePicker(LocalDate.now());
        dpCheckInDate.setEditable(false);

        chkBreakfast = new CheckBox("Breakfast Required");

        recGrid.add(new Label("First Name:"), 0, 0); recGrid.add(txtFName, 1, 0);
        recGrid.add(new Label("Last Name:"), 0, 1); recGrid.add(txtLName, 1, 1);
        recGrid.add(new Label("Telephone No.:"), 0, 2); recGrid.add(txtPhone, 1, 2);
        recGrid.add(new Label("Number Guests:"), 0, 3); recGrid.add(txtGuests, 1, 3);
        recGrid.add(new Label("Check In Date:"), 0, 4); recGrid.add(dpCheckInDate, 1, 4);
        recGrid.add(new Label("Number Nights:"), 0, 5); recGrid.add(txtNights, 1, 5);
        recGrid.add(chkBreakfast, 1, 6);
// Action buttons for guest check-in and check-out
        Button btnCheckIn = new Button("Check In");
        Button btnCheckOut = new Button("Check Out");
        HBox btnBox = new HBox(20, btnCheckIn, btnCheckOut);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        rightPanel.getChildren().addAll(new Label("Accommodation Reception"), recGrid, btnBox);
        bottomBox.getChildren().addAll(leftPanel, new Separator(), rightPanel);

        lblStatusMessage = new Label();
        lblStatusMessage.setPadding(new Insets(5, 10, 5, 10));
// Main application layout: header at top, table in center, controls and messages at bottom
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(headerArea);
        mainPane.setCenter(table);
        mainPane.setBottom(new VBox(bottomBox, lblStatusMessage));
        BorderPane.setMargin(table, new Insets(0, 10, 0, 10));

        comboArea.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateTableAndStats();
            }
        });

        dpViewDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateTableAndStats();
                showMessage("System displaying room availability and stats for: " + dpViewDate.getValue(), "blue");
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Accommodation>() {
            @Override
            public void changed(ObservableValue<? extends Accommodation> obs, Accommodation oldSel, Accommodation newSel) {
                if (newSel != null) {
                    txtInfoType.setText(newSel.getType().toString());
                    txtInfoNumber.setText(String.valueOf(newSel.getAccommodationNumber()));
                    txtInfoAccommodates.setText(String.valueOf(newSel.getMaxOccupancy()));
                    txtInfoPrice.setText(String.valueOf(newSel.getPricePerNight()));
                    choiceCleaningStatus.setValue(newSel.getCleaningStatus());
                }
            }
        });

        choiceCleaningStatus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Accommodation selected = table.getSelectionModel().getSelectedItem();
                CleaningStatus newStatus = choiceCleaningStatus.getValue();

                if (selected != null && newStatus != null && selected.getCleaningStatus() != newStatus) {
                    String result = cedarSystem.updateCleaningStatus(selected.getAccommodationNumber(), newStatus);

                    if (result.startsWith("SUCCESS")) {
                        showMessage(result, "green");
                    } else {
                        showMessage(result, "red");
                        choiceCleaningStatus.setValue(selected.getCleaningStatus());
                    }
                    updateTableAndStats();
                }
            }
        });

        btnCheckIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleCheckIn();
            }
        });

        btnCheckOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleCheckOut();
            }
        });

        updateTableAndStats();

        Scene scene = new Scene(mainPane, 850, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTable() {
        // Create the main table used to display accommodation details
        table = new TableView<>();
// Column: accommodation number
        TableColumn<Accommodation, Integer> colNo = new TableColumn<>("No.");
        colNo.setCellValueFactory(new PropertyValueFactory<>("accommodationNumber"));
 // Column: accommodation type
        TableColumn<Accommodation, String> colType = new TableColumn<>("Accomm. Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

         // The table does not contain business logic here.
        // It simply gets the correct display value from the controller.
        TableColumn<Accommodation, String> colOccupancy = new TableColumn<>("Occupancy");
        colOccupancy.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> cell) {
                LocalDate targetDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
                String status = cedarSystem.getOccupancyStringForDate(cell.getValue().getAccommodationNumber(), targetDate);
                return new SimpleStringProperty(status);
            }
        });
// Column: room availability for the selected date
        TableColumn<Accommodation, String> colAvailability = new TableColumn<>("Availability");
        colAvailability.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> cell) {
                LocalDate targetDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
                String status = cedarSystem.getAvailabilityStringForDate(cell.getValue().getAccommodationNumber(), targetDate);
                return new SimpleStringProperty(status);
            }
        });
// Column: cleaning/maintenance status
        TableColumn<Accommodation, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> cell) {
                LocalDate targetDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
                String status = cedarSystem.getCleaningStatusStringForDate(cell.getValue().getAccommodationNumber(), targetDate);
                return new SimpleStringProperty(status);
            }
        });
// Column: number of guests for the selected date
        TableColumn<Accommodation, String> colGuests = new TableColumn<>("Guests");
        colGuests.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> cell) {
                LocalDate targetDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
                String guests = cedarSystem.getGuestCountStringForDate(cell.getValue().getAccommodationNumber(), targetDate);
                return new SimpleStringProperty(guests);
            }
        });
// Column: breakfast requirement for the selected date
        TableColumn<Accommodation, String> colBreakfast = new TableColumn<>("Breakfast");
        colBreakfast.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> cell) {
                LocalDate targetDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
                String breakfast = cedarSystem.getBreakfastStringForDate(cell.getValue().getAccommodationNumber(), targetDate);
                return new SimpleStringProperty(breakfast);
            }
        });
    // Add all columns to the table
        table.getColumns().addAll(colNo, colType, colOccupancy, colAvailability, colStatus, colGuests, colBreakfast);
          // Set table size and resize behavior
        table.setPrefHeight(250);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
// Refreshes the accommodation table and statistics based on the selected area and date
    private void updateTableAndStats() {
        AreaType area = comboArea.getValue();
        if (area == null) return;

        LocalDate statsDate = dpViewDate.getValue();
        if (statsDate == null) statsDate = LocalDate.now();

        table.setItems(FXCollections.observableArrayList(cedarSystem.getAccommodationsByArea(area)));
        table.refresh();

        txtCleaningStats.setText(String.valueOf(cedarSystem.getCleaningCount(area)));
        txtBreakfastStats.setText(String.valueOf(cedarSystem.getBreakfastCount(area, statsDate)));
    }
// Handles guest check-in using the selected accommodation and form input values
    private void handleCheckIn() {
        Accommodation selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select an accommodation first.", "red");
            return;
        }

        String result = cedarSystem.checkInGuest(
                selected.getAccommodationNumber(),
                txtFName.getText(),
                txtLName.getText(),
                txtPhone.getText(),
                txtGuests.getText(),
                txtNights.getText(),
                chkBreakfast.isSelected(),
                dpCheckInDate.getValue()
        );

        if (result.startsWith("SUCCESS")) {
            updateTableAndStats();
            showMessage(result, "green");
            txtFName.clear(); txtLName.clear(); txtPhone.clear(); txtGuests.clear(); txtNights.clear(); chkBreakfast.setSelected(false); dpCheckInDate.setValue(LocalDate.now());
        } else {
            showMessage(result, "red");
        }
    }
// Handles guest check-out for the selected accommodation on the selected view date
    private void handleCheckOut() {
        Accommodation selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a room to check out.", "red");
            return;
        }

        LocalDate viewDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
        String result = cedarSystem.checkOutGuest(selected.getAccommodationNumber(), viewDate);

        if (result.startsWith("SUCCESS")) {
            updateTableAndStats();
            showMessage(result, "green");
        } else {
            showMessage(result, "red");
        }
    }
// Displays a coloured status message at the bottom of the interface
    private void showMessage(String msg, String color) {
        lblStatusMessage.setText(msg);
        lblStatusMessage.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 14px;");
    }
    
    // Updates the given label every second to show the current time and date
   private void startClock(Label lblClock) {
       // Formatter for the time display, e.g. 5:02 AM
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
    // Formatter for the date display, e.g. 3/13/2026
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
// Timeline runs repeatedly and refreshes the label every second
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(0), e -> {
             // Get the current system date and time
            LocalDateTime now = LocalDateTime.now();
            // Show time on the first line and date on the second line
            lblClock.setText(now.format(timeFormatter) + "\n" + now.format(dateFormatter));
        }),
        new KeyFrame(Duration.seconds(1))
    );
// Repeat forever while the application is running
    timeline.setCycleCount(Timeline.INDEFINITE);
    // Start the clock animation
    timeline.play();
}
// Launches the JavaFX application
    public static void main(String[] args) {
        launch();
    }
}