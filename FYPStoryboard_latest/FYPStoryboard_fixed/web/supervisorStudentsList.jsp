<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.entity.Users" %>
<%
  // Auth handled centrally by AuthFilter
  Users u = (Users) session.getAttribute("user");

  List<Map<String,Object>> students = (List<Map<String,Object>>) request.getAttribute("students");
  List<Map<String,Object>> unassigned = (List<Map<String,Object>>) request.getAttribute("unassignedStudents");
  String assigned = request.getParameter("assigned");
%>
<!DOCTYPE html>
<html>
<head>
  <title>My Students</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-supervisor.jspf" %>

<div class="page-title-wrap">
  <h1>My Students</h1>
  <div class="subtitle">Students under your supervision</div>
</div>

<div class="container has-top-action">
  <div class="top-action">
    <a class="btn" href="supervisorDashboard">&#8592; Back</a>
  </div>
  <% if ("1".equals(assigned)) { %>
    <div class="notice">Student assigned ✅</div>
  <% } %>

  <h3>Assign Students</h3>
  <table>
    <tr>
      <th>Student</th><th>Matric</th><th>Program</th><th>Action</th>
    </tr>
    <%
      if (unassigned == null || unassigned.isEmpty()) {
    %>
      <tr><td colspan="4" style="text-align:center;">No unassigned students.</td></tr>
    <%
      } else {
        for (Map<String,Object> s : unassigned) {
    %>
      <tr>
        <td>
          <b><%= s.get("studentName") %></b><br>
          <span style="color:#666; font-size:13px;"><%= s.get("studentEmail") %></span>
        </td>
        <td><%= s.get("matricNum") %></td>
        <td><%= s.get("program") %></td>
        <td>
          <form method="post" action="supervisorDashboard" style="margin:0;">
            <input type="hidden" name="assignStudentID" value="<%= s.get("studentID") %>">
            <button type="submit">Assign To Me</button>
          </form>
        </td>
      </tr>
    <%
        }
      }
    %>
  </table>

  <h3 style="margin-top:20px;">My Students</h3>
  <table>
    <tr>
      <th>Student</th>
      <th>Matric</th>
      <th>Program</th>
      <th>Project</th>
      <th>Status</th>
    </tr>

    <%
      if (students == null || students.isEmpty()) {
    %>
      <tr>
        <td colspan="5" style="text-align:center;">No students assigned yet.</td>
      </tr>
    <%
      } else {
        for (Map<String,Object> s : students) {
    %>
      <tr>
        <td>
          <b><%= s.get("studentName") %></b><br>
          <span style="color:#666; font-size:13px;"><%= s.get("studentEmail") %></span>
        </td>
        <td><%= s.get("matricNum") %></td>
        <td><%= s.get("program") %></td>
        <td><%= s.get("projectTitle") != null ? s.get("projectTitle") : "-" %></td>
        <td><%= s.get("projectStatus") != null ? s.get("projectStatus") : "-" %></td>
      </tr>
    <%
        }
      }
    %>
  </table>
</div>

<%@ include file="includes/footer.jspf" %>

</body>
</html>
