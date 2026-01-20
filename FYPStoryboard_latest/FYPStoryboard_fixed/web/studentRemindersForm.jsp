<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.entity.Reminders" %>
<%
    // Auth handled centrally by AuthFilter
    Reminders r = (Reminders) request.getAttribute("reminder");
    boolean editMode = (r != null);
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= editMode ? "Edit Reminder" : "Add Reminder" %></title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-student.jspf" %>

<div class="page-wrap">

<div class="page-title-wrap">
    <h1><%= editMode ? "Edit Reminder" : "Add Reminder" %></h1>
    <div class="subtitle">Set deadlines and track submission status</div>
</div>

<div class="container">

    <form method="post" action="reminders">

        <% if (editMode) { %>
            <input type="hidden" name="id" value="<%= r.getReminderID() %>">
        <% } %>

        <h3>Reminder Name</h3>
        <input type="text" name="name" required value="<%= editMode ? r.getReminder_name() : "" %>">

        <h3>Due Date</h3>
        <input type="date" name="due" required value="<%= editMode ? r.getDue_date() : "" %>">

        <h3>Status</h3>
        <select name="status" required>
            <option value="Not Completed" <%= (editMode && "Not Completed".equalsIgnoreCase(r.getSubmission_status())) ? "selected" : "" %>>Not Completed</option>
            <option value="Completed" <%= (editMode && "Completed".equalsIgnoreCase(r.getSubmission_status())) ? "selected" : "" %>>Completed</option>
        </select>

        <button type="submit"><%= editMode ? "Update Reminder" : "Save Reminder" %></button>
        <a class="btn" href="reminders">Cancel</a>
    </form>

</div>

</div>

<%@ include file="includes/footer.jspf" %>
</body>
</html>
