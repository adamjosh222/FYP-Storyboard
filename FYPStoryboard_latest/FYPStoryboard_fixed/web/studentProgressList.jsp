<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.entity.Progresssubmission" %>

<%
  // Auth handled centrally by AuthFilter
  List<Progresssubmission> list = (List<Progresssubmission>) request.getAttribute("progressList");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Progress Submissions</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-student.jspf" %>

<div class="page-wrap">

<div class="page-title-wrap">
  <h1>Progress Submissions</h1>
  <div class="subtitle">Submit and track your project progress</div>
</div>

<div class="container">

  <a class="btn" href="studentDashboard">← Back to Dashboard</a>
  <a class="btn" href="studentProgressForm.jsp">+ Submit New Progress</a>

  <table>
    <tr>
      <th>Date</th>
      <th>Status</th>
      <th>My File</th>
      <th>Supervisor File</th>
      <th>Action</th>
    </tr>

    <%
      if (list == null || list.isEmpty()) {
    %>
      <tr><td colspan="5" style="text-align:center;">No progress yet.</td></tr>
    <%
      } else {
        for (Progresssubmission p : list) {
    %>
      <tr>
        <td><%= p.getSubmitted_date() %></td>
        <td><%= p.getProgress_status() %></td>
        <td>
          <%
            if (p.getStudentFileName() != null && p.getStudentFileName().trim().length() > 0) {
          %>
            <a class="btn" href="downloadProgressFile?progressID=<%= p.getProgressID() %>&who=student">Download</a>
            <div style="font-size:12px;opacity:0.8;margin-top:4px;"><%= p.getStudentFileName() %></div>
          <%
            } else {
          %>
            <span style="opacity:0.6;">—</span>
          <%
            }
          %>
        </td>
        <td>
          <%
            if (p.getSupervisorFileName() != null && p.getSupervisorFileName().trim().length() > 0) {
          %>
            <a class="btn" href="downloadProgressFile?progressID=<%= p.getProgressID() %>&who=supervisor">Download</a>
            <div style="font-size:12px;opacity:0.8;margin-top:4px;"><%= p.getSupervisorFileName() %></div>
          <%
            } else {
          %>
            <span style="opacity:0.6;">—</span>
          <%
            }
          %>
        </td>
        <td>
          <a class="btn" href="feedback?progressID=<%= p.getProgressID() %>">View Feedback</a>
          <a class="btn" href="progress?action=delete&id=<%= p.getProgressID() %>"
             onclick="return confirm('Delete this progress?');">Delete</a>
        </td>
      </tr>
    <%
        }
      }
    %>
  </table>

</div>

</div>

<%@ include file="includes/footer.jspf" %>
</body>
</html>
