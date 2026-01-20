package controller;

import dao.*;
import model.entity.Reminders;
import model.entity.Users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/studentDashboard")
public class StudentDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        if (!"STUDENT".equals(u.getUser_role())) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        StudentDAO studentDAO = new StudentDAO();
        int studentID = studentDAO.getStudentIDByUserID(u.getUserID());
        int projectID = new ProjectDAO().getOrCreateProjectForStudent(studentID);

        // Summary counts (existing)
        int notesCount = new NotesDAO().countByUserAndProject(u.getUserID(), projectID);
        int progressCount = new ProgressSubmissionDAO().countByProject(projectID);
        int reminderCount = new ReminderDAO().countByProject(projectID);

        // Show up to 2 closest reminders in "Next Deadline" panel
        java.util.List<Reminders> nextReminders = new ReminderDAO().getNextTwoRemindersByProject(projectID);

        // ✅ New info
        String supervisedBy = studentDAO.getSupervisorNameByUserID(u.getUserID());
        int upcomingDeadlines = studentDAO.countUpcomingDeadlinesForStudentProject(projectID);

        req.setAttribute("notesCount", notesCount);
        req.setAttribute("progressCount", progressCount);
        req.setAttribute("reminderCount", reminderCount);
        req.setAttribute("nextReminders", nextReminders);

        req.setAttribute("supervisedBy", supervisedBy);
        req.setAttribute("upcomingDeadlines", upcomingDeadlines);

        req.getRequestDispatcher("studentDashboard.jsp").forward(req, resp);
    }
}
