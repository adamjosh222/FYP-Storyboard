<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.entity.Progresssubmission" %>
<%@ page import="model.entity.Users" %>
<%
  // Auth handled centrally by AuthFilter
  Users u = (Users) session.getAttribute("user");
  String role = (u != null ? u.getUser_role() : "");
  boolean isStudent = "STUDENT".equalsIgnoreCase(role);

  List<Progresssubmission> list = (List<Progresssubmission>) request.getAttribute("progressList");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Progress</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<% if (isStudent) { %>
  <%@ include file="includes/navbar-student.jspf" %>
<% } else { %>
  <%@ include file="includes/navbar-supervisor.jspf" %>
<% } %>

<div class="page-wrap">

  <div class="page-title-wrap">
    <h1>Progress</h1>
    <div class="subtitle">Submit and view progress updates</div>
  </div>

  <div class="container">

    <a class="btn" href="<%= isStudent ? "studentDashboard" : "supervisorDashboard" %>">← Back to Dashboard</a>

    <% if (isStudent) { %>
      <a class="btn" href="studentProgressForm.jsp">+ Submit New Progress</a>
    <% } %>

    <table>
      <tr>
        <th>No.</th>
        <th>Date</th>
        <th>Status</th>
        <th>Action</th>
      </tr>

      <%
        if (list == null || list.isEmpty()) {
      %>
        <tr>
          <td colspan="4" style="text-align:center;">No progress submissions yet.</td>
        </tr>
      <%
        } else {
          int no = 1;
          for (Progresssubmission p : list) {
      %>
        <tr>
          <td><%= no++ %></td>
          <td><%= p.getSubmitted_date() %></td>
          <td><%= p.getProgress_status() %></td>
          <td>
            <% if (isStudent) { %>
              <a class="btn" href="progress?action=delete&id=<%= p.getProgressID() %>" onclick="return confirm('Delete this progress entry?');">Delete</a>
            <% } else { %>
              <span class="muted">-</span>
            <% } %>
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
