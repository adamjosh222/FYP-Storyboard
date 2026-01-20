package controller;

import dao.NotesDAO;
import dao.ProjectDAO;
import dao.StudentDAO;
import model.entity.Notes;
import model.entity.Users;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;




@WebServlet("/notes")
public class NotesServlet extends HttpServlet {

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
        NotesDAO dao = new NotesDAO();

        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            dao.delete(id);
            resp.sendRedirect(req.getContextPath() + "/notes");
            return;
        }

        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("note", dao.findById(id));
            req.getRequestDispatcher("studentNotesForm.jsp").forward(req, resp);
            return;
        }

        // default list
        req.setAttribute("notesList", dao.listByUserAndProject(u.getUserID(), projectID));
        req.getRequestDispatcher("studentNotesList.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        int projectID = getProjectID(u);
        NotesDAO dao = new NotesDAO();

        String notesID = req.getParameter("notesID"); // hidden utk edit
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        if (notesID != null && !notesID.isEmpty()) {
            // update
            Notes n = new Notes();
            n.setNotesID(Integer.parseInt(notesID));
            n.setNotes_title(title);
            n.setNotes_content(content);
            dao.update(n);
        } else {
            // insert
            Notes n = new Notes();
            n.setNotes_title(title);
            n.setNotes_content(content);
            n.setCreated_date(new java.sql.Date(System.currentTimeMillis()));
            n.setUserID(u.getUserID());
            n.setProjectID(projectID);
            dao.insert(n);
        }

        resp.sendRedirect(req.getContextPath() + "/notes");
    }
}
