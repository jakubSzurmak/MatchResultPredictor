package pg.gda.edu.lsea;


import java.io.IOException;
import java.util.Scanner;
import pg.gda.edu.lsea.database.DbManager;
import java.util.List;



/**
 * 1st entry point class
 */
public class Main {

    /**
     * Retrieves and prints the names of all teams from the database.
     *
     * @param dbManager Instance of DbManager to perform database operations
     */

    private static void displayTeams(DbManager dbManager) {
        Object result = dbManager.getFromDB("teams", "id", "all");

        List<Object[]> resultList = (List<Object[]>) result;

        System.out.println("Teams List:");
        for (Object[] row : resultList) {
            String teamName = (String) row[1];
            System.out.println(teamName);
        }
    }

    /**
     * Runs a console interface to perform delete or update operations on the database.
     */
    public static void main(String[] args) {
        DbManager dbManager = DbManager.getInstance();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an operation: [delete] or [update]");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("delete")) {
            System.out.print("Enter table name: ");
            String table = scanner.nextLine().trim();

            System.out.print("Enter condition column: ");
            String conditionColumn = scanner.nextLine().trim();

            System.out.print("Enter condition value (or type 'all'): ");
            String conditionValue = scanner.nextLine().trim();

            dbManager.deleteFromDb(table, conditionColumn, conditionValue);

        } else if (choice.equals("update")) {
            System.out.print("Enter table name: ");
            String table = scanner.nextLine().trim();

            System.out.print("Enter column to update: ");
            String setColumn = scanner.nextLine().trim();

            System.out.print("Enter new value for that column: ");
            String setValue = scanner.nextLine().trim();

            System.out.print("Enter condition column: ");
            String conditionColumn = scanner.nextLine().trim();

            System.out.print("Enter condition value (or type 'all'): ");
            String conditionValue = scanner.nextLine().trim();

            dbManager.updateInDb(table, setColumn, setValue, conditionColumn, conditionValue);

        } else {
            System.out.println("Invalid option. Please choose 'delete' or 'update'.");
        }

        scanner.close();
    }
}