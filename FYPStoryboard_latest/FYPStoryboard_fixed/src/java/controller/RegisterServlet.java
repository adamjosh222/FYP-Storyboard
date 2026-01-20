package controller;

import dao.StudentDAO;
import dao.SupervisorDAO;
import dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) get basic input
        String role = req.getParameter("role");     // STUDENT / SUPERVISOR
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Basic validation
        if (role == null || name == null || email == null || password == null ||
            role.trim().isEmpty() || name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {

            req.setAttribute("error", "Please fill in all required fields.");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 2) Check email exists
        UserDAO userDAO = new UserDAO();
        if (userDAO.emailExists(email)) {
            req.setAttribute("error", "Email already exists. Please use another email.");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 3) Create USERS record (password chosen by the user)
        int userID = userDAO.registerUser(name, email, role, password);

        if (userID <= 0) {
            req.setAttribute("error", "Register failed. Please try again.");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 4) Create STUDENT / SUPERVISOR record
        try {
            if ("STUDENT".equalsIgnoreCase(role)) {

                String matric = req.getParameter("matric");
                String program = req.getParameter("program");

                if (matric == null || program == null || matric.trim().isEmpty() || program.trim().isEmpty()) {
                    req.setAttribute("error", "Student fields (Matric No, Program) are required.");
                    req.getRequestDispatcher("register.jsp").forward(req, resp);
                    return;
                }

                new StudentDAO().createStudent(userID, name, email, matric, program);

            } else if ("SUPERVISOR".equalsIgnoreCase(role)) {

                String phone = req.getParameter("phone");
                String dept  = req.getParameter("dept");
                String room  = req.getParameter("room");

                // phone/dept/room can be empty; set defaults to avoid null
                if (phone == null) phone = "";
                if (dept == null)  dept = "";
                if (room == null)  room = "";

                // Create supervisor record
                new SupervisorDAO().createSupervisor(userID, name, email, phone, dept, room);

            } else {
                req.setAttribute("error", "Invalid role selected.");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
                return;
            }

        } catch (Exception e) {
            // If student/supervisor insert fails, show a clear error message.
            req.setAttribute("error", "Register failed: " + e.getMessage());
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // 5) Success -> go login
        req.setAttribute("msg", "Registration successful. Please log in using the password you created.");
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }
}
