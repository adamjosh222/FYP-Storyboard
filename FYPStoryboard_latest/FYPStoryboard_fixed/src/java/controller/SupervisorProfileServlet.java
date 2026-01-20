package controller;

import dao.SupervisorDAO;
import model.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/supervisorProfile")
public class SupervisorProfileServlet extends HttpServlet {

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
        SupervisorDAO dao = new SupervisorDAO();
        Map<String, Object> profile = dao.getSupervisorProfileByUserID(u.getUserID());
        req.setAttribute("profile", profile);

        req.getRequestDispatcher("supervisorProfile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!guardSupervisor(req, resp)) return;

        Users u = (Users) req.getSession().getAttribute("user");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String dept = req.getParameter("department");
        String room = req.getParameter("room");

        new SupervisorDAO().updateSupervisorProfile(u.getUserID(), name, email, phone, dept, room);

        u.setUser_name(name);
        u.setUser_email(email);
        req.getSession().setAttribute("user", u);

        resp.sendRedirect(req.getContextPath() + "/supervisorProfile?updated=1");
    }
}
