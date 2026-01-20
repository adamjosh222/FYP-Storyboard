<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.entity.Notes" %>

<%
    // Auth handled centrally by AuthFilter
    Notes note = (Notes) request.getAttribute("note");
    boolean editing = (note != null);
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= editing ? "Edit Note" : "New Note" %></title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-student.jspf" %>

<div class="page-wrap">

<div class="page-title-wrap">
    <h1><%= editing ? "Edit Note" : "Create New Note" %></h1>
    <div class="subtitle">Save meeting notes, ideas, and tasks</div>
</div>

<div class="container">

    <form method="post" action="notes">
        <input type="hidden" name="notesID" value="<%= editing ? note.getNotesID() : "" %>">

        <h3>Title</h3>
        <input type="text" name="title" required
               value="<%= editing && note.getNotes_title()!=null ? note.getNotes_title() : "" %>">

        <h3>Content</h3>
        <textarea name="content" rows="8" required><%= editing && note.getNotes_content()!=null ? note.getNotes_content() : "" %></textarea>

        <button type="submit"><%= editing ? "Update" : "Save" %></button>
        <a class="btn" href="notes">Cancel</a>
    </form>

</div>

</div>

<%@ include file="includes/footer.jspf" %>
</body>
</html>
