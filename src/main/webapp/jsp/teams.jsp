
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<style>
  table {
    border-collapse: collapse;
    width: 100%;
  }
  th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
  }
  th {
    background-color: #f2f2f2;
  }
</style>

<html>
<head>
  <title>Teams List</title>
  <a href="index.jsp">Back to Home</a>
</head>
<body>
<h2>All Teams</h2>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Name</th>
    <th>Delete</th>
    <th>Edit</th>
  </tr>
  <c:forEach var="team" items="${teams}">
    <tr>
      <td>${team.id}</td>
      <td>${team.name}</td>
      <td><a href="delete-item?type=${"team"}&teamId=${team.id}">Delete</a></td>
      <td><a href="editTeamServlet">Edit</a></td>

    </tr>
  </c:forEach>
</table>
</body>
</html>