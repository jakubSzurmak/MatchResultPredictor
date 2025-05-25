<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <title>Team Dropdown</title>
</head>
<body>

<h2>Select a Team:</h2>

<form action="analysis-servlet" method="post">
    <p> Select Home Team
        <select name="homeTeam">
            <c:forEach var="team" items="${teams}">
                <option value="${team.name}">${team.name}</option>
            </c:forEach>
        </select>
    </p>

    <p> Select away Team
        <select name="awayTeam">
            <c:forEach var="team" items="${teams}">
                <option value="${team.name}">${team.name}</option>
            </c:forEach>
        </select>
    </p>

    <input type="submit" value="Submit" />
</form>

<a href="index">Back to Home</a>

</body>
</html>
