<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.entity.Reminders" %>
<%
  List<Reminders> nextList = (List<Reminders>) request.getAttribute("nextReminders");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Student Dashboard</title>
  <%@ include file="includes/head.jspf" %>
</head>
<body>

<%@ include file="includes/navbar-student.jspf" %>

<div class="page-wrap">
  <div class="container">
    <div class="mb-4">
      <h1 class="h3 app-title mb-1">Student Dashboard</h1>
      <div class="app-subtitle">Welcome, <span class="fw-semibold text-primary">${sessionScope.user.user_name}</span></div>
    </div>

    <!-- Overview cards -->
    <div class="row g-3 mb-4">
      <div class="col-6 col-lg-3">
        <div class="card app-card h-100">
          <div class="card-body">
            <div class="text-muted small">Total Notes</div>
            <div class="fs-3 fw-bold">${notesCount}</div>
          </div>
        </div>
      </div>
      <div class="col-6 col-lg-3">
        <div class="card app-card h-100">
          <div class="card-body">
            <div class="text-muted small">Progress Submissions</div>
            <div class="fs-3 fw-bold">${progressCount}</div>
          </div>
        </div>
      </div>
      <div class="col-6 col-lg-3">
        <div class="card app-card h-100">
          <div class="card-body">
            <div class="text-muted small">Reminders</div>
            <div class="fs-3 fw-bold">${reminderCount}</div>
          </div>
        </div>
      </div>
      <div class="col-6 col-lg-3">
        <div class="card app-card h-100">
          <div class="card-body">
            <div class="text-muted small">Upcoming Deadlines</div>
            <div class="fs-3 fw-bold">${upcomingDeadlines}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="row g-3">
      <div class="col-lg-7">
        <div class="card app-card">
          <div class="card-body p-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <h2 class="h5 mb-0">Next Deadline</h2>
              <span class="badge text-bg-secondary">Supervised by: ${supervisedBy}</span>
            </div>

            <% if (nextList == null || nextList.isEmpty()) { %>
              <div class="alert alert-secondary mb-0">No reminder created yet.</div>
            <% } else { %>
              <div class="table-responsive">
                <table class="table table-sm align-middle mb-0">
                  <thead>
                    <tr>
                      <th>Reminder</th>
                      <th>Due Date</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <% for (Reminders next : nextList) { %>
                      <tr>
                        <td><%= next.getReminder_name() %></td>
                        <td><%= next.getDue_date() %></td>
                        <td>
                          <span class="badge text-bg-<%= (next.getSubmission_status()!=null && next.getSubmission_status().equalsIgnoreCase("COMPLETED")) ? "success" : "warning" %>">
                            <%= (next.getSubmission_status()==null || next.getSubmission_status().trim().isEmpty()) ? "PENDING" : next.getSubmission_status() %>
                          </span>
                        </td>
                      </tr>
                    <% } %>
                  </tbody>
                </table>
              </div>
            <% } %>
          </div>
        </div>
      </div>

      <div class="col-lg-5">
        <div class="card app-card">
          <div class="card-body p-4">
            <h2 class="h5 mb-3">Quick Actions</h2>
            <div class="d-grid gap-2">
              <a class="btn btn-primary" href="studentProfile"><i class="bi bi-person-circle me-1"></i>My Profile</a>
              <a class="btn btn-outline-primary" href="notes"><i class="bi bi-stickies me-1"></i>Notes Taking</a>
              <a class="btn btn-outline-primary" href="progress"><i class="bi bi-graph-up-arrow me-1"></i>Progress Submission</a>
              <a class="btn btn-outline-primary" href="reminders"><i class="bi bi-bell me-1"></i>Reminders & Deadlines</a>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</div>

<%@ include file="includes/footer.jspf" %>

<script>
  // Fix: when user navigates back from another page, some browsers restore
  // the page from back-forward cache and the JSP-rendered numbers may appear blank.
  // Force a refresh when the page is restored from cache.
  window.addEventListener('pageshow', function (e) {
    if (e.persisted) {
      window.location.reload();
    }
  });
</script>
</body>
</html>
