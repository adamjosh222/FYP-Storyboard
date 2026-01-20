package controller;

import dao.ProgressSubmissionDAO;
import dao.ProjectDAO;
import dao.StudentDAO;
import model.entity.Progresssubmission;
import model.entity.Users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;

@WebServlet("/progress")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024) // 10MB
public class ProgressServlet extends HttpServlet {

    private int getProjectID(Users u) {
        int studentID = new StudentDAO().getStudentIDByUserID(u.getUserID());
        return new ProjectDAO().getOrCreateProjectForStudent(studentID);
    }

    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    Users u = (Users) req.getSession().getAttribute("user");
    if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

    ProgressSubmissionDAO dao = new ProgressSubmissionDAO();

    // STUDENT: view own progress
    if ("STUDENT".equals(u.getUser_role())) {
        int projectID = getProjectID(u);

        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            dao.delete(id);
            resp.sendRedirect(req.getContextPath() + "/progress");
            return;
        }

        req.setAttribute("progressList", dao.listByProject(projectID));
        req.setAttribute("projectID", projectID);
        req.getRequestDispatcher("studentProgressList.jsp").forward(req, resp);
        return;
    }

    // SUPERVISOR: must pass projectID
    String projectIDStr = req.getParameter("projectID");
    if (projectIDStr == null || projectIDStr.trim().isEmpty()) {
        resp.sendRedirect(req.getContextPath() + "/supervisorDashboard");
        return;
    }

    int projectID = Integer.parseInt(projectIDStr);

    // Optional: decide Back button target for supervisor progress list
    String returnTo = req.getParameter("returnTo");
    String backUrl = req.getContextPath() + "/supervisorProgress";
    String backLabel = "\u2190 Back to Progress";
    if ("projectDetails".equals(returnTo)) {
        backUrl = req.getContextPath() + "/supervisorProjectDetails?projectID=" + projectID;
        backLabel = "\u2190 Back to Project";
    } else if ("projects".equals(returnTo)) {
        backUrl = req.getContextPath() + "/supervisorProject";
        backLabel = "\u2190 Back to Projects";
    }

    req.setAttribute("progressList", dao.listByProject(projectID));
    req.setAttribute("projectID", projectID);
    req.setAttribute("backUrl", backUrl);
    req.setAttribute("backLabel", backLabel);
    req.getRequestDispatcher("supervisorProgressList.jsp").forward(req, resp);
}


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        // Only student submit
        if (!"STUDENT".equals(u.getUser_role())) {
            resp.sendRedirect(req.getContextPath() + "/progress");
            return;
        }

        int projectID = getProjectID(u);
        String dateStr = req.getParameter("date"); // yyyy-mm-dd
        String status = req.getParameter("status");

        Progresssubmission p = new Progresssubmission();
        p.setSubmitted_date(Date.valueOf(dateStr));
        p.setProgress_status(status);
        p.setProjectID(projectID);

        ProgressSubmissionDAO dao = new ProgressSubmissionDAO();
        int progressID = dao.insertAndReturnId(p);

        // Optional file upload (student)
        Part filePart = null;
        try { filePart = req.getPart("studentFile"); } catch (Exception ignored) {}
        if (filePart != null && filePart.getSize() > 0 && progressID > 0) {
            String submittedName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            // Basic sanitization
            submittedName = submittedName.replaceAll("[^a-zA-Z0-9._-]", "_");

            // NOTE: getRealPath("/uploads") can be null on some app servers (WAR not exploded).
            // Fallback to a stable folder under user home.
            String uploadsRoot = getServletContext().getRealPath("/uploads");
            if (uploadsRoot == null) {
                uploadsRoot = System.getProperty("user.home") + java.io.File.separator + "FYPStoryboard_uploads";
            }
            Path dir = Paths.get(uploadsRoot, "progress", String.valueOf(progressID));
            Files.createDirectories(dir);

            String storedName = "student_" + submittedName;
            Path target = dir.resolve(storedName);

            try (InputStream in = filePart.getInputStream()) {
                Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            // Store relative path for portability
            String relativePath = "uploads/progress/" + progressID + "/" + storedName;
            dao.updateStudentFile(progressID, submittedName, relativePath);
        }

        resp.sendRedirect(req.getContextPath() + "/progress");
    }
}
