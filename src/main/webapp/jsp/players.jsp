<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <title>Players List</title>
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
</head>
<body>
<h2>All Players</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="player" items="${players}">
        <tr>
            <td>${player.id}</td>
            <td><a href="delete-item?type=${"player"}&playerId=${player.id}">Delete</a></td>
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