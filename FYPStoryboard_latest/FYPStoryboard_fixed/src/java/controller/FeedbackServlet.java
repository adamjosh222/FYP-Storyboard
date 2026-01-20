package controller;

import dao.FeedbackDAO;
import dao.SupervisorDAO;
import model.entity.Feedbacks;
import model.entity.Users;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        String progressIDStr = req.getParameter("progressID");
        if (progressIDStr == null || progressIDStr.trim().isEmpty()) {
            // balik ikut role
            if ("STUDENT".equals(u.getUser_role())) resp.sendRedirect(req.getContextPath() + "/progress");
            else resp.sendRedirect(req.getContextPath() + "/supervisorDashboard?action=students");
            return;
        }

        int progressID = Integer.parseInt(progressIDStr);

        FeedbackDAO dao = new FeedbackDAO();
        req.setAttribute("progressID", progressID);
        req.setAttribute("feedbackList", dao.listByProgress(progressID));

        // preserve where supervisor came from (so Back button returns to previous page)
        String back = req.getParameter("back");
        if (back == null || back.trim().isEmpty()) {
            back = req.getHeader("Referer");
        }
        req.setAttribute("backUrl", back);

        // ✅ guna JSP asal feedback.jsp
        req.getRequestDispatcher("feedback.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Users u = (Users) req.getSession().getAttribute("user");
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }

        // ✅ ONLY supervisor boleh submit
        if (!"SUPERVISOR".equals(u.getUser_role())) {
            resp.sendRedirect(req.getContextPath() + "/studentDashboard");
            return;
        }

        String progressIDStr = req.getParameter("progressID");
        String text = req.getParameter("feedback_text");
        String back = req.getParameter("back");

        if (progressIDStr == null || progressIDStr.trim().isEmpty()
                || text == null || text.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/supervisorDashboard?action=students");
            return;
        }

        int progressID = Integer.parseInt(progressIDStr);
        int supervisorID = new SupervisorDAO().getSupervisorIDByUserID(u.getUserID());

        Feedbacks f = new Feedbacks();
        f.setFeedback_text(text);
        f.setFeedback_date(new java.sql.Date(System.currentTimeMillis()));
        f.setSupervisorID(supervisorID);
        f.setProgressID(progressID);

        new FeedbackDAO().insert(f);

        String redirect = req.getContextPath() + "/feedback?progressID=" + progressID;

        if (back != null && !back.trim().isEmpty()) {
            redirect += "&back=" + URLEncoder.encode(back, "UTF-8");
        }

        resp.sendRedirect(redirect);
    }
}
