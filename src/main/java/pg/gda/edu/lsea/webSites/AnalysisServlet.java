package pg.gda.edu.lsea.webSites;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.dataHandlers.ParseData;
import pg.gda.edu.lsea.event.Event;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@WebServlet(name = "analysisServlet", value = "/analysis-servlet" )
public class AnalysisServlet extends HttpServlet {
    private DbManager dbMgr;

    public void init() {
        dbMgr = DbManager.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Team> teams = (List<Team>) dbMgr.getFromDBJPQL("Teams",  "id", "all"); // Fetch from DB
        request.setAttribute("teams", teams);   // Set data for JSP
        request.getRequestDispatcher("/jsp/analysis.jsp").forward(request, response); // Forward to JSP
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String homeName = request.getParameter("homeTeam");
        String awayName = request.getParameter("awayTeam");
        if (homeName == null || awayName == null || homeName.trim().isEmpty() || awayName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please select both home and away teams.");
            doGet(request, response);
            return;
        }

        // Check if same team is selected for both home and away
        if (homeName.equals(awayName)) {
            request.setAttribute("errorMessage", "Home and away teams must be different.");
            doGet(request, response);
            return;
        }

        System.out.println("Analyzing match: " + homeName + " vs " + awayName);

        try {
            // Get all necessary data for prediction
            List<Match> matches = (List<Match>) dbMgr.getFromDBJPQL("Matches", "id", "all");
            List<Team> teams = (List<Team>) dbMgr.getFromDBJPQL("Teams", "id", "all");



            // Get match prediction
            String predictionResult = ParseData.getPrediction(matches, null, teams, homeName, awayName);

            // Set results for display
            request.setAttribute("predictionResult", predictionResult);
            request.setAttribute("teams", teams);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error generating prediction: " + e.getMessage());
        }

        // Forward back to the same page to display results
        doGet(request, response);

    }
}