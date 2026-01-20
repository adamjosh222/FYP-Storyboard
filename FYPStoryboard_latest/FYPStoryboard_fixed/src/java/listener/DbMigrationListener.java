package listener;

import dao.DBConnection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.*;

/**
 * Minimal DB auto-migration for Derby.
 *
 * Tujuan: elak "error page" bila schema DB pengguna belum ada column baru.
 * - STUDENTS.SUPERVISORID (untuk assign supervisor)
 *
 * Migrations ni selamat: akan check dulu column wujud atau tidak.
 */
@WebListener
public class DbMigrationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(true);

            ensureColumn(con, "STUDENTS", "SUPERVISORID", "INT");

        } catch (Exception e) {
            // Jangan crash app kalau migration gagal;
            // Log ke server console untuk debugging.
            System.err.println("[DB MIGRATION] Failed: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no-op
    }

    private void ensureColumn(Connection con, String table, String column, String typeSql) throws SQLException {
        if (hasColumn(con, table, column)) return;

        String ddl = "ALTER TABLE " + table + " ADD " + column + " " + typeSql;
        try (Statement st = con.createStatement()) {
            st.executeUpdate(ddl);
            System.out.println("[DB MIGRATION] Applied: " + ddl);
        }
    }

    private boolean hasColumn(Connection con, String table, String column) throws SQLException {
        DatabaseMetaData md = con.getMetaData();
        // Derby stores unquoted identifiers in upper-case
        try (ResultSet rs = md.getColumns(null, null, table.toUpperCase(), column.toUpperCase())) {
            return rs.next();
        }
    }
}
