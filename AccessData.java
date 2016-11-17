package coach.management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.LoginController;
import coach.management.AccidentReport;

public class AccessData {
	private Connection connection;

	// Constructor takes in necessary connection details
	public AccessData(String driverClassName, String dbURL, String user, String password)
			throws SQLException, ClassNotFoundException {
		Class.forName(driverClassName);
		connection = DriverManager.getConnection(dbURL, user, password);

	}

	// kills connection when something goes wrong with database
	public void shutdown() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	// gets list of accident reports arraylist from db and creates objects of
	// accident report class
	public List<AccidentReport> getReportList() throws SQLException {
		try (Statement stmnt = connection.createStatement();
				ResultSet rs = stmnt.executeQuery(
						"SELECT a.reportID,a.dateOfAccident,a.timeOfAccident,a.dateOfReport,a.timeOfReport,c.memberId,c.firstName,c.lastName,c.dob,t.teamName,"
								+ "a.firstAiderFirstName,a.firstAiderLastName,a.firstAiderPhoneNum,a.firstWitnessFirstName,a.firstWitnessLastName,a.firstWitnessPhoneNum,"
								+ "a.secondWitnessFirstName,a.secondWitnessLastName,a.secondWitnessPhoneNum,a.detailsOfAccident,a.actionTaken,a.contactedGarda,"
								+ "a.contactedAmbulance,a.contactedParentGuardian,a.contactedGP,a.coach_coachID,a.member_memberID"
								+ " FROM accident_report a, member c, member_has_team tc, team t"
								+ " WHERE  c.memberID = a.member_memberID AND c.memberID = tc.member_memberID"
								+ " AND tc.team_teamID = t.teamID AND a.coach_coachID = '"
								+ LoginController.coachLoginID + "'");

		) {
			List<AccidentReport> accidentReportList = new ArrayList<>();
			while (rs.next()) {
				Integer reportID = rs.getInt("reportID");
				String dateOfAccident = rs.getString("dateOfAccident");
				String timeOfAccident = rs.getString("timeOfAccident");
				String dateOfReport = rs.getString("dateOfReport");
				String timeOfReport = rs.getString("timeOfReport");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String injuredPlayerDOB = rs.getString("dob");
				String team = rs.getString("teamName");
				String firstAiderFirstName = rs.getString("firstAiderFirstName");
				String firstAiderLastName = rs.getString("firstAiderLastName");
				String firstAiderPhoneNum = rs.getString("firstAiderPhoneNum");
				String witnessOneFirstName = rs.getString("firstWitnessFirstName");
				String witnessOneLastName = rs.getString("firstWitnessLastName");
				String witnessOnePhoneNumber = rs.getString("firstWitnessPhoneNum");
				String witnessTwoFirstName = rs.getString("secondWitnessFirstName");
				String witnessTwoLastName = rs.getString("secondWitnessLastName");
				String witnessTwoPhoneNumber = rs.getString("secondWitnessPhoneNum");
				String details = rs.getString("detailsOfAccident");
				String actionTaken = rs.getString("actionTaken");
				Boolean garda = rs.getBoolean("contactedGarda");
				Boolean gp = rs.getBoolean("contactedGP");
				Boolean parent = rs.getBoolean("contactedParentGuardian");
				Boolean ambulance = rs.getBoolean("contactedAmbulance");
				Integer coachId = rs.getInt("coach_coachID");
				Integer memberId = rs.getInt("member_memberID");
				// passes data from database to accident report class to create
				// objects
				AccidentReport report = new AccidentReport(reportID, dateOfAccident, timeOfAccident, dateOfReport,
						timeOfReport, firstName, lastName, injuredPlayerDOB, team, firstAiderFirstName,
						firstAiderLastName, firstAiderPhoneNum, witnessOneFirstName, witnessOneLastName,
						witnessOnePhoneNumber, witnessTwoFirstName, witnessTwoLastName, witnessTwoPhoneNumber, details,
						actionTaken, garda, gp, parent, ambulance, coachId, memberId);
				accidentReportList.add(report);
			}
			connection.close();
			return accidentReportList;

		}
	}

	// gets list of training schedules arraylist from database and creates
	// objects of training schedule class
	public List<TrainingSchedule> getScheduleList() throws SQLException {
		try (Statement stmnt = connection.createStatement();
				ResultSet rs = stmnt.executeQuery(
						"SELECT s.scheduleID,t.teamName,s.season,s.firstDay,s.firstDayTimeSlot,s.secondDay,s.secondDayTimeSlot,"
								+ "s.team_teamID" + " FROM training_schedule s, team t"
								+ " WHERE s.team_teamID = t.teamID AND t.coach_coachID = '"
								+ LoginController.coachLoginID + "'");

		) {
			List<TrainingSchedule> schedule = new ArrayList<>();
			while (rs.next()) {
				Integer scheduleId = rs.getInt("scheduleID");
				String team = rs.getString("teamName");
				String season = rs.getString("season");
				String firstDay = rs.getString("firstDay");
				String firstDayTimeSlot = rs.getString("firstDayTimeSlot");
				String secondDay = rs.getString("secondDay");
				String secondDayTimeSlot = rs.getString("secondDayTimeSlot");
				Integer teamId = rs.getInt("team_teamID");
				// passes data from database to Training Schedule class to
				// create objects
				TrainingSchedule trainingSchedule = new TrainingSchedule(scheduleId, season, team, firstDay,
						firstDayTimeSlot, secondDay, secondDayTimeSlot, teamId);

				schedule.add(trainingSchedule);
			}
			connection.close();
			return schedule;
		}
	}

	// gets list of persons to arraylist from database and creates object of
	// attendance class
	public List<Attendance> getPlayerList() throws SQLException {
		try (Statement stmnt = connection.createStatement();
				ResultSet rs = stmnt.executeQuery("SELECT * from attendance a, member_has_team ms, team t"
						+ " WHERE a.member_memberID = ms.member_memberID AND t.teamID == ms.team_teamID "
						+ "AND t.coach_coachID =t.coach_coachID = '" + LoginController.coachLoginID + "'");)

		{

			List<Attendance> attendanceList = new ArrayList<>();
			while (rs.next()) {
				Integer attendanceId = rs.getInt("attendanceID");
				String date = rs.getString("date");
				Boolean present = rs.getBoolean("present");
				Boolean excused = rs.getBoolean("excused");
				Integer memberIdFk = rs.getInt("member_memberID");
				// passes data from database to Attendance class to create
				// objects
				Attendance attendance = new Attendance(attendanceId, date, present, excused, memberIdFk);
				attendanceList.add(attendance);
			}
			connection.close();
			return attendanceList;
		}
	}

}
