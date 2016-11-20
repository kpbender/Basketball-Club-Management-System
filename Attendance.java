package coach.management;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//class to set properties for data from database so record can be displayed in table view
public class Attendance {

	// constructor of the class takes all parameters from database send from
	// data access class
	public Attendance(Integer attendanceId, String date, Boolean present, Boolean excused, Integer memberIdFk) {
		setAttendanceId(attendanceId);
		setDate(date);
		setPresent(present);
		setExcused(excused);
		setMemberIdFk(memberIdFk);
	}

	public Attendance() {
	}

	// sets integer property for attendance id for very record in table
	private final IntegerProperty attendanceId = new SimpleIntegerProperty(this, "attendanceID");

	public IntegerProperty attendanceIdProperty() {
		return attendanceId;
	}

	public final Integer getAttendanceId() {
		return attendanceIdProperty().get();
	}

	public final void setAttendanceId(Integer attendanceId) {
		attendanceIdProperty().set(attendanceId);
	}

	// sets String property for date of attendance for very record in table
	private final StringProperty date = new SimpleStringProperty(this, "date");

	public StringProperty dateProperty() {
		return date;
	}

	public final String getDate() {
		return dateProperty().get();
	}

	public final void setDate(String date) {
		dateProperty().set(date);
	}

	// sets boolean property to check if member was present for very record in
	// table
	private final BooleanProperty present = new SimpleBooleanProperty(this, "present");

	public BooleanProperty presentProperty() {
		return present;
	}

	public final Boolean getPresent() {
		return presentProperty().get();
	}

	public final void setPresent(Boolean present) {
		presentProperty().set(present);
	}

	// sets boolean property to check if member was excused when absent for very
	// record in table
	private final BooleanProperty excused = new SimpleBooleanProperty(this, "excused");

	public BooleanProperty excusedProperty() {
		return excused;
	}

	public final Boolean getExcused() {
		return excusedProperty().get();
	}

	public final void setExcused(Boolean excused) {
		excusedProperty().set(excused);
	}

	// sets String property to get member id to table view
	private final IntegerProperty memberIdFk = new SimpleIntegerProperty(this, "member_memberID");

	public IntegerProperty memberIdFkProperty() {
		return memberIdFk;
	}

	public final Integer getMemberIdFk() {
		return memberIdFkProperty().get();
	}

	public final void setMemberIdFk(Integer memberIdFk) {
		memberIdFkProperty().set(memberIdFk);
	}

}
