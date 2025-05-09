package pg.gda.edu.lsea;


import java.io.IOException;
import java.util.Scanner;
import pg.gda.edu.lsea.database.DbManager;
import java.util.List;



/**
 * 1st entry point class
 */
public class Main {

    private static void displayTeams(DbManager dbManager) {
        Object result = dbManager.getFromDB("teams", "id", "all");

        List<Object[]> resultList = (List<Object[]>) result;

        System.out.println("Teams List:");
        for (Object[] row : resultList) {
            String teamName = (String) row[1];
            System.out.println(teamName);
        }
    }

    public static void main(String[] args) {

        DbManager dbManager = new DbManager();

        displayTeams(dbManager);

        String nameToDelete = "Bolton Wanderers";
        dbManager.deleteFromDb("teams", "name", nameToDelete);

        dbManager.updateInDb("teams", "name", "Fulham FC", "name", "Fulham");

        displayTeams(dbManager);

    }
}