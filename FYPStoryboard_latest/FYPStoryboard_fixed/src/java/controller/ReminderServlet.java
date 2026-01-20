package controller;

import dao.ProjectDAO;
import dao.ReminderDAO;
import dao.StudentDAO;
import model.entity.Reminders;
import model.entity.Users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/reminders")
public class ReminderServlet extends HttpServlet {

    private int getProjectID(Users u) {
        int studentID = new StudentDAO().getStudentIDByUserID(u.getUserID());
        return new ProjectDAO().getOrCreateProjectForStudent(studentID);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        int projectID = getProjectID(u);
        ReminderDAO dao = new ReminderDAO();

        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Reminders existing = dao.getById(id);
            if (existing == null) {
                resp.sendRedirect(req.getContextPath() + "/reminders");
                return;
            }
            req.setAttribute("reminder", existing);
            req.getRequestDispatcher("studentRemindersForm.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            dao.delete(id);
            resp.sendRedirect(req.getContextPath() + "/reminders");
            return;
        }

        req.setAttribute("reminderList", dao.listByProject(projectID));
        req.getRequestDispatcher("studentRemindersList.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        int projectID = getProjectID(u);

        String idStr = req.getParameter("id");
        boolean isUpdate = idStr != null && !idStr.trim().isEmpty();

        Reminders r = new Reminders();
        if (isUpdate) r.setReminderID(Integer.parseInt(idStr));
        r.setReminder_name(req.getParameter("name"));
        r.setDue_date(java.sql.Date.valueOf(req.getParameter("due")));
        r.setSubmission_status(req.getParameter("status"));
        r.setRecurrence(req.getParameter("recurrence"));
        r.setProjectID(projectID);

        ReminderDAO dao = new ReminderDAO();
        if (isUpdate) dao.update(r);
        else dao.insert(r);

        resp.sendRedirect(req.getContextPath() + "/reminders");
    }
}

