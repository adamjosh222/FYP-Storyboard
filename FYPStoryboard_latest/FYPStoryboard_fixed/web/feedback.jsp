<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.entity.Users" %>
<%@ page import="model.entity.Feedbacks" %>

<%
  // Auth handled centrally by AuthFilter
  Users u = (Users) session.getAttribute("user");

  Integer progressID = (Integer) request.getAttribute("progressID");
  List<Feedbacks> list = (List<Feedbacks>) request.getAttribute("feedbackList");

  String backUrl = (String) request.getAttribute("backUrl");
  if (backUrl == null || backUrl.trim().isEmpty()) backUrl = "supervisorDashboard?action=students";
  String backUrlHtml = backUrl.replace("&", "&amp;");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Feedback</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<% if ("STUDENT".equals(u.getUser_role())) { %>
  <%@ include file="includes/navbar-student.jspf" %>
<% } else { %>
  <%@ include file="includes/navbar-supervisor.jspf" %>
<% } %>

<div class="page-wrap">

<div class="page-title-wrap">
  <h1>Feedback</h1>
  <div class="subtitle">Progress ID: <%= progressID %></div>
</div>

<div class="container">

  <% if ("SUPERVISOR".equals(u.getUser_role())) { %>
    <h2>Submit Feedback</h2>

    <form method="post" action="feedback">
      <input type="hidden" name="progressID" value="<%= progressID %>">
      <input type="hidden" name="back" value="<%= backUrl %>">
      Feedback
      <textarea name="feedback_text" rows="6" required></textarea>

      <button type="submit">Submit Feedback</button>
    </form>

    <div class="mt-3">
      <h3 style="margin:0 0 10px 0;">Attach File to Student (Return File)</h3>
      <form method="post" action="supervisorUploadProgressFile" enctype="multipart/form-data">
        <input type="hidden" name="progressID" value="<%= progressID %>">
        <input type="hidden" name="back" value="<%= backUrl %>">
        <input type="hidden" name="returnTo" value="feedback">
        <input type="file" name="supervisorFile" accept=".pdf,.doc,.docx,.ppt,.pptx,.xlsx,.zip,.rar,.jpg,.png" required>
        <button type="submit" class="btn">Upload &amp; Send</button>
      </form>
      <div style="font-size:12px;opacity:0.75;margin-top:6px;">Tip: Upload marked report / corrected document here. Student will download it from this Feedback page.</div>
    </div>

    <hr>
  <% } %>

  <%
    // Show the supervisor return file (if any) for both student and supervisor
    model.entity.Progresssubmission _p = new dao.ProgressSubmissionDAO().getById(progressID);
    String supFileName = (_p != null) ? _p.getSupervisorFileName() : null;
    if (supFileName != null && supFileName.trim().length() > 0) {
  %>
    <div class="card" style="margin:14px 0; padding:14px;">
      <strong>Supervisor Attachment:</strong>
      <div style="margin-top:8px;">
        <a class="btn" href="downloadProgressFile?progressID=<%= progressID %>&who=supervisor">Download</a>
        <span style="margin-left:10px; font-size:12px; opacity:0.8;"><%= supFileName %></span>
      </div>
    </div>
  <%
    }
  %>

  <h2>Feedback History</h2>

  <table>
    <tr>
      <th>Date</th>
      <th>Feedback</th>
    </tr>

    <%
      if (list == null || list.isEmpty()) {
    %>
      <tr><td colspan="2" style="text-align:center;">No feedback yet.</td></tr>
    <%
      } else {
        for (Feedbacks f : list) {
    %>
      <tr>
        <td><%= f.getFeedback_date() %></td>
        <td><%= f.getFeedback_text() %></td>
      </tr>
    <%
        }
      }
    %>
  </table>

  <br>

  <% if ("STUDENT".equals(u.getUser_role())) { %>
    <a class="btn" href="progress">← Back to Progress</a>
    <a class="btn" href="studentDashboard">← Back to Dashboard</a>
  <% } else { %>
    <a class="btn" href="<%= backUrlHtml %>">← Back</a>
  <% } %>

</div>

</div>

<%@ include file="includes/footer.jspf" %>
</body>
</html>
