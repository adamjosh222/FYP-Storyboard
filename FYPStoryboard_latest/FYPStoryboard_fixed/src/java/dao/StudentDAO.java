package dao;

import java.sql.*;

public class StudentDAO {

    public int createStudent(int userID, String name, String email, String matric, String program) {
        String sql = "INSERT INTO STUDENTS(STUDENT_NAME, STUDENT_EMAIL, MATRICNUM, PROGRAM, USERID) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, matric);
            ps.setString(4, program);
            ps.setInt(5, userID);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
            return 0;
        } catch (Exception e) {
            throw new RuntimeException("createStudent error: " + e.getMessage(), e);
        }
    }

    public int getStudentIDByUserID(int userID) {
        String sql = "SELECT STUDENTID FROM STUDENTS WHERE USERID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("STUDENTID");
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Load student profile fields for profile page */
    public java.util.Map<String, Object> getStudentProfileByUserID(int userID) {
        // 1) Cuba query lengkap (dengan SUPERVISORID). Kalau column belum wujud, fallback.
        String sqlFull =
                "SELECT st.STUDENTID, st.STUDENT_NAME, st.STUDENT_EMAIL, st.MATRICNUM, st.PROGRAM, " +
                "       st.SUPERVISORID, sv.SV_NAME AS SUPERVISOR_NAME " +
                "FROM STUDENTS st " +
                "LEFT JOIN SUPERVISORS sv ON st.SUPERVISORID = sv.SUPERVISORID " +
                "WHERE st.USERID=?";

        String sqlBasic =
                "SELECT st.STUDENTID, st.STUDENT_NAME, st.STUDENT_EMAIL, st.MATRICNUM, st.PROGRAM " +
                "FROM STUDENTS st " +
                "WHERE st.USERID=?";

        try (Connection con = DBConnection.getConnection()) {
            try {
                return runStudentProfileQuery(con, sqlFull, true, userID);
            } catch (SQLException ex) {
                // Column belum wujud (contoh SUPERVISORID) -> fallback supaya page tak crash
                return runStudentProfileQuery(con, sqlBasic, false, userID);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private java.util.Map<String, Object> runStudentProfileQuery(Connection con, String sql, boolean hasSupervisor, int userID) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("studentID", rs.getInt("STUDENTID"));
            m.put("name", rs.getString("STUDENT_NAME"));
            m.put("email", rs.getString("STUDENT_EMAIL"));
            m.put("matric", rs.getString("MATRICNUM"));
            m.put("program", rs.getString("PROGRAM"));

            if (hasSupervisor) {
                m.put("supervisorID", rs.getObject("SUPERVISORID"));
                String svName = rs.getString("SUPERVISOR_NAME");
                m.put("supervisorName", svName == null ? "Not Assigned" : svName);
            } else {
                m.put("supervisorID", null);
                m.put("supervisorName", "Not Assigned");
            }
            return m;
        }
    }

    /** Update student profile (also keeps USERS name/email in sync) */
    public void updateStudentProfile(int userID, String name, String email, String matric, String program) {
        String sql1 = "UPDATE STUDENTS SET STUDENT_NAME=?, STUDENT_EMAIL=?, MATRICNUM=?, PROGRAM=? WHERE USERID=?";
        String sql2 = "UPDATE USERS SET USER_NAME=?, USER_EMAIL=? WHERE USERID=?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql1)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, matric);
                ps.setString(4, program);
                ps.setInt(5, userID);
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
            throw new RuntimeException("updateStudentProfile error: " + e.getMessage(), e);
        }
    }
    
    public String getSupervisorNameByUserID(int userID) {
    String sql =
        "SELECT sv.SV_NAME " +
        "FROM STUDENTS st " +
        "JOIN SUPERVISORS sv ON st.SUPERVISORID = sv.SUPERVISORID " +
        "WHERE st.USERID=?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getString("SV_NAME");
        return "Not Assigned";

    } catch (SQLException ex) {
        // jika schema belum ada SUPERVISORID, jangan crash
        return "Not Assigned";
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

public int countUpcomingDeadlinesForStudentProject(int projectID) {
    // upcoming = due_date >= today and status not Completed
    String sql =
        "SELECT COUNT(*) AS TOTAL " +
        "FROM REMINDERS " +
        "WHERE PROJECTID=? AND DUE_DATE >= CURRENT_DATE " +
        "AND (SUBMISSION_STATUS IS NULL OR UPPER(SUBMISSION_STATUS) <> 'COMPLETED')";

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

}
