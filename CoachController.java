package coach.management;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import application.LoginController;
import coach.management.AccidentReport;
import coach.management.TrainingSchedule;
import coach.management.Attendance;
import coach.management.AccessData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class CoachController implements Initializable {

	// members declaration
	private AccessData getData;
	private Integer reportId, scheduleId;
	@FXML
	private Tab accidentReportTab, trainingScheduleTab;
	@FXML
	private DatePicker dateOfAccident, dateOfReport, dateOfAttendance;
	@FXML
	private DatePicker statisticsStartDate;
	@FXML
	private DatePicker statisticsEndDate;
	@FXML
	private RadioButton attendance, absence;
	@FXML
	private PieChart chart;
	@SuppressWarnings("rawtypes")
	@FXML // combo boxes added in fxml file
	private ComboBox injuredPlayerCombo, attendanceSelectTeamCombo, statisticsSelectTeamCombo, scheduleSelectTeamCombo,
			firstDayCombo, secondDayCombo, firstDayTimeSlotCombo, secondDayTimeSlotCombo, attendanceSelectPlayerCombo,
			statisticsSelectPlayerCombo, seasonCombo;
	@FXML // buttons from fxml file
	private Button deleteReportBtn, clearReportBtn, quitBtn, quitTwoBtn, saveReportBtn, saveScheduleBtn,
			clearScheduleBtn, loadScheduleBtn, saveAttendanceBtn, clearAttendanceBtn, deleteScheduleBtn,
			viewStatisticsBtn, clearStatisticResultBtn;
	@FXML // test area from fxml file
	private TextArea detailsOfAccidentText, actionTakenText, detailsOfAccidentResultText, actionTakenResultText;
	@FXML // checkboxes from fxml files
	private CheckBox firstWitnessCheckBox, secondWitnessCheckBox, parentCheckBox, gpCheckBox, ambulanceCheckBox,
			gardaCheckBox, presentCheckBox, excusedCheckBox;
	@FXML // Table to store Accident Reports
	private TableView<AccidentReport> accidentReportTable;
	@FXML // Table to store training schedules
	private TableView<TrainingSchedule> scheduleTable;
	@FXML
	private TableColumn<AccidentReport, Integer> idCol;
	@FXML
	private TableColumn<TrainingSchedule, Integer> scheduleIdCol;
	@FXML // accident report columns
	private TableColumn<AccidentReport, String> dateOfAccidentCol, timeOfAccidentCol, dateOfReportCol, timeOfReportCol,
			fNameCol, lNameCol, playerDobCol, teamCol, coachIdCol, detailsOfAccidentCol, firstAiderFirstNameCol,
			firstAiderLastNameCol, firstAiderPhoneNumCol, witnessOneFirstNameCol, witnessOneLastNameCol,
			witnessOnePhoneNumCol, witnessTwoFirstNameCol, witnessTwoLastNameCol, witnessTwoPhoneNumCol, gardaCol,
			parentGuardianCol, gpCol, ambulanceCol, actionTakenCol;
	@FXML // training schedule columns
	private TableColumn<TrainingSchedule, String> teamNameCol, seasonCol, firstDayCol, firstDayTimeSlotCol,
			secondDayTimeSlotCol, secondDayCol, teamIdCol;
	@FXML // text fields added in fxml file
	private TextField filterReportField, timeOfAccident, timeOfReport, firstAiderFirstName, firstAiderLastName,
			firstAiderPhoneNum, injuredPlayerFirstName, injuredPlayerLastName, injuredPlayerDob, witnessOneFirstName,
			witnessOneLastName, witnessOnePhoneNum, witnessTwoFirstName, witnessTwoLastName, witnessTwoPhoneNum,
			filterSchedule, playersTeam;
	@FXML // label added in fxml file
	private Label timeOfAccidentResultLbl, dateOfAccidentResultLbl, dateOfReportResultLbl, timeOfReportResultLbl,
			firstAiderFirstNameResultLbl, firstAiderLastNameResultLbl, firstAiderPhoneNumResultLbl,
			injuredPlayerFirstNameResultLbl, injuredPlayerLastNameResultLbl, injuredPlayerDobResultLbl,
			witnessOneFirstNameResultLbl, witnesOneLastNameResultLbl, witnesOnePhoneNumResultLbl,
			witnesTwoFirstNameResultLbl, witnesTwoLastNameResultLbl, witnesTwoPhoneNumResultLbl, gardaResultLbl,
			gpResultLbl, parentResultLbl, ambulanceResultLbl, attendFirstNameResultLbl, attendLastNameResultLbl,
			attendStartDateResultLbl, attendEndDateResultLbl, attendTeamNameResultLbl, attendPercentageResultLbl,
			attendExcusedResultLbl, attendMissedResultLbl, conductedTrainingsResultLbl, attendedTrainingsResultLbl;
	final ToggleGroup group = new ToggleGroup();
	ObservableList<TrainingSchedule> schedule;

	// For Connection to Database
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	String dbURL = "jdbc:mysql://us-iron-auto-dca-03-a.cleardb.net:3306/ad_778443fac3470d0";
	String user = "bd80a40c4b34f0";
	String password = "5ad36c2f";

	// Initializes all components and specifies behavior on the start of the app
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// disables buttons to prevent sending null values to databse
		witnessOneFirstName.setDisable(true);
		witnessOneLastName.setDisable(true);
		witnessTwoFirstName.setDisable(true);
		witnessTwoLastName.setDisable(true);
		witnessOnePhoneNum.setDisable(true);
		witnessTwoPhoneNum.setDisable(true);
		attendanceSelectPlayerCombo.setDisable(true);
		statisticsSelectPlayerCombo.setDisable(true);
		clearAttendanceBtn.setDisable(true);
		saveAttendanceBtn.setDisable(true);
		saveScheduleBtn.setDisable(true);
		saveReportBtn.setDisable(true);
		injuredPlayerFirstName.setDisable(true);
		injuredPlayerLastName.setDisable(true);
		injuredPlayerDob.setDisable(true);
		playersTeam.setDisable(true);
		viewStatisticsBtn.setDisable(true);
		statisticsEndDate.setDisable(true);
		clearStatisticResultBtn.setDisable(true);
		clearScheduleBtn.setDisable(true);
		// Observable lists to store days of the week for training schedule
		ObservableList<String> days = FXCollections.observableArrayList("Monday", "Tuesday", "Wednesday", "Thursday",
				"Friday", "Saturday");
		final ObservableList<String> dayOneSelected = FXCollections.observableArrayList("Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday");
		final ObservableList<String> dayTwoSelected = FXCollections.observableArrayList("Monday", "Wednesday",
				"Thursday", "Friday", "Saturday");
		final ObservableList<String> dayThreeSelected = FXCollections.observableArrayList("Monday", "Tuesday",
				"Thursday", "Friday", "Saturday");
		final ObservableList<String> dayFourSelected = FXCollections.observableArrayList("Monday", "Tuesday",
				"Wednesday", "Friday", "Saturday");
		final ObservableList<String> dayFiveSelected = FXCollections.observableArrayList("Monday", "Tuesday",
				"Thursday", "Wednesday", "Saturday");
		final ObservableList<String> daySixSelected = FXCollections.observableArrayList("Monday", "Tuesday", "Thursday",
				"Wednesday", "Friday");
		firstDayCombo.getItems().addAll(days);

		// changes values in second combobox depending on selection from first
		// combo
		// to avoid having two training in same day by one team
		firstDayCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (firstDayCombo.getValue() != null) {
					switch (t1.toString()) {
					case "Monday":
						secondDayCombo.setItems(dayOneSelected);
						break;
					case "Tuesday":
						secondDayCombo.setItems(dayTwoSelected);
						break;
					case "Wednesday":
						secondDayCombo.setItems(dayThreeSelected);
						break;
					case "Thursday":
						secondDayCombo.setItems(dayFourSelected);
						break;
					case "Friday":
						secondDayCombo.setItems(dayFiveSelected);
						break;
					case "Saturday":
						secondDayCombo.setItems(daySixSelected);
						break;

					}
				}
			}
		});
		// set tool tip for appropriate text fields to show user valid format
		final Tooltip phoneTip = new Tooltip();
		phoneTip.setText("Pattern: 10 digits in lenght\n" + "eg. 0851234567");
		firstAiderPhoneNum.setTooltip(phoneTip);
		witnessOnePhoneNum.setTooltip(phoneTip);
		witnessTwoPhoneNum.setTooltip(phoneTip);
		final Tooltip textAreaTip = new Tooltip();
		textAreaTip.setText("Max number of characters = 1000");
		detailsOfAccidentText.setTooltip(textAreaTip);
		actionTakenText.setTooltip(textAreaTip);
		final Tooltip onlyLettersTip = new Tooltip();
		onlyLettersTip.setText("Only letters are accepted");
		firstAiderFirstName.setTooltip(onlyLettersTip);
		firstAiderLastName.setTooltip(onlyLettersTip);
		witnessOneFirstName.setTooltip(onlyLettersTip);
		witnessOneLastName.setTooltip(onlyLettersTip);
		witnessTwoFirstName.setTooltip(onlyLettersTip);
		witnessTwoLastName.setTooltip(onlyLettersTip);
		final Tooltip timeTip = new Tooltip();
		timeTip.setText("Time format: HH:mm\n" + "eg. 18:20");
		timeOfAccident.setTooltip(timeTip);
		timeOfReport.setTooltip(timeTip);

		// adds seasons to combobox up to 2026
		seasonCombo.getItems().addAll("2016/2017", "2017/2018", "2018/2019", "2019/2020", "2020/2021", "2021/2022",
				"2022/2023", "2023/2024", "2024/2025", "2025/2026");
		firstDayTimeSlotCombo.getItems().addAll("15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00",
				"19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00");
		secondDayTimeSlotCombo.getItems().addAll("15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00",
				"19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00");
		attendance.setToggleGroup(group);
		attendance.setSelected(false);
		absence.setToggleGroup(group);
		attendance.setUserData("A");
		absence.setUserData("B");
		getTeam();// loads teams to combobox
		getPlayerName();// loads players names from selected team
		getPlayerDetails();// loads selected players details
		loadAccidentReport();// loads accident report
		loadSchedule();// loads training schedule

		dateOfReport.setValue(LocalDate.now());
		dateOfAttendance.setValue(LocalDate.now());
		// Format attendance date date picker
		// to limit dates selection to prevent future and past dates being added
		// to attendance
		Callback<DatePicker, DateCell> attendance = dp -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				// restricts dates only to current date
				if (item.isBefore(LocalDate.now()) || item.isAfter(LocalDate.now())) {
					setStyle("-fx-background-color: #ffc0cb;");
					setDisable(true);
				}
			}
		};
		// formats date to valid mysql date format
		StringConverter attendanceDateConverter = new StringConverter<LocalDate>() {
			final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			@Override
			public String toString(LocalDate date) {
				if (date != null)
					return dateFormatter.format(date);
				else
					return "";
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					LocalDate date = LocalDate.parse(string, dateFormatter);

					if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now())) {
						return dateOfAttendance.getValue();
					} else
						return date;
				} else
					return null;
			}
		};
		dateOfAttendance.setConverter(attendanceDateConverter);
		dateOfAttendance.setDayCellFactory(attendance);

		// Format accident date picker to limit choice to past week only
		Callback<DatePicker, DateCell> accident = dp -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);

				if (item.isBefore(LocalDate.now().minusWeeks(1)) || item.isAfter(LocalDate.now())) {
					setStyle("-fx-background-color: #ffc0cb;");
					setDisable(true);
				}
			}
		};
		// formats date to valid mysql date format
		StringConverter accidentDateConverter = new StringConverter<LocalDate>() {
			final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			@Override
			public String toString(LocalDate date) {
				if (date != null)
					return dateFormatter.format(date);
				else
					return "";
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					LocalDate date = LocalDate.parse(string, dateFormatter);

					if (date.isBefore(LocalDate.now().minusWeeks(1)) || date.isAfter(LocalDate.now())) {
						return dateOfAccident.getValue();
					} else
						return date;
				} else
					return null;
			}
		};
		dateOfAccident.setConverter(accidentDateConverter);
		dateOfAccident.setDayCellFactory(accident);
		// Format report date picker
		Callback<DatePicker, DateCell> report = dp -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);

				if (item.isBefore(LocalDate.now()) || item.isAfter(LocalDate.now())) {
					setStyle("-fx-background-color: #ffc0cb;");
					setDisable(true);
				}
			}
		};
		// formats date to valid mysql date format
		StringConverter reportDateConverter = new StringConverter<LocalDate>() {
			final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			@Override
			public String toString(LocalDate date) {
				if (date != null)
					return dateFormatter.format(date);
				else
					return "";
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					LocalDate date = LocalDate.parse(string, dateFormatter);

					if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now())) {
						return dateOfReport.getValue();
					} else
						return date;
				} else
					return null;
			}
		};
		dateOfReport.setConverter(reportDateConverter);
		dateOfReport.setDayCellFactory(report);

		// Format statistics start date picker
		Callback<DatePicker, DateCell> startDate = dp -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);

				if (item.isBefore(LocalDate.now().minusYears(5)) || item.isAfter(LocalDate.now())) {
					setStyle("-fx-background-color: #ffc0cb;");
					setDisable(true);
				}
			}
		};
		StringConverter startDateConverter = new StringConverter<LocalDate>() {
			final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			@Override
			public String toString(LocalDate date) {
				if (date != null)
					return dateFormatter.format(date);
				else
					return "";
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					LocalDate date = LocalDate.parse(string, dateFormatter);

					if (date.isBefore(LocalDate.now().minusYears(5)) || date.isAfter(LocalDate.now())) {
						return statisticsStartDate.getValue();
					} else
						return date;
				} else
					return null;
			}
		};
		statisticsStartDate.setConverter(startDateConverter);
		statisticsStartDate.setDayCellFactory(startDate);
		// Format statistics end date picker
		Callback<DatePicker, DateCell> endDate = dp -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				if (statisticsStartDate.getValue() != statisticsEndDate.getValue()) {
					try {
						if (item.isBefore(statisticsStartDate.getValue().plusDays(1))
								|| item.isAfter(LocalDate.now())) {
							setStyle("-fx-background-color: #ffc0cb;");
							setDisable(true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		StringConverter endDateConverter = new StringConverter<LocalDate>() {
			final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			@Override
			public String toString(LocalDate date) {
				if (date != null)
					return dateFormatter.format(date);
				else
					return "";
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					LocalDate date = LocalDate.parse(string, dateFormatter);

					if (date.isBefore(statisticsStartDate.getValue().plusDays(1)) || date.isAfter(LocalDate.now())) {
						return statisticsEndDate.getValue();
					} else
						return date;
				} else
					return null;
			}
		};
		statisticsEndDate.setConverter(endDateConverter);
		statisticsEndDate.setDayCellFactory(endDate);

		// disable/enable attendance check boxes
		presentCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (presentCheckBox.isSelected()) {
					excusedCheckBox.setDisable(true);
				} else {
					excusedCheckBox.setDisable(false);
				}
			}
		});
		// disable/enable present check boxes
		excusedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (excusedCheckBox.isSelected()) {
					presentCheckBox.setDisable(true);
				} else {
					presentCheckBox.setDisable(false);
				}
			}
		});
		// validates if medical report notes are not longer than 1000 characters
		detailsOfAccidentText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (detailsOfAccidentText.getText().length() > 1000 || detailsOfAccidentText.getText().isEmpty()) {
					detailsOfAccidentText.getStyleClass().add("invalidValue");
				} else {
					detailsOfAccidentText.getStyleClass().clear();
					detailsOfAccidentText.getStyleClass().addAll("text-area", "text-input");
				}
				enableSaveReportBtn();
			}
		});
		// validates if action taken notes are not longer than 1000 characters
		actionTakenText.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (actionTakenText.getText().length() > 1000 || actionTakenText.getText().isEmpty()) {
					actionTakenText.getStyleClass().add("invalidValue");
				} else {
					actionTakenText.getStyleClass().clear();
					actionTakenText.getStyleClass().addAll("text-area", "text-input");
				}
				enableSaveReportBtn();
			}
		});
		// validates phone format
		firstAiderPhoneNum.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (firstAiderPhoneNum.getText().matches("08[35679]\\d{7}")) {
					firstAiderPhoneNum.getStyleClass().clear();
					firstAiderPhoneNum.getStyleClass().addAll("text-field", "text-input");
				} else {
					firstAiderPhoneNum.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}
		});
		// validates phone format
		witnessOnePhoneNum.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (witnessOnePhoneNum.getText().matches("08[35679]\\d{7}")) {
					witnessOnePhoneNum.getStyleClass().clear();
					witnessOnePhoneNum.getStyleClass().addAll("text-field", "text-input");
				} else {
					witnessOnePhoneNum.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}

		});

		// validates phone format
		witnessTwoPhoneNum.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (witnessTwoPhoneNum.getText().matches("08[35679]\\d{7}")) {
					witnessTwoPhoneNum.getStyleClass().clear();
					witnessTwoPhoneNum.getStyleClass().addAll("text-field", "text-input");
				} else {
					witnessTwoPhoneNum.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}
		});

		// Validate time of report field
		timeOfAccident.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (timeOfAccident.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
					timeOfAccident.getStyleClass().clear();
					timeOfAccident.getStyleClass().addAll("text-field", "text-input");
				} else {
					timeOfAccident.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}
		});

		// Validate time of accident field
		timeOfReport.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (timeOfReport.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
					timeOfReport.getStyleClass().clear();
					timeOfReport.getStyleClass().addAll("text-field", "text-input");

				} else {
					timeOfReport.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}
		});
		// checks if user entry contains of letters only and is not empty
		firstAiderFirstName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (firstAiderFirstName.getText().matches("[A-Za-z\\s]+") && !firstAiderFirstName.getText().isEmpty()) {
					firstAiderFirstName.getStyleClass().clear();
					firstAiderFirstName.getStyleClass().addAll("text-field", "text-input");
				} else {
					firstAiderFirstName.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}
		});
		// checks if user entry contains of letters only and is not empty
		firstAiderLastName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (firstAiderLastName.getText().matches("[A-Za-z\\s]+") && !firstAiderLastName.getText().isEmpty()) {
					firstAiderLastName.getStyleClass().clear();
					firstAiderLastName.getStyleClass().addAll("text-field", "text-input");
				} else {
					firstAiderLastName.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}
		});
		// checks if user entry contains of letters only and is not empty
		witnessOneFirstName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (witnessOneFirstName.getText().matches("[A-Za-z\\s]+") && !witnessOneFirstName.getText().isEmpty()) {
					witnessOneFirstName.getStyleClass().clear();
					witnessOneFirstName.getStyleClass().addAll("text-field", "text-input");
				} else {
					witnessOneFirstName.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}
		});
		// checks if user entry contains of letters only and is not empty
		witnessOneLastName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (witnessOneLastName.getText().matches("[A-Za-z\\s]+") && !witnessOneLastName.getText().isEmpty()) {
					witnessOneLastName.getStyleClass().clear();
					witnessOneLastName.getStyleClass().addAll("text-field", "text-input");
				} else {
					witnessOneLastName.getStyleClass().add("invalidValue");

				}
				enableSaveReportBtn();
			}
		});

		// checks if user entry contains of letters only and is not empty
		witnessTwoFirstName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (witnessTwoFirstName.getText().matches("[A-Za-z\\s]+") && !witnessTwoFirstName.getText().isEmpty()) {
					witnessTwoFirstName.getStyleClass().clear();
					witnessTwoFirstName.getStyleClass().addAll("text-field", "text-input");
				} else {
					witnessTwoFirstName.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}
		});
		// checks if user entry contains of letters only and is not empty
		witnessTwoLastName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (witnessTwoLastName.getText().matches("[A-Za-z\\s]+") && !witnessTwoLastName.getText().isEmpty()) {
					witnessTwoLastName.getStyleClass().clear();
					witnessTwoLastName.getStyleClass().addAll("text-field", "text-input");
				} else {
					witnessTwoLastName.getStyleClass().add("invalidValue");
				}
				enableSaveReportBtn();
			}
		});

		// filter accident report table
		ObservableList<AccidentReport> reportTable = accidentReportTable.getItems();
		// Allows for data filtering in the table view according to the user
		// entry
		filterReportField.textProperty()
				.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					if (oldValue != null && (newValue.length() < oldValue.length())) {
						accidentReportTable.setItems(reportTable);
					}
					String value = newValue.toLowerCase();
					ObservableList<AccidentReport> subentries = FXCollections.observableArrayList();

					long count = accidentReportTable.getColumns().stream().count();
					for (int i = 0; i < accidentReportTable.getItems().size(); i++) {
						for (int j = 0; j < count; j++) {
							String entry = "" + accidentReportTable.getColumns().get(j).getCellData(i);
							if (entry.toLowerCase().contains(value)) {
								subentries.add(accidentReportTable.getItems().get(i));
								break;
							}
						}
					}
					accidentReportTable.setItems(subentries);
				});

		// filter schedule table
		schedule = scheduleTable.getItems();
		filterSchedule.textProperty()
				.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					if (oldValue != null && (newValue.length() < oldValue.length())) {
						scheduleTable.setItems(schedule);
					}
					String value = newValue.toLowerCase();
					ObservableList<TrainingSchedule> subentries = FXCollections.observableArrayList();

					long count = scheduleTable.getColumns().stream().count();
					for (int i = 0; i < scheduleTable.getItems().size(); i++) {
						for (int j = 0; j < count; j++) {
							String entry = "" + scheduleTable.getColumns().get(j).getCellData(i);
							if (entry.toLowerCase().contains(value)) {
								subentries.add(scheduleTable.getItems().get(i));
								break;
							}
						}
					}
					scheduleTable.setItems(subentries);
				});
	}

	// connects and load data from database to show players attendance
	// statistics
	public void viewStatistics() {
		String firstName = null;
		String lastName = null;
		int memberId = 0;
		String value = statisticsSelectPlayerCombo.getValue().toString();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(dbURL, user, password);
			String query = "SELECT m.memberID,m.firstName,m.lastname" + " FROM member m, member_has_team tc, team t "
					+ "WHERE m.lastName=? AND m.memberID = tc.member_memberID AND tc.team_teamID = t.teamID";
			pstmt = conn.prepareStatement(query); // create a statement
			pstmt.setString(1, value); // set input parameter
			rs = pstmt.executeQuery();
			while (rs.next()) {
				memberId = rs.getInt(1);
				firstName = rs.getString(2);
				lastName = rs.getString(3);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String trainingNum = null;
		String present = null;
		String absent = null;
		String excused = null;
		boolean valid = true;
		try {// checks status of selected players according to boolean values
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(dbURL, user, password);
			Statement s = conn.createStatement();
			ResultSet getDetails = s.executeQuery("SELECT count(present) as conductedTrainings, "
					+ "SUM(CASE WHEN present = 1 THEN 1 ELSE 0 END) AS present, "
					+ "SUM(CASE WHEN present = 0 THEN 1 ELSE 0 END) AS absent,"
					+ "SUM(CASE WHEN excused = 1 THEN 1 ELSE 0 END) AS excused,"
					+ "SUM(CASE WHEN excused = 0 THEN 1 ELSE 0 END) AS notExcused FROM attendance"
					+ " WHERE member_memberID='" + memberId + "' AND date BETWEEN'"
					+ statisticsStartDate.getValue().toString() + "' AND '" + statisticsEndDate.getValue().toString()
					+ "'");

			while (getDetails.next()) {
				trainingNum = getDetails.getString("conductedTrainings");
				present = getDetails.getString("present");
				absent = getDetails.getString("absent");
				excused = getDetails.getString("excused");
			}
			conn.close();
			if (trainingNum.equals("0")) {
				valid = false;
				dataNotFound();
			} else
				valid = true;
		} catch (Exception e) {
			valid = false;
			dataNotFound();
			e.printStackTrace();
		}
		// if query returns at least one rcord data is added to pie chart
		if (valid) {
			double attendedTrainings = Double.parseDouble(present);
			double total = Double.parseDouble(trainingNum);
			double absentValue = Double.parseDouble(absent);
			double excusedAbsence = Double.parseDouble(excused);
			double unexcusedAbsence = absentValue - excusedAbsence;
			double percentage = attendedTrainings * 100 / total;
			DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
			percentage = Double.valueOf(oneDigit.format(percentage));
			attendFirstNameResultLbl.setText(firstName);
			attendLastNameResultLbl.setText(lastName);
			attendStartDateResultLbl.setText(statisticsStartDate.getValue().toString());
			attendEndDateResultLbl.setText(statisticsEndDate.getValue().toString());
			attendTeamNameResultLbl.setText(statisticsSelectTeamCombo.getValue().toString());
			conductedTrainingsResultLbl.setText(trainingNum);
			attendPercentageResultLbl.setText(Double.toString(percentage));
			attendExcusedResultLbl.setText(excused);
			attendMissedResultLbl.setText(absent);
			attendedTrainingsResultLbl.setText(present);
			if (statisticsSelectPlayerCombo.getValue() != null && statisticsSelectTeamCombo.getValue() != null) {
				ObservableList<PieChart.Data> attendanceData = FXCollections.observableArrayList(
						new PieChart.Data("Attended Trainings", attendedTrainings),
						new PieChart.Data("Missed Trainings", absentValue));
				ObservableList<PieChart.Data> absenceData = FXCollections.observableArrayList(
						new PieChart.Data("Unexcused Absence", unexcusedAbsence),
						new PieChart.Data("Excused Absence", excusedAbsence));
				group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
						if (group.getSelectedToggle() != null) {
							if (group.getSelectedToggle().getUserData().toString().equals("A")) {
								chart.setData(attendanceData);
							} else {
								chart.setData(absenceData);
							}

						}
					}
				});
			} // clears pie chart after each player
			else {
				chart.getData().clear();
			}
		}
		enableClearStatisticsResultBtn();

	}

	// displays accident report in details
	private void showReportDetails(AccidentReport report) {
		if (report != null) {
			dateOfAccidentResultLbl.setText(report.getDateOfAccident());
			timeOfAccidentResultLbl.setText(report.getTimeOfAccident());
			dateOfReportResultLbl.setText(report.getDateOfReport());
			timeOfReportResultLbl.setText(report.getTimeOfReport());
			firstAiderFirstNameResultLbl.setText(report.getFirstAiderFirstName());
			firstAiderLastNameResultLbl.setText(report.getFirstAiderLastName());
			firstAiderPhoneNumResultLbl.setText(report.getFirstAiderPhoneNumber());
			injuredPlayerFirstNameResultLbl.setText(report.getFirstName());
			injuredPlayerLastNameResultLbl.setText(report.getLastName());
			injuredPlayerDobResultLbl.setText(report.getInjuredPlayerDOB());
			detailsOfAccidentResultText.setText(report.getDetails());
			actionTakenResultText.setText(report.getActionTaken());

			if (report.getWitnessOneFirstName().isEmpty())
				witnessOneFirstNameResultLbl.setText("n/a");
			else
				witnessOneFirstNameResultLbl.setText(report.getWitnessOneFirstName());

			if (report.getWitnessOneLastName().isEmpty())
				witnesOneLastNameResultLbl.setText("n/a");
			else
				witnesOneLastNameResultLbl.setText(report.getWitnessOneLastName());
			if (report.getWitnessOnePhoneNumber().isEmpty())
				witnesOnePhoneNumResultLbl.setText("n/a");
			else
				witnesOnePhoneNumResultLbl.setText(report.getWitnessOnePhoneNumber());

			if (report.getWitnessTwoFirstName().isEmpty())
				witnesTwoFirstNameResultLbl.setText("n/a");
			else
				witnesTwoFirstNameResultLbl.setText(report.getWitnessTwoFirstName());
			if (report.getWitnessTwoLastName().isEmpty())
				witnesTwoLastNameResultLbl.setText("n/a");
			else
				witnesTwoLastNameResultLbl.setText(report.getWitnessTwoLastName());

			if (report.getWitnessTwoPhoneNumber().isEmpty())
				witnesTwoPhoneNumResultLbl.setText("n/a");
			else
				witnesTwoPhoneNumResultLbl.setText(report.getWitnessTwoPhoneNumber());

			if (report.getAmbulance() == true) {
				ambulanceResultLbl.setText("Yes");
			} else {
				ambulanceResultLbl.setText("No");
			}
			if (report.getGarda() == true) {
				gardaResultLbl.setText("Yes");
			} else {
				gardaResultLbl.setText("No");
			}
			if (report.getGp() == true) {
				gpResultLbl.setText("Yes");
			} else {
				gpResultLbl.setText("No");
			}
			if (report.getParent() == true) {
				parentResultLbl.setText("Yes");
			} else {
				parentResultLbl.setText("No");
			}
		} else {
			dateOfAccidentResultLbl.setText("-");
			timeOfAccidentResultLbl.setText("-");
			dateOfReportResultLbl.setText("-");
			timeOfReportResultLbl.setText("-");
			firstAiderFirstNameResultLbl.setText("-");
			firstAiderLastNameResultLbl.setText("-");
			firstAiderPhoneNumResultLbl.setText("-");
			injuredPlayerFirstNameResultLbl.setText("-");
			injuredPlayerLastNameResultLbl.setText("-");
			injuredPlayerDobResultLbl.setText("-");
			witnessOneFirstNameResultLbl.setText("-");
			witnesOneLastNameResultLbl.setText("-");
			witnesOnePhoneNumResultLbl.setText("-");
			witnesTwoFirstNameResultLbl.setText("-");
			witnesTwoLastNameResultLbl.setText("-");
			witnesTwoLastNameResultLbl.setText("-");
			witnesTwoPhoneNumResultLbl.setText("-");
			detailsOfAccidentResultText.setText("-");
			actionTakenResultText.setText("-");
			gardaResultLbl.setText("-");
			gpResultLbl.setText("-");
			parentResultLbl.setText("-");
			ambulanceResultLbl.setText("-");
			detailsOfAccidentResultText.setText("-");
			actionTakenResultText.setText("-");
		}
	}

	// quit app after confirmation
	public void quitApp() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		ButtonType yesType = new ButtonType("Yes");
		ButtonType noType = new ButtonType("No");
		alert.getButtonTypes().setAll(yesType, noType);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("You are about to quit application!\nAny unsaved changes will be lost!");
		alert.setContentText("Are you sure you want to quit?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == yesType) {
			System.exit(0);
		} else {
		}
	}

	// Clear report form
	@SuppressWarnings("unchecked")
	public void resetReportValues() {
		detailsOfAccidentText.setText("");
		actionTakenText.setText("");
		timeOfReport.setText("");
		timeOfAccident.setText("");
		witnessOnePhoneNum.setText("");
		witnessTwoPhoneNum.setText("");
		firstAiderPhoneNum.setText("");
		firstAiderFirstName.setText("");
		firstAiderLastName.setText("");
		witnessOneFirstName.setText("");
		witnessOneLastName.setText("");
		witnessOnePhoneNum.setText("");
		witnessTwoFirstName.setText("");
		witnessTwoLastName.setText("");
		if (!timeOfReport.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
			timeOfReport.getStyleClass().clear();
			timeOfReport.getStyleClass().addAll("text-field", "text-input");
		}
		timeOfAccident.setText("");
		if (!timeOfAccident.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
			timeOfAccident.getStyleClass().clear();
			timeOfAccident.getStyleClass().addAll("text-field", "text-input");
		}
		if (!firstAiderPhoneNum.getText().matches("08[35679]\\d{7}")) {
			firstAiderPhoneNum.getStyleClass().clear();
			firstAiderPhoneNum.getStyleClass().addAll("text-field", "text-input");
		}
		if (!witnessOnePhoneNum.getText().matches("08[35679]\\d{7}")) {
			witnessOnePhoneNum.getStyleClass().clear();
			witnessOnePhoneNum.getStyleClass().addAll("text-field", "text-input");
		}
		if (!witnessTwoPhoneNum.getText().matches("08[35679]\\d{7}")) {
			witnessTwoPhoneNum.getStyleClass().clear();
			witnessTwoPhoneNum.getStyleClass().addAll("text-field", "text-input");
		}
		if (!witnessOneFirstName.getText().matches("[A-Za-z\\s]+")) {
			witnessOneFirstName.getStyleClass().clear();
			witnessOneFirstName.getStyleClass().addAll("text-field", "text-input");
		}
		if (!witnessOneLastName.getText().matches("[A-Za-z\\s]+")) {
			witnessOneLastName.getStyleClass().clear();
			witnessOneLastName.getStyleClass().addAll("text-field", "text-input");
		}
		if (!witnessTwoFirstName.getText().matches("[A-Za-z\\s]+")) {
			witnessTwoFirstName.getStyleClass().clear();
			witnessTwoFirstName.getStyleClass().addAll("text-field", "text-input");
		}
		if (!witnessTwoLastName.getText().matches("[A-Za-z\\s]+")) {
			witnessTwoLastName.getStyleClass().clear();
			witnessTwoLastName.getStyleClass().addAll("text-field", "text-input");
		}
		if (!firstAiderFirstName.getText().matches("[A-Za-z\\s]+")) {
			firstAiderFirstName.getStyleClass().clear();
			firstAiderFirstName.getStyleClass().addAll("text-field", "text-input");
		}
		if (!firstAiderLastName.getText().matches("[A-Za-z\\s]+")) {
			firstAiderLastName.getStyleClass().clear();
			firstAiderLastName.getStyleClass().addAll("text-field", "text-input");
		}
		if (detailsOfAccidentText.getText().isEmpty()) {
			detailsOfAccidentText.getStyleClass().clear();
			detailsOfAccidentText.getStyleClass().addAll("text-area", "text-input");
		}
		if (actionTakenText.getText().isEmpty()) {
			actionTakenText.getStyleClass().clear();
			actionTakenText.getStyleClass().addAll("text-area", "text-input");
		}

		dateOfAccident.setValue(null);
		dateOfReport.setValue(LocalDate.now());
		injuredPlayerCombo.setValue(null);
		firstWitnessCheckBox.setSelected(false);
		secondWitnessCheckBox.setSelected(false);
		parentCheckBox.setSelected(false);
		gpCheckBox.setSelected(false);
		ambulanceCheckBox.setSelected(false);
		gardaCheckBox.setSelected(false);
		filterReportField.setText("");
		injuredPlayerFirstName.setText("");
		injuredPlayerLastName.setText("");
		injuredPlayerDob.setText("");
		playersTeam.setText("");
		witnessOneFirstName.setDisable(true);
		witnessOneLastName.setDisable(true);
		witnessTwoFirstName.setDisable(true);
		witnessTwoLastName.setDisable(true);
		witnessOnePhoneNum.setDisable(true);
		witnessTwoPhoneNum.setDisable(true);
		saveReportBtn.setDisable(true);
		injuredPlayerFirstName.setDisable(true);
		injuredPlayerLastName.setDisable(true);
		injuredPlayerDob.setDisable(true);
		playersTeam.setDisable(true);
	}

	// sets training schedule comboboxes to default values
	@SuppressWarnings("unchecked")
	public void resetScheduleValues() {
		seasonCombo.setValue(null);
		firstDayTimeSlotCombo.setValue(null);
		secondDayTimeSlotCombo.setValue(null);
		scheduleSelectTeamCombo.setValue(null);
		firstDayCombo.setValue(null);
		secondDayCombo.setValue(null);
	}

	// sets attendance comboboxes and date picker to default values
	@SuppressWarnings("unchecked")
	public void resetAttendanceValues() {
		presentCheckBox.setSelected(false);
		excusedCheckBox.setSelected(false);
		dateOfAttendance.setValue(LocalDate.now());
		attendanceSelectTeamCombo.setValue(null);
		attendanceSelectPlayerCombo.setValue(null);
	}

	// resets all statistics results related fields labels and comboboxes
	@SuppressWarnings("unchecked")
	public void resetStatisticResult() {
		clearStatisticResultBtn.setDisable(true);
		attendance.setSelected(false);
		absence.setSelected(false);
		chart.getData().clear();
		attendFirstNameResultLbl.setText("-");
		attendLastNameResultLbl.setText("-");
		attendStartDateResultLbl.setText("-");
		attendEndDateResultLbl.setText("-");
		attendTeamNameResultLbl.setText("-");
		attendPercentageResultLbl.setText("-");
		attendExcusedResultLbl.setText("-");
		attendMissedResultLbl.setText("-");
		conductedTrainingsResultLbl.setText("-");
		attendedTrainingsResultLbl.setText("-");
		statisticsStartDate.setValue(null);
		statisticsEndDate.setValue(null);
		statisticsSelectTeamCombo.setValue(null);
		statisticsSelectPlayerCombo.setValue(null);
		statisticsSelectPlayerCombo.setDisable(true);
		// resetStatisticSelection();
	}

	// enables extra fields when witnesses are required to the report
	public void enableWitnessTextFields() {
		enableSaveReportBtn();
		if (firstWitnessCheckBox.isSelected()) {
			witnessOneFirstName.setDisable(false);
			witnessOneLastName.setDisable(false);
			witnessOnePhoneNum.setDisable(false);
		} else {
			witnessOneFirstName.setDisable(true);
			witnessOneLastName.setDisable(true);
			witnessOnePhoneNum.setDisable(true);
			witnessOnePhoneNum.setText("");
			witnessOneFirstName.setText("");
			witnessOneLastName.setText("");
			witnessOneFirstName.getStyleClass().clear();
			witnessOnePhoneNum.getStyleClass().clear();
			witnessOnePhoneNum.getStyleClass().addAll("text-field", "text-input");
			witnessOneFirstName.getStyleClass().addAll("text-field", "text-input");
			witnessOneLastName.getStyleClass().clear();
			witnessOneLastName.getStyleClass().addAll("text-field", "text-input");
		}
		if (secondWitnessCheckBox.isSelected()) {
			witnessTwoFirstName.setDisable(false);
			witnessTwoLastName.setDisable(false);
			witnessTwoPhoneNum.setDisable(false);
		} else {
			witnessTwoFirstName.setDisable(true);
			witnessTwoLastName.setDisable(true);
			witnessTwoPhoneNum.setDisable(true);
			witnessTwoFirstName.setText("");
			witnessTwoLastName.setText("");
			witnessTwoPhoneNum.setText("");
			witnessTwoFirstName.getStyleClass().clear();
			witnessTwoFirstName.getStyleClass().addAll("text-field", "text-input");
			witnessTwoLastName.getStyleClass().clear();
			witnessTwoLastName.getStyleClass().addAll("text-field", "text-input");
			witnessTwoPhoneNum.getStyleClass().clear();
			witnessTwoPhoneNum.getStyleClass().addAll("text-field", "text-input");
		}
	}

	// this allows for creation of new accident report and inserts new report to
	// the database
	public void createReport() throws ClassNotFoundException {

		String memberId = null;
		if (injuredPlayerCombo.getValue() != null && !injuredPlayerCombo.getValue().toString().isEmpty()) {
			String value = injuredPlayerCombo.getValue().toString();
			try {
				Class.forName(DRIVER);
				Connection conn = DriverManager.getConnection(dbURL, user, password);
				Statement s = conn.createStatement();
				ResultSet getDetails = s.executeQuery("SELECT memberID FROM member WHERE lastName='" + value + "'");
				while (getDetails.next()) {
					memberId = getDetails.getString("memberID");
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int gardaValue, parentValue, ambulanceValue, gpValue;
		AccidentReport report = new AccidentReport();
		report.setDateOfAccident(dateOfAccident.getValue().toString());
		report.setDateOfReport(dateOfReport.getValue().toString());
		report.setTimeOfAccident(timeOfAccident.getText());
		report.setTimeOfReport(timeOfReport.getText());
		report.setFirstName(injuredPlayerFirstName.getText());
		report.setLastName(injuredPlayerLastName.getText());
		report.setInjuredPlayerDOB(injuredPlayerDob.getText());
		report.setTeam(playersTeam.getText());
		report.setFirstAiderFirstName(firstAiderFirstName.getText());
		report.setFirstAiderLastName(firstAiderLastName.getText());
		report.setFirstAiderPhoneNumber(firstAiderPhoneNum.getText());
		report.setWitnessOneFirstName(witnessOneFirstName.getText());
		report.setWitnessOneLastName(witnessOneLastName.getText());
		report.setWitnessOnePhoneNumber(witnessOnePhoneNum.getText());
		report.setWitnessTwoFirstName(witnessTwoFirstName.getText());
		report.setWitnessTwoLastName(witnessTwoLastName.getText());
		report.setWitnessTwoPhoneNumber(witnessTwoPhoneNum.getText());
		report.setDetails(detailsOfAccidentText.getText());
		report.setActionTaken(actionTakenText.getText());
		report.setGarda(gardaCheckBox.isSelected());
		report.setGp(gpCheckBox.isSelected());
		report.setParent(parentCheckBox.isSelected());
		report.setAmbulance(ambulanceCheckBox.isSelected());
		if (gardaCheckBox.isSelected()) {
			gardaValue = 1;
		} else {
			gardaValue = 0;
		}
		if (gpCheckBox.isSelected()) {
			gpValue = 1;
		} else {
			gpValue = 0;
		}
		if (parentCheckBox.isSelected()) {
			parentValue = 1;
		} else {
			parentValue = 0;
		}
		if (ambulanceCheckBox.isSelected()) {
			ambulanceValue = 1;
		} else {
			ambulanceValue = 0;
		}
		// inserts new accident report to the database
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(dbURL, user, password);
			Statement s = conn.createStatement();
			String sql = "INSERT INTO accident_report" + "(dateOfAccident,timeOfAccident,dateOfReport,timeOfReport,"
					+ "firstAiderFirstName,firstAiderLastName,firstAiderPhoneNum,firstWitnessFirstName,firstWitnessLastName,firstWitnessPhoneNum,"
					+ "secondWitnessFirstName,secondWitnessLastName,secondWitnessPhoneNum,detailsOfAccident,actionTaken,contactedGarda,contactedAmbulance,"
					+ "contactedParentGuardian,contactedGP,coach_coachID,member_memberID)" + "values ('"
					+ dateOfAccident.getValue().toString() + "','" + timeOfAccident.getText() + "','"
					+ dateOfReport.getValue().toString() + "','" + timeOfReport.getText() + "','"
					+ firstAiderFirstName.getText() + "','" + firstAiderLastName.getText() + "','"
					+ firstAiderPhoneNum.getText() + "','" + witnessOneFirstName.getText() + "','"
					+ witnessOneLastName.getText() + "','" + witnessOnePhoneNum.getText() + "','"
					+ witnessTwoFirstName.getText() + "','" + witnessTwoLastName.getText() + "','"
					+ witnessTwoPhoneNum.getText() + "','" + detailsOfAccidentText.getText() + "','"
					+ actionTakenText.getText() + "','" + gardaValue + "','" + parentValue + "','" + gpValue + "','"
					+ ambulanceValue + "','" + LoginController.coachLoginID + "','" + memberId + "')";
			s.executeUpdate(sql);
			conn.close();
			dbUpdated();
			accidentReportTable.getItems().add(report);
			System.out.println("Updated");
		} catch (SQLException e) {
			dbNotUpdated();
			e.printStackTrace();
		}
		resetReportValues();
	}

	// checks availability of selected slot for new training,creates one if
	// available
	// or brings warning message if occupied
	public void checkExistingSchedules() {

		String firstDay = firstDayCombo.getValue().toString();
		String secondDay = secondDayCombo.getValue().toString();
		int firstDayTimeSlot = Integer.parseInt(((String) firstDayTimeSlotCombo.getValue()).substring(0, 2));
		int secondDayTimeSlot = Integer.parseInt(((String) secondDayTimeSlotCombo.getValue()).substring(0, 2));
		boolean valid = true;
		for (int i = 0; i < schedule.size(); i++) {
			int firstDayValue = Integer.parseInt(schedule.get(i).getFirstDayTimeSlot().substring(0, 2));
			int secondDayValue = Integer.parseInt(schedule.get(i).getSecondDayTimeSlot().substring(0, 2));

			if (firstDayTimeSlot == firstDayValue && schedule.get(i).getFirstDay().equals(firstDay)
					&& secondDayTimeSlot == secondDayValue && schedule.get(i).getSecondDay().equals(secondDay)) {
				bothTimeSlotsOccupied();
				valid = false;

			} else if (firstDayTimeSlot == firstDayValue && schedule.get(i).getFirstDay().equals(firstDay)) {
				firstTimeSlotOccupied();
				valid = false;
			} else if (secondDayTimeSlot == secondDayValue && schedule.get(i).getSecondDay().equals(secondDay)) {
				secondTimeSlotOccupied();
				valid = false;
			}
		}
		if (valid) {
			createSchedule();
		}

	}

	// this method allow for creating new training schedule and inserts new
	// record to database
	public void createSchedule() {
		String teamId = null;
		if (scheduleSelectTeamCombo.getValue() != null && !scheduleSelectTeamCombo.getValue().toString().isEmpty()) {
			String value = scheduleSelectTeamCombo.getValue().toString();
			try {
				Class.forName(DRIVER);
				Connection conn = DriverManager.getConnection(dbURL, user, password);
				Statement s = conn.createStatement();
				ResultSet getDetails = s.executeQuery("SELECT teamID FROM team WHERE teamName='" + value + "'");
				while (getDetails.next()) {
					teamId = getDetails.getString("teamID");
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (scheduleSelectTeamCombo.getValue() != null && seasonCombo.getValue() != null
				&& firstDayCombo.getValue() != null && secondDayCombo.getValue() != null
				&& firstDayTimeSlotCombo.getValue() != null && secondDayTimeSlotCombo.getValue() != null) {
			TrainingSchedule trainingSchedule = new TrainingSchedule();
			trainingSchedule.setSeason(seasonCombo.getValue().toString());
			trainingSchedule.setTeam(scheduleSelectTeamCombo.getValue().toString());
			trainingSchedule.setFirstDay(firstDayCombo.getValue().toString());
			trainingSchedule.setSecondDay(secondDayCombo.getValue().toString());
			trainingSchedule.setFirstDayTimeSlot(firstDayTimeSlotCombo.getValue().toString());
			trainingSchedule.setSecondDayTimeSlot(secondDayTimeSlotCombo.getValue().toString());

			try {
				Class.forName(DRIVER);
				Connection conn = DriverManager.getConnection(dbURL, user, password);
				Statement s = conn.createStatement();
				String sql = "INSERT INTO training_schedule" + "(season,firstDay,firstDayTimeSlot,secondDay,"
						+ "secondDayTimeSlot,team_teamID)" + " values ('" + seasonCombo.getValue().toString() + "','"
						+ firstDayCombo.getValue().toString() + "','" + firstDayTimeSlotCombo.getValue().toString()
						+ "','" + secondDayCombo.getValue().toString() + "','"
						+ secondDayTimeSlotCombo.getValue().toString() + "','" + teamId + "')";
				s.executeUpdate(sql);
				conn.close();
				dbUpdated();
				System.out.println("Updated");
				scheduleTable.getItems().add(trainingSchedule);
			} catch (SQLException | ClassNotFoundException e) {
				dbNotUpdated();
				e.printStackTrace();
			}
		}
		resetScheduleValues();
	}

	// allows for creating attendance for particular player and inserts new
	// record to the database
	public void createAttendance() throws ClassNotFoundException {
		String memberId = null;
		int presentValue, excusedValue;
		if (attendanceSelectPlayerCombo.getValue() != null
				&& !attendanceSelectPlayerCombo.getValue().toString().isEmpty()) {
			String value = attendanceSelectPlayerCombo.getValue().toString();
			try {
				Class.forName(DRIVER);
				Connection conn = DriverManager.getConnection(dbURL, user, password);
				Statement s = conn.createStatement();
				ResultSet getDetails = s.executeQuery("SELECT memberID FROM member WHERE lastName='" + value + "'");
				while (getDetails.next()) {
					memberId = getDetails.getString("memberID");
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Attendance attendance = new Attendance();
		attendance.setDate(dateOfAttendance.getValue().toString());
		attendance.setPresent(presentCheckBox.isSelected());
		attendance.setExcused(excusedCheckBox.isSelected());
		if (presentCheckBox.isSelected()) {
			presentValue = 1;
		} else {
			presentValue = 0;
		}
		if (excusedCheckBox.isSelected()) {
			excusedValue = 1;
		} else {
			excusedValue = 0;
		}
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(dbURL, user, password);
			Statement s = conn.createStatement();
			String sql = "INSERT INTO attendance" + "(date,present,excused,member_memberID)" + " VALUES ('"
					+ dateOfAttendance.getValue().toString() + "','" + presentValue + "','" + excusedValue + "','"
					+ memberId + "')";
			s.executeUpdate(sql);
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		resetAttendanceValues();

	}

	// notifies that both selected training time slots is occupied already
	public void bothTimeSlotsOccupied() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Both Time Slots Are Occupied");
		alert.setHeaderText("Both selected slots are occupied!");
		alert.setContentText("Please select another time slot!");
		alert.showAndWait();
	}

	// notifies that selected training time slot is occupied already
	public void firstTimeSlotOccupied() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("First Time Slot Occupied");
		alert.setHeaderText("Selected first slot is occupied!");
		alert.setContentText("Please select another time slot!");
		alert.showAndWait();
	}

	// notifies that selected training time slot is occupied already
	public void secondTimeSlotOccupied() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Second Time Slot Occupied");
		alert.setHeaderText("Selected Second Slot is Occupied!");
		alert.setContentText("Please select another time slot!");
		alert.showAndWait();
	}

	// notifies when database has been updated
	public void dbUpdated() {
		Alert success = new Alert(AlertType.INFORMATION);
		success.setTitle("Info");
		success.setHeaderText("Success!");
		success.setContentText("New record created.");
		success.showAndWait();
	}

	// notifies when updating database failed
	public void dbNotUpdated() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Failure! Data base not updated!");
		alert.setContentText("Please check your internet connection!");
		alert.showAndWait();
	}

	// Notifies when there is no data in this period of selected time
	public void dataNotFound() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText("Data not found from selected date range!");
		alert.setContentText("Please select different date range!");
		alert.showAndWait();
	}

	// checks if user selected row in the table
	public void noItemSelected() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("No Selection");
		alert.setHeaderText("No item selected");
		alert.setContentText("Please select an item in the table!");
		alert.showAndWait();
	}

	// allows for deleting Training Schedule from database after previous
	// confirmation
	public void deleteSchedule() {
		ObservableList<TrainingSchedule> scheduleSelected, schedules;
		schedules = scheduleTable.getItems();
		scheduleSelected = scheduleTable.getSelectionModel().getSelectedItems();
		int selectedIndex = scheduleTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			getSelectedSchedule();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			ButtonType yesType = new ButtonType("Yes");
			ButtonType noType = new ButtonType("No");
			alert.getButtonTypes().setAll(yesType, noType);
			alert.setTitle("Confirmation");
			alert.setHeaderText("You are about to delete selected schedule!");
			alert.setContentText("Are you sure?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == yesType) {
				try {
					Class.forName(DRIVER);
					Connection conn = DriverManager.getConnection(dbURL, user, password);
					Statement s = conn.createStatement();
					String sql = "DELETE FROM training_schedule WHERE (scheduleID='" + scheduleId + "')";
					s.executeUpdate(sql);
					conn.close();
					System.out.println("Record deleted!");
				} catch (Exception e) {
					System.out.println("Record not deleted!");
					e.printStackTrace();
				}
				scheduleSelected.forEach(schedules::remove);
				scheduleTable.getSelectionModel().clearSelection();

			} else {
			}

		} else {
			noItemSelected();
		}
	}

	// allows for deleting Accident Report from database after previous
	// confirmation
	public void deleteReport() {
		ObservableList<AccidentReport> reportSelected, allReports;
		allReports = accidentReportTable.getItems();
		reportSelected = accidentReportTable.getSelectionModel().getSelectedItems();
		int selectedIndex = accidentReportTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			getSelectedAccidentReport();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			ButtonType yesType = new ButtonType("Yes");
			ButtonType noType = new ButtonType("No");
			alert.getButtonTypes().setAll(yesType, noType);
			alert.setTitle("Confirmation");
			alert.setHeaderText("You area about to delete selected report!");
			alert.setContentText("Are you sure?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == yesType) {
				try {
					Class.forName(DRIVER);
					Connection conn = DriverManager.getConnection(dbURL, user, password);
					Statement s = conn.createStatement();
					String sql = "DELETE FROM accident_report WHERE (reportID='" + reportId + "')";
					s.executeUpdate(sql);
					conn.close();
					System.out.println("Record deleted!");
				} catch (Exception e) {
					System.out.println("Record not deleted!");
					e.printStackTrace();
				}
				reportSelected.forEach(allReports::remove);
				showReportDetails(null);
				accidentReportTable.getSelectionModel().clearSelection();

			} else {
			}

		} else {
			noItemSelected();
		}
	}

	// gets selected row from Accident Report table
	@SuppressWarnings("rawtypes")
	public void getSelectedAccidentReport() {
		TablePosition pos = accidentReportTable.getSelectionModel().getSelectedCells().get(0);
		int row = pos.getRow();
		AccidentReport item = accidentReportTable.getItems().get(row);
		reportId = (Integer) idCol.getCellObservableValue(item).getValue();
	}

	// gets selected row from schedule table
	@SuppressWarnings("rawtypes")
	public void getSelectedSchedule() {
		TablePosition pos = scheduleTable.getSelectionModel().getSelectedCells().get(0);
		int row = pos.getRow();
		// Item here is the table view type:
		TrainingSchedule item = scheduleTable.getItems().get(row);
		scheduleId = (Integer) scheduleIdCol.getCellObservableValue(item).getValue();
	}

	// loads Accident Reports from database to table
	public void loadAccidentReport() {
		try {

			getData = new AccessData(DRIVER, dbURL, user, password);
		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
		idCol.setCellValueFactory(new PropertyValueFactory<>("reportId"));
		dateOfAccidentCol.setCellValueFactory(new PropertyValueFactory<>("dateOfAccident"));
		timeOfAccidentCol.setCellValueFactory(new PropertyValueFactory<>("timeOfAccident"));
		dateOfReportCol.setCellValueFactory(new PropertyValueFactory<>("dateOfReport"));
		timeOfReportCol.setCellValueFactory(new PropertyValueFactory<>("timeOfReport"));
		fNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		playerDobCol.setCellValueFactory(new PropertyValueFactory<>("injuredPlayerDOB"));
		teamCol.setCellValueFactory(new PropertyValueFactory<>("team"));
		firstAiderFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstAiderFirstName"));
		firstAiderLastNameCol.setCellValueFactory(new PropertyValueFactory<>("firstAiderLastName"));
		firstAiderPhoneNumCol.setCellValueFactory(new PropertyValueFactory<>("firstAiderPhoneNum"));
		witnessOneFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstWitnessFirstName"));
		witnessOneLastNameCol.setCellValueFactory(new PropertyValueFactory<>("firstWitnessLastName"));
		witnessOnePhoneNumCol.setCellValueFactory(new PropertyValueFactory<>("firstWitnessPhoneNum"));
		witnessTwoFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("secondWitnessFirstName"));
		witnessTwoLastNameCol.setCellValueFactory(new PropertyValueFactory<>("secondWitnessLastName"));
		witnessTwoPhoneNumCol.setCellValueFactory(new PropertyValueFactory<>("secondWitnessPhoneNum"));
		detailsOfAccidentCol.setCellValueFactory(new PropertyValueFactory<>("detailsOfAccident"));
		actionTakenCol.setCellValueFactory(new PropertyValueFactory<>("actionTaken"));
		gardaCol.setCellValueFactory(new PropertyValueFactory<>("contactedGarda"));
		gpCol.setCellValueFactory(new PropertyValueFactory<>("contactedGP"));
		parentGuardianCol.setCellValueFactory(new PropertyValueFactory<>("contactedParentGuardian"));
		ambulanceCol.setCellValueFactory(new PropertyValueFactory<>("contactedAmbulance"));
		coachIdCol.setCellValueFactory(new PropertyValueFactory<>("coachID"));

		showReportDetails(null);
		accidentReportTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showReportDetails(newValue));

		try {
			accidentReportTable.getItems().addAll(getData.getReportList());
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	// loads training schedule from database to table view
	public void loadSchedule() {
		try {

			getData = new AccessData(DRIVER, dbURL, user, password);
		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
		scheduleIdCol.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
		seasonCol.setCellValueFactory(new PropertyValueFactory<>("season"));
		teamNameCol.setCellValueFactory(new PropertyValueFactory<>("team"));
		firstDayCol.setCellValueFactory(new PropertyValueFactory<>("firstDay"));
		firstDayTimeSlotCol.setCellValueFactory(new PropertyValueFactory<>("firstDayTimeSlot"));
		secondDayCol.setCellValueFactory(new PropertyValueFactory<>("secondDay"));
		secondDayTimeSlotCol.setCellValueFactory(new PropertyValueFactory<>("secondDayTimeSlot"));
		teamIdCol.setCellValueFactory(new PropertyValueFactory<>("Teams_teamID"));
		try {
			scheduleTable.getItems().addAll(getData.getScheduleList());
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	// gets player names from database
	@SuppressWarnings("unchecked")
	public void getPlayerName() {

		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(dbURL, user, password);
			Statement s = conn.createStatement();
			ResultSet getName = s.executeQuery("SELECT m.lastName from member m,team t,member_has_team ms "
					+ " WHERE m.memberID = ms.member_memberID AND t.teamID = ms.team_teamID"
					+ " AND t.coach_coachID = '" + LoginController.coachLoginID + "'");

			while (getName.next()) {
				String playerName = getName.getString("lastName");
				injuredPlayerCombo.getItems().addAll(playerName);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Gets teams names from database but only teams trained by logged coach
	@SuppressWarnings("unchecked")
	public void getTeam() {
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(dbURL, user, password);
			Statement s = conn.createStatement();
			ResultSet getTeamName = s.executeQuery(
					"SELECT teamName from team where coach_coachID = " + "'" + LoginController.coachLoginID + "'");

			while (getTeamName.next()) {
				String teamName = getTeamName.getString("teamName");
				scheduleSelectTeamCombo.getItems().addAll(teamName);
				attendanceSelectTeamCombo.getItems().addAll(teamName);
				statisticsSelectTeamCombo.getItems().addAll(teamName);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// gets players details from data base according to the team selected
	// and loads them to combobox
	@SuppressWarnings({ "unchecked" })
	public void getTeamMembersForAttendance() {
		String teamId = null;
		attendanceSelectPlayerCombo.setDisable(false);
		if (attendanceSelectTeamCombo.getValue() != null
				&& !attendanceSelectTeamCombo.getValue().toString().isEmpty()) {
			String value = attendanceSelectTeamCombo.getValue().toString();
			attendanceSelectPlayerCombo.getSelectionModel().clearSelection();
			attendanceSelectPlayerCombo.getItems().clear();
			try {
				Class.forName(DRIVER);
				Connection conn = DriverManager.getConnection(dbURL, user, password);
				Statement s = conn.createStatement();
				ResultSet getDetails = s.executeQuery("SELECT teamID FROM team WHERE teamName='" + value + "'"
						+ " AND coach_coachID = '" + LoginController.coachLoginID + "'");
				while (getDetails.next()) {
					teamId = getDetails.getString("teamID");
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Class.forName(DRIVER);
				Connection conn = DriverManager.getConnection(dbURL, user, password);
				Statement s = conn.createStatement();
				ResultSet getMembers = s
						.executeQuery("SELECT m.memberID,m.lastname,t.teamID,tc.member_memberID,tc.team_teamID "
								+ "FROM member m, team t,member_has_team tc " + "WHERE t.teamID='" + teamId
								+ "' AND m.memberID = tc.member_memberID AND tc.team_teamID = t.teamID"
								+ " AND t.coach_coachID = '" + LoginController.coachLoginID + "'");
				while (getMembers.next()) {
					String playerName = getMembers.getString("lastName");
					attendanceSelectPlayerCombo.getItems().addAll(playerName);
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// gets teams names from data base and loads them to combobox
	@SuppressWarnings({ "unchecked" })
	public void getTeamMembersForStatistics() {
		String teamId = null;
		statisticsSelectPlayerCombo.setDisable(false);
		if (statisticsSelectTeamCombo.getValue() != null
				&& !statisticsSelectTeamCombo.getValue().toString().isEmpty()) {
			String value = statisticsSelectTeamCombo.getValue().toString();
			statisticsSelectPlayerCombo.getSelectionModel().clearSelection();
			statisticsSelectPlayerCombo.getItems().clear();
			try {
				Class.forName(DRIVER);
				Connection conn = DriverManager.getConnection(dbURL, user, password);
				Statement s = conn.createStatement();
				ResultSet getDetails = s.executeQuery("SELECT teamID FROM team WHERE teamName='" + value + "'"
						+ " AND coach_coachID = '" + LoginController.coachLoginID + "'");
				while (getDetails.next()) {
					teamId = getDetails.getString("teamID");
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Class.forName(DRIVER);
				Connection conn = DriverManager.getConnection(dbURL, user, password);
				Statement s = conn.createStatement();
				ResultSet getMembers = s
						.executeQuery("SELECT m.memberID,m.lastname,t.teamID,tc.member_memberID,tc.team_teamID "
								+ "FROM member m, team t,member_has_team tc " + "WHERE t.teamID='" + teamId
								+ "' AND m.memberID = tc.member_memberID AND tc.team_teamID = t.teamID "
								+ " AND t.coach_coachID = '" + LoginController.coachLoginID + "'");
				while (getMembers.next()) {
					String playerName = getMembers.getString("lastName");
					statisticsSelectPlayerCombo.getItems().addAll(playerName);
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// gets players details from data base according to the team selected
	// and loads them to combobox
	public void getPlayerDetails() {
		if (injuredPlayerCombo.getValue() != null && !injuredPlayerCombo.getValue().toString().isEmpty()) {
			enablePlayerFields();
			String value = injuredPlayerCombo.getValue().toString();
			try {
				Class.forName(DRIVER);
				Connection conn = DriverManager.getConnection(dbURL, user, password);
				Statement s = conn.createStatement();
				ResultSet getDetails = s.executeQuery("SELECT m.memberID,m.firstName,m.lastname,m.dob,t.teamName "
						+ "FROM member m, member_has_team tc, team t " + "WHERE m.lastName='" + value
						+ "' AND m.memberID = tc.member_memberID AND tc.team_teamID = t.teamID "
						+ " AND t.coach_coachID = '" + LoginController.coachLoginID + "'");
				while (getDetails.next()) {
					// Integer memberId = getDetails.getInt("memberId");
					String firstName = getDetails.getString("firstName");
					String lastName = getDetails.getString("lastName");
					String dob = getDetails.getString("dob");
					String team = getDetails.getString("teamName");
					injuredPlayerFirstName.setText(firstName);
					injuredPlayerLastName.setText(lastName);
					injuredPlayerDob.setText(dob);
					playersTeam.setText(team);
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		enableSaveReportBtn();
	}

	// enables create attendance button when required data is provided
	public void enableAttendanceButtons() {
		if (attendanceSelectTeamCombo.getValue() != null && !attendanceSelectTeamCombo.getValue().toString().isEmpty()
				&& attendanceSelectPlayerCombo.getValue() != null
				&& !attendanceSelectPlayerCombo.getValue().toString().isEmpty()) {
			clearAttendanceBtn.setDisable(false);
			saveAttendanceBtn.setDisable(false);
		} else {
			clearAttendanceBtn.setDisable(true);
			saveAttendanceBtn.setDisable(true);
		}

	}

	// enables statistic button when required data is provided
	public void enableStatisticsButtons() {
		if (statisticsStartDate.getValue() != null && statisticsEndDate.getValue() != null
				&& statisticsSelectTeamCombo.getValue() != null && statisticsSelectPlayerCombo.getValue() != null) {
			chart.getData().clear();
			attendance.setSelected(false);
			absence.setSelected(false);
			viewStatisticsBtn.setDisable(false);
			attendFirstNameResultLbl.setText("-");
			attendLastNameResultLbl.setText("-");
			attendStartDateResultLbl.setText("-");
			attendEndDateResultLbl.setText("-");
			attendTeamNameResultLbl.setText("-");
			attendPercentageResultLbl.setText("-");
			attendExcusedResultLbl.setText("-");
			attendMissedResultLbl.setText("-");
			conductedTrainingsResultLbl.setText("-");
			attendedTrainingsResultLbl.setText("-");
			clearStatisticResultBtn.setDisable(true);
		} else {
			viewStatisticsBtn.setDisable(true);
		}

	}

	// enables end date date picker
	public void enableEndDate() {
		if (statisticsStartDate.getValue() != null) {
			statisticsEndDate.setDisable(false);
		} else {
			statisticsEndDate.setDisable(true);
		}
	}

	// enables save schedule button when all required fields are selected
	public void enableSaveScheduleBtn() {
		if (scheduleSelectTeamCombo.getValue() != null && seasonCombo.getValue() != null
				&& firstDayCombo.getValue() != null && secondDayCombo.getValue() != null
				&& firstDayTimeSlotCombo.getValue() != null && secondDayTimeSlotCombo.getValue() != null) {
			saveScheduleBtn.setDisable(false);
			clearScheduleBtn.setDisable(false);

		} else {
			saveScheduleBtn.setDisable(true);
			clearScheduleBtn.setDisable(true);
		}

	}

	// validates user entry and enables create report button when valid
	public void enableSaveReportBtn() {
		boolean validForm = true;
		if (detailsOfAccidentText.getText().length() > 1000 || detailsOfAccidentText.getText().isEmpty()) {
			validForm = false;
		} else if (actionTakenText.getText().length() > 1000 || actionTakenText.getText().isEmpty()) {
			validForm = false;
		} else if (!firstAiderPhoneNum.getText().matches("08[35679]\\d{7}")) {
			validForm = false;
		} else if (!timeOfAccident.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
			validForm = false;
		} else if (!timeOfReport.getText().matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
			validForm = false;
		}

		else if (!firstAiderFirstName.getText().matches("[A-Za-z\\s]+") || firstAiderFirstName.getText().isEmpty()) {
			validForm = false;
		} else if (!firstAiderLastName.getText().matches("[A-Za-z\\s]+") || firstAiderLastName.getText().isEmpty()) {
			validForm = false;
		} else if (firstWitnessCheckBox.isSelected()) {
			if (!witnessOneFirstName.getText().matches("[A-Za-z\\s]+") || witnessOneFirstName.getText().isEmpty()) {
				validForm = false;
			}
			if (!witnessOneLastName.getText().matches("[A-Za-z\\s]+") || witnessOneLastName.getText().isEmpty()) {
				validForm = false;
			}
			if (!witnessOnePhoneNum.getText().matches("08[35679]\\d{7}")) {
				validForm = false;
			}
		}

		else if (secondWitnessCheckBox.isSelected()) {
			if (!witnessTwoFirstName.getText().matches("[A-Za-z\\s]+") || witnessTwoFirstName.getText().isEmpty()) {
				validForm = false;
			}
			if (!witnessTwoLastName.getText().matches("[A-Za-z\\s]+") || witnessTwoLastName.getText().isEmpty()) {
				validForm = false;
			}
			if (!witnessTwoPhoneNum.getText().matches("08[35679]\\d{7}")) {
				validForm = false;
			}
		}
		if (validForm) {
			saveReportBtn.setDisable(false);
		} else {
			saveReportBtn.setDisable(true);
		}

	}

	// enables clear button to see new statistics
	public void enableClearStatisticsResultBtn() {
		if (!attendFirstNameResultLbl.getText().equals("-"))
			clearStatisticResultBtn.setDisable(false);
	}

	// enables fields when checkbox is checked
	public void enablePlayerFields() {
		if (injuredPlayerCombo.getValue() != null) {
			injuredPlayerFirstName.setDisable(false);
			injuredPlayerLastName.setDisable(false);
			injuredPlayerDob.setDisable(false);
			playersTeam.setDisable(false);
		} else {
			injuredPlayerFirstName.setDisable(true);
			injuredPlayerLastName.setDisable(true);
			injuredPlayerDob.setDisable(true);
			playersTeam.setDisable(true);
		}
	}

}
