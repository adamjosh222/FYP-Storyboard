package controller;

import dao.ProgressSubmissionDAO;
import model.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URLEncoder;

@WebServlet("/supervisorUploadProgressFile")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024) // 10MB
public class SupervisorUploadProgressFileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        if (!"SUPERVISOR".equals(u.getUser_role())) {
            resp.sendRedirect(req.getContextPath() + "/supervisorDashboard");
            return;
        }

        String progressIDStr = req.getParameter("progressID");
        String projectIDStr = req.getParameter("projectID");
        String returnTo = req.getParameter("returnTo");
        String back = req.getParameter("back");

        if (progressIDStr == null || progressIDStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/supervisorDashboard");
            return;
        }

        int progressID = Integer.parseInt(progressIDStr);

        Part filePart = null;
        try { filePart = req.getPart("supervisorFile"); } catch (Exception ignored) {}

        if (filePart != null && filePart.getSize() > 0) {
            String submittedName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            submittedName = submittedName.replaceAll("[^a-zA-Z0-9._-]", "_");

            String uploadsRoot = getServletContext().getRealPath("/uploads");
            if (uploadsRoot == null) {
                uploadsRoot = System.getProperty("user.home") + java.io.File.separator + "FYPStoryboard_uploads";
            }
            Path dir = Paths.get(uploadsRoot, "progress", String.valueOf(progressID));
            Files.createDirectories(dir);

            String storedName = "supervisor_" + submittedName;
            Path target = dir.resolve(storedName);

            try (InputStream in = filePart.getInputStream()) {
                Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            String relativePath = "uploads/progress/" + progressID + "/" + storedName;
            new ProgressSubmissionDAO().updateSupervisorFile(progressID, submittedName, relativePath);
        }

        // Redirect back based on where upload came from
        if ("feedback".equalsIgnoreCase(returnTo)) {
            String redirect = req.getContextPath() + "/feedback?progressID=" + progressID;
            if (back != null && !back.trim().isEmpty()) {
                redirect += "&back=" + URLEncoder.encode(back, "UTF-8");
            }
            resp.sendRedirect(redirect);
            return;
        }

        // Default: go back to progress list with same navigation context
        String redirect = req.getContextPath() + "/progress";
        if (projectIDStr != null && projectIDStr.trim().length() > 0) {
            redirect += "?projectID=" + projectIDStr;
            if (returnTo != null && returnTo.trim().length() > 0) {
                redirect += "&returnTo=" + returnTo;
            }
        }
        resp.sendRedirect(redirect);
    }
}
