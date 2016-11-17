package coach.management;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TrainingSchedule {
	public TrainingSchedule(Integer scheduleId, String season, String team, String firstDay, String firstDayTimeSlot,
			String secondDay, String secondDayTimeSlot, Integer teamId) {

		setScheduleId(scheduleId);
		setSeason(season);
		setTeam(team);
		setFirstDay(firstDay);
		setFirstDayTimeSlot(firstDayTimeSlot);
		setSecondDay(secondDay);
		setSecondDayTimeSlot(secondDayTimeSlot);
		setTeamId(teamId);
	}

	public TrainingSchedule() {
	}

	// sets integer property for schedule id for every record in table
	private final IntegerProperty scheduleId = new SimpleIntegerProperty(this, "scheduleID");

	public IntegerProperty idProperty() {
		return scheduleId;
	}

	public final Integer getScheduleId() {
		return idProperty().get();
	}

	public final void setScheduleId(Integer scheduleId) {
		idProperty().set(scheduleId);
	}

	// sets String property for season for every record in table
	private final StringProperty season = new SimpleStringProperty(this, "season");

	public StringProperty seasonProperty() {
		return season;
	}

	public final String getSeason() {
		return seasonProperty().get();
	}

	public final void setSeason(String season) {
		seasonProperty().set(season);
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

	// sets String property for first day of training for every record in table
	private final StringProperty firstDay = new SimpleStringProperty(this, "firstDay");

	public StringProperty firstDayProperty() {
		return firstDay;
	}

	public final String getFirstDay() {
		return firstDayProperty().get();
	}

	public final void setFirstDay(String firstDay) {
		firstDayProperty().set(firstDay);
	}

	// sets String property for first time slot of training for every record in
	// table
	private final StringProperty firstDayTimeSlot = new SimpleStringProperty(this, "firstDayTimeSlot");

	public StringProperty firstDayTimeSlotProperty() {
		return firstDayTimeSlot;
	}

	public final String getFirstDayTimeSlot() {
		return firstDayTimeSlotProperty().get();
	}

	public final void setFirstDayTimeSlot(String firstDayTimeSlot) {
		firstDayTimeSlotProperty().set(firstDayTimeSlot);
	}

	// sets String property for second day of training for every record in table
	private final StringProperty secondDay = new SimpleStringProperty(this, "secondDay");

	public StringProperty secondDayProperty() {
		return secondDay;
	}

	public final String getSecondDay() {
		return secondDayProperty().get();
	}

	public final void setSecondDay(String secondDay) {
		secondDayProperty().set(secondDay);
	}

	// sets String property for second time slot of training for every record in
	// table
	private final StringProperty secondDayTimeSlot = new SimpleStringProperty(this, "secondDayTimeSlot");

	public StringProperty secondDayTimeSlotProperty() {
		return secondDayTimeSlot;
	}

	public final String getSecondDayTimeSlot() {
		return secondDayTimeSlotProperty().get();
	}

	public final void setSecondDayTimeSlot(String secondDayTimeSlot) {
		secondDayTimeSlotProperty().set(secondDayTimeSlot);
	}

	// sets String property for team ID for every record in table
	private final IntegerProperty teamId = new SimpleIntegerProperty(this, "team_teamID");

	public IntegerProperty teamIdProperty() {
		return teamId;
	}

	public final Integer getTeamId() {
		return teamIdProperty().get();
	}

	public final void setTeamId(Integer teamId) {
		teamIdProperty().set(teamId);
	}

}
