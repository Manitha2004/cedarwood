

//our code
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
 

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
   private HBox workspaceBanner;
    
    
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
        comboArea.getStyleClass().add("top-area-combo");
        

comboArea.setButtonCell(new ListCell<AreaType>() {
    @Override
    protected void updateItem(AreaType item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            setText(item.toString());
        }
    }
});

        dpViewDate = new DatePicker(LocalDate.now());
        dpViewDate.setEditable(false);
        dpViewDate.setPrefWidth(120);
        
        btnHistoryView = new ToggleButton("History View: OFF");
        btnHistoryView.setPrefWidth(140);

        lblViewMode = new Label("Operational View");
lblViewMode.getStyleClass().add("header-label");

// Top control cards
Label lblAreaCard = new Label("Selected Area");
lblAreaCard.getStyleClass().add("filter-card-label");
comboArea.setPrefWidth(170);

ImageView icoArea = createIconView("area.png", 16);
StackPane icoAreaWrap = new StackPane(icoArea);
icoAreaWrap.getStyleClass().addAll("top-card-icon-wrap", "icon-area-wrap");


ImageView icoDate = createIconView("calendar.png", 16);
StackPane icoDateWrap = new StackPane(icoDate);
icoDateWrap.getStyleClass().addAll("top-card-icon-wrap", "icon-date-wrap");

ImageView icoView = createIconView("view.png", 16);
StackPane icoViewWrap = new StackPane(icoView);
icoViewWrap.getStyleClass().addAll("top-card-icon-wrap", "icon-view-wrap");

HBox areaHeader = new HBox(8, icoAreaWrap, lblAreaCard);
areaHeader.setAlignment(Pos.CENTER_LEFT);

VBox areaCard = new VBox(8);
areaCard.setAlignment(Pos.CENTER_LEFT);
areaCard.getStyleClass().add("top-filter-card");
areaCard.getChildren().addAll(areaHeader, comboArea);

Label lblDateCard = new Label("View Date");
lblDateCard.getStyleClass().add("filter-card-label");
dpViewDate.setPrefWidth(165);

HBox dateHeader = new HBox(8, icoDateWrap, lblDateCard);
dateHeader.setAlignment(Pos.CENTER_LEFT);

VBox dateCard = new VBox(8);
dateCard.setAlignment(Pos.CENTER_LEFT);
dateCard.getStyleClass().add("top-filter-card");
dateCard.getChildren().addAll(dateHeader, dpViewDate);

Label lblViewCard = new Label("View Mode");
lblViewCard.getStyleClass().add("filter-card-label");
btnHistoryView.setPrefWidth(185);

HBox viewHeader = new HBox(8, icoViewWrap, lblViewCard);
viewHeader.setAlignment(Pos.CENTER_LEFT);

VBox viewCard = new VBox(8);
viewCard.setAlignment(Pos.CENTER_LEFT);
viewCard.getStyleClass().add("top-filter-card");
viewCard.getChildren().addAll(viewHeader, btnHistoryView);

HBox controlStrip = new HBox(18);
controlStrip.setAlignment(Pos.CENTER);
controlStrip.getChildren().addAll(areaCard, dateCard, viewCard);

// Left title
Label lblAppTitle = new Label("Cedar Woods Front Desk");
lblAppTitle.getStyleClass().add("app-title");

Label lblAppSubtitle = new Label("Operations Dashboard");
lblAppSubtitle.getStyleClass().add("app-subtitle");

VBox titleBox = new VBox(4);
titleBox.setAlignment(Pos.CENTER_LEFT);
titleBox.getChildren().addAll(lblAppTitle, lblAppSubtitle);

// Right clock card
btnResetSystem = new Button("Reset System");
btnResetSystem.getStyleClass().add("reset-button");
btnResetSystem.setPrefWidth(130);
btnResetSystem.setMinWidth(130);
btnResetSystem.setWrapText(false);
addButtonHoverAnimation(btnResetSystem);
addButtonClickAnimation(btnResetSystem);

HBox rightBox = new HBox();
rightBox.setAlignment(Pos.CENTER);
rightBox.getStyleClass().add("clock-card");
rightBox.getChildren().add(lblClock);

// Header wrapper
BorderPane topBox = new BorderPane();
topBox.setPadding(new Insets(18, 22, 18, 22));
topBox.setLeft(titleBox);
topBox.setCenter(controlStrip);
topBox.setRight(rightBox);
topBox.getStyleClass().add("header-box");

BorderPane.setMargin(controlStrip, new Insets(0, 20, 0, 20));

// Stats row
statsBox = new HBox(18);
statsBox.setAlignment(Pos.CENTER_LEFT);
statsBox.getStyleClass().add("stats-row");

// Breakfast card
Label lblBreakfastCard = new Label("Breakfast Forecast");
lblBreakfastCard.getStyleClass().add("metric-title");

ImageView icoBreakfast = createIconView("breakfast.png", 18);
StackPane icoBreakfastWrap = new StackPane(icoBreakfast);
icoBreakfastWrap.getStyleClass().addAll("metric-icon-wrap", "icon-breakfast-wrap");

HBox breakfastHeader = new HBox(10, icoBreakfastWrap, lblBreakfastCard);
breakfastHeader.setAlignment(Pos.CENTER_LEFT);

txtBreakfastStats = new TextField("0");
txtBreakfastStats.setEditable(false);
txtBreakfastStats.setFocusTraversable(false);
txtBreakfastStats.setMouseTransparent(true);
txtBreakfastStats.setAlignment(Pos.CENTER);
txtBreakfastStats.getStyleClass().add("metric-value");
txtBreakfastStats.setPrefWidth(120);

Label lblBreakfastSub = new Label("Guests requiring breakfast");
lblBreakfastSub.getStyleClass().add("metric-subtitle");

VBox breakfastCard = new VBox(10);
breakfastCard.setAlignment(Pos.CENTER_LEFT);
breakfastCard.getStyleClass().add("metric-card");
breakfastCard.setPrefWidth(240);
breakfastCard.getChildren().addAll(breakfastHeader, txtBreakfastStats, lblBreakfastSub);
// Cleaning card
Label lblCleaningCard = new Label("Rooms Requiring Cleaning");
lblCleaningCard.getStyleClass().add("metric-title");

ImageView icoCleaning = createIconView("cleaning.png", 18);
StackPane icoCleaningWrap = new StackPane(icoCleaning);
icoCleaningWrap.getStyleClass().addAll("metric-icon-wrap", "icon-cleaning-wrap");

HBox cleaningHeader = new HBox(10, icoCleaningWrap, lblCleaningCard);
cleaningHeader.setAlignment(Pos.CENTER_LEFT);

txtCleaningStats = new TextField("0");
txtCleaningStats.setEditable(false);
txtCleaningStats.setFocusTraversable(false);
txtCleaningStats.setMouseTransparent(true);
txtCleaningStats.setAlignment(Pos.CENTER);
txtCleaningStats.getStyleClass().add("metric-value");
txtCleaningStats.setPrefWidth(120);

Label lblCleaningSub = new Label("Daily housekeeping workload");
lblCleaningSub.getStyleClass().add("metric-subtitle");

VBox cleaningCard = new VBox(10);
cleaningCard.setAlignment(Pos.CENTER_LEFT);
cleaningCard.getStyleClass().add("metric-card");
cleaningCard.setPrefWidth(290);
cleaningCard.getChildren().addAll(cleaningHeader, txtCleaningStats, lblCleaningSub);

// Live operations summary card
Label lblOpsTitle = new Label("Live Operations");
lblOpsTitle.getStyleClass().add("section-title");

Label icoOps = new Label("⚙");
icoOps.getStyleClass().addAll("metric-icon", "icon-ops");

HBox opsHeader = new HBox(10, icoOps, lblOpsTitle);
opsHeader.setAlignment(Pos.CENTER_LEFT);

Label lblOpsMode = new Label("Mode: " + lblViewMode.getText());
lblOpsMode.getStyleClass().add("ops-chip");

Label lblOpsNote = new Label("Use the top controls to switch area and date.");
lblOpsNote.getStyleClass().add("ops-note");

VBox summaryCard = new VBox(10);
summaryCard.setAlignment(Pos.CENTER_LEFT);
summaryCard.getStyleClass().add("metric-card");
summaryCard.setMaxWidth(Double.MAX_VALUE);
HBox.setHgrow(summaryCard, Priority.ALWAYS);
summaryCard.getChildren().addAll(opsHeader, lblOpsMode, lblOpsNote);

statsBox.getChildren().addAll(breakfastCard, cleaningCard, summaryCard);

VBox headerArea = new VBox(14, topBox, statsBox);
headerArea.setPadding(new Insets(0, 10, 8, 10));

        setupTable();

       
// Left panel contains maintenance controls and selected room details
 SplitPane dashboardBody = new SplitPane();

dashboardBody.setDividerPositions(0.22, 0.75);
dashboardBody.getStyleClass().add("dashboard-body");
dashboardBody.setMinWidth(1700);
dashboardBody.setPrefWidth(1700);

// ===== LEFT PANEL =====
VBox leftPanel = new VBox(16);
leftPanel.setMaxWidth(Double.MAX_VALUE);
HBox.setHgrow(leftPanel, Priority.ALWAYS);
leftPanel.setPrefWidth(285);
leftPanel.getStyleClass().addAll("panel-card", "dashboard-shell", "dark-side-card");
addCardHoverAnimation(leftPanel);

choiceCleaningStatus = new ChoiceBox<>();
choiceCleaningStatus.getItems().addAll(CleaningStatus.values());
choiceCleaningStatus.setDisable(true);
choiceCleaningStatus.setMaxWidth(Double.MAX_VALUE);
choiceCleaningStatus.getStyleClass().add("maintenance-choice");

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

txtInfoNumber = new TextField();
txtInfoNumber.setEditable(false);
txtInfoNumber.setFocusTraversable(false);
txtInfoNumber.setMouseTransparent(true);
txtInfoNumber.setAlignment(Pos.CENTER_LEFT);
txtInfoNumber.setMaxWidth(Double.MAX_VALUE);

txtInfoAccommodates = new TextField();
txtInfoAccommodates.setEditable(false);
txtInfoAccommodates.setFocusTraversable(false);
txtInfoAccommodates.setMouseTransparent(true);
txtInfoAccommodates.setAlignment(Pos.CENTER_LEFT);
txtInfoAccommodates.setMaxWidth(Double.MAX_VALUE);

txtInfoPrice = new TextField();
txtInfoPrice.setEditable(false);
txtInfoPrice.setFocusTraversable(false);
txtInfoPrice.setMouseTransparent(true);
txtInfoPrice.setAlignment(Pos.CENTER_LEFT);
txtInfoPrice.setMaxWidth(Double.MAX_VALUE);

VBox typeTile = createSnapshotTile("Type", txtInfoType);
VBox numberTile = createSnapshotTile("Room No.", txtInfoNumber);
VBox capacityTile = createSnapshotTile("Capacity", txtInfoAccommodates);
VBox priceTile = createSnapshotTile("Price / Night (£)", txtInfoPrice);

infoGrid.add(typeTile, 0, 0);
infoGrid.add(numberTile, 1, 0);
infoGrid.add(capacityTile, 0, 1);
infoGrid.add(priceTile, 1, 1);

ImageView icoHousekeeping = createIconView("housekeeping.png", 18);
StackPane icoHousekeepingWrap = new StackPane(icoHousekeeping);
icoHousekeepingWrap.getStyleClass().addAll("metric-icon-wrap", "icon-housekeeping-wrap");

Label lblRoomMaintenance = new Label("Housekeeping & Maintenance");
lblRoomMaintenance.getStyleClass().add("dark-section-title");

HBox housekeepingTitleRow = new HBox(10, icoHousekeepingWrap, lblRoomMaintenance);
housekeepingTitleRow.setAlignment(Pos.CENTER_LEFT);

Label lblHousekeepingSub = new Label("Cleaning controls for the selected room");
lblHousekeepingSub.getStyleClass().add("dark-section-subtitle");

Label lblCleaningStatus = new Label("Cleaning Status");
lblCleaningStatus.getStyleClass().add("dark-field-title");

VBox housekeepingCard = new VBox(10);
housekeepingCard.getStyleClass().add("dark-inner-surface");
housekeepingCard.getChildren().addAll(lblCleaningStatus, choiceCleaningStatus);

Label lblAccommodationInfo = new Label("Selected Room Snapshot");
lblAccommodationInfo.getStyleClass().add("dark-section-title");

Label lblSnapshotSub = new Label("Live room details for reception and housekeeping");
lblSnapshotSub.getStyleClass().add("dark-section-subtitle");

VBox roomSnapshotCard = new VBox(12);
roomSnapshotCard.getStyleClass().add("dark-inner-surface");
roomSnapshotCard.getChildren().addAll(lblSnapshotSub, infoGrid);
VBox.setVgrow(roomSnapshotCard, Priority.ALWAYS);

Button btnUpdateStatus = new Button("Update Status");
btnUpdateStatus.getStyleClass().add("left-panel-action-button");
btnUpdateStatus.setMaxWidth(Double.MAX_VALUE);
addButtonHoverAnimation(btnUpdateStatus);
addButtonClickAnimation(btnUpdateStatus);

btnUpdateStatus.setOnAction(e -> {
    if (choiceCleaningStatus.getValue() != null) {
        choiceCleaningStatus.fireEvent(new ActionEvent());
    }
});

leftPanel.getChildren().addAll(
        housekeepingTitleRow,
        lblHousekeepingSub,
        housekeepingCard,
        lblAccommodationInfo,
        roomSnapshotCard,
        btnUpdateStatus
);


     // ===== RIGHT / CENTER UNIFIED WORKSPACE =====
VBox workspaceShell = new VBox(12);
workspaceShell.setMaxWidth(Double.MAX_VALUE);
HBox.setHgrow(workspaceShell, Priority.ALWAYS);
workspaceShell.getStyleClass().addAll("panel-card", "workspace-shell");
workspaceShell.setPadding(new Insets(16));
addCardHoverAnimation(workspaceShell);

// ----- booking side panel inside workspace -----
VBox bookingSideCard = new VBox(14);
bookingSideCard.setPrefWidth(360);
bookingSideCard.setMinWidth(320);
bookingSideCard.getStyleClass().add("booking-side-card");

GridPane recGrid = new GridPane();
recGrid.setVgap(10);
recGrid.setHgap(12);

ColumnConstraints recCol1 = new ColumnConstraints();
recCol1.setPercentWidth(50);
recCol1.setHgrow(Priority.ALWAYS);
recCol1.setFillWidth(true);

ColumnConstraints recCol2 = new ColumnConstraints();
recCol2.setPercentWidth(50);
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
btnChangeAccommodation.getStyleClass().add("soft-action-button");
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

Label lblReception = new Label("Guest Check-In / Booking");
lblReception.getStyleClass().add("section-title");

Label lblReceptionSub = new Label("Guest details, booking actions, and room changes");
lblReceptionSub.getStyleClass().add("section-subtitle");

VBox receptionFormCard = new VBox(12, lblReception, lblReceptionSub, recGrid);
receptionFormCard.getStyleClass().add("booking-inner-card");

// ----- action buttons under table -----
btnCheckIn = new Button("Check In");
btnUndoCheckIn = new Button("Undo Check-In");
btnCheckOut = new Button("Check Out");
btnCancelBooking = new Button("Cancel Booking");

btnCheckIn.setPrefWidth(120);
btnCheckOut.setPrefWidth(140);
btnUndoCheckIn.setPrefWidth(150);
btnCancelBooking.setPrefWidth(150);
btnChangeAccommodation.setPrefWidth(190);

btnCancelBooking.setWrapText(true);
btnUndoCheckIn.setWrapText(true);
btnChangeAccommodation.setWrapText(true);

btnUndoCheckIn.getStyleClass().add("secondary-action-button");
addButtonHoverAnimation(btnUndoCheckIn);
addButtonClickAnimation(btnUndoCheckIn);

btnCancelBooking.getStyleClass().add("soft-action-button");
addButtonHoverAnimation(btnCancelBooking);
addButtonClickAnimation(btnCancelBooking);

btnCheckIn.getStyleClass().add("primary-action-button");
btnCheckOut.getStyleClass().add("danger-action-button");
updateCheckInButtonText();

addButtonHoverAnimation(btnCheckIn);
addButtonHoverAnimation(btnCheckOut);
addButtonClickAnimation(btnCheckIn);
addButtonClickAnimation(btnCheckOut);

btnCheckOut.setDisable(true);
btnUndoCheckIn.setDisable(true);
btnCancelBooking.setDisable(true);
btnCheckIn.setDisable(true);

Region actionSpacer = new Region();
HBox.setHgrow(actionSpacer, Priority.ALWAYS);

HBox actionRibbon = new HBox(10);
actionRibbon.setAlignment(Pos.CENTER_LEFT);
actionRibbon.getStyleClass().add("action-ribbon");
actionRibbon.getChildren().addAll(
        btnCheckIn,
        btnUndoCheckIn,
        btnCheckOut,
        btnCancelBooking,
        btnChangeAccommodation,
        actionSpacer,
        btnResetSystem
);

// ----- room dashboard title and table -----
Label lblRoomDashboard = new Label("Rooms Dashboard");
lblRoomDashboard.getStyleClass().add("section-title");

Label lblDashboardSubtitle = new Label("Live room overview");
lblDashboardSubtitle.getStyleClass().add("section-subtitle");

StackPane tableWrapper = new StackPane(table);
tableWrapper.getStyleClass().add("table-shell");
VBox.setVgrow(tableWrapper, Priority.ALWAYS);

VBox tableSection = new VBox(12);
tableSection.setMaxWidth(Double.MAX_VALUE);
HBox.setHgrow(tableSection, Priority.ALWAYS);
tableSection.getChildren().addAll(
        lblRoomDashboard,
        lblDashboardSubtitle,
        tableWrapper,
        actionRibbon
);

// ----- workspace row: table + booking panel -----
HBox workspaceRow = new HBox(14);
workspaceRow.setAlignment(Pos.TOP_LEFT);
workspaceRow.getChildren().addAll(tableSection, bookingSideCard);
HBox.setHgrow(tableSection, Priority.ALWAYS);

// put the reception form into booking card
bookingSideCard.getChildren().addAll(receptionFormCard);

// workspace message banner
lblStatusMessage = new Label();
lblStatusMessage.setPadding(new Insets(0, 0, 0, 0));
lblStatusMessage.getStyleClass().addAll("status-label", "workspace-banner-label");

workspaceBanner = new HBox(lblStatusMessage);
workspaceBanner.setAlignment(Pos.CENTER_LEFT);
workspaceBanner.getStyleClass().addAll("workspace-banner", "banner-info");
workspaceBanner.setVisible(false);
workspaceBanner.setManaged(false);

// add to unified shell
workspaceShell.getChildren().addAll(workspaceRow, workspaceBanner);

// add to split pane
dashboardBody.getItems().addAll(leftPanel, workspaceShell);
leftPanel.setMinWidth(290);
workspaceShell.setMinWidth(860);


        // Main application layout: header at top, table in center, controls and messages at bottom
        BorderPane mainPane = new BorderPane();
mainPane.getStyleClass().add("main-pane");
mainPane.setTop(headerArea);

ScrollPane dashboardScroll = new ScrollPane(dashboardBody);
dashboardScroll.setFitToHeight(true);
dashboardScroll.setFitToWidth(false);
dashboardScroll.setPannable(true);
dashboardScroll.getStyleClass().add("dashboard-scroll");

mainPane.setCenter(dashboardScroll);
BorderPane.setMargin(dashboardScroll, new Insets(0, 10, 0, 10));

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

        boolean historyMode = btnHistoryView != null && btnHistoryView.isSelected();
CleaningStatus currentDisplayedStatus;

if (historyMode) {
    currentDisplayedStatus = CleaningStatus.valueOf(
            cedarSystem.getHistoricalCleaningStatusStringForDate(
                    selected.getAccommodationNumber(),
                    dpViewDate.getValue()
            )
    );
} else {
    currentDisplayedStatus = cedarSystem.getCleaningStatusForDate(
            selected.getAccommodationNumber(),
            dpViewDate.getValue()
    );
}

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

workspaceShell.setOpacity(0);

FadeTransition workspaceFade = new FadeTransition(Duration.seconds(2.2), workspaceShell);
workspaceFade.setFromValue(0);
workspaceFade.setToValue(1);

primaryStage.setScene(scene);
primaryStage.show();

FadeTransition windowFade = new FadeTransition(Duration.seconds(2.5), mainPane);
windowFade.setFromValue(0);
windowFade.setToValue(1);
windowFade.play();

leftSlide.play();
workspaceFade.play();
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
        TableColumn<Accommodation, String> colOccupancy = new TableColumn<>("Stay Status");
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
               setStyle("-fx-background-color: #ede9fe; -fx-text-fill: #6d28d9; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");
                break;
            case "unoccupied":
                setStyle("-fx-background-color: #f8fafc; -fx-text-fill: #475569; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");
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
                setStyle("-fx-background-color: #dcfce7; -fx-text-fill: #166534; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");

                break;
            case "unavailable":
                setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #991b1b; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");
                break;
            case "n/a":
                setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #475569; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");
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
                setStyle("-fx-background-color: #dcfce7; -fx-text-fill: #166534; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");
                break;
            case "dirty":
               setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #991b1b; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");
                break;
            case "maintenance":
                setStyle("-fx-background-color: #fef3c7; -fx-text-fill: #92400e; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");
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
                setStyle("-fx-background-color: #fef3c7; -fx-text-fill: #92400e; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");
                break;
            case "no":
                setStyle("-fx-background-color: #f8fafc; -fx-text-fill: #475569; -fx-font-weight: 700; -fx-background-insets: 4; -fx-background-radius: 12;");
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
            row.setStyle("-fx-background-color: #f5f3ff; -fx-border-color: #7c3aed; -fx-border-width: 0 0 0 4;");
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
          row.setStyle("-fx-background-color: #f5f3ff; -fx-border-color: #7c3aed; -fx-border-width: 0 0 0 4;");
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

        boolean historyMode = btnHistoryView != null && btnHistoryView.isSelected();
        LocalDate selectedDate = dpViewDate.getValue();

        CleaningStatus displayedStatus;

        if (historyMode) {
            String historicalStatusText = cedarSystem.getHistoricalCleaningStatusStringForDate(
                    selected.getAccommodationNumber(),
                    selectedDate
            );
            displayedStatus = CleaningStatus.valueOf(historicalStatusText);
        } else {
            displayedStatus = cedarSystem.getCleaningStatusForDate(
                    selected.getAccommodationNumber(),
                    selectedDate
            );
        }

        choiceCleaningStatus.setValue(displayedStatus);

        if (historyMode) {
            choiceCleaningStatus.setDisable(true);
        } else {
            choiceCleaningStatus.setDisable(
                    cedarSystem.isRoomOccupiedOnDate(
                            selected.getAccommodationNumber(),
                            selectedDate
                    )
            );
        }

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
    lblStatusMessage.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

    workspaceBanner.getStyleClass().removeAll("banner-success", "banner-error", "banner-info", "banner-warn");

    switch (color.toLowerCase()) {
        case "green":
            workspaceBanner.getStyleClass().add("banner-success");
            break;
        case "red":
            workspaceBanner.getStyleClass().add("banner-error");
            break;
        case "orange":
            workspaceBanner.getStyleClass().add("banner-warn");
            break;
        default:
            workspaceBanner.getStyleClass().add("banner-info");
            break;
    }

    workspaceBanner.setVisible(true);
    workspaceBanner.setManaged(true);

    workspaceBanner.setOpacity(0);
    FadeTransition fade = new FadeTransition(Duration.seconds(0.6), workspaceBanner);
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
lblTitle.getStyleClass().add("tile-label");

    VBox tile = new VBox(6);
    tile.setAlignment(Pos.CENTER_LEFT);
    tile.getChildren().addAll(lblTitle, field);

    return tile;
}
private VBox createFormTile(String title, Control control) {
    Label lblTitle = new Label(title);
lblTitle.getStyleClass().add("tile-label");

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

private ImageView createIconView(String fileName, double size) {
    Image image = new Image(HelloApplication.class.getResourceAsStream(fileName));
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(size);
    imageView.setFitHeight(size);
    imageView.setPreserveRatio(true);
    imageView.setSmooth(true);
    return imageView;
}
// Launches the JavaFX application
    public static void main(String[] args) {
        launch();
    }
}