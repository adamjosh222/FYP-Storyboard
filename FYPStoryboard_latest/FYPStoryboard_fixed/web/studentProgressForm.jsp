<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Submit Progress</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-student.jspf" %>

<div class="page-wrap">

<div class="container">
    <div class="mb-4">
      <h1 class="h3 app-title mb-1">Submit Progress</h1>
      <div class="app-subtitle">Upload and document your latest progress</div>
    </div>

    <a class="btn" href="progress">← Back to Progress</a>

    <form method="post" action="progress" enctype="multipart/form-data">
        Submitted Date:
        <input type="date" name="date" required>
        <br><br>

        Progress Status:
        <input type="text" name="status" placeholder="e.g. Chapter 1 Draft Done" required>
        <br><br>

        Attach File (optional):
        <input type="file" name="studentFile" accept=".pdf,.doc,.docx,.ppt,.pptx,.xlsx,.zip,.rar,.jpg,.png">
        <br><br>

        <button type="submit">Submit</button>
    </form>
</div>

</div>

<%@ include file="includes/footer.jspf" %>
</body>
</html>
