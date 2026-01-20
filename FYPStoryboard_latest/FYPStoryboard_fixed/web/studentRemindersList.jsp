<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.entity.Reminders" %>

<%
  // Auth handled centrally by AuthFilter
  List<Reminders> reminderList = (List<Reminders>) request.getAttribute("reminderList");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Reminders & Deadlines</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-student.jspf" %>

<div class="page-wrap">

<div class="page-title-wrap">
  <h1>Reminders & Deadlines</h1>
  <div class="subtitle">Track progress and upcoming deadlines</div>
</div>

<div class="container">

  <a class="btn" href="studentDashboard">← Back to Dashboard</a>
  <a class="btn" href="studentRemindersForm.jsp">+ Add Reminder</a>

  <table>
    <tr><th>Reminder</th><th>Due Date</th><th>Status</th><th>Action</th></tr>

    <%
      if (reminderList == null || reminderList.isEmpty()) {
    %>
      <tr><td colspan="4" style="text-align:center;">No reminders created.</td></tr>
    <%
      } else {
        for (Reminders r : reminderList) {
    %>
      <tr>
        <td><%= r.getReminder_name() %></td>
        <td><%= r.getDue_date() %></td>
        <td><%= r.getSubmission_status() %></td>
        <td>
          <a class="btn" href="reminders?action=edit&id=<%= r.getReminderID() %>">Edit</a>
          <a class="btn" href="reminders?action=delete&id=<%= r.getReminderID() %>"
             onclick="return confirm('Delete this reminder?');">Delete</a>
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
