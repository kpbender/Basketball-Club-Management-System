package coach.management;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//class to set properties for data from database so record can be displayed in table view
public class AccidentReport {

	// constructor of the class takes all parameters from database send from
	// data access class
	public AccidentReport(Integer reportId, String dateOfAccident, String timeOfAccident, String dateOfReport,
			String timeOfReport, String firstName, String lastName, String injuredPlayerDOB, String team,
			String firstAiderFirstName, String firstAiderLastName, String firstAiderPhoneNum,
			String witnessOneFirstName, String witnessOneLastName, String witnessOnePhoneNumber,
			String witnessTwoFirstName, String witnessTwoLastName, String witnessTwoPhoneNumber, String details,
			String actionTaken, Boolean garda, Boolean gp, Boolean parent, Boolean ambulance, Integer coachId,
			Integer memberId) {

		setReportID(reportId);
		setDateOfAccident(dateOfAccident);
		setTimeOfAccident(timeOfAccident);
		setDateOfReport(dateOfReport);
		setTimeOfReport(timeOfReport);
		setFirstName(firstName);
		setLastName(lastName);
		setInjuredPlayerDOB(injuredPlayerDOB);
		setTeam(team);
		setFirstAiderFirstName(firstAiderFirstName);
		setFirstAiderLastName(firstAiderLastName);
		setFirstAiderPhoneNumber(firstAiderPhoneNum);
		setWitnessOneFirstName(witnessOneFirstName);
		setWitnessOneLastName(witnessOneLastName);
		setWitnessOnePhoneNumber(witnessOnePhoneNumber);
		setWitnessTwoFirstName(witnessTwoFirstName);
		setWitnessTwoLastName(witnessTwoLastName);
		setWitnessTwoPhoneNumber(witnessTwoPhoneNumber);
		setActionTaken(actionTaken);
		setDetails(details);
		setGarda(garda);
		setGp(gp);
		setParent(parent);
		setAmbulance(ambulance);
		setCoachId(coachId);
		setMemberId(memberId);

	}

	public AccidentReport() {
	}
	// sets integer property for report id for every record in table
	private final IntegerProperty reportId = new SimpleIntegerProperty(this, "reportID");

	public IntegerProperty idProperty() {
		return reportId;
	}

	public final Integer getReportId() {
		return idProperty().get();
	}

	public final void setReportID(Integer reportId) {
		idProperty().set(reportId);
	}

	// sets String property for date of accident for every record in table
	private final StringProperty dateOfAccident = new SimpleStringProperty(this, "dateOfAccident");

	public StringProperty dateOfAccidentProperty() {
		return dateOfAccident;
	}

	public final String getDateOfAccident() {
		return dateOfAccidentProperty().get();
	}

	public final void setDateOfAccident(String dateOfAccident) {
		dateOfAccidentProperty().set(dateOfAccident);
	}

	// sets String property for time of accident for every record in table
	private final StringProperty timeOfAccident = new SimpleStringProperty(this, "timeOfAccident");

	public StringProperty timeOfAccidentProperty() {
		return timeOfAccident;
	}

	public final String getTimeOfAccident() {
		return timeOfAccidentProperty().get();
	}

	public final void setTimeOfAccident(String timeOfAccident) {
		timeOfAccidentProperty().set(timeOfAccident);
	}

	// sets String property for date of report for every record in table
	private final StringProperty dateOfReport = new SimpleStringProperty(this, "dateOfReport");

	public StringProperty dateOfReportProperty() {
		return dateOfReport;
	}

	public final String getDateOfReport() {
		return dateOfReportProperty().get();
	}

	public final void setDateOfReport(String dateOfReport) {
		dateOfReportProperty().set(dateOfReport);
	}

	// sets String property for time of report for every record in table
	private final StringProperty timeOfReport = new SimpleStringProperty(this, "timeOfReport");

	public StringProperty timeOfReportProperty() {
		return timeOfReport;
	}

	public final String getTimeOfReport() {
		return timeOfReportProperty().get();
	}

	public final void setTimeOfReport(String timeOfReport) {
		timeOfReportProperty().set(timeOfReport);
	}

	// sets String property for first name of injured person for every record in
	// table
	private final StringProperty firstName = new SimpleStringProperty(this, "firstName");

	public StringProperty firstNameProperty() {
		return firstName;
	}

	public final String getFirstName() {
		return firstNameProperty().get();
	}

	public final void setFirstName(String firstName) {
		firstNameProperty().set(firstName);
	}

	// sets String property for last name of injured person for every record in
	// table
	private final StringProperty lastName = new SimpleStringProperty(this, "lastName");

	public StringProperty lastNameProperty() {
		return lastName;
	}

	public final String getLastName() {
		return lastNameProperty().get();
	}

	public final void setLastName(String lastName) {
		lastNameProperty().set(lastName);
	}

	// sets String property for dob of injured person for every record in table
	private final StringProperty injuredPlayerDOB = new SimpleStringProperty(this, "dob");

	public StringProperty injuredPlayerDOBProperty() {
		return injuredPlayerDOB;
	}

	public final String getInjuredPlayerDOB() {
		return injuredPlayerDOBProperty().get();
	}

	public final void setInjuredPlayerDOB(String injuredPlayerDOB) {
		injuredPlayerDOBProperty().set(injuredPlayerDOB);
	}

	// sets String property for team name for every record in table
	private final StringProperty team = new SimpleStringProperty(this, "teamName");

	public StringProperty teamProperty() {
		return team;
	}

	public final String getTeam() {
		return teamProperty().get();
	}

	public final void setTeam(String team) {
		teamProperty().set(team);
	}

	// sets String property for first name of First Aider for every record in
	// table
	private final StringProperty firstAiderFirstName = new SimpleStringProperty(this, "firstAiderFirstName");

	public StringProperty firstAiderFirstNameProperty() {
		return firstAiderFirstName;
	}

	public final String getFirstAiderFirstName() {
		return firstAiderFirstNameProperty().get();
	}

	public final void setFirstAiderFirstName(String firstAiderFirstName) {
		firstAiderFirstNameProperty().set(firstAiderFirstName);
	}

	// sets String property for last name of First Aider for every record in
	// table
	private final StringProperty firstAiderLastName = new SimpleStringProperty(this, "firstAiderLastName");

	public StringProperty firstAiderLastNameProperty() {
		return firstAiderLastName;
	}

	public final String getFirstAiderLastName() {
		return firstAiderLastNameProperty().get();
	}

	public final void setFirstAiderLastName(String firstAiderLastName) {
		firstAiderLastNameProperty().set(firstAiderLastName);
	}

	// sets String property for phone of First Aider for every record in table
	private final StringProperty firstAiderPhoneNumber = new SimpleStringProperty(this, "firstAiderPhoneNum");

	public StringProperty firstAiderPhoneNumberProperty() {
		return firstAiderPhoneNumber;
	}

	public final String getFirstAiderPhoneNumber() {
		return firstAiderPhoneNumberProperty().get();
	}

	public final void setFirstAiderPhoneNumber(String firstAiderPhoneNumber) {
		firstAiderPhoneNumberProperty().set(firstAiderPhoneNumber);
	}

	// sets String property for witness first name for every record in table
	private final StringProperty witnessOneFirstName = new SimpleStringProperty(this, "firstWitnessFirstName");

	public StringProperty witnessOneFirstNameProperty() {
		return witnessOneFirstName;
	}

	public final String getWitnessOneFirstName() {
		return witnessOneFirstNameProperty().get();
	}

	public final void setWitnessOneFirstName(String witnessOneFirstName) {
		witnessOneFirstNameProperty().set(witnessOneFirstName);
	}

	// sets String property for witness last name for every record in table
	private final StringProperty witnessOneLastName = new SimpleStringProperty(this, "firstWitnessLastName");

	public StringProperty witnessOneLastNameProperty() {
		return witnessOneLastName;
	}

	public final String getWitnessOneLastName() {
		return witnessOneLastNameProperty().get();
	}

	public final void setWitnessOneLastName(String witnessOneLastName) {
		witnessOneLastNameProperty().set(witnessOneLastName);
	}

	// sets String property for witness phone for every record in table
	private final StringProperty witnessOnePhoneNumber = new SimpleStringProperty(this, "firstWitnessPhoneNum");

	public StringProperty witnessOnePhoneNumberProperty() {
		return witnessOnePhoneNumber;
	}

	public final String getWitnessOnePhoneNumber() {
		return witnessOnePhoneNumberProperty().get();
	}

	public final void setWitnessOnePhoneNumber(String witnessOnePhoneNumber) {
		witnessOnePhoneNumberProperty().set(witnessOnePhoneNumber);
	}

	// sets String property for witness 2 first name for every record in table
	private final StringProperty witnessTwoFirstName = new SimpleStringProperty(this, "secondWitnessFirstName");

	public StringProperty witnessTwoFirstNameProperty() {
		return witnessTwoFirstName;
	}

	public final String getWitnessTwoFirstName() {
		return witnessTwoFirstNameProperty().get();
	}

	public final void setWitnessTwoFirstName(String witnessTwoFirstName) {
		witnessTwoFirstNameProperty().set(witnessTwoFirstName);
	}

	// sets String property for witness 2 last name for every record in table
	private final StringProperty witnessTwoLastName = new SimpleStringProperty(this, "secondWitnessLastName");

	public StringProperty witnessTwoLastNameProperty() {
		return witnessTwoLastName;
	}

	public final String getWitnessTwoLastName() {
		return witnessTwoLastNameProperty().get();
	}

	public final void setWitnessTwoLastName(String witnessTwoLastName) {
		witnessTwoLastNameProperty().set(witnessTwoLastName);
	}

	// sets String property for witness 2 phone for every record in table
	private final StringProperty witnessTwoPhoneNumber = new SimpleStringProperty(this, "secondWitnessPhoneNum");

	public StringProperty witnessTwoPhoneNumberProperty() {
		return witnessTwoPhoneNumber;
	}

	public final String getWitnessTwoPhoneNumber() {
		return witnessTwoPhoneNumberProperty().get();
	}

	public final void setWitnessTwoPhoneNumber(String witnessTwoPhoneNumber) {
		witnessTwoPhoneNumberProperty().set(witnessTwoPhoneNumber);
	}

	// sets String property for details of the report for every record in table
	private final StringProperty details = new SimpleStringProperty(this, "detailsOfAccident");

	public StringProperty detailsProperty() {
		return details;
	}

	public final String getDetails() {
		return detailsProperty().get();
	}

	public final void setDetails(String details) {
		detailsProperty().set(details);
	}

	// sets String property for action taken during accident for every record in
	// table
	private final StringProperty actionTaken = new SimpleStringProperty(this, "actionTaken");

	public StringProperty actionTakenProperty() {
		return actionTaken;
	}

	public final String getActionTaken() {
		return actionTakenProperty().get();
	}

	public final void setActionTaken(String actionTaken) {
		actionTakenProperty().set(actionTaken);
	}

	// sets Boolean property if garda was required for every record in table
	private final BooleanProperty garda = new SimpleBooleanProperty(this, "garda");

	public BooleanProperty gardaProperty() {
		return garda;
	}

	public final Boolean getGarda() {
		return gardaProperty().get();
	}

	public final void setGarda(Boolean garda) {
		gardaProperty().set(garda);
	}

	// sets Boolean property if gp was required for every record in table
	private final BooleanProperty gp = new SimpleBooleanProperty(this, "gp");

	public BooleanProperty gpProperty() {
		return gp;
	}

	public final Boolean getGp() {
		return gpProperty().get();
	}

	public final void setGp(Boolean gp) {
		gpProperty().set(gp);
	}

	// sets boolean property for parent for every record in table
	private final BooleanProperty parent = new SimpleBooleanProperty(this, "parentGuardian");

	public BooleanProperty parentProperty() {
		return parent;
	}

	public final Boolean getParent() {
		return parentProperty().get();
	}

	public final void setParent(Boolean parent) {
		parentProperty().set(parent);
	}

	// sets boolean property for ambulance for every record in table
	private final BooleanProperty ambulance = new SimpleBooleanProperty(this, "ambulance");

	public BooleanProperty ambulanceProperty() {
		return ambulance;
	}

	public final Boolean getAmbulance() {
		return ambulanceProperty().get();
	}

	public final void setAmbulance(Boolean ambulance) {
		ambulanceProperty().set(ambulance);
	}

	// sets Integer property for coach Id for every record in table
	private final IntegerProperty coachId = new SimpleIntegerProperty(this, "coach_coachID");

	public IntegerProperty coachIdProperty() {
		return coachId;
	}

	public final Integer getCoachId() {
		return coachIdProperty().get();
	}

	public final void setCoachId(Integer coachId) {
		coachIdProperty().set(coachId);
	}

	// sets Integer property for member Id for every record in table
	private final IntegerProperty memberId = new SimpleIntegerProperty(this, "member_memberID");

	public IntegerProperty memberIdProperty() {
		return memberId;
	}

	public final Integer getMemberId() {
		return memberIdProperty().get();
	}

	public final void setMemberId(Integer memberId) {
		memberIdProperty().set(memberId);
	}

}
