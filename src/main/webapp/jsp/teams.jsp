<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
  <title>Teams List</title>
</head>
<body>
<h2>All Teams</h2>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Name</th>
    <th>Country</th>
  </tr>
  <c:forEach var="team" items="${teams}">
    <tr>
      <td>${team.id}</td>
      <td>${team.name}</td>
      <td>${team.country}</td>
    </tr>
  </c:forEach>
</table>

<a href="index.jsp">Back to Home</a>
</body>
</html>

