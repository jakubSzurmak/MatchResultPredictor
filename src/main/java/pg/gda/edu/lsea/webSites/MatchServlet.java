package pg.gda.edu.lsea.webSites;

import java.io.*;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.match.Match;

@WebServlet(name = "matchServlet", value = "/match-servlet")
public class MatchServlet extends HttpServlet {
    private DbManager dbMgr;

    public void init() {
        dbMgr = DbManager.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Match> matches = (List<Match>) dbMgr.getFromDBJPQL("Matches",  "id", "all");
        request.setAttribute("matches", matches);
        request.getRequestDispatcher("/jsp/matches.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String matchIdStr = request.getParameter("matchId");

            try {
                UUID matchId = UUID.fromString(matchIdStr);

                Match match = dbMgr.getTableById(matchId, Match.class);
                if (match != null) {
                    String matchInfo = "Match between " + match.getHomeTeamId() +
                            " and " + match.getAwayTeamId();

                    dbMgr.deleteFromDb("Matches", "id", matchId.toString());

                    request.setAttribute("message", matchInfo + " deleted successfully!");
                } else {
                    request.setAttribute("error", "Match not found!");
                }

            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid match ID format!");
            } catch (Exception e) {
                request.setAttribute("error", "Error deleting match: " + e.getMessage());
            }
        }
        doGet(request, response);
    }

    public void destroy() {
    }
}