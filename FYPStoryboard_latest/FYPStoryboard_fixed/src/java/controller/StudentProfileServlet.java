package controller;

import dao.StudentDAO;
import model.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/studentProfile")
public class StudentProfileServlet extends HttpServlet {

    private boolean guardStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        if (!"STUDENT".equals(u.getUser_role())) {
            resp.sendRedirect(req.getContextPath() + "/supervisorDashboard");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!guardStudent(req, resp)) return;

        Users u = (Users) req.getSession().getAttribute("user");
        StudentDAO dao = new StudentDAO();
        Map<String, Object> profile = dao.getStudentProfileByUserID(u.getUserID());
        req.setAttribute("profile", profile);

        req.getRequestDispatcher("studentProfile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!guardStudent(req, resp)) return;

        Users u = (Users) req.getSession().getAttribute("user");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String matric = req.getParameter("matric");
        String program = req.getParameter("program");

        new StudentDAO().updateStudentProfile(u.getUserID(), name, email, matric, program);

        // keep session user display updated
        u.setUser_name(name);
        u.setUser_email(email);
        req.getSession().setAttribute("user", u);

        resp.sendRedirect(req.getContextPath() + "/studentProfile?updated=1");
    }
}
