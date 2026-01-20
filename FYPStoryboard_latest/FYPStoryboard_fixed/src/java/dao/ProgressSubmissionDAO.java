package dao;

import model.entity.Progresssubmission;
import java.sql.*;
import java.util.*;

public class ProgressSubmissionDAO {

    public List<Progresssubmission> listByProject(int projectID) {
        String sql = "SELECT PROGRESSID, SUBMITTED_DATE, PROGRESS_STATUS, PROJECTID, " +
                     "       STUDENT_FILE_NAME, STUDENT_FILE_PATH, SUPERVISOR_FILE_NAME, SUPERVISOR_FILE_PATH " +
                     "FROM PROGRESSSUBMISSION WHERE PROJECTID=? ORDER BY PROGRESSID DESC";
        List<Progresssubmission> list = new ArrayList<Progresssubmission>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, projectID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Progresssubmission p = new Progresssubmission();
                p.setProgressID(rs.getInt("PROGRESSID"));
                p.setSubmitted_date(rs.getDate("SUBMITTED_DATE"));
                p.setProgress_status(rs.getString("PROGRESS_STATUS"));
                p.setProjectID(rs.getInt("PROJECTID"));
                p.setStudentFileName(rs.getString("STUDENT_FILE_NAME"));
                p.setStudentFilePath(rs.getString("STUDENT_FILE_PATH"));
                p.setSupervisorFileName(rs.getString("SUPERVISOR_FILE_NAME"));
                p.setSupervisorFilePath(rs.getString("SUPERVISOR_FILE_PATH"));
                list.add(p);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int insertAndReturnId(Progresssubmission p) {
        String sql = "INSERT INTO PROGRESSSUBMISSION(SUBMITTED_DATE, PROGRESS_STATUS, PROJECTID) VALUES (?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, p.getSubmitted_date());
            ps.setString(2, p.getProgress_status());
            ps.setInt(3, p.getProjectID());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
            return 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStudentFile(int progressID, String fileName, String filePath) {
        String sql = "UPDATE PROGRESSSUBMISSION SET STUDENT_FILE_NAME=?, STUDENT_FILE_PATH=? WHERE PROGRESSID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fileName);
            ps.setString(2, filePath);
            ps.setInt(3, progressID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSupervisorFile(int progressID, String fileName, String filePath) {
        String sql = "UPDATE PROGRESSSUBMISSION SET SUPERVISOR_FILE_NAME=?, SUPERVISOR_FILE_PATH=? WHERE PROGRESSID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fileName);
            ps.setString(2, filePath);
            ps.setInt(3, progressID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Progresssubmission getById(int progressID) {
        String sql = "SELECT PROGRESSID, SUBMITTED_DATE, PROGRESS_STATUS, PROJECTID, " +
                     "       STUDENT_FILE_NAME, STUDENT_FILE_PATH, SUPERVISOR_FILE_NAME, SUPERVISOR_FILE_PATH " +
                     "FROM PROGRESSSUBMISSION WHERE PROGRESSID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, progressID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            Progresssubmission p = new Progresssubmission();
            p.setProgressID(rs.getInt("PROGRESSID"));
            p.setSubmitted_date(rs.getDate("SUBMITTED_DATE"));
            p.setProgress_status(rs.getString("PROGRESS_STATUS"));
            p.setProjectID(rs.getInt("PROJECTID"));
            p.setStudentFileName(rs.getString("STUDENT_FILE_NAME"));
            p.setStudentFilePath(rs.getString("STUDENT_FILE_PATH"));
            p.setSupervisorFileName(rs.getString("SUPERVISOR_FILE_NAME"));
            p.setSupervisorFilePath(rs.getString("SUPERVISOR_FILE_PATH"));
            return p;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int progressID) {
        String sql = "DELETE FROM PROGRESSSUBMISSION WHERE PROGRESSID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, progressID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public int countByProject(int projectID) {
    String sql = "SELECT COUNT(*) AS TOTAL FROM PROGRESSSUBMISSION WHERE PROJECTID=?";
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
