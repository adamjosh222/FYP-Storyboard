<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
  <%@ include file="includes/head.jspf" %>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-secondary-subtle">
  <div class="container">
    <a class="navbar-brand fw-bold" href="login">
      <img src="images/fyp-logo.png" alt="FYP Storyboard" class="app-logo-small">
      <span>FYP Storyboard</span>
    </a>
    <div class="d-flex gap-2">
      <a class="btn btn-outline-light btn-sm" href="login">Login</a>
      <a class="btn btn-primary btn-sm" href="register">Register</a>
    </div>
  </div>
</nav>

<div class="page-wrap">
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-12 col-md-8 col-lg-5">
        <div class="text-center mb-4">
          <!-- Main logo for the login page title area -->
          <img src="images/fyp-logo.png" alt="FYP Logo" class="app-logo-main">
          <h1 class="h3 app-title mb-1">Academic Advising & Progress Tracking</h1>
          <div class="app-subtitle fyp-subtitle">Final Year Project (FYP)</div>
        </div>

        <div class="card app-card">
          <div class="card-body p-4">
            <div class="d-flex align-items-center gap-2 mb-3">
              <i class="bi bi-box-arrow-in-right text-primary fs-4"></i>
              <div>
                <div class="fw-bold">Sign In</div>
                <div class="text-body-secondary small">Login as Student or Supervisor</div>
              </div>
            </div>

            <div class="alert alert-success py-2 mb-3" role="alert" style="display:${empty msg ? 'none':'block'}">${msg}</div>
            <div class="alert alert-danger py-2 mb-3" role="alert" style="display:${empty error ? 'none':'block'}">${error}</div>

            <form method="post" action="login" class="row g-3">
              <div class="col-12">
                <label class="form-label">Email</label>
                <input type="email" class="form-control" name="email" required placeholder="example@email.com">
              </div>
              <div class="col-12">
                <label class="form-label">Password</label>
                <input type="password" class="form-control" name="password" required placeholder="Enter password">
              </div>
              <div class="col-12">
                <label class="form-label">Role</label>
                <select class="form-select" name="role" required>
                  <option value="STUDENT">Student</option>
                  <option value="SUPERVISOR">Supervisor</option>
                </select>
              </div>
              <div class="col-12 d-grid">
                <button type="submit" class="btn btn-primary"><i class="bi bi-lock-fill me-1"></i>Login</button>
              </div>
            </form>

            <div class="text-center mt-3">
              <a class="link-light" href="register">No account? Create one here</a>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</div>

<%@ include file="includes/footer.jspf" %>
</body>
</html>
