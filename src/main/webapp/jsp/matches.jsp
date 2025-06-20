<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <title>Match List</title>
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
<a href="index.jsp">Back to Home</a>
<h2>All Matches</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Home Team</th>
        <th>Away Team</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="match" items="${matches}">
        <tr>
            <td>${match.id}</td>
            <td>${match.awayscore}</td>
            <td>${match.homescore}</td>
            <td>${match.awayteamid}</td>
            <td>${match.hometeamid}</td>
            <td>
                <a href="delete-item?type=${"match"}&matchId=${match.id}">Delete</a>
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

</body>
</html>

