package controller;

import dao.FeedbackDAO;
import dao.ProgressSubmissionDAO;
import dao.SupervisorDAO;
import dao.ProjectDAO;
import model.entity.Feedbacks;
import model.entity.Users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/supervisorDashboard")
public class SupervisorDashboardServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!guardSupervisor(req, resp)) return;

        Users u = (Users) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        if (action == null) action = "dashboard";

        SupervisorDAO svDAO = new SupervisorDAO();
        int supervisorID = svDAO.getSupervisorIDByUserID(u.getUserID());

        switch (action) {

            case "students": {
                req.setAttribute("students", svDAO.listStudentsWithProject(supervisorID));
                req.setAttribute("unassignedStudents", svDAO.listUnassignedStudents());
                req.getRequestDispatcher("supervisorStudentsList.jsp").forward(req, resp);
                return;
            }

            case "progress": {
                String projectIDStr = req.getParameter("projectID");
                if (projectIDStr == null || projectIDStr.trim().isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/supervisorDashboard?action=students");
                    return;
                }
                int projectID = Integer.parseInt(projectIDStr);

                // Determine where user came from so the Back button returns to the right place
                String returnTo = req.getParameter("returnTo");
                String backUrl = req.getContextPath() + "/supervisorProgress";
                String backLabel = "\u2190 Back to Progress";

                if ("projectDetails".equals(returnTo)) {
                    backUrl = req.getContextPath() + "/supervisorProjectDetails?projectID=" + projectID;
                    backLabel = "\u2190 Back to Project";
                } else if ("projects".equals(returnTo)) {
                    backUrl = req.getContextPath() + "/supervisorProject";
                    backLabel = "\u2190 Back to Projects";
                } else if ("progress".equals(returnTo)) {
                    backUrl = req.getContextPath() + "/supervisorProgress";
                    backLabel = "\u2190 Back to Progress";
                }

                ProgressSubmissionDAO pdao = new ProgressSubmissionDAO();
                req.setAttribute("progressList", pdao.listByProject(projectID));
                req.setAttribute("projectID", projectID);
                req.setAttribute("backUrl", backUrl);
                req.setAttribute("backLabel", backLabel);
                req.getRequestDispatcher("supervisorProgressList.jsp").forward(req, resp);
                return;
            }

            /**
             * Progress landing page.
             *
             * "My Students" is used for assigning + managing supervised students.
             * "Progress" is used to review submissions, so we first list supervised students
             * and then allow the supervisor to jump into the progress list for a student's
             * project.
             */
            case "progressStudents": {
                req.setAttribute("students", svDAO.listStudentsWithProject(supervisorID));
                req.getRequestDispatcher("supervisorProgressStudents.jsp").forward(req, resp);
                return;
            }

            case "feedback": {
                String progressIDStr = req.getParameter("progressID");
                if (progressIDStr == null || progressIDStr.trim().isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/supervisorDashboard?action=students");
                    return;
                }
                int progressID = Integer.parseInt(progressIDStr);

                FeedbackDAO fdao = new FeedbackDAO();
                req.setAttribute("progressID", progressID);
                req.setAttribute("feedbackList", fdao.listByProgress(progressID));
                req.getRequestDispatcher("supervisorFeedbackForm.jsp").forward(req, resp);
                return;
            }
              

            default: {
                
                int studentsCount = svDAO.countStudents(supervisorID);
                int activeProjects = new ProjectDAO().countActiveProjectsForSupervisor(supervisorID);
                int pendingProgress = svDAO.countPendingProgress(supervisorID);
                int upcomingDeadlines = svDAO.countUpcomingDeadlines(supervisorID);

                req.setAttribute("studentsCount", studentsCount);
                req.setAttribute("activeProjects", activeProjects);
                req.setAttribute("pendingProgress", pendingProgress);
                req.setAttribute("upcomingDeadlines", upcomingDeadlines);
                req.getRequestDispatcher("supervisorDashboard.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!guardSupervisor(req, resp)) return;

        Users u = (Users) req.getSession().getAttribute("user");
        SupervisorDAO svDAO = new SupervisorDAO();
        int supervisorID = svDAO.getSupervisorIDByUserID(u.getUserID());

        // Assign student to this supervisor
        String assignStudentIDStr = req.getParameter("assignStudentID");
        if (assignStudentIDStr != null && !assignStudentIDStr.trim().isEmpty()) {
            int studentID = Integer.parseInt(assignStudentIDStr);
            svDAO.assignStudent(studentID, supervisorID);
            resp.sendRedirect(req.getContextPath() + "/supervisorDashboard?action=students&assigned=1");
            return;
        }

        String progressIDStr = req.getParameter("progressID");
        String feedbackText = req.getParameter("feedback_text");

        if (progressIDStr == null || progressIDStr.trim().isEmpty()
                || feedbackText == null || feedbackText.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/supervisorDashboard?action=students");
            return;
        }

        int progressID = Integer.parseInt(progressIDStr);

        Feedbacks f = new Feedbacks();
        f.setFeedback_text(feedbackText);
        f.setFeedback_date(new java.sql.Date(System.currentTimeMillis()));
        f.setSupervisorID(supervisorID);
        f.setProgressID(progressID);

        new FeedbackDAO().insert(f);

        resp.sendRedirect(req.getContextPath() + "/supervisorDashboard?action=feedback&progressID=" + progressID);
    }
}
