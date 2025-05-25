package pg.gda.edu.lsea.webSites;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pg.gda.edu.lsea.network.services.ParseDataService;

@WebListener
public class Sniffer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        new ParseDataService();
    }
}
