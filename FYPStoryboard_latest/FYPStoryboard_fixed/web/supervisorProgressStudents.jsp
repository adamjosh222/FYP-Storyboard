<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<%
  List<Map<String,Object>> students = (List<Map<String,Object>>) request.getAttribute("students");
  if (students == null) students = new ArrayList<Map<String,Object>>();
%>

<!DOCTYPE html>
<html>
<head>
  <title>Progress</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-supervisor.jspf" %>

<div class="page-title-wrap">
  <h1>Progress</h1>
  <div class="subtitle">Select a student to view progress submissions</div>
</div>

<div class="container left">
  <div class="table-wrap">
    <table>
      <tr>
        <th>Student</th>
        <th>Matric</th>
        <th>Program</th>
        <th>Project</th>
        <th>Status</th>
        <th>Action</th>
      </tr>

      <%
        if (students.isEmpty()) {
      %>
        <tr><td colspan="6" style="text-align:center;">No students assigned yet. Go to Students to assign students.</td></tr>
      <%
        } else {
          for (Map<String,Object> s : students) {
            Object projectID = s.get("projectID");
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
          <td>
            <%
              if (projectID != null) {
            %>
              <a class="btn" style="padding:9px 12px;" href="supervisorDashboard?action=progress&projectID=<%= projectID %>">View Progress</a>
            <%
              } else {
            %>
              <span style="color:#666;">No project</span>
            <%
              }
            %>
          </td>
        </tr>
      <%
          }
        }
      %>

    </table>
  </div>

  <div style="text-align:center; margin-top:16px;">
    <a class="btn" href="supervisorDashboard" style="padding:10px 16px;">&#8592; Back</a>
  </div>
</div>

<%@ include file="includes/footer.jspf" %>

</body>
</html>
