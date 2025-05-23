package pg.gda.edu.lsea.webSites;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import pg.gda.edu.lsea.database.DbManager;

@WebServlet(name = "analysisServlet", value = "/analysis-servlet")
public class analysisServlet extends HttpServlet {
    private String message;
    private DbManager dbMgr;

    public void init() {
        message = "Select teams for match outcome analysis";
        dbMgr = DbManager.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}