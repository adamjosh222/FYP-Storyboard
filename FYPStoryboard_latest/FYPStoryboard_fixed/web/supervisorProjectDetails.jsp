<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.entity.Progresssubmission" %>

<%
  Map<String,Object> project = (Map<String,Object>) request.getAttribute("project");
  List<Progresssubmission> progressList = (List<Progresssubmission>) request.getAttribute("progressList");
  Integer progressCount = (Integer) request.getAttribute("progressCount");
  // Avoid Java 7+ diamond operator (<>).
  if (progressList == null) progressList = new ArrayList<Progresssubmission>();
  if (progressCount == null) progressCount = progressList.size();

  int projectID = (project != null && project.get("projectID") != null) ? (Integer) project.get("projectID") : 0;
%>

<!DOCTYPE html>
<html>
<head>
  <title>Project Details</title>
  <%@ include file="includes/head.jspf" %>
</head>
<body>

<%@ include file="includes/navbar-supervisor.jspf" %>

<div class="page-wrap">
  <div class="container">
    <div class="d-flex flex-wrap justify-content-between align-items-end gap-3 mb-4">
      <div>
        <h1 class="h3 app-title mb-1">Project Details</h1>
        <div class="app-subtitle">Project hub: info + progress submissions</div>
      </div>
      <div class="d-flex gap-2">
        <a class="btn btn-outline-light" href="supervisorProject"><i class="bi bi-arrow-left me-1"></i>Back to Projects</a>
        <a class="btn btn-outline-primary" href="supervisorDashboard?action=progress&projectID=<%= projectID %>&returnTo=projectDetails"><i class="bi bi-graph-up-arrow me-1"></i>Open Submissions</a>
      </div>
    </div>

    <div class="row g-3">
      <div class="col-lg-5">
        <div class="card app-card h-100">
          <div class="card-body p-4">
            <h2 class="h5 mb-3">Project info</h2>

            <div class="mb-3">
              <div class="text-muted small">Student</div>
              <div class="fw-semibold"><%= project.get("studentName") %> <span class="text-muted fw-normal">(<%= project.get("matricNum") %>)</span></div>
              <div class="small text-muted"><%= project.get("studentEmail") %> • <%= project.get("program") %></div>
            </div>

            <div class="mb-3">
              <div class="text-muted small">Title</div>
              <div class="fw-semibold"><%= project.get("projectTitle") %></div>
            </div>

            <div class="mb-3">
              <div class="text-muted small">Description</div>
              <div class="text-muted"><%= project.get("description") != null ? project.get("description") : "-" %></div>
            </div>

            <div class="row g-2">
              <div class="col-6">
                <div class="text-muted small">Start date</div>
                <div class="fw-semibold"><%= project.get("startDate") != null ? project.get("startDate") : "-" %></div>
              </div>
              <div class="col-6">
                <div class="text-muted small">End date</div>
                <div class="fw-semibold"><%= project.get("endDate") != null ? project.get("endDate") : "-" %></div>
              </div>
            </div>

            <div class="mt-3">
              <div class="text-muted small">Status</div>
              <%
                String status = (String) project.get("projectStatus");
                String badgeClass = "bg-secondary";
                if (status != null) {
                  String up = status.toUpperCase();
                  if (up.contains("ONGOING") || up.contains("ACTIVE")) badgeClass = "bg-success";
                  else if (up.contains("PROPOSE") || up.contains("PENDING")) badgeClass = "bg-warning text-dark";
                  else if (up.contains("COMPLETE") || up.contains("DONE")) badgeClass = "bg-info text-dark";
                }
              %>
              <span class="badge <%= badgeClass %>"><%= status != null ? status : "-" %></span>
            </div>
          </div>
        </div>
      </div>

      <div class="col-lg-7">
        <div class="card app-card h-100">
          <div class="card-body p-4">
            <div class="d-flex flex-wrap justify-content-between align-items-center mb-3">
              <h2 class="h5 mb-0">Progress submissions</h2>
              <span class="text-muted small"><%= progressCount %> total</span>
            </div>

            <div class="table-responsive">
              <table class="table table-dark table-hover align-middle mb-0">
                <thead>
                  <tr>
                    <th style="width: 90px;">#</th>
                    <th>Submitted</th>
                    <th>Status</th>
                    <th class="text-end" style="width: 170px;">Action</th>
                  </tr>
                </thead>
                <tbody>
                <%
                  if (progressList.isEmpty()) {
                %>
                  <tr>
                    <td colspan="4" class="text-center text-white py-4">No submissions yet.</td>
                  </tr>
                <%
                  } else {
                    for (Progresssubmission pr : progressList) {
                %>
                  <tr>
                    <td class="fw-semibold">#<%= pr.getProgressID() %></td>
                    <td><%= pr.getSubmitted_date() != null ? pr.getSubmitted_date() : "-" %></td>
                    <td>
                      <span class="badge bg-secondary"><%= pr.getProgress_status() != null ? pr.getProgress_status() : "-" %></span>
                    </td>
                    <td class="text-end">
                      <div class="d-flex justify-content-end gap-2">
                        <a class="btn btn-sm btn-outline-primary" href="supervisorDashboard?action=feedback&progressID=<%= pr.getProgressID() %>"><i class="bi bi-chat-left-text me-1"></i>Feedback</a>
                      </div>
                    </td>
                  </tr>
                <%
                    }
                  }
                %>
                </tbody>
              </table>
            </div>


          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="includes/footer.jspf" %>
</body>
</html>
