package controller;

import dao.ProgressSubmissionDAO;
import dao.ProjectDAO;
import dao.SupervisorDAO;
import model.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Supervisor project details (hub) page.
 */
@WebServlet("/supervisorProjectDetails")
public class SupervisorProjectDetailsServlet extends HttpServlet {

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

        String projectIDStr = req.getParameter("projectID");
        if (projectIDStr == null || projectIDStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/supervisorProject");
            return;
        }

        int projectID;
        try {
            projectID = Integer.parseInt(projectIDStr);
        } catch (NumberFormatException ex) {
            resp.sendRedirect(req.getContextPath() + "/supervisorProject");
            return;
        }

        Users u = (Users) req.getSession().getAttribute("user");
        int supervisorID = new SupervisorDAO().getSupervisorIDByUserID(u.getUserID());

        ProjectDAO pdao = new ProjectDAO();
        java.util.Map<String, Object> project = pdao.getProjectDetailsForSupervisor(projectID, supervisorID);
        if (project == null) {
            resp.sendRedirect(req.getContextPath() + "/supervisorProject?notfound=1");
            return;
        }

        ProgressSubmissionDAO psdao = new ProgressSubmissionDAO();
        req.setAttribute("project", project);
        req.setAttribute("progressList", psdao.listByProject(projectID));
        req.setAttribute("progressCount", psdao.countByProject(projectID));
        req.getRequestDispatcher("supervisorProjectDetails.jsp").forward(req, resp);
    }
}
