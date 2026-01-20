<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Supervisor Dashboard</title>
  <%@ include file="includes/head.jspf" %>
</head>
<body>

<%@ include file="includes/navbar-supervisor.jspf" %>

<div class="page-wrap">
  <div class="container">
    <div class="mb-4">
      <h1 class="h3 app-title mb-1">Supervisor Dashboard</h1>
      <div class="app-subtitle">Welcome, <span class="fw-semibold text-primary">${sessionScope.user.user_name}</span></div>
    </div>

    <div class="row g-3 mb-4">
      <div class="col-sm-6 col-xl-3">
        <div class="card app-card"><div class="card-body">
          <div class="d-flex justify-content-between align-items-start">
            <div>
              <div class="text-muted small">Students</div>
              <div class="h4 mb-0">${studentsCount}</div>
            </div>
            <div class="fs-4"><i class="bi bi-people"></i></div>
          </div>
        </div></div>
      </div>
      <div class="col-sm-6 col-xl-3">
        <div class="card app-card"><div class="card-body">
          <div class="d-flex justify-content-between align-items-start">
            <div>
              <div class="text-muted small">Active Projects</div>
              <div class="h4 mb-0">${activeProjects}</div>
            </div>
            <div class="fs-4"><i class="bi bi-kanban"></i></div>
          </div>
        </div></div>
      </div>
      <div class="col-sm-6 col-xl-3">
        <div class="card app-card"><div class="card-body">
          <div class="d-flex justify-content-between align-items-start">
            <div>
              <div class="text-muted small">Pending Progress</div>
              <div class="h4 mb-0">${pendingProgress}</div>
            </div>
            <div class="fs-4"><i class="bi bi-hourglass-split"></i></div>
          </div>
        </div></div>
      </div>
      <div class="col-sm-6 col-xl-3">
        <div class="card app-card"><div class="card-body">
          <div class="d-flex justify-content-between align-items-start">
            <div>
              <div class="text-muted small">Upcoming Deadlines</div>
              <div class="h4 mb-0">${upcomingDeadlines}</div>
            </div>
            <div class="fs-4"><i class="bi bi-calendar-event"></i></div>
          </div>
        </div></div>
      </div>
    </div>

    <div class="row g-3">
      <div class="col-lg-8">
        <div class="card app-card">
          <div class="card-body p-4">
            <h2 class="h5 mb-3">Quick actions</h2>
            <div class="d-flex flex-wrap gap-2">
              <a class="btn btn-primary" href="supervisorProfile"><i class="bi bi-person-circle me-1"></i>My Profile</a>
              <a class="btn btn-primary" href="supervisorStudents"><i class="bi bi-people me-1"></i>My Students</a>
              <a class="btn btn-primary" href="supervisorProject"><i class="bi bi-kanban me-1"></i>Projects</a>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="card app-card">
          <div class="card-body p-4">
            <h2 class="h6 mb-2">Tip</h2>
            <p class="text-muted mb-0">Review student progress weekly and leave feedback early so students can adjust before the deadline.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="includes/footer.jspf" %>
</body>
</html>
