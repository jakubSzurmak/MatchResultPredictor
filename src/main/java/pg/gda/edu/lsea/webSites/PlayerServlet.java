package pg.gda.edu.lsea.webSites;

import java.io.*;
import java.util.List;

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
}