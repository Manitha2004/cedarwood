
package cedarwood;
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
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioClip;
 

public class HelloApplication extends Application {
    
    
// Singleton controller instance used to access the main business logic of the system
    private CedarWoodSystem cedarSystem = CedarWoodSystem.getInstance();
// Top section controls: area selection, date view, and statistics display
    private TableView<Accommodation> table;
    private ComboBox<AreaType> comboArea;
    private DatePicker dpViewDate;
    private TextField txtBreakfastStats, txtCleaningStats;
    private ToggleButton btnHistoryView;
    private Label lblViewMode;
// Left panel controls: room maintenance and accommodation information
    private ChoiceBox<CleaningStatus> choiceCleaningStatus;
    private TextField txtInfoType, txtInfoNumber, txtInfoAccommodates, txtInfoPrice;
// Right panel controls: guest check-in form
   private TextField txtFName, txtLName, txtPhone, txtGuests, txtNights, txtNewRoomNumber;
private DatePicker dpCheckInDate;
private CheckBox chkBreakfast;
private Button btnCheckIn, btnUndoCheckIn, btnCheckOut, btnCancelBooking, btnResetSystem, btnChangeAccommodation;
// Bottom status label used to show success and error messages
    private Label lblStatusMessage;
  
   private HBox statsBox;
    
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cedar Woods Accommodation System");
        // Create a label to display the live clock on the top-right of the window
        Label lblClock = new Label();
        // Style the clock label so the time and date are clearly visible
        lblClock.setAlignment(Pos.CENTER_RIGHT);
lblClock.getStyleClass().add("header-label");
lblClock.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        // Start the live clock so the label updates every second
        startClock(lblClock);
       
        /* HBox topBox = new HBox(15);
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
        
        btnHistoryView = new ToggleButton("History View: OFF");
        btnHistoryView.setPrefWidth(140);

        lblViewMode = new Label("Operational View");
        lblViewMode.getStyleClass().add("header-label");

Label lblAreaCard = new Label("AREA");
lblAreaCard.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 10px; -fx-font-weight: bold;");

comboArea.setPrefWidth(150);

VBox areaCard = new VBox(4);
areaCard.setAlignment(Pos.CENTER_LEFT);
areaCard.setPadding(new Insets(8, 10, 8, 10));
areaCard.setStyle(
        "-fx-background-color: rgba(255,255,255,0.10);" +
        "-fx-background-radius: 14;"
);
areaCard.getChildren().addAll(lblAreaCard, comboArea);

Label lblDateCard = new Label("VIEW DATE");
lblDateCard.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 10px; -fx-font-weight: bold;");

dpViewDate.setPrefWidth(145);

VBox dateCard = new VBox(4);
dateCard.setAlignment(Pos.CENTER_LEFT);
dateCard.setPadding(new Insets(8, 10, 8, 10));
dateCard.setStyle(
        "-fx-background-color: rgba(255,255,255,0.10);" +
        "-fx-background-radius: 14;"
);
dateCard.getChildren().addAll(lblDateCard, dpViewDate);

Label lblViewCard = new Label("VIEW MODE");
lblViewCard.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 10px; -fx-font-weight: bold;");

btnHistoryView.setPrefWidth(165);

VBox viewCard = new VBox(4);
viewCard.setAlignment(Pos.CENTER_LEFT);
viewCard.setPadding(new Insets(8, 10, 8, 10));
viewCard.setStyle(
        "-fx-background-color: rgba(255,255,255,0.10);" +
        "-fx-background-radius: 14;"
);
viewCard.getChildren().addAll(lblViewCard, btnHistoryView);

HBox controlStrip = new HBox(14);
controlStrip.setAlignment(Pos.CENTER);
controlStrip.getChildren().addAll(areaCard, dateCard, viewCard);        

Label lblAppTitle = new Label("Cedar Woods Front Desk");
lblAppTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

Label lblAppSubtitle = new Label("Operations Dashboard");
lblAppSubtitle.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 11px;");

VBox titleBox = new VBox(2);
titleBox.setAlignment(Pos.CENTER_LEFT);
titleBox.getChildren().addAll(lblAppTitle, lblAppSubtitle);


btnResetSystem = new Button("Reset System");
btnResetSystem.getStyleClass().add("reset-button");
addButtonHoverAnimation(btnResetSystem);
addButtonClickAnimation(btnResetSystem);
        // Right-side container for the clock display
        
        
       HBox rightBox = new HBox(10);
rightBox.setAlignment(Pos.CENTER_RIGHT);
rightBox.setPadding(new Insets(8, 14, 8, 14));
rightBox.setStyle(
        "-fx-background-color: rgba(255,255,255,0.10);" +
        "-fx-background-radius: 14;"
);
rightBox.getChildren().addAll(lblClock);

BorderPane topBox = new BorderPane();
topBox.setPadding(new Insets(10));
topBox.setLeft(titleBox);
topBox.setCenter(controlStrip);
topBox.setRight(rightBox);
topBox.getStyleClass().add("header-box");

BorderPane.setMargin(controlStrip, new Insets(0, 18, 0, 18));
// Statistics section showing breakfasts for the selected date and current dirty rooms
        statsBox = new HBox(16);
statsBox.setAlignment(Pos.CENTER_LEFT);
statsBox.setPadding(new Insets(8, 10, 14, 10));

Label lblBreakfastCard = new Label("Breakfast Forecast");
lblBreakfastCard.getStyleClass().add("section-title");

txtBreakfastStats = new TextField("0");
txtBreakfastStats.setEditable(false);
txtBreakfastStats.setFocusTraversable(false);
txtBreakfastStats.setMouseTransparent(true);
txtBreakfastStats.setPrefWidth(100);
txtBreakfastStats.setAlignment(Pos.CENTER);
txtBreakfastStats.setStyle(
    "-fx-font-size: 20px; " +
    "-fx-font-weight: bold; " +
    "-fx-background-radius: 10; " +
    "-fx-border-radius: 10; " +
    "-fx-background-color: white; " +
    "-fx-border-color: #d6dde5;"
);

VBox breakfastCard = new VBox(8);
breakfastCard.setAlignment(Pos.CENTER_LEFT);
breakfastCard.getStyleClass().add("panel-card");
breakfastCard.setPadding(new Insets(12));
breakfastCard.setPrefWidth(230);
breakfastCard.getChildren().addAll(lblBreakfastCard, txtBreakfastStats);

Label lblCleaningCard = new Label("Rooms Requiring Cleaning");
lblCleaningCard.getStyleClass().add("section-title");

txtCleaningStats = new TextField("0");
txtCleaningStats.setEditable(false);
txtCleaningStats.setFocusTraversable(false);
txtCleaningStats.setMouseTransparent(true);
txtCleaningStats.setPrefWidth(100);
txtCleaningStats.setAlignment(Pos.CENTER);
txtCleaningStats.setStyle(
    "-fx-font-size: 20px; " +
    "-fx-font-weight: bold; " +
    "-fx-background-radius: 10; " +
    "-fx-border-radius: 10; " +
    "-fx-background-color: white; " +
    "-fx-border-color: #d6dde5;"
);

VBox cleaningCard = new VBox(8);
cleaningCard.setAlignment(Pos.CENTER_LEFT);
cleaningCard.getStyleClass().add("panel-card");
cleaningCard.setPadding(new Insets(12));
cleaningCard.setPrefWidth(280);
cleaningCard.getChildren().addAll(lblCleaningCard, txtCleaningStats);

Label lblOpsTitle = new Label("Live Operations");
lblOpsTitle.getStyleClass().add("section-title");

Label lblOpsMode = new Label("Mode: " + lblViewMode.getText());
lblOpsMode.setStyle(
        "-fx-text-fill: #334155; " +
        "-fx-font-size: 12px; " +
        "-fx-font-weight: bold; " +
        "-fx-background-color: #eef3f8; " +
        "-fx-background-radius: 12; " +
        "-fx-padding: 6 12 6 12;"
);

Label lblOpsNote = new Label("Use the top controls to switch area and date.");
lblOpsNote.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");

VBox summaryCard = new VBox(8);
summaryCard.setAlignment(Pos.CENTER_LEFT);
summaryCard.getStyleClass().add("panel-card");
summaryCard.setPadding(new Insets(12));
summaryCard.setMaxWidth(Double.MAX_VALUE);
HBox.setHgrow(summaryCard, Priority.ALWAYS);
summaryCard.getChildren().addAll(lblOpsTitle, lblOpsMode, lblOpsNote);

statsBox.getChildren().addAll(breakfastCard, cleaningCard, summaryCard);

VBox headerArea = new VBox(8, topBox, statsBox);

        setupTable();

       SplitPane dashboardBody = new SplitPane();
dashboardBody.setPadding(new Insets(15, 10, 10, 10));
dashboardBody.setDividerPositions(0.20, 0.76);
// Left panel contains maintenance controls and selected room details
        VBox leftPanel = new VBox(10);
        leftPanel.setMaxWidth(Double.MAX_VALUE);
HBox.setHgrow(leftPanel, Priority.ALWAYS);
        leftPanel.setPrefWidth(260);
        leftPanel.getStyleClass().add("panel-card");
        addCardHoverAnimation(leftPanel);

        choiceCleaningStatus = new ChoiceBox<>();
choiceCleaningStatus.getItems().addAll(CleaningStatus.values());
choiceCleaningStatus.setDisable(true);

        GridPane infoGrid = new GridPane();
        infoGrid.setVgap(12);
        infoGrid.setHgap(12);

        ColumnConstraints infoCol1 = new ColumnConstraints();
        infoCol1.setPercentWidth(50);
        infoCol1.setHgrow(Priority.ALWAYS);
        infoCol1.setFillWidth(true);

        ColumnConstraints infoCol2 = new ColumnConstraints();
        infoCol2.setPercentWidth(50);
        infoCol2.setHgrow(Priority.ALWAYS);
        infoCol2.setFillWidth(true);

infoGrid.getColumnConstraints().addAll(infoCol1, infoCol2);

        txtInfoType = new TextField();
txtInfoType.setEditable(false);
txtInfoType.setFocusTraversable(false);
txtInfoType.setMouseTransparent(true);
txtInfoType.setAlignment(Pos.CENTER_LEFT);
txtInfoType.setMaxWidth(Double.MAX_VALUE);
txtInfoType.setStyle(
        "-fx-background-color: white;" +
        "-fx-border-color: #d6dde5;" +
        "-fx-background-radius: 12;" +
        "-fx-border-radius: 12;" +
        "-fx-font-size: 13px;" +
        "-fx-font-weight: bold;"
);

txtInfoNumber = new TextField();
txtInfoNumber.setEditable(false);
txtInfoNumber.setFocusTraversable(false);
txtInfoNumber.setMouseTransparent(true);
txtInfoNumber.setAlignment(Pos.CENTER_LEFT);
txtInfoNumber.setMaxWidth(Double.MAX_VALUE);
txtInfoNumber.setStyle(
        "-fx-background-color: white;" +
        "-fx-border-color: #d6dde5;" +
        "-fx-background-radius: 12;" +
        "-fx-border-radius: 12;" +
        "-fx-font-size: 13px;" +
        "-fx-font-weight: bold;"
);

txtInfoAccommodates = new TextField();
txtInfoAccommodates.setEditable(false);
txtInfoAccommodates.setFocusTraversable(false);
txtInfoAccommodates.setMouseTransparent(true);
txtInfoAccommodates.setAlignment(Pos.CENTER_LEFT);
txtInfoAccommodates.setMaxWidth(Double.MAX_VALUE);
txtInfoAccommodates.setStyle(
        "-fx-background-color: white;" +
        "-fx-border-color: #d6dde5;" +
        "-fx-background-radius: 12;" +
        "-fx-border-radius: 12;" +
        "-fx-font-size: 13px;" +
        "-fx-font-weight: bold;"
);

txtInfoPrice = new TextField();
txtInfoPrice.setEditable(false);
txtInfoPrice.setFocusTraversable(false);
txtInfoPrice.setMouseTransparent(true);
txtInfoPrice.setAlignment(Pos.CENTER_LEFT);
txtInfoPrice.setMaxWidth(Double.MAX_VALUE);
txtInfoPrice.setStyle(
        "-fx-background-color: white;" +
        "-fx-border-color: #d6dde5;" +
        "-fx-background-radius: 12;" +
        "-fx-border-radius: 12;" +
        "-fx-font-size: 13px;" +
        "-fx-font-weight: bold;"
);

       VBox typeTile = createSnapshotTile("Type", txtInfoType);
VBox numberTile = createSnapshotTile("Room No.", txtInfoNumber);
VBox capacityTile = createSnapshotTile("Capacity", txtInfoAccommodates);
VBox priceTile = createSnapshotTile("Price / Night (£)", txtInfoPrice);

infoGrid.add(typeTile, 0, 0);
infoGrid.add(numberTile, 1, 0);
infoGrid.add(capacityTile, 0, 1);
infoGrid.add(priceTile, 1, 1);

       Label lblRoomMaintenance = new Label("Housekeeping Control");
Label lblAccommodationInfo = new Label("Selected Room Snapshot");
lblRoomMaintenance.getStyleClass().add("section-title");
lblAccommodationInfo.getStyleClass().add("section-title");

Label lblCleaningStatus = new Label("Cleaning Status");
lblCleaningStatus.getStyleClass().add("section-title");

VBox housekeepingCard = new VBox(8);
housekeepingCard.setPadding(new Insets(8, 0, 8, 0));
housekeepingCard.getChildren().addAll(lblCleaningStatus, choiceCleaningStatus);

VBox roomSnapshotCard = new VBox(10);
roomSnapshotCard.setPadding(new Insets(8, 0, 0, 0));
roomSnapshotCard.getChildren().addAll(lblAccommodationInfo, infoGrid);

VBox.setVgrow(roomSnapshotCard, Priority.ALWAYS);

leftPanel.getChildren().addAll(
    lblRoomMaintenance,
    housekeepingCard,
    new Separator(),
    roomSnapshotCard
); 
// Right panel contains the reception form for guest check-in and check-out


        VBox rightPanel = new VBox(10);
        rightPanel.setMaxWidth(Double.MAX_VALUE);
HBox.setHgrow(rightPanel, Priority.ALWAYS);
       rightPanel.setPrefWidth(340);
        rightPanel.getStyleClass().add("panel-card");
        addCardHoverAnimation(rightPanel);

        GridPane recGrid = new GridPane();
       recGrid.setVgap(12);
        recGrid.setHgap(12);
        ColumnConstraints recCol1 = new ColumnConstraints();
recCol1.setMinWidth(110);

ColumnConstraints recCol2 = new ColumnConstraints();
recCol2.setHgrow(Priority.ALWAYS);
recCol2.setFillWidth(true);

recGrid.getColumnConstraints().addAll(recCol1, recCol2);
        txtFName = new TextField();
        txtLName = new TextField();
        txtPhone = new TextField();
        txtGuests = new TextField();
        txtNights = new TextField();

        dpCheckInDate = new DatePicker(LocalDate.now());
txtFName.setMaxWidth(Double.MAX_VALUE);
txtLName.setMaxWidth(Double.MAX_VALUE);
txtPhone.setMaxWidth(Double.MAX_VALUE);
txtGuests.setMaxWidth(Double.MAX_VALUE);
txtNights.setMaxWidth(Double.MAX_VALUE);
dpCheckInDate.setMaxWidth(Double.MAX_VALUE);
dpCheckInDate.setEditable(false);
dpCheckInDate.setOnAction(e -> {
    updateCheckInButtonText();
    updateActionButtonsState();
});


        chkBreakfast = new CheckBox("Breakfast Required");
        
       txtNewRoomNumber = new TextField();
txtNewRoomNumber.setMaxWidth(Double.MAX_VALUE);

btnChangeAccommodation = new Button("Change Accommodation");
btnChangeAccommodation.setPrefWidth(180);
btnChangeAccommodation.getStyleClass().add("check-out-button");
addButtonHoverAnimation(btnChangeAccommodation);
addButtonClickAnimation(btnChangeAccommodation);
btnChangeAccommodation.setDisable(true); 

        VBox firstNameTile = createFormTile("First Name", txtFName);
        VBox lastNameTile = createFormTile("Last Name", txtLName);
        VBox phoneTile = createFormTile("Telephone No.", txtPhone);
        VBox guestsTile = createFormTile("Number Guests", txtGuests);
        VBox checkInTile = createFormTile("Check In Date", dpCheckInDate);
        VBox nightsTile = createFormTile("Number Nights", txtNights);
        VBox breakfastTile = createFormTile("Breakfast", chkBreakfast);
        VBox newRoomTile = createFormTile("New Room No.", txtNewRoomNumber);

        recGrid.add(firstNameTile, 0, 0);
        recGrid.add(lastNameTile, 1, 0);

        recGrid.add(phoneTile, 0, 1);
        recGrid.add(guestsTile, 1, 1);

        recGrid.add(checkInTile, 0, 2);
        recGrid.add(nightsTile, 1, 2);

        recGrid.add(breakfastTile, 0, 3);
        recGrid.add(newRoomTile, 1, 3);
        
        // Action buttons for guest check-in and check-out
         btnCheckIn = new Button("Check In");
         btnUndoCheckIn = new Button("Undo");
         btnCheckOut = new Button("Check Out");
         
         btnCancelBooking = new Button("Cancel Booking");
         
        btnCheckIn.setPrefWidth(120);
btnCheckOut.setPrefWidth(120);
btnUndoCheckIn.setPrefWidth(120);
btnCancelBooking.setPrefWidth(150);
btnChangeAccommodation.setPrefWidth(180);

btnCancelBooking.setWrapText(true);
btnUndoCheckIn.setWrapText(true);
btnChangeAccommodation.setWrapText(true);
        btnUndoCheckIn.getStyleClass().add("check-out-button");
addButtonHoverAnimation(btnUndoCheckIn);
addButtonClickAnimation(btnUndoCheckIn);

btnCancelBooking.getStyleClass().add("check-out-button");
addButtonHoverAnimation(btnCancelBooking);
addButtonClickAnimation(btnCancelBooking);
        btnCheckIn.getStyleClass().add("check-in-button");
btnCheckOut.getStyleClass().add("check-out-button");
updateCheckInButtonText();


addButtonHoverAnimation(btnCheckIn);
addButtonHoverAnimation(btnCheckOut);

addButtonClickAnimation(btnCheckIn);
addButtonClickAnimation(btnCheckOut);

       Label lblQuickActions = new Label("Quick Actions");
lblQuickActions.getStyleClass().add("section-title");

HBox quickActionsRow = new HBox(10);
quickActionsRow.setAlignment(Pos.CENTER_LEFT);
quickActionsRow.getChildren().addAll(btnCheckIn, btnUndoCheckIn, btnCheckOut);

Label lblBookingActions = new Label("Booking Actions");
lblBookingActions.getStyleClass().add("section-title");

HBox bookingActionsRow = new HBox(10);
bookingActionsRow.setAlignment(Pos.CENTER_LEFT);
bookingActionsRow.getChildren().addAll(btnCancelBooking, btnChangeAccommodation);

VBox btnBox = new VBox(10);
btnBox.setAlignment(Pos.CENTER_LEFT);
btnBox.getChildren().addAll(
        lblQuickActions,
        quickActionsRow,
        lblBookingActions,
        bookingActionsRow
);
btnCheckOut.setDisable(true);
btnUndoCheckIn.setDisable(true);
btnCancelBooking.setDisable(true);
btnCheckIn.setDisable(true);
        Label lblReception = new Label("Accommodation Reception");
lblReception.getStyleClass().add("section-title");

rightPanel.getChildren().addAll(lblReception, recGrid, btnBox);
Label lblRoomDashboard = new Label("Room Dashboard");
lblRoomDashboard.getStyleClass().add("section-title");

Label lblDashboardSubtitle = new Label("Live room overview");
lblDashboardSubtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");

VBox tableCard = new VBox(12);
tableCard.setPrefWidth(650);
tableCard.setMaxWidth(Double.MAX_VALUE);
HBox.setHgrow(tableCard, Priority.ALWAYS);
tableCard.getStyleClass().add("panel-card");
tableCard.setPadding(new Insets(12));
addCardHoverAnimation(tableCard);

VBox.setVgrow(table, Priority.ALWAYS);

tableCard.getChildren().addAll(
        lblRoomDashboard,
        lblDashboardSubtitle,
        table
);

dashboardBody.getItems().addAll(leftPanel, tableCard, rightPanel);
leftPanel.setMinWidth(240);
tableCard.setMinWidth(520);
rightPanel.setMinWidth(320);
        

        lblStatusMessage = new Label();
        lblStatusMessage.setPadding(new Insets(5, 10, 5, 10));
        lblStatusMessage.getStyleClass().add("status-label");
        Region statusSpacer = new Region();
HBox.setHgrow(statusSpacer, Priority.ALWAYS);

HBox statusBar = new HBox(10, lblStatusMessage, statusSpacer, btnResetSystem);
statusBar.setAlignment(Pos.CENTER_LEFT);
statusBar.setPadding(new Insets(5, 10, 5, 10));
        // Main application layout: header at top, table in center, controls and messages at bottom
        BorderPane mainPane = new BorderPane();
        mainPane.getStyleClass().add("main-pane");
        mainPane.setTop(headerArea);
mainPane.setCenter(dashboardBody);
mainPane.setBottom(statusBar);
BorderPane.setMargin(dashboardBody, new Insets(0, 10, 0, 10));

       comboArea.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        table.getSelectionModel().clearSelection();
        updateTableAndStats();
        refreshSelectedRoomPanel();
        updateActionButtonsState();
        showMessage("Now displaying rooms for area: " + comboArea.getValue(), "blue");
    }
});
       

      dpViewDate.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        updateTableAndStats();
        refreshSelectedRoomPanel();
        updateActionButtonsState();

        if (btnHistoryView != null && btnHistoryView.isSelected()
                && dpViewDate.getValue() != null
                && dpViewDate.getValue().isAfter(LocalDate.now())) {
            showMessage("History view is only meaningful for past/current dates.", "orange");
        } else {
            showMessage("System displaying room availability and stats for: " + dpViewDate.getValue(), "blue");
        }
    }
});
table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Accommodation>() {
    @Override
    public void changed(ObservableValue<? extends Accommodation> obs, Accommodation oldSel, Accommodation newSel) {
        refreshSelectedRoomPanel();
        updateActionButtonsState();
    }
});

       choiceCleaningStatus.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        Accommodation selected = table.getSelectionModel().getSelectedItem();
        CleaningStatus newStatus = choiceCleaningStatus.getValue();

        if (selected == null || newStatus == null) {
            return;
        }

        CleaningStatus currentDisplayedStatus = cedarSystem.getCleaningStatusForDate(
                selected.getAccommodationNumber(),
                dpViewDate.getValue()
        );

        if (currentDisplayedStatus != newStatus) {
            String result = cedarSystem.updateCleaningStatus(
                    selected.getAccommodationNumber(),
                    newStatus,
                    dpViewDate.getValue()
            );

            if (result.startsWith("SUCCESS")) {
                showMessage(result, "green");
            } else {
                showMessage(result, "red");
            }

            updateTableAndStats();
            refreshSelectedRoomPanel();
            updateActionButtonsState();
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
 btnCancelBooking.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        handleCancelBooking();
    }
        
    
});
        btnResetSystem.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        handleResetSystem();
    }
});
        btnUndoCheckIn.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        handleUndoCheckIn();
    }
});

btnChangeAccommodation.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent event) {
        handleChangeAccommodation();
    }
});

btnHistoryView.setOnAction(e -> {
    boolean historyMode = btnHistoryView.isSelected();

    if (historyMode) {
        btnHistoryView.setText("History View: ON");
        lblViewMode.setText("History View (Read Only)");
        lblOpsMode.setText("Mode: History View");
        updateTableAndStats();
        refreshSelectedRoomPanel();
        updateActionButtonsState();
        showHistoryModeHintIfNeeded();

        if (dpViewDate.getValue() == null || !dpViewDate.getValue().isAfter(LocalDate.now())) {
            showMessage("History view enabled for the selected date.", "blue");
        }
    } else {
        btnHistoryView.setText("History View: OFF");
        lblViewMode.setText("Operational View");
        lblOpsMode.setText("Mode: Operational View");
        updateTableAndStats();
        refreshSelectedRoomPanel();
        updateActionButtonsState();
        showMessage("Operational view enabled.", "blue");
    }
});
      updateTableAndStats();
        updateCheckInButtonText();
        updateActionButtonsState();
        

mainPane.setOpacity(0);
      Scene scene = new Scene(mainPane, 1280, 820);
primaryStage.setMinWidth(1180);
primaryStage.setMinHeight(760);

var cssUrl = HelloApplication.class.getResource("style.css");
System.out.println("CSS URL = " + cssUrl);

if (cssUrl != null) {
    scene.getStylesheets().add(cssUrl.toExternalForm());
}

leftPanel.setTranslateX(-120);

TranslateTransition leftSlide = new TranslateTransition(Duration.seconds(2.0), leftPanel);
leftSlide.setFromX(-120);
leftSlide.setToX(0);

rightPanel.setOpacity(0);

FadeTransition rightFade = new FadeTransition(Duration.seconds(2.2), rightPanel);
rightFade.setFromValue(0);
rightFade.setToValue(1);

primaryStage.setScene(scene);
primaryStage.show();
FadeTransition windowFade = new FadeTransition(Duration.seconds(2.5), mainPane);
windowFade.setFromValue(0);
windowFade.setToValue(1);
windowFade.play();

leftSlide.play();
rightFade.play();
playStartupSound();
    }

    private void setupTable() {
        // Create the main table used to display accommodation details
        table = new TableView<>();
        table.setPlaceholder(new Label("No rooms available for the selected area/date."));
        
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
                String status;
if (btnHistoryView != null && btnHistoryView.isSelected()) {
    status = cedarSystem.getHistoricalOccupancyStringForDate(
            cell.getValue().getAccommodationNumber(), targetDate
    );
} else {
    status = cedarSystem.getOccupancyStringForDate(
            cell.getValue().getAccommodationNumber(), targetDate
    );
}
                return new SimpleStringProperty(status);
            }
        });
        
        colOccupancy.setCellFactory(column -> new TableCell<Accommodation, String>() {
    @Override
    protected void updateItem(String occupancy, boolean empty) {
        super.updateItem(occupancy, empty);

        if (empty || occupancy == null) {
            setText(null);
            setStyle("");
            return;
        }

        setText(occupancy);
        setAlignment(Pos.CENTER);

        switch (occupancy.toLowerCase()) {
            case "occupied":
                setStyle("-fx-background-color: #dbeafe; -fx-text-fill: #1d4ed8; -fx-font-weight: bold;");
                break;
            case "unoccupied":
                setStyle("-fx-background-color: #f8fafc; -fx-text-fill: #475569; -fx-font-weight: bold;");
                break;
            default:
                setStyle("");
        }
    }
});
      // Column: room availability for the selected date
        TableColumn<Accommodation, String> colAvailability = new TableColumn<>("Availability");
        colAvailability.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> cell) {
                LocalDate targetDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
               String status;
                if (btnHistoryView != null && btnHistoryView.isSelected()) {
                    status = "N/A";
                } else {
                    status = cedarSystem.getAvailabilityStringForDate(
                            cell.getValue().getAccommodationNumber(), targetDate
                    );
                }
                return new SimpleStringProperty(status);
            }
        });
        
        colAvailability.setCellFactory(column -> new TableCell<Accommodation, String>() {
    @Override
    protected void updateItem(String availability, boolean empty) {
        super.updateItem(availability, empty);

        if (empty || availability == null) {
            setText(null);
            setStyle("");
            return;
        }

        setText(availability);
        setAlignment(Pos.CENTER);

        switch (availability.toLowerCase()) {
            case "available":
                setStyle("-fx-background-color: #ecfdf5; -fx-text-fill: #166534; -fx-font-weight: bold;");
                break;
            case "unavailable":
                setStyle("-fx-background-color: #fef2f2; -fx-text-fill: #991b1b; -fx-font-weight: bold;");
                break;
            case "n/a":
                setStyle("-fx-background-color: #f1f5f9; -fx-text-fill: #475569; -fx-font-weight: bold;");
                break;
            default:
                setStyle("");
        }
    }
});
        
// Column: cleaning/maintenance status
        TableColumn<Accommodation, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> cell) {
                LocalDate targetDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
                String status;
if (btnHistoryView != null && btnHistoryView.isSelected()) {
    status = cedarSystem.getHistoricalCleaningStatusStringForDate(
            cell.getValue().getAccommodationNumber(), targetDate
    );
} else {
    status = cedarSystem.getCleaningStatusStringForDate(
            cell.getValue().getAccommodationNumber(), targetDate
    );
}
                return new SimpleStringProperty(status);
            }
        });
        
       colStatus.setCellFactory(column -> new TableCell<Accommodation, String>() {
    @Override
    protected void updateItem(String status, boolean empty) {
        super.updateItem(status, empty);

        if (empty || status == null) {
            setText(null);
            setStyle("");
            return;
        }

        setText(status);
        setAlignment(Pos.CENTER);

        switch (status.toLowerCase()) {
            case "clean":
                setStyle("-fx-background-color: #dcfce7; -fx-text-fill: #166534; -fx-font-weight: bold;");
                break;
            case "dirty":
                setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #991b1b; -fx-font-weight: bold;");
                break;
            case "maintenance":
                setStyle("-fx-background-color: #fef3c7; -fx-text-fill: #92400e; -fx-font-weight: bold;");
                break;
            default:
                setStyle("");
        }
    }
}); 
        
// Column: number of guests for the selected date
        TableColumn<Accommodation, String> colGuests = new TableColumn<>("Guests");
        colGuests.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> cell) {
                LocalDate targetDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
               String guests;
                if (btnHistoryView != null && btnHistoryView.isSelected()) {
                    guests = cedarSystem.getHistoricalGuestCountStringForDate(
                            cell.getValue().getAccommodationNumber(), targetDate
                    );
                } else {
                    guests = cedarSystem.getGuestCountStringForDate(
                            cell.getValue().getAccommodationNumber(), targetDate
                    );
                }
                return new SimpleStringProperty(guests);
            }
        });
// Column: breakfast requirement for the selected date
        TableColumn<Accommodation, String> colBreakfast = new TableColumn<>("Breakfast");
        colBreakfast.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> cell) {
                LocalDate targetDate = dpViewDate.getValue() != null ? dpViewDate.getValue() : LocalDate.now();
                String breakfast;
if (btnHistoryView != null && btnHistoryView.isSelected()) {
    breakfast = cedarSystem.getHistoricalBreakfastStringForDate(
            cell.getValue().getAccommodationNumber(), targetDate
    );
} else {
    breakfast = cedarSystem.getBreakfastStringForDate(
            cell.getValue().getAccommodationNumber(), targetDate
    );
}
                return new SimpleStringProperty(breakfast);
            }
        });
        
        colBreakfast.setCellFactory(column -> new TableCell<Accommodation, String>() {
    @Override
    protected void updateItem(String breakfast, boolean empty) {
        super.updateItem(breakfast, empty);

        if (empty || breakfast == null) {
            setText(null);
            setStyle("");
            return;
        }

        setText(breakfast);
        setAlignment(Pos.CENTER);

        switch (breakfast.toLowerCase()) {
            case "yes":
                setStyle("-fx-background-color: #fef3c7; -fx-text-fill: #92400e; -fx-font-weight: bold;");
                break;
            case "no":
                setStyle("-fx-background-color: #f8fafc; -fx-text-fill: #475569; -fx-font-weight: bold;");
                break;
            default:
                setStyle("");
        }
    }
});
        
        
    // Add all columns to the table
        table.getColumns().addAll(colNo, colType, colOccupancy, colAvailability, colStatus, colGuests, colBreakfast);
          // Set table size and resize behavior
        table.setMinHeight(420);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        table.setRowFactory(tv -> {
    TableRow<Accommodation> row = new TableRow<>();

    row.hoverProperty().addListener((obs, wasHover, isNowHover) -> {
        if (row.isEmpty()) {
            row.setStyle("");
        } else if (row.isSelected()) {
            row.setStyle("-fx-background-color: #dbeafe; -fx-border-color: #3b82f6; -fx-border-width: 0 0 2 0;");
        } else if (isNowHover) {
            row.setStyle("-fx-background-color: #f8fafc;");
        } else {
            row.setStyle("");
        }
    });

    row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
        if (row.isEmpty()) {
            row.setStyle("");
        } else if (isNowSelected) {
            row.setStyle("-fx-background-color: #dbeafe; -fx-border-color: #3b82f6; -fx-border-width: 0 0 2 0;");
        } else if (row.isHover()) {
            row.setStyle("-fx-background-color: #f8fafc;");
        } else {
            row.setStyle("");
        }
    });

    return row;
});
    }
// Refreshes the accommodation table and statistics based on the selected area and date
    private void updateTableAndStats() {
        AreaType area = comboArea.getValue();
        if (area == null) return;

        LocalDate statsDate = dpViewDate.getValue();
        if (statsDate == null) statsDate = LocalDate.now();

        table.setItems(FXCollections.observableArrayList(cedarSystem.getAccommodationsByArea(area)));
        table.refresh();

        if (btnHistoryView != null && btnHistoryView.isSelected()) {
    txtCleaningStats.setText(String.valueOf(
            cedarSystem.getHistoricalCleaningCount(area, statsDate)
    ));
} else {
    txtCleaningStats.setText(String.valueOf(
            cedarSystem.getCleaningCount(area, statsDate)
    ));
}

if (btnHistoryView != null && btnHistoryView.isSelected()) {
    txtBreakfastStats.setText(String.valueOf(
            cedarSystem.getHistoricalBreakfastCount(area, statsDate)
    ));
} else {
    txtBreakfastStats.setText(String.valueOf(
            cedarSystem.getBreakfastCount(area, statsDate)
    ));
}
          animateTableRefresh();
          animateStatsBox();
    }
// Handles guest check-in using the selected accommodation and form input values
   private void handleCheckIn() {
    Accommodation selected = table.getSelectionModel().getSelectedItem();
    if (selected == null) {
        showMessage("Please select an accommodation first.", "red");
        return;
    }

    LocalDate selectedDate = dpCheckInDate.getValue();
    if (selectedDate == null) {
        showMessage("Please select a valid check-in date.", "red");
        return;
    }

    String result;

    if (selectedDate.equals(LocalDate.now())) {
        Booking todayBooking = cedarSystem.getBookingForDate(
                selected.getAccommodationNumber(),
                selectedDate
        );

        if (todayBooking != null) {
            result = cedarSystem.checkInGuest(
                    selected.getAccommodationNumber(),
                    selectedDate
            );
        } else {
            String bookingResult = cedarSystem.createBooking(
                    selected.getAccommodationNumber(),
                    txtFName.getText(),
                    txtLName.getText(),
                    txtPhone.getText(),
                    txtGuests.getText(),
                    txtNights.getText(),
                    chkBreakfast.isSelected(),
                    selectedDate
            );

            if (!bookingResult.startsWith("SUCCESS")) {
                showMessage(bookingResult, "red");
                return;
            }

            result = cedarSystem.checkInGuest(
                    selected.getAccommodationNumber(),
                    selectedDate
            );
        }
    } else {
        result = cedarSystem.createBooking(
                selected.getAccommodationNumber(),
                txtFName.getText(),
                txtLName.getText(),
                txtPhone.getText(),
                txtGuests.getText(),
                txtNights.getText(),
                chkBreakfast.isSelected(),
                selectedDate
        );
    }

    if (!result.startsWith("SUCCESS")) {
        showMessage(result, "red");
        return;
    }

    updateTableAndStats();
    refreshSelectedRoomPanel();
    updateActionButtonsState();
    showMessage(result, "green");
   

   txtFName.clear();
txtLName.clear();
txtPhone.clear();
txtGuests.clear();
txtNights.clear();
txtNewRoomNumber.clear();
chkBreakfast.setSelected(false);
dpCheckInDate.setValue(LocalDate.now());
updateCheckInButtonText();
}
// Handles guest check-out for the selected accommodation using today's date
  private void handleCheckOut() {
    Accommodation selected = table.getSelectionModel().getSelectedItem();
    if (selected == null) {
        showMessage("Please select a room first.", "red");
        return;
    }

    LocalDate selectedDate = dpViewDate.getValue();
    if (selectedDate == null) {
        showMessage("Please select a valid date to view.", "red");
        return;
    }

    if (!selectedDate.equals(LocalDate.now())) {
        showMessage("Guests can only be checked out on today's date.", "red");
        return;
    }

    String result = cedarSystem.checkOutGuest(
            selected.getAccommodationNumber(),
            selectedDate
    );

    if (result.startsWith("SUCCESS")) {
        updateTableAndStats();
        refreshSelectedRoomPanel();
        updateActionButtonsState();
        showMessage(result, "green");
    } else {
        showMessage(result, "red");
    }
}
  private void handleUndoCheckIn() {
    Accommodation selected = table.getSelectionModel().getSelectedItem();
    if (selected == null) {
        showMessage("Please select a room first.", "red");
        return;
    }

    String result = cedarSystem.undoTodayCheckIn(selected.getAccommodationNumber());

    if (result.startsWith("SUCCESS")) {
        updateTableAndStats();
        refreshSelectedRoomPanel();
        updateActionButtonsState();
        showMessage(result, "green");

        txtFName.clear();
        txtLName.clear();
        txtPhone.clear();
        txtGuests.clear();
        txtNights.clear();
        txtNewRoomNumber.clear();
        chkBreakfast.setSelected(false);
        dpCheckInDate.setValue(LocalDate.now());
        updateCheckInButtonText();
        
    } else {
        showMessage(result, "red");
    }
}
  
  
 private void handleChangeAccommodation() {
    Accommodation selected = table.getSelectionModel().getSelectedItem();
    if (selected == null) {
        showMessage("Please select the current room first.", "red");
        return;
    }

    LocalDate selectedDate = dpViewDate.getValue();
    if (selectedDate == null) {
        showMessage("Please select a valid booking date.", "red");
        return;
    }

    String newRoomStr = txtNewRoomNumber.getText();
    if (newRoomStr == null || newRoomStr.trim().isEmpty()) {
        showMessage("Please enter the new room number.", "red");
        return;
    }

    int newRoomNumber;
    try {
        newRoomNumber = Integer.parseInt(newRoomStr.trim());
    } catch (NumberFormatException e) {
        showMessage("New room number must be a valid number.", "red");
        return;
    }

    String result = cedarSystem.changeAccommodation(
            selected.getAccommodationNumber(),
            newRoomNumber,
            selectedDate
    );

    if (result.startsWith("SUCCESS")) {
        updateTableAndStats();
        refreshSelectedRoomPanel();
        updateActionButtonsState();
        showMessage(result, "green");
        txtNewRoomNumber.clear();
    } else {
        showMessage(result, "red");
    }
} 
 private void refreshSelectedRoomPanel() {
    Accommodation selected = table.getSelectionModel().getSelectedItem();

    if (selected != null) {
        txtInfoType.setText(selected.getType().toString());
        txtInfoNumber.setText(String.valueOf(selected.getAccommodationNumber()));
        txtInfoAccommodates.setText(String.valueOf(selected.getMaxOccupancy()));
        txtInfoPrice.setText(String.valueOf(selected.getPricePerNight()));
        choiceCleaningStatus.setValue(
                cedarSystem.getCleaningStatusForDate(
                        selected.getAccommodationNumber(),
                        dpViewDate.getValue()
                )
        );
       if (btnHistoryView != null && btnHistoryView.isSelected()) {
    choiceCleaningStatus.setDisable(true);
} else {
    choiceCleaningStatus.setDisable(
            cedarSystem.isRoomOccupiedOnDate(
                    selected.getAccommodationNumber(),
                    dpViewDate.getValue()
            )
    );
};
    } else {
        txtInfoType.clear();
        txtInfoNumber.clear();
        txtInfoAccommodates.clear();
        txtInfoPrice.clear();
        choiceCleaningStatus.setValue(null);
        choiceCleaningStatus.setDisable(true);
    }
}
// Displays a coloured status message at the bottom of the interface
    private void showMessage(String msg, String color) {
        lblStatusMessage.setText(msg);
        lblStatusMessage.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 14px;");
         lblStatusMessage.setOpacity(0);

    FadeTransition fade = new FadeTransition(Duration.seconds(0.6), lblStatusMessage);
    fade.setFromValue(0);
    fade.setToValue(1);
    fade.play();
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
private void addButtonHoverAnimation(Button button) {
    ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2), button);
    scaleUp.setToX(1.05);
    scaleUp.setToY(1.05);

    ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), button);
    scaleDown.setToX(1.0);
    scaleDown.setToY(1.0);

    button.setOnMouseEntered(e -> scaleUp.playFromStart());
    button.setOnMouseExited(e -> scaleDown.playFromStart());
}
private void animateTableRefresh() {
    table.setOpacity(0);

    FadeTransition fade = new FadeTransition(Duration.seconds(0.5), table);
    fade.setFromValue(0);
    fade.setToValue(1);
    fade.play();
}
private void animateStatsBox() {
    statsBox.setOpacity(0);

    FadeTransition fade = new FadeTransition(Duration.seconds(0.5), statsBox);
    fade.setFromValue(0);
    fade.setToValue(1);
    fade.play();
}
private void addCardHoverAnimation(Region card) {
    ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2), card);
    scaleUp.setToX(1.02);
    scaleUp.setToY(1.02);

    ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), card);
    scaleDown.setToX(1.0);
    scaleDown.setToY(1.0);

    card.setOnMouseEntered(e -> scaleUp.playFromStart());
    card.setOnMouseExited(e -> scaleDown.playFromStart());
}

private VBox createSnapshotTile(String title, TextField field) {
    Label lblTitle = new Label(title);
    lblTitle.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px; -fx-font-weight: bold;");

    VBox tile = new VBox(6);
    tile.setAlignment(Pos.CENTER_LEFT);
    tile.getChildren().addAll(lblTitle, field);

    return tile;
}
private VBox createFormTile(String title, Control control) {
    Label lblTitle = new Label(title);
    lblTitle.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px; -fx-font-weight: bold;");

    if (control instanceof Region) {
        ((Region) control).setMaxWidth(Double.MAX_VALUE);
    }

    VBox tile = new VBox(6);
    tile.setAlignment(Pos.CENTER_LEFT);
    tile.getChildren().addAll(lblTitle, control);

    return tile;
}


private void addButtonClickAnimation(Button button) {
    button.setOnMousePressed(e -> {
        ScaleTransition press = new ScaleTransition(Duration.seconds(0.1), button);
        press.setToX(0.96);
        press.setToY(0.96);
        press.playFromStart();
    });

    button.setOnMouseReleased(e -> {
        ScaleTransition release = new ScaleTransition(Duration.seconds(0.1), button);
        release.setToX(1.05);
        release.setToY(1.05);
        release.playFromStart();
    });
}
private void playStartupSound() {
    var soundUrl = HelloApplication.class.getResource("startup.wav");
    System.out.println("Sound URL = " + soundUrl);

    if (soundUrl != null) {
        AudioClip clip = new AudioClip(soundUrl.toExternalForm());
        clip.setVolume(0.5);
        clip.play();
    } else {
        System.out.println("startup.wav not found");
    }
}
private void updateCheckInButtonText() {
    LocalDate selectedDate = dpCheckInDate.getValue();

    if (selectedDate != null && selectedDate.isAfter(LocalDate.now())) {
        btnCheckIn.setText("Create Booking");
    } else {
        btnCheckIn.setText("Check In");
    }
}

private void handleCancelBooking() {
    Accommodation selected = table.getSelectionModel().getSelectedItem();
    if (selected == null) {
        showMessage("Please select a room first.", "red");
        return;
    }

    LocalDate selectedDate = dpViewDate.getValue();
    if (selectedDate == null) {
        showMessage("Please select a valid date to cancel.", "red");
        return;
    }
    

    String result = cedarSystem.cancelFutureBooking(
            selected.getAccommodationNumber(),
            selectedDate
    );

    if (result.startsWith("SUCCESS")) {
        updateTableAndStats();
refreshSelectedRoomPanel();
updateActionButtonsState();

txtFName.clear();
txtLName.clear();
txtPhone.clear();
txtGuests.clear();
txtNights.clear();
txtNewRoomNumber.clear();
chkBreakfast.setSelected(false);
dpCheckInDate.setValue(LocalDate.now());
updateCheckInButtonText();

showMessage(result, "green");
    } else {
        showMessage(result, "red");
    }
}
private void handleResetSystem() {
    cedarSystem.resetSystem();

    btnHistoryView.setSelected(false);
    btnHistoryView.setText("History View: OFF");
    lblViewMode.setText("Operational View");

    table.getSelectionModel().clearSelection();

    txtInfoType.clear();
    txtInfoNumber.clear();
    txtInfoAccommodates.clear();
    txtInfoPrice.clear();

    txtFName.clear();
    txtLName.clear();
    txtPhone.clear();
    txtGuests.clear();
    txtNights.clear();
    txtNewRoomNumber.clear();
    chkBreakfast.setSelected(false);

    choiceCleaningStatus.setValue(null);
    choiceCleaningStatus.setDisable(true);

    dpViewDate.setValue(LocalDate.now());
    dpCheckInDate.setValue(LocalDate.now());
    
comboArea.setValue(AreaType.Hilltop);
    updateTableAndStats();
    updateCheckInButtonText();
    updateActionButtonsState();

    showMessage("System reset successfully.", "blue");
}



private void updateActionButtonsState() {
    Accommodation selected = table.getSelectionModel().getSelectedItem();
    LocalDate selectedViewDate = dpViewDate.getValue();
    
    if (btnHistoryView != null && btnHistoryView.isSelected()) {
    btnCheckIn.setDisable(true);
    btnCheckOut.setDisable(true);
    btnUndoCheckIn.setDisable(true);
    btnCancelBooking.setDisable(true);
    btnChangeAccommodation.setDisable(true);
    return;
}

    if (selected == null) {
        btnCheckIn.setDisable(true);
        btnCheckOut.setDisable(true);
        btnUndoCheckIn.setDisable(true);
        btnCancelBooking.setDisable(true);
        btnChangeAccommodation.setDisable(true);
        return;
    }

    int roomNumber = selected.getAccommodationNumber();

   LocalDate selectedCheckInDate = dpCheckInDate.getValue();
btnCheckIn.setDisable(
        !cedarSystem.canUseCheckInButton(roomNumber, selectedCheckInDate)
);
    btnCheckOut.setDisable(!cedarSystem.canCheckOutOnDate(roomNumber, selectedViewDate));
    btnUndoCheckIn.setDisable(!cedarSystem.canUndoTodayCheckIn(roomNumber));
    btnCancelBooking.setDisable(!cedarSystem.hasFutureBooking(roomNumber, selectedViewDate));
    btnChangeAccommodation.setDisable(
        !cedarSystem.canChangeAccommodation(roomNumber, selectedViewDate)
);
}

private void showHistoryModeHintIfNeeded() {
    if (btnHistoryView != null && btnHistoryView.isSelected()) {
        LocalDate selectedDate = dpViewDate.getValue();

        if (selectedDate != null && selectedDate.isAfter(LocalDate.now())) {
            showMessage("History view is only meaningful for past/current dates.", "orange");
        }
    }
}
// Launches the JavaFX application
    public static void main(String[] args) {
        launch();
    }
}