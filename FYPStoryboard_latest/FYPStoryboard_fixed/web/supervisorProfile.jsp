<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%
    Map<String,Object> p = (Map<String,Object>) request.getAttribute("profile");
    if (p == null) p = new HashMap<String, Object>();

    String updated = request.getParameter("updated");

    String name = (p.get("name") == null) ? "" : String.valueOf(p.get("name"));
    String email = (p.get("email") == null) ? "" : String.valueOf(p.get("email"));
    String phone = (p.get("phone") == null) ? "" : String.valueOf(p.get("phone"));
%>

<!DOCTYPE html>
<html>
<head>
  <title>Supervisor Profile</title>
  <%@ include file="includes/head.jspf" %>
</head>
<body>

<%@ include file="includes/navbar-supervisor.jspf" %>

<div class="page-wrap">
  <div class="container">
    <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-end gap-2 mb-4">
      <div>
        <h1 class="h3 app-title mb-1">Supervisor Profile</h1>
        <div class="app-subtitle">Update your details</div>
      </div>
    </div>

    <% if ("1".equals(updated)) { %>
      <div class="alert alert-success d-flex align-items-center" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i>
        Profile updated successfully.
      </div>
    <% } %>

    <div class="card app-card">
      <div class="card-body p-4">
        <form method="post" action="supervisorProfile" class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Name</label>
            <input type="text" class="form-control" name="name" value="<%= name %>" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Email</label>
            <input type="email" class="form-control" name="email" value="<%= email %>" required>
          </div>
          <div class="col-md-6">
            <label class="form-label">Phone</label>
            <input type="text" class="form-control" name="phone" value="<%= phone %>">
          </div>

          <div class="col-12 d-flex justify-content-end gap-2 mt-2">
            <a class="btn btn-primary" href="supervisorDashboard">
              <i class="bi bi-arrow-left me-1"></i>Back
            </a>
            <button type="submit" class="btn btn-primary">
              <i class="bi bi-save2 me-1"></i>Update Profile
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<%@ include file="includes/footer.jspf" %>
</body>
</html>
