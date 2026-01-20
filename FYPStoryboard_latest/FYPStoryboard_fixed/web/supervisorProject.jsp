<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Projects</title>
  <%@ include file="includes/head.jspf" %>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-supervisor.jspf" %>

<main class="container py-4">
  <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
    <div>
      <div class="app-title">Projects</div>
      <div class="app-subtitle">Manage and view project records</div>
    </div>
    <a class="btn btn-outline-primary" href="supervisorDashboard"><i class="bi bi-arrow-left"></i> Back to Dashboard</a>
  </div>

  <div class="alert alert-info mb-0">
    This page is kept for compatibility. Please use <b>Projects</b> from the navigation bar.
  </div>
</main>

<%@ include file="includes/footer.jspf" %>

</body>
</html>
