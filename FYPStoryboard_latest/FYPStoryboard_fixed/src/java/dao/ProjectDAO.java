package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ProjectDAO {

    /**
     * List projects that belong to students supervised by the given supervisor.
     * Returned as Map rows for easy JSP rendering.
     */
    public List<Map<String, Object>> listProjectsForSupervisor(int supervisorID) {
        String sql =
            "SELECT p.PROJECTID, p.PROJECT_TITLE, p.DESCRIPTION, p.START_DATE, p.END_DATE, p.PROJECT_STATUS, " +
            "       s.STUDENTID, s.STUDENT_NAME, s.STUDENT_EMAIL, s.MATRICNUM, s.PROGRAM, " +
            "       (SELECT MAX(pr.SUBMITTED_DATE) FROM PROGRESSSUBMISSION pr WHERE pr.PROJECTID = p.PROJECTID) AS LAST_SUBMITTED " +
            "FROM PROJECTS p " +
            "JOIN STUDENTS s ON p.STUDENTID = s.STUDENTID " +
            "WHERE s.SUPERVISORID=? " +
            "ORDER BY s.STUDENT_NAME";

        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, supervisorID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("projectID", rs.getInt("PROJECTID"));
                row.put("projectTitle", rs.getString("PROJECT_TITLE"));
                row.put("description", rs.getString("DESCRIPTION"));
                row.put("startDate", rs.getDate("START_DATE"));
                row.put("endDate", rs.getDate("END_DATE"));
                row.put("projectStatus", rs.getString("PROJECT_STATUS"));
                row.put("studentID", rs.getInt("STUDENTID"));
                row.put("studentName", rs.getString("STUDENT_NAME"));
                row.put("studentEmail", rs.getString("STUDENT_EMAIL"));
                row.put("matricNum", rs.getString("MATRICNUM"));
                row.put("program", rs.getString("PROGRAM"));
                row.put("lastSubmitted", rs.getDate("LAST_SUBMITTED"));
                list.add(row);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load a single project (and its student info) that belongs to this supervisor.
     * Returns null if not found or not owned by the supervisor.
     */
    public Map<String, Object> getProjectDetailsForSupervisor(int projectID, int supervisorID) {
        String sql =
            "SELECT p.PROJECTID, p.PROJECT_TITLE, p.DESCRIPTION, p.START_DATE, p.END_DATE, p.PROJECT_STATUS, " +
            "       s.STUDENTID, s.STUDENT_NAME, s.STUDENT_EMAIL, s.MATRICNUM, s.PROGRAM " +
            "FROM PROJECTS p " +
            "JOIN STUDENTS s ON p.STUDENTID = s.STUDENTID " +
            "WHERE p.PROJECTID=? AND s.SUPERVISORID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, projectID);
            ps.setInt(2, supervisorID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            Map<String, Object> row = new HashMap<>();
            row.put("projectID", rs.getInt("PROJECTID"));
            row.put("projectTitle", rs.getString("PROJECT_TITLE"));
            row.put("description", rs.getString("DESCRIPTION"));
            row.put("startDate", rs.getDate("START_DATE"));
            row.put("endDate", rs.getDate("END_DATE"));
            row.put("projectStatus", rs.getString("PROJECT_STATUS"));
            row.put("studentID", rs.getInt("STUDENTID"));
            row.put("studentName", rs.getString("STUDENT_NAME"));
            row.put("studentEmail", rs.getString("STUDENT_EMAIL"));
            row.put("matricNum", rs.getString("MATRICNUM"));
            row.put("program", rs.getString("PROGRAM"));
            return row;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getOrCreateProjectForStudent(int studentID) {
        int existing = getProjectIDByStudent(studentID);
        if (existing != 0) return existing;

        String sql = "INSERT INTO PROJECTS(PROJECT_TITLE, DESCRIPTION, START_DATE, PROJECT_STATUS, STUDENTID) " +
                     "VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, "My FYP Project");
            ps.setString(2, "Auto-created project (edit later).");
            // Use fully-qualified java.sql.Date to avoid ambiguity with java.util.Date
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            ps.setString(4, "ONGOING");
            ps.setInt(5, studentID);

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
            return 0;

        } catch (Exception e) {
            throw new RuntimeException("create project error: " + e.getMessage(), e);
        }
    }

    public int getProjectIDByStudent(int studentID) {
        String sql = "SELECT PROJECTID FROM PROJECTS WHERE STUDENTID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("PROJECTID");
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public int countActiveProjectsForSupervisor(int supervisorID) {
    String sql =
        "SELECT COUNT(DISTINCT p.PROJECTID) AS TOTAL " +
        "FROM PROJECTS p " +
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

    /**
     * Security helper: check if a project belongs to students supervised by the given supervisor.
     */
    public boolean isSupervisorOwnerOfProject(int supervisorID, int projectID) {
        String sql = "SELECT 1 FROM PROJECTS p JOIN STUDENTS s ON p.STUDENTID=s.STUDENTID " +
                     "WHERE p.PROJECTID=? AND s.SUPERVISORID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, projectID);
            ps.setInt(2, supervisorID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
