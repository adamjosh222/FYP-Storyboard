<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<%
  List<Map<String,Object>> students = (List<Map<String,Object>>) request.getAttribute("students");
  List<Map<String,Object>> projects = (List<Map<String,Object>>) request.getAttribute("projects");

  if (students == null) students = new ArrayList<Map<String,Object>>();
  if (projects == null) projects = new ArrayList<Map<String,Object>>();

  Map studentToProject = new HashMap();
  for (Map<String,Object> p : projects) {
    Integer sid = (Integer) p.get("studentID");
    Integer pid = (Integer) p.get("projectID");
    if (sid != null && pid != null) {
      studentToProject.put(sid, pid);
    }
  }
%>

<!DOCTYPE html>
<html>
<head>
  <title>Projects</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-supervisor.jspf" %>

<div class="page-title-wrap">
  <h1>Projects</h1>
  <div class="subtitle">Manage and review your supervised students' projects</div>
</div>

<div class="container left has-top-action">
  <div class="top-action">
    <a class="btn" href="supervisorDashboard">&#8592; Back</a>
  </div>
  <div class="subtitle" style="text-align:left;">
    Tip: create a project record for each student to start tracking progress.
  </div>

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
        <tr>
          <td colspan="6" style="text-align:center;">No students assigned yet. Go to <b>Students</b> to assign students first.</td>
        </tr>
      <%
        } else {
          for (Map<String,Object> s : students) {
            Integer sid = (Integer) s.get("studentID");
            Integer pid = (Integer) studentToProject.get(sid);
            String title = (String) s.get("projectTitle");
            String status = (String) s.get("projectStatus");

            String badgeClass = "badge";
            String up = (status == null) ? "" : status.toUpperCase();
            if (up.indexOf("ONGOING") >= 0 || up.indexOf("ACTIVE") >= 0) badgeClass = "badge success";
            else if (up.indexOf("PROPOSE") >= 0 || up.indexOf("PENDING") >= 0) badgeClass = "badge warning";
      %>
        <tr>
          <td>
            <b><%= s.get("studentName") %></b><br>
            <span style="color:#666; font-size:13px;"><%= s.get("studentEmail") %></span>
          </td>
          <td><%= s.get("matricNum") %></td>
          <td><%= s.get("program") %></td>
          <td><%= (title != null && !title.trim().isEmpty()) ? title : "-" %></td>
          <td>
            <span class="<%= badgeClass %>"><%= (status != null && !status.trim().isEmpty()) ? status : "No Project" %></span>
          </td>
          <td>
            <%
              if (pid != null) {
            %>
              <a class="btn" style="padding:9px 12px; margin-right:6px;" href="supervisorProjectDetails?projectID=<%= pid %>">View</a>
              <a class="btn" style="padding:9px 12px;" href="supervisorDashboard?action=progress&projectID=<%= pid %>&returnTo=projects">Submissions</a>
            <%
              } else {
            %>
              <form method="post" action="supervisorProject" style="margin:0; display:inline;">
                <input type="hidden" name="createStudentID" value="<%= sid %>">
                <button type="submit" style="padding:9px 12px;">Create Project</button>
              </form>
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

</div>

<%@ include file="includes/footer.jspf" %>

</body>
</html>
