<!DOCTYPE html>
<html lang="en">
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
<meta charset="UTF-8">
    <title>Supervisor Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<h2>Supervisor Login</h2>

<div class="container">

    <form action="supervisorDashboard.jsp" method="post">

        <label>Supervisor ID:</label>
        <input type="text" name="supervisorID" placeholder="Enter your Supervisor ID" required>

        <label>Password:</label>
        <input type="password" name="password" placeholder="Enter your password" required>

        <button type="submit" class="btn">Login</button>
    </form>

    <a href="login.jsp">Back to Homepage</a>

</div>

</body>
</html>