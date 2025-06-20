package pg.gda.edu.lsea.webSites;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pg.gda.edu.lsea.database.DbManager;

import java.io.IOException;

@WebServlet(name="deleteItemServlet", value = "/delete-item")
public class DeleteItem extends HttpServlet {
    private DbManager dbMgr;

    public void init(){
        dbMgr = DbManager.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        if (request.getParameter("type").trim().equalsIgnoreCase("team")){
            dbMgr.deleteFromDb("Teams", "id", request.getParameter("teamId"));
            response.sendRedirect("team-servlet");
        }else if (request.getParameter("type").trim().equalsIgnoreCase("player")){
            dbMgr.deleteFromDb("Players", "id", request.getParameter("playerId"));
            response.sendRedirect("player-servlet");
        }else if (request.getParameter("type").trim().equalsIgnoreCase("match")) {
            dbMgr.deleteFromDb("Matches", "id", request.getParameter("matchId"));
            response.sendRedirect("match-servlet");
        }else{
            response.sendRedirect("index.jsp");
        }
    }
}
