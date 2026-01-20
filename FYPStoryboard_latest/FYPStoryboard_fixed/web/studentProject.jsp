<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>My Project</title>
  <%@ include file="includes/head.jspf" %>
  <!-- Legacy helpers used by some pages -->
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="includes/navbar-student.jspf" %>

<main class="container py-4">
  <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
    <div>
      <div class="app-title">My Project</div>
      <div class="app-subtitle">View your project details</div>
    </div>
    <a class="btn btn-outline-primary" href="studentDashboard"><i class="bi bi-arrow-left"></i> Back to Dashboard</a>
  </div>

  <div class="card app-card">
    <div class="card-body">
      <div class="table-responsive">
        <table class="table align-middle mb-0">
          <tbody>
            <tr><th style="width:220px;">Project Title</th><td>FYP Progress Tracking System</td></tr>
            <tr><th>Description</th><td>System to manage student FYP progress</td></tr>
            <tr><th>Start Date</th><td>01/10/2025</td></tr>
            <tr><th>End Date</th><td>20/12/2025</td></tr>
            <tr><th>Status</th><td><span class="badge text-bg-info">In Progress</span></td></tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</main>

<%@ include file="includes/footer.jspf" %>

</body>
</html>
