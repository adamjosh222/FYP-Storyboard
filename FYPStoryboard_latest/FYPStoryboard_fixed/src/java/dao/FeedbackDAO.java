package dao;

import model.entity.Feedbacks;
import java.sql.*;
import java.util.*;

public class FeedbackDAO {

    public List<Feedbacks> listByProgress(int progressID) {
        String sql = "SELECT FEEDBACKID, FEEDBACK_TEXT, FEEDBACK_DATE, SUPERVISORID, PROGRESSID " +
                     "FROM FEEDBACK WHERE PROGRESSID=? ORDER BY FEEDBACKID DESC";

        List<Feedbacks> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, progressID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Feedbacks f = new Feedbacks();
                f.setFeedbackID(rs.getInt("FEEDBACKID"));
                f.setFeedback_text(rs.getString("FEEDBACK_TEXT"));
                f.setFeedback_date(rs.getDate("FEEDBACK_DATE"));
                f.setSupervisorID(rs.getInt("SUPERVISORID"));
                f.setProgressID(rs.getInt("PROGRESSID"));
                list.add(f);
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Feedbacks f) {
        String sql = "INSERT INTO FEEDBACK(FEEDBACK_TEXT, FEEDBACK_DATE, SUPERVISORID, PROGRESSID) VALUES (?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, f.getFeedback_text());
            ps.setDate(2, f.getFeedback_date());
            ps.setInt(3, f.getSupervisorID());
            ps.setInt(4, f.getProgressID());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
