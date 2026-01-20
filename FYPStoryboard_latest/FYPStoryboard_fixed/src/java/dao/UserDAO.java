package dao;

import model.entity.Users;
import java.sql.*;
import util.PasswordUtil;

public class UserDAO {

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM USERS WHERE USER_EMAIL=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Register a user with a password chosen by the user.
     */
    public int registerUser(String name, String email, String role, String password) {
        String sql = "INSERT INTO USERS(USER_NAME, USER_EMAIL, PASSWORD, USER_ROLE) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            // Store hashed password (PBKDF2)
            ps.setString(3, PasswordUtil.hash(password));
            ps.setString(4, role);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
            return 0;
        } catch (Exception e) {
            throw new RuntimeException("register user error: " + e.getMessage(), e);
        }
    }

    public Users login(String email, String password, String role) {
        // Fetch stored password then verify in Java.
        // This allows safe hashing (PBKDF2) and also supports legacy plaintext records.
        String sql = "SELECT USERID, USER_NAME, USER_EMAIL, USER_ROLE, PASSWORD " +
                     "FROM USERS WHERE USER_EMAIL=? AND USER_ROLE=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, role);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            String stored = rs.getString("PASSWORD");
            if (!PasswordUtil.verify(password, stored)) return null;

            // If legacy plaintext matched, upgrade to hashed so next login is secure.
            if (stored != null && !stored.startsWith("pbkdf2$")) {
                try (PreparedStatement ups = con.prepareStatement(
                        "UPDATE USERS SET PASSWORD=? WHERE USERID=?")) {
                    ups.setString(1, PasswordUtil.hash(password));
                    ups.setInt(2, rs.getInt("USERID"));
                    ups.executeUpdate();
                }
            }

            Users u = new Users();
            u.setUserID(rs.getInt("USERID"));
            u.setUser_name(rs.getString("USER_NAME"));
            u.setUser_email(rs.getString("USER_EMAIL"));
            u.setUser_role(rs.getString("USER_ROLE"));
            return u;
            

        } catch (Exception e) {
            throw new RuntimeException("login error: " + e.getMessage(), e);
        }

    }

}
