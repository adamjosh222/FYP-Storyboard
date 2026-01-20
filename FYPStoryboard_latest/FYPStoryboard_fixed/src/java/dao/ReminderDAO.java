package dao;

import model.entity.Reminders;
import java.sql.*;
import java.util.*;

public class ReminderDAO {

    /** Fetch a single reminder for edit form */
    public Reminders getById(int reminderID) {
        String sql = "SELECT REMINDERID, REMINDER_NAME, DUE_DATE, SUBMISSION_STATUS, RECURRENCE, PROJECTID " +
                     "FROM REMINDERS WHERE REMINDERID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reminderID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            Reminders r = new Reminders();
            r.setReminderID(rs.getInt("REMINDERID"));
            r.setReminder_name(rs.getString("REMINDER_NAME"));
            r.setDue_date(rs.getDate("DUE_DATE"));
            r.setSubmission_status(rs.getString("SUBMISSION_STATUS"));
            r.setRecurrence(rs.getString("RECURRENCE"));
            r.setProjectID(rs.getInt("PROJECTID"));
            return r;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Update reminder (used by Edit button) */
    public void update(Reminders r) {
        String sql = "UPDATE REMINDERS SET REMINDER_NAME=?, DUE_DATE=?, SUBMISSION_STATUS=?, RECURRENCE=? WHERE REMINDERID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getReminder_name());
            ps.setDate(2, r.getDue_date());
            ps.setString(3, r.getSubmission_status());
            ps.setString(4, r.getRecurrence());
            ps.setInt(5, r.getReminderID());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reminders> listByProject(int projectID) {
        String sql = "SELECT REMINDERID, REMINDER_NAME, DUE_DATE, SUBMISSION_STATUS, RECURRENCE, PROJECTID " +
                     "FROM REMINDERS WHERE PROJECTID=? ORDER BY DUE_DATE ASC";
        List<Reminders> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, projectID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reminders r = new Reminders();
                r.setReminderID(rs.getInt("REMINDERID"));
                r.setReminder_name(rs.getString("REMINDER_NAME"));
                r.setDue_date(rs.getDate("DUE_DATE"));
                r.setSubmission_status(rs.getString("SUBMISSION_STATUS"));
                r.setRecurrence(rs.getString("RECURRENCE"));
                r.setProjectID(rs.getInt("PROJECTID"));
                list.add(r);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Reminders r) {
        String sql = "INSERT INTO REMINDERS(REMINDER_NAME, DUE_DATE, SUBMISSION_STATUS, RECURRENCE, PROJECTID) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getReminder_name());
            ps.setDate(2, r.getDue_date());
            ps.setString(3, r.getSubmission_status());
            ps.setString(4, r.getRecurrence());
            ps.setInt(5, r.getProjectID());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int reminderID) {
        String sql = "DELETE FROM REMINDERS WHERE REMINDERID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reminderID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public int countByProject(int projectID) {
    String sql = "SELECT COUNT(*) AS TOTAL FROM REMINDERS WHERE PROJECTID=?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, projectID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("TOTAL");
        return 0;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public Reminders getNextReminderByProject(int projectID) {
    // next upcoming due_date
    String sql = "SELECT REMINDERID, REMINDER_NAME, DUE_DATE, SUBMISSION_STATUS, RECURRENCE, PROJECTID " +
                 "FROM REMINDERS WHERE PROJECTID=? ORDER BY DUE_DATE ASC FETCH FIRST 1 ROWS ONLY";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, projectID);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) return null;

        Reminders r = new Reminders();
        r.setReminderID(rs.getInt("REMINDERID"));
        r.setReminder_name(rs.getString("REMINDER_NAME"));
        r.setDue_date(rs.getDate("DUE_DATE"));
        r.setSubmission_status(rs.getString("SUBMISSION_STATUS"));
        r.setRecurrence(rs.getString("RECURRENCE"));
        r.setProjectID(rs.getInt("PROJECTID"));
        return r;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

    /**
     * Fetch up to 2 nearest reminders by due date for a project.
     * Used on the dashboard "Next Deadline" box.
     */
    public List<Reminders> getNextTwoRemindersByProject(int projectID) {
        // NOTE: keep it simple and consistent with getNextReminderByProject
        String sql = "SELECT REMINDERID, REMINDER_NAME, DUE_DATE, SUBMISSION_STATUS, RECURRENCE, PROJECTID " +
                     "FROM REMINDERS WHERE PROJECTID=? ORDER BY DUE_DATE ASC FETCH FIRST 2 ROWS ONLY";

        List<Reminders> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, projectID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reminders r = new Reminders();
                r.setReminderID(rs.getInt("REMINDERID"));
                r.setReminder_name(rs.getString("REMINDER_NAME"));
                r.setDue_date(rs.getDate("DUE_DATE"));
                r.setSubmission_status(rs.getString("SUBMISSION_STATUS"));
                r.setRecurrence(rs.getString("RECURRENCE"));
                r.setProjectID(rs.getInt("PROJECTID"));
                list.add(r);
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
