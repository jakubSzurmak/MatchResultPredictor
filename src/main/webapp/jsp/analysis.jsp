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
        <label for="homeTeam">Select Home Team:</label>
        <select name="homeTeam" id="homeTeam" required>
            <option value="">-- Choose Home Team --</option>
            <c:forEach var="team" items="${teams}">
                <option value="${team.name}" ${param.homeTeam == team.name ? 'selected' : ''}>${team.name}</option>
            </c:forEach>
        </select>
    </p>

    <p> Select away Team
        <label for="awayTeam">Select Away Team:</label>
        <select name="awayTeam" id="awayTeam" required>
            <option value="">-- Choose Away Team --</option>
            <c:forEach var="team" items="${teams}">
                <option value="${team.name}" ${param.awayTeam == team.name ? 'selected' : ''}>${team.name}</option>
            </c:forEach>
        </select>
    </p>

    <input type="submit" value="Predict Match Outcome" />
</form>

<c:if test="${not empty predictionResult}">
    <div class="result-container">
        <h3>Match Prediction Results:</h3>
        <p><strong>Match:</strong> ${param.homeTeam} vs ${param.awayTeam}</p>
        <pre>${predictionResult}</pre>
    </div>
</c:if>

<!-- Display error messages -->
<c:if test="${not empty errorMessage}">
    <div class="error-container">
        <h3>Error:</h3>
        <p>${errorMessage}</p>
    </div>
</c:if>


<a href="index.jsp">Back to Home</a>

</body>
</html>
