package controller;

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
 * Projects module (Supervisor).
 *
 * - GET: shows list of supervised students + their projects (if any)
 * - POST: optional action to auto-create a project for a supervised student
 */
@WebServlet("/supervisorProject")
public class SupervisorProjectServlet extends HttpServlet {

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

        Users u = (Users) req.getSession().getAttribute("user");
        int supervisorID = new SupervisorDAO().getSupervisorIDByUserID(u.getUserID());

        // List supervised students + their project info (if already created)
        req.setAttribute("students", new SupervisorDAO().listStudentsWithProject(supervisorID));
        req.setAttribute("projects", new ProjectDAO().listProjectsForSupervisor(supervisorID));

        req.getRequestDispatcher("supervisorProjectsList.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!guardSupervisor(req, resp)) return;

        Users u = (Users) req.getSession().getAttribute("user");
        int supervisorID = new SupervisorDAO().getSupervisorIDByUserID(u.getUserID());

        String createStudentIDStr = req.getParameter("createStudentID");
        if (createStudentIDStr != null && !createStudentIDStr.trim().isEmpty()) {
            int studentID = Integer.parseInt(createStudentIDStr);

            // Safety: only allow creating project for students under this supervisor
            boolean owned = new SupervisorDAO().listStudentsWithProject(supervisorID)
                    .stream().anyMatch(m -> ((Integer) m.get("studentID")) == studentID);
            if (owned) {
                new ProjectDAO().getOrCreateProjectForStudent(studentID);
            }

            resp.sendRedirect(req.getContextPath() + "/supervisorProject?created=1");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/supervisorProject");
    }
}
