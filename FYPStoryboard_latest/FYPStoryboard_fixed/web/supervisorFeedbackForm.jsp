<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.entity.Feedbacks" %>
<%@ page import="model.entity.Users" %>
<%
  // Auth handled centrally by AuthFilter
  Users u = (Users) session.getAttribute("user");

  Integer progressID = (Integer) request.getAttribute("progressID");
  List<Feedbacks> list = (List<Feedbacks>) request.getAttribute("feedbackList");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Feedback</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-supervisor.jspf" %>

<div class="page-title-wrap">
  <h1>Feedback</h1>
  <div class="subtitle">Progress ID: <%= progressID %></div>
</div>

<div class="container">

  <form method="post" action="supervisorDashboard">
    <input type="hidden" name="progressID" value="<%= progressID %>">

    Feedback
    <textarea name="feedback_text" rows="6" required></textarea>

    <button type="submit">Submit Feedback</button>
  </form>

  <div style="margin-top:16px;">
    <h3 style="margin:0 0 10px 0;">Attach File to Student (Return File)</h3>
    <form method="post" action="supervisorUploadProgressFile" enctype="multipart/form-data">
      <input type="hidden" name="progressID" value="<%= progressID %>">
      <input type="hidden" name="returnTo" value="feedback">
      <input type="file" name="supervisorFile" accept=".pdf,.doc,.docx,.ppt,.pptx,.xlsx,.zip,.rar,.jpg,.png" required>
      <button type="submit" class="btn">Upload &amp; Send</button>
    </form>
    <%
      model.entity.Progresssubmission _p = new dao.ProgressSubmissionDAO().getById(progressID);
      String supFileName = (_p != null) ? _p.getSupervisorFileName() : null;
      if (supFileName != null && supFileName.trim().length() > 0) {
    %>
      <div style="margin-top:10px;">
        <a class="btn" href="downloadProgressFile?progressID=<%= progressID %>&who=supervisor">Download Current File</a>
        <span style="margin-left:10px;font-size:12px;opacity:0.8;"><%= supFileName %></span>
      </div>
    <%
      }
    %>
  </div>

  <h3 style="margin-top:25px;">Previous Feedback</h3>

  <table>
    <tr><th>Date</th><th>Feedback</th></tr>

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

  <a class="small-link" href="supervisorDashboard?action=students">← Back to Students</a>
</div>

<%@ include file="includes/footer.jspf" %>

</body>
</html>
