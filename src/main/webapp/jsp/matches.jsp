<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <title>Match List</title>
</head>
<body>
<h2>All Teams</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>City</th>
    </tr>
    <c:forEach var="match" items="${matches}">
        <tr>
            <td>${match.id}</td>
        </tr>
    </c:forEach>
</table>

<a href="index">Back to Home</a>
</body>
</html>

