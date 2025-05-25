package pg.gda.edu.lsea.webSites;

import java.io.*;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.absPerson.implPerson.Player;

@WebServlet(name = "playerServlet", value = "/player-servlet")
public class PlayerServlet extends HttpServlet {
    private DbManager dbMgr;

    public void init() {
        dbMgr = DbManager.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Player> players = (List<Player>) dbMgr.getFromDBJPQL("Players",  "id", "all");
        request.setAttribute("players", players);
        request.getRequestDispatcher("/jsp/players.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String playerIdStr = request.getParameter("playerId");

            try {
                UUID playerId = UUID.fromString(playerIdStr);

                Player player = dbMgr.getTableById(playerId, Player.class);
                if (player != null) {
                    String playerName = player.getName();


                    dbMgr.deleteFromDb("Players", "id", playerId.toString());

                    request.setAttribute("message", "Player '" + playerName + "' deleted successfully!");
                } else {
                    request.setAttribute("error", "Player not found!");
                }

            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid player ID format!");
            } catch (Exception e) {
                request.setAttribute("error", "Error deleting player: " + e.getMessage());
            }
        }
        doGet(request, response);
    }

    public void destroy() {
    }
}