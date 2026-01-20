package dao;

import java.sql.*;
import java.util.*;

public class SupervisorDAO {

    /** Load supervisor profile fields */
    public java.util.Map<String,Object> getSupervisorProfileByUserID(int userID) {
        String sql = "SELECT SUPERVISORID, SV_NAME, SV_EMAIL, PHONENUM, DEPARTMENT, ROOMNO FROM SUPERVISORS WHERE USERID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            java.util.Map<String,Object> m = new java.util.HashMap<>();
            m.put("supervisorID", rs.getInt("SUPERVISORID"));
            m.put("name", rs.getString("SV_NAME"));
            m.put("email", rs.getString("SV_EMAIL"));
            m.put("phone", rs.getString("PHONENUM"));
            m.put("department", rs.getString("DEPARTMENT"));
            m.put("room", rs.getString("ROOMNO"));
            return m;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Update supervisor profile (also keeps USERS name/email in sync) */
    public void updateSupervisorProfile(int userID, String name, String email, String phone, String dept, String room) {
        String sql1 = "UPDATE SUPERVISORS SET SV_NAME=?, SV_EMAIL=?, PHONENUM=?, DEPARTMENT=?, ROOMNO=? WHERE USERID=?";
        String sql2 = "UPDATE USERS SET USER_NAME=?, USER_EMAIL=? WHERE USERID=?";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql1)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, phone);
                ps.setString(4, dept);
                ps.setString(5, room);
                ps.setInt(6, userID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement(sql2)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setInt(3, userID);
                ps.executeUpdate();
            }
            con.commit();
        } catch (Exception e) {
            throw new RuntimeException("updateSupervisorProfile error: " + e.getMessage(), e);
        }
    }

    /** Students not assigned to any supervisor yet */
    public java.util.List<java.util.Map<String,Object>> listUnassignedStudents() {
        String sql = "SELECT STUDENTID, STUDENT_NAME, STUDENT_EMAIL, MATRICNUM, PROGRAM FROM STUDENTS WHERE SUPERVISORID IS NULL OR SUPERVISORID=0 ORDER BY STUDENT_NAME";
        java.util.List<java.util.Map<String,Object>> list = new java.util.ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.util.Map<String,Object> row = new java.util.HashMap<>();
                row.put("studentID", rs.getInt("STUDENTID"));
                row.put("studentName", rs.getString("STUDENT_NAME"));
                row.put("studentEmail", rs.getString("STUDENT_EMAIL"));
                row.put("matricNum", rs.getString("MATRICNUM"));
                row.put("program", rs.getString("PROGRAM"));
                list.add(row);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Assign a student to a supervisor */
    public void assignStudent(int studentID, int supervisorID) {
        String sql = "UPDATE STUDENTS SET SUPERVISORID=? WHERE STUDENTID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, supervisorID);
            ps.setInt(2, studentID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ✅ ADD THIS METHOD (needed by RegisterServlet)
    public int createSupervisor(int userID, String name, String email, String phone, String dept, String room) {

        String sql = "INSERT INTO SUPERVISORS(SV_NAME, SV_EMAIL, PHONENUM, DEPARTMENT, ROOMNO, USERID) " +
                     "VALUES (?,?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, dept);
            ps.setString(5, room);
            ps.setInt(6, userID);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (Exception e) {
            throw new RuntimeException("createSupervisor error: " + e.getMessage(), e);
        }
    }

    // 1) Get supervisorID based on userID
    public int getSupervisorIDByUserID(int userID) {
        String sql = "SELECT SUPERVISORID FROM SUPERVISORS WHERE USERID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("SUPERVISORID");
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 2) List all students under this supervisor + their project (if exists)
    public List<Map<String, Object>> listStudentsWithProject(int supervisorID) {

        String sql =
            "SELECT s.STUDENTID, s.STUDENT_NAME, s.STUDENT_EMAIL, s.MATRICNUM, s.PROGRAM, " +
            "       p.PROJECTID, p.PROJECT_TITLE, p.PROJECT_STATUS " +
            "FROM STUDENTS s " +
            "LEFT JOIN PROJECTS p ON p.STUDENTID = s.STUDENTID " +
            "WHERE s.SUPERVISORID=? " +
            "ORDER BY s.STUDENT_NAME";

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, supervisorID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("studentID", rs.getInt("STUDENTID"));
                row.put("studentName", rs.getString("STUDENT_NAME"));
                row.put("studentEmail", rs.getString("STUDENT_EMAIL"));
                row.put("matricNum", rs.getString("MATRICNUM"));
                row.put("program", rs.getString("PROGRAM"));
                row.put("projectID", rs.getObject("PROJECTID"));
                row.put("projectTitle", rs.getString("PROJECT_TITLE"));
                row.put("projectStatus", rs.getString("PROJECT_STATUS"));
                list.add(row);
            }

            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public int countStudents(int supervisorID) {
    String sql = "SELECT COUNT(*) AS TOTAL FROM STUDENTS WHERE SUPERVISORID=?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, supervisorID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("TOTAL");
        return 0;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public int countProgressForSupervisor(int supervisorID) {
    String sql =
        "SELECT COUNT(*) AS TOTAL " +
        "FROM PROGRESSSUBMISSION pr " +
        "JOIN PROJECTS p ON pr.PROJECTID = p.PROJECTID " +
        "JOIN STUDENTS s ON p.STUDENTID = s.STUDENTID " +
        "WHERE s.SUPERVISORID=?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, supervisorID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("TOTAL");
        return 0;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public int countFeedbackBySupervisor(int supervisorID) {
    String sql = "SELECT COUNT(*) AS TOTAL FROM FEEDBACK WHERE SUPERVISORID=?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, supervisorID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("TOTAL");
        return 0;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public int countPendingProgress(int supervisorID) {
    // pending = progress submission yang belum ada feedback langsung
    String sql =
        "SELECT COUNT(*) AS TOTAL " +
        "FROM PROGRESSSUBMISSION pr " +
        "JOIN PROJECTS p ON pr.PROJECTID = p.PROJECTID " +
        "JOIN STUDENTS s ON p.STUDENTID = s.STUDENTID " +
        "LEFT JOIN FEEDBACK f ON f.PROGRESSID = pr.PROGRESSID " +
        "WHERE s.SUPERVISORID=? AND f.FEEDBACKID IS NULL";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, supervisorID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("TOTAL");
        return 0;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public int countUpcomingDeadlines(int supervisorID) {
    // upcoming deadlines for all projects under supervisor
    String sql =
        "SELECT COUNT(*) AS TOTAL " +
        "FROM REMINDERS r " +
        "JOIN PROJECTS p ON r.PROJECTID = p.PROJECTID " +
        "JOIN STUDENTS s ON p.STUDENTID = s.STUDENTID " +
        "WHERE s.SUPERVISORID=? AND r.DUE_DATE >= CURRENT_DATE " +
        "AND (r.SUBMISSION_STATUS IS NULL OR UPPER(r.SUBMISSION_STATUS) <> 'COMPLETED')";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, supervisorID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("TOTAL");
        return 0;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}


}
