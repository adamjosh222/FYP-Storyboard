<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="model.entity.Progresssubmission" %>

<%
  List<Progresssubmission> list = (List<Progresssubmission>) request.getAttribute("progressList");
  if (list == null) list = new ArrayList<Progresssubmission>();

  String backUrl = (String) request.getAttribute("backUrl");
  String backLabel = (String) request.getAttribute("backLabel");

  if (backUrl == null || backUrl.trim().isEmpty()) backUrl = "supervisorProgress";
  if (backLabel == null || backLabel.trim().isEmpty()) backLabel = "← Back";

  // Build current page URL (even when forwarded) so Feedback page can go back here
  String selfUri = (String) request.getAttribute("javax.servlet.forward.request_uri");
  String selfQs  = (String) request.getAttribute("javax.servlet.forward.query_string");
  if (selfUri == null) selfUri = request.getRequestURI();
  String selfUrl = selfUri + ((selfQs != null && selfQs.trim().length() > 0) ? ("?" + selfQs) : "");
  String encodedSelf = URLEncoder.encode(selfUrl, "UTF-8");

%>

<!DOCTYPE html>
<html>
<head>
  <title>Progress List</title>
  <%@ include file="includes/head.jspf" %>
</head>
<body>

<%@ include file="includes/navbar-supervisor.jspf" %>

<div class="page-wrap">
  <div class="container">
    <div class="d-flex align-items-center justify-content-between mb-3">
      <div>
        <h1 class="h4 app-title mb-1">Progress List</h1>
        <div class="text-dark">View student submissions &amp; give feedback</div>
      </div>
      <a class="btn btn-outline-light" href="<%= backUrl %>"><i class="bi bi-arrow-left me-1"></i><%= backLabel.replace("\u2190 ", "") %></a>
    </div>

    <div class="card app-card">
      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table table-dark table-hover mb-0 align-middle">
            <thead>
              <tr>
                <th style="width: 90px;">ID</th>
                <th>Date</th>
                <th>Status</th>
                <th style="width: 160px;">Student File</th>
                <th style="width: 220px;">Supervisor File</th>
                <th style="width: 160px;">Action</th>
              </tr>
            </thead>
            <tbody>
              <%
                if (list.isEmpty()) {
              %>
                <tr>
                  <td colspan="6" class="text-center text-white py-4">No progress submitted.</td>
                </tr>
              <%
                } else {
                  for (Progresssubmission p : list) {
              %>
                <tr>
                  <td><%= p.getProgressID() %></td>
                  <td><%= p.getSubmitted_date() %></td>
                  <td><span class="badge text-bg-secondary"><%= p.getProgress_status() %></span></td>
                  <td>
                    <%
                      if (p.getStudentFileName() != null && p.getStudentFileName().trim().length() > 0) {
                    %>
                      <a class="btn btn-sm btn-outline-light" href="downloadProgressFile?progressID=<%= p.getProgressID() %>&who=student">
                        <i class="bi bi-download me-1"></i>Download
                      </a>
                      <div class="small text-white-50 mt-1"><%= p.getStudentFileName() %></div>
                    <%
                      } else {
                    %>
                      <span class="text-white-50">—</span>
                    <%
                      }
                    %>
                  </td>
                  <td>
                    <%
                      if (p.getSupervisorFileName() != null && p.getSupervisorFileName().trim().length() > 0) {
                    %>
                      <a class="btn btn-sm btn-outline-info" href="downloadProgressFile?progressID=<%= p.getProgressID() %>&who=supervisor">
                        <i class="bi bi-download me-1"></i>Download
                      </a>
                      <div class="small text-white-50 mt-1"><%= p.getSupervisorFileName() %></div>
                    <%
                      } else {
                    %>
                      <span class="text-white-50">—</span>
                      <div class="small text-white-50 mt-1">Upload via Feedback</div>
                    <%
                      }
                    %>
                  </td>
                  <td>
                    <a class="btn btn-sm btn-outline-primary" href="feedback?progressID=<%= p.getProgressID() %>&back=<%= encodedSelf %>">
                      <i class="bi bi-chat-left-text me-1"></i>Feedback
                    </a>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
