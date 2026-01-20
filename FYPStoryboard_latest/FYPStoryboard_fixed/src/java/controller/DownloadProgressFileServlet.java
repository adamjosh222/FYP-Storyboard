package controller;

import dao.ProgressSubmissionDAO;
import dao.ProjectDAO;
import dao.StudentDAO;
import model.entity.Progresssubmission;
import model.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/downloadProgressFile")
public class DownloadProgressFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        String idStr = req.getParameter("progressID");
        String who = req.getParameter("who"); // student | supervisor
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendError(400, "Missing progressID");
            return;
        }
        int progressID = Integer.parseInt(idStr);

        Progresssubmission p = new ProgressSubmissionDAO().getById(progressID);
        if (p == null) {
            resp.sendError(404, "Not found");
            return;
        }

        // Authorization
        if ("STUDENT".equals(u.getUser_role())) {
            int studentID = new StudentDAO().getStudentIDByUserID(u.getUserID());
            int myProjectID = new dao.ProjectDAO().getOrCreateProjectForStudent(studentID);
            if (p.getProjectID() != myProjectID) {
                resp.sendError(403, "Forbidden");
                return;
            }
        } else if ("SUPERVISOR".equals(u.getUser_role())) {
            boolean ok = new ProjectDAO().isSupervisorOwnerOfProject(u.getUserID(), p.getProjectID());
            // Note: in this app supervisorID is stored separately; fallback to session attr if present
            Object svIdObj = req.getSession().getAttribute("supervisorID");
            int supervisorID = (svIdObj != null) ? Integer.parseInt(svIdObj.toString()) : u.getUserID();
            ok = new ProjectDAO().isSupervisorOwnerOfProject(supervisorID, p.getProjectID());
            if (!ok) {
                resp.sendError(403, "Forbidden");
                return;
            }
        }

        boolean wantStudent = "student".equalsIgnoreCase(who);
        boolean wantSupervisor = "supervisor".equalsIgnoreCase(who);

        String fileName = null;
        String relPath = null;
        if (wantStudent) {
            fileName = p.getStudentFileName();
            relPath = p.getStudentFilePath();
        } else if (wantSupervisor) {
            fileName = p.getSupervisorFileName();
            relPath = p.getSupervisorFilePath();
        } else {
            resp.sendError(400, "Invalid 'who' parameter");
            return;
        }

        if (relPath == null || relPath.trim().isEmpty()) {
            resp.sendError(404, "No file");
            return;
        }

        // Resolve to real path inside webapp.
        // getRealPath can be null on some servers, so fall back to a stable folder under user home.
        String abs = getServletContext().getRealPath("/" + relPath);
        Path path;
        if (abs == null) {
            String base = System.getProperty("user.home") + java.io.File.separator + "FYPStoryboard_uploads";
            String cleaned = relPath;
            if (cleaned.startsWith("/")) cleaned = cleaned.substring(1);
            if (cleaned.startsWith("uploads/")) cleaned = cleaned.substring("uploads/".length());
            path = Paths.get(base).resolve(cleaned);
        } else {
            path = Paths.get(abs);
        }
        if (!Files.exists(path)) {
            resp.sendError(404, "File missing on server");
            return;
        }

        // Content type
        String contentType = getServletContext().getMimeType(path.getFileName().toString());
        if (contentType == null) contentType = "application/octet-stream";
        resp.setContentType(contentType);
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + (fileName != null ? fileName : path.getFileName().toString()) + "\"");

        try (InputStream in = Files.newInputStream(path)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                resp.getOutputStream().write(buffer, 0, len);
            }
        }
    }
}
