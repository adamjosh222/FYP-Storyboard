package controller;

import dao.UserDAO;
import model.entity.Users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String pass = req.getParameter("password");
        String role = req.getParameter("role");

        Users u = new UserDAO().login(email, pass, role);
        if (u == null) {
            req.setAttribute("error", "Login failed. Please check your email, password, and role.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", u);

        // Log before redirect (useful for debugging)
        System.out.println("LOGIN TRY: " + email + " role=" + role);

        if ("SUPERVISOR".equals(u.getUser_role())) {
            // IMPORTANT: redirect to the servlet so dashboard stats are prepared
            resp.sendRedirect(req.getContextPath() + "/supervisorDashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/studentDashboard");
        }


    }
}
