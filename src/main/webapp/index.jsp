<%@ page import="pg.gda.edu.lsea.network.services.ParseDataService" %>
<%@ page import="java.nio.file.Paths" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <%
        // prepare data once to not import in each servlet, servlets only parse from db
        new ParseDataService();
        System.out.println(Paths.get("matches"));
    %>
</head>
<body>
<h1><%= "Hello World! \n" %></h1>
<p>1. <a href="team-servlet">View Teams</a></p>
<p>2. <a href="player-servlet">View Players</a></p>
<p>3. <a href="match-servlet">View Matches</a></p>
<p>4. <a href="statistic-servlet">View Statistics</a></p>
<p>5. <a href="analysis-servlet">Perform Analysis</a></p>

</body>
</html>