package pg.gda.edu.lsea.webSites;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pg.gda.edu.lsea.database.DbManager;

import java.io.IOException;

@WebServlet(name="deleteTeamServlet", value = "/delete-team")
public class DeleteTeam extends HttpServlet {
    private DbManager dbMgr;

    public void init(){
        dbMgr = DbManager.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dbMgr.deleteFromDb("Teams", "id", request.getParameter("teamId"));
        response.sendRedirect("team-servlet");
    }

}
