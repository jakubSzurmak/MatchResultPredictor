package pg.gda.edu.lsea.webSites;

import java.io.*;
import java.util.List;

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

    public void destroy() {
    }
}