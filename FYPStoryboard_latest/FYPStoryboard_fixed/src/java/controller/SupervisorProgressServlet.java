package controller;

import model.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Convenience route used by the UI navbar.
 *
 * "Progress" should be different from "My Students":
 * - My Students: assign/manage supervised students
 * - Progress: review submissions
 *
 * So we send the supervisor to a dedicated progress landing page that lists
 * supervised students with a "View Progress" action.
 */
@WebServlet("/supervisorProgress")
public class SupervisorProgressServlet extends HttpServlet {

    private boolean guardSupervisor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        if (!"SUPERVISOR".equals(u.getUser_role())) {
            resp.sendRedirect(req.getContextPath() + "/studentDashboard");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!guardSupervisor(req, resp)) return;
        // Progress landing page removed. Submissions remain accessible from Project Details.
        resp.sendRedirect(req.getContextPath() + "/supervisorDashboard");
    }
}
