<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <title>Players List</title>
</head>
<body>
<h2>All Players</h2>
<table border="1">
    <tr>
        <th>ID</th>

    </tr>
    <c:forEach var="player" items="${players}">
        <tr>
            <td>${player.id}</td>
        </tr>
    </c:forEach>
</table>

<a href="index.jsp">Back to Home</a>
</body>
</html>

