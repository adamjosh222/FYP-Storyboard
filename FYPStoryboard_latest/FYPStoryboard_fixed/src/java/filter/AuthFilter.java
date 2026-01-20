package filter;

import model.entity.Users;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Centralized authentication/authorization guard.
 *
 * Goals:
 *  - Avoid repeating "if (session user == null) redirect login" in every JSP.
 *  - Block role-mismatch access (student pages vs supervisor pages).
 *  - Keep static assets accessible.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Always allow static resources
        if (path.startsWith("/css/") || path.startsWith("/images/") || path.startsWith("/js/")
                || path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png")
                || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif")
                || path.endsWith(".svg") || path.endsWith(".woff") || path.endsWith(".woff2")) {
            chain.doFilter(request, response);
            return;
        }

        // Public pages/routes
        if (path.equals("/") || path.equals("/login") || path.equals("/register")
                || path.equals("/login.jsp") || path.equals("/register.jsp")
                || path.equals("/studentLogin.jsp") || path.equals("/supervisorLogin.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        // Logout should work even if session already expired
        if (path.equals("/logout") || path.equals("/logout.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        Users u = (session == null) ? null : (Users) session.getAttribute("user");

        // Anything else requires login
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = u.getUser_role();

        // Student-only routes/pages
        boolean studentArea = path.startsWith("/student")
                || path.equals("/notes")
                || path.equals("/progress")
                || path.equals("/reminders")
                || path.equals("/progress.jsp");

        // Supervisor-only routes/pages
        boolean supervisorArea = path.startsWith("/supervisor")
                || path.equals("/supervisorDashboard")
                || path.equals("/supervisorDashboard.jsp")
                || path.startsWith("/supervisor");

        // Specific JSPs that are obviously student/supervisor pages
        if (path.endsWith("studentDashboard.jsp") || path.contains("studentNotes")
                || path.contains("studentProgress") || path.contains("studentReminders")
                || path.endsWith("studentProject.jsp") || path.endsWith("studentProfile.jsp")) {
            studentArea = true;
        }
        if (path.endsWith("supervisorStudentsList.jsp") || path.endsWith("supervisorProgressList.jsp")
                || path.endsWith("supervisorFeedbackForm.jsp") || path.endsWith("supervisorProject.jsp")
                || path.endsWith("supervisorProfile.jsp")) {
            supervisorArea = true;
        }

        if (studentArea && !"STUDENT".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (supervisorArea && !"SUPERVISOR".equals(role)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Default allow
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
