<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Register</title>
  <%@ include file="includes/head.jspf" %>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-secondary-subtle">
  <div class="container">
    <a class="navbar-brand fw-bold" href="login">
      <img src="images/fyp-logo.png" alt="FYP Storyboard" class="app-logo-small">
      <span>FYP Storyboard</span>
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navPublic" aria-controls="navPublic" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navPublic">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item"><a class="nav-link" href="login">Login</a></li>
        <li class="nav-item"><a class="nav-link active" href="register" aria-current="page">Register</a></li>
      </ul>
    </div>
  </div>
</nav>

<div class="page-wrap">
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-12 col-md-8 col-lg-6">
        <div class="text-center mb-4">
          <h1 class="h3 app-title mb-1">Create Account</h1>
          <div class="app-subtitle">Register as Student or Supervisor</div>
        </div>

        <div class="card app-card">
          <div class="card-body p-4">
            <div class="alert alert-success py-2 px-3 mb-3" role="alert" style="display:${empty msg ? 'none':'block'}">${msg}</div>
            <div class="alert alert-danger py-2 px-3 mb-3" role="alert" style="display:${empty error ? 'none':'block'}">${error}</div>

            <form method="post" action="register" class="row g-3">
              <div class="col-md-6">
                <label class="form-label">Name</label>
                <input type="text" name="name" class="form-control" required>
              </div>
              <div class="col-md-6">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" required>
              </div>
              <div class="col-md-6">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required>
              </div>
              <div class="col-md-6">
                <label class="form-label">Role</label>
                <select name="role" class="form-select" required>
                  <option value="STUDENT">Student</option>
                  <option value="SUPERVISOR">Supervisor</option>
                </select>
              </div>

              <div class="col-12">
                <div class="border rounded-3 p-3 bg-body-tertiary">
                  <div class="fw-semibold mb-2">Student Details (if role is Student)</div>
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="form-label">Matric Number</label>
                      <input type="text" name="matric" class="form-control">
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">Program</label>
                      <input type="text" name="program" class="form-control">
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-12 d-grid mt-2">
                <button type="submit" class="btn btn-primary btn-lg"><i class="bi bi-person-plus me-1"></i>Create account</button>
              </div>
            </form>

            <div class="text-center mt-3">
              <a href="login" class="link-light">Already have an account? Login</a>
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
