<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.entity.Notes" %>

<%
  // Auth handled centrally by AuthFilter
  List<Notes> notesList = (List<Notes>) request.getAttribute("notesList");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Notes Taking</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-student.jspf" %>

<div class="page-wrap">

<div class="page-title-wrap">
  <h1>Notes Taking</h1>
  <div class="subtitle">Create and manage your FYP notes</div>
</div>

<div class="container">

  <a class="btn" href="studentDashboard">← Back to Dashboard</a>
  <a class="btn" href="studentNotesForm.jsp">+ New Note</a>

  <table class="notes-table">
    <tr><th>No.</th><th>Title</th><th>Content</th><th>Date</th><th>Action</th></tr>

    <%
      if (notesList == null || notesList.isEmpty()) {
    %>
      <tr><td colspan="5" style="text-align:center;">No notes yet.</td></tr>
    <%
      } else {
        int no = 1;
        for (Notes n : notesList) {
    %>
      <tr>
        <%
          String c = n.getNotes_content();
          if (c == null) c = "";
          String snippet = c.length() > 220 ? c.substring(0, 220) + "..." : c;
        %>
        <td><%= no++ %></td>
        <td><%= n.getNotes_title() %></td>
        <td class="notes-content"><%= snippet %></td>
        <td><%= n.getCreated_date() %></td>
        <td class="notes-actions">
          <a class="btn" href="notes?action=edit&id=<%= n.getNotesID() %>">Edit</a>
          <a class="btn" href="notes?action=delete&id=<%= n.getNotesID() %>"
             onclick="return confirm('Delete this note?');">Delete</a>
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
