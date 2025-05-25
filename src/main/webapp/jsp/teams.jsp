
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
  <title>Teams List</title>
  <style>
    .delete-btn {
      background-color: #dc3545;
      color: white;
      border: none;
      padding: 5px 10px;
      cursor: pointer;
      border-radius: 3px;
    }
    .delete-btn:hover {
      background-color: #c82333;
    }
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
</head>
<body>
<h2>All Teams</h2>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Name</th>
    <th>Country</th>
    <th>Actions</th>
  </tr>
  <c:forEach var="team" items="${teams}">
    <tr>
      <td>${team.id}</td>
      <td>${team.name}</td>
      <td>${team.country}</td>
      <td>
        <form method="post" action="team-servlet" style="display: inline;"
              onsubmit="return confirm('Are you sure you want to delete team: ${team.name}?');">
          <input type="hidden" name="action" value="delete">
          <input type="hidden" name="teamId" value="${team.id}">
          <button type="submit" class="delete-btn">Delete</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>

<c:if test="${not empty message}">
  <div style="margin-top: 10px; padding: 10px; background-color: #d4edda; border: 1px solid #c3e6cb; color: #155724; border-radius: 4px;">
      ${message}
  </div>
</c:if>

<c:if test="${not empty error}">
  <div style="margin-top: 10px; padding: 10px; background-color: #f8d7da; border: 1px solid #f5c6cb; color: #721c24; border-radius: 4px;">
      ${error}
  </div>
</c:if>

<br>
<a href="index.jsp">Back to Home</a>
</body>
</html>