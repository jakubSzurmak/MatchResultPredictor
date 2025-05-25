package pg.gda.edu.lsea.webSites;

import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.team.Team;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@WebServlet(name = "teamServlet", value = "/team-servlet" )
public class TeamServlet extends HttpServlet {
    private DbManager dbMgr;

    public void init() {
        dbMgr = DbManager.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Team> teams = (List<Team>) dbMgr.getFromDBJPQL("Teams",  "id", "all"); // Fetch from DB
        request.setAttribute("teams", teams);   // Set data for JSP
        request.getRequestDispatcher("/jsp/teams.jsp").forward(request, response); // Forward to JSP
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String teamIdStr = request.getParameter("teamId");

            try {
                UUID teamId = UUID.fromString(teamIdStr);

                Team team = dbMgr.getTableById(teamId, Team.class);
                if (team != null) {
                    String teamName = team.getName();

                    dbMgr.deleteFromDb("Teams", "id", teamId.toString());

                    request.setAttribute("message", "Team '" + teamName + "' deleted successfully!");
                } else {
                    request.setAttribute("error", "Team not found!");
                }

            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid team ID format!");
            } catch (Exception e) {
                request.setAttribute("error", "Error deleting team: " + e.getMessage());
            }
        }
        doGet(request, response);
    }
}