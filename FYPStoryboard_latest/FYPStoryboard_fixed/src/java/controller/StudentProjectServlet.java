package controller;

import dao.ProjectDAO;
import dao.StudentDAO;
import model.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Student Project page.
 *
 * The UI navbar links to /studentProject, so we provide this servlet to avoid 404.
 * For now, it forwards to the existing studentProject.jsp (static view).
 *
 * Later you can enhance it to load real project info from DB.
 */
@WebServlet("/studentProject")
public class StudentProjectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (!"STUDENT".equals(u.getUser_role())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Ensure student has a project record (same approach used in StudentDashboard)
        int studentID = new StudentDAO().getStudentIDByUserID(u.getUserID());
        new ProjectDAO().getOrCreateProjectForStudent(studentID);

        req.getRequestDispatcher("studentProject.jsp").forward(req, resp);
    }
}
