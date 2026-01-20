package dao;

import model.entity.Notes;
import java.sql.*;
import java.util.*;

public class NotesDAO {

    public List<Notes> listByUserAndProject(int userID, int projectID) {
        String sql = "SELECT NOTESID, NOTES_TITLE, NOTES_CONTENT, CREATED_DATE, USERID, PROJECTID " +
                     "FROM NOTES WHERE USERID=? AND PROJECTID=? ORDER BY NOTESID DESC";
        List<Notes> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.setInt(2, projectID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notes n = new Notes();
                n.setNotesID(rs.getInt("NOTESID"));
                n.setNotes_title(rs.getString("NOTES_TITLE"));
                n.setNotes_content(rs.getString("NOTES_CONTENT"));
                n.setCreated_date(rs.getDate("CREATED_DATE"));
                n.setUserID(rs.getInt("USERID"));
                n.setProjectID(rs.getInt("PROJECTID"));
                list.add(n);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Notes n) {
        String sql = "INSERT INTO NOTES(NOTES_TITLE, NOTES_CONTENT, CREATED_DATE, USERID, PROJECTID) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, n.getNotes_title());
            ps.setString(2, n.getNotes_content());
            ps.setDate(3, n.getCreated_date());
            ps.setInt(4, n.getUserID());
            ps.setInt(5, n.getProjectID());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Notes findById(int notesID) {
        String sql = "SELECT NOTESID, NOTES_TITLE, NOTES_CONTENT, CREATED_DATE, USERID, PROJECTID FROM NOTES WHERE NOTESID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, notesID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            Notes n = new Notes();
            n.setNotesID(rs.getInt("NOTESID"));
            n.setNotes_title(rs.getString("NOTES_TITLE"));
            n.setNotes_content(rs.getString("NOTES_CONTENT"));
            n.setCreated_date(rs.getDate("CREATED_DATE"));
            n.setUserID(rs.getInt("USERID"));
            n.setProjectID(rs.getInt("PROJECTID"));
            return n;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Notes n) {
        String sql = "UPDATE NOTES SET NOTES_TITLE=?, NOTES_CONTENT=? WHERE NOTESID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, n.getNotes_title());
            ps.setString(2, n.getNotes_content());
            ps.setInt(3, n.getNotesID());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int notesID) {
        String sql = "DELETE FROM NOTES WHERE NOTESID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, notesID);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public int countByUserAndProject(int userID, int projectID) {
    String sql = "SELECT COUNT(*) AS TOTAL FROM NOTES WHERE USERID=? AND PROJECTID=?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, userID);
        ps.setInt(2, projectID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("TOTAL");
        return 0;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

}
