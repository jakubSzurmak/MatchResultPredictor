package pg.gda.edu.lsea.performance;

import com.sun.tools.xjc.reader.gbind.Graph;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.dataHandlers.ParseData;
import pg.gda.edu.lsea.event.Event;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Main class for executing database performance tests
 * Implements the requirements for LSEA Laboratory 6 and 7
 */
public class PerformanceTestMain {

    // Sets the number of test iterations for reliable measurements
    private static final int TEST_RUNS = 10;

    public static void main(String[] args) {
        System.out.println("Starting Database Performance Tests");

        // Initialize Collections for data
        List<Match> matches = new ArrayList<>();
        Set<Referee> referees = new HashSet<>();
        Map<UUID, Coach> coaches = new HashMap<>();
        List<Team> teams = new ArrayList<>();
        HashSet<Player> players = new HashSet<>();
        List<Event> events = Collections.synchronizedList(new ArrayList<>());

        System.out.println("Parsing initial data...");
        try {
            // Parse all required data
            ParseData.parseData(matches, referees, coaches, teams, players, events);

            // Run the actual performance tests
            runDatabasePerformanceTests(teams, players, events, matches);
           runAnalyticsPerformanceTests(teams, players, events, matches);


        } catch (Exception e) {
            System.err.println("Error during performance testing: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void runDatabasePerformanceTests(
            List<Team> teams, HashSet<Player> players, List<Event> events, List<Match> matches) {

        DbManager dbManager = DbManager.getInstance();
        List<PerformanceTestUtil.PerformanceResult> rResults = new ArrayList<>();

        System.out.println("\n== Testing Database Read Operations ==");
        List<PerformanceTestUtil.PerformanceResult> readResults =
                PerformanceTestUtil.testDatabaseReads(dbManager, TEST_RUNS);
        rResults.addAll(readResults);
        readResults.forEach(System.out::println);

        System.out.println("\n== Testing Database Write Operations ==");
        List<PerformanceTestUtil.PerformanceResult> writeResults =
                PerformanceTestUtil.testDatabaseWrites(dbManager, TEST_RUNS);
        rResults.addAll(writeResults);
        writeResults.forEach(System.out::println);

        exportResultsToFile(rResults, "Transaction operations");


        System.out.println("\n== Testing Specific Query Performance ==");

        List<PerformanceTestUtil.PerformanceResult> qResults = new ArrayList<>();


        PerformanceTestUtil.PerformanceResult teamsByNameQuery = PerformanceTestUtil.measurePerformance(
                "Search Teams by Name Pattern",
                () -> dbManager.getFromDB("teams", "name", "Man%"),
                1, TEST_RUNS);
        System.out.println(teamsByNameQuery);
        qResults.add(teamsByNameQuery);

        exportResultsToFile(qResults, "Transaction operations");


        // Test transaction impact
        System.out.println("\n== Testing Transaction Impact ==");


        List<PerformanceTestUtil.PerformanceResult> tResults = new ArrayList<>();

        // Test with transactions
        PerformanceTestUtil.PerformanceResult batchTransaction = PerformanceTestUtil.measurePerformance(
                "Updates in Single Transaction",
                () -> performMultipleUpdatesInSingleTransaction(dbManager),
                1, TEST_RUNS);
        System.out.println(batchTransaction);
        tResults.add(batchTransaction);

        PerformanceTestUtil.PerformanceResult multipleTransactions = PerformanceTestUtil.measurePerformance(
                "Multiple Updates with Separate Transactions",
                () -> performMultipleUpdatesWithSeparateTransactions(dbManager),
                1, TEST_RUNS);
        System.out.println(multipleTransactions);
        tResults.add(multipleTransactions);


        // Export results to CSV for graphing
        exportResultsToFile(tResults, "Transaction operations");

        System.out.println("Performance tests completed. Results exported.");
    }


    /**
     * Performs multiple updates within a single transaction
     */
    private static void performMultipleUpdatesInSingleTransaction(DbManager dbManager) {


        List<String> testTeamNames = new ArrayList<>();

        testTeamNames.add("Arsenal");
        testTeamNames.add("Watford");
        testTeamNames.add("Everton");
        testTeamNames.add("Fulham");
        testTeamNames.add("Argentina");
        testTeamNames.add("Colombia");
        testTeamNames.add("Canada");
        testTeamNames.add("Uruguay");
        testTeamNames.add("Panama");
        testTeamNames.add("Peru");




        // Perform updates in a single transaction
        dbManager.updateMultipleRowsSingleTransaction(
                "teams",
                "name",
                "Batch_" + System.currentTimeMillis(),
                "name",
                testTeamNames);
    }

    /**
     * Performs multiple updates using separate transactions
     */
    private static void performMultipleUpdatesWithSeparateTransactions(DbManager dbManager) {
        // Create list of team names to update
        List<String> testTeamNames = new ArrayList<>();


        testTeamNames.add("Athletic Club");
        testTeamNames.add("Valencia");
        testTeamNames.add("Las Palmas");
        testTeamNames.add("Eibar");
        testTeamNames.add("Real Betis");
        testTeamNames.add("Villarreal");
        testTeamNames.add("Levante UD");
        testTeamNames.add("Espanyol");
        testTeamNames.add("Celta Vigo");
        testTeamNames.add("Getafe");


        // Perform updates with separate transactions
        dbManager.updateMultipleRowsMultipleTransactions(
                "teams",
                "name",
                "Individual_" + System.currentTimeMillis(),
                "name",
                testTeamNames);
    }



    /**
     * Exports test results to CSV and TXT files for analysis and generates charts
     */
    private static void exportResultsToFile(List<PerformanceTestUtil.PerformanceResult> results, String name) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        try {
            // Export as CSV for graphing
            String csvContent = PerformanceTestUtil.exportResultsToCSV(results);
            try (PrintWriter out = new PrintWriter(new FileWriter("performance_results_" + timestamp + ".csv"))) {
                out.print(csvContent);
            }


            try (BufferedWriter writer = new BufferedWriter(new FileWriter("performance_report_" + timestamp + ".txt"))) {
                writer.write("DATABASE PERFORMANCE TEST REPORT\n");
                writer.write("===============================\n\n");

                for (PerformanceTestUtil.PerformanceResult result : results) {
                    writer.write(result.toString());
                    writer.write("\n---\n\n");
                }

                writer.write("\nRaw Data Summary:\n");
                writer.write("Operation,Avg Time (ms),StdDev (ms),Avg Memory (KB)\n");

                for (PerformanceTestUtil.PerformanceResult result : results) {
                    writer.write(String.format("%s,%.2f,%.2f,%.2f\n",
                            result.getOperationName(),
                            result.getAverageExecutionTime(),
                            result.getStdDevExecutionTime(),
                            result.getAverageMemoryUsage() / 1024.0));
                }
            }

            // Generate charts
            System.out.println("Generating visualization charts...");

            // Generate execution time bar chart
            GraphGenerator.generateExecutionTimeBarChart(
                    results,
                    name + " - Average Execution Time",
                    "execution_time_bar_chart");

            // Generate execution time box plot
            GraphGenerator.generateExecutionTimeBoxPlot(
                    results,
                    name + " - Execution Time Distribution",
                    "execution_time_box_plot");

            // Generate memory usage bar chart
            GraphGenerator.generateMemoryUsageBarChart(
                    results,
                    name + " - Average Memory Usage",
                    "memory_usage_bar_chart");
        } catch (Exception e) {
            System.err.println("Error during performance testing: " + e.getMessage());
        }
    }



    private static void runAnalyticsPerformanceTests(
            List<Team> teams, HashSet<Player> players, List<Event> events, List<Match> matches) {

        DbManager dbManager = DbManager.getInstance();
        List<PerformanceTestUtil.PerformanceResult> analysisResults = new ArrayList<>();




        Map<UUID, Statistics> stats = ParseData.getStats(players, events, matches);


        // Test prediction calculation performance
        PerformanceTestUtil.PerformanceResult predictionResult = PerformanceTestUtil.measurePerformance(
                "Match Prediction - No Database",
                () -> {
                    try {
                        ParseData.getPrediction(matches, stats, teams, "Barcelona", "Real Madrid");
                        System.out.println(ParseData.getPrediction(matches, stats, teams, "Barcelona", "Real Madrid"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                1, TEST_RUNS);
        System.out.println(predictionResult);
        analysisResults.add(predictionResult);

        PerformanceTestUtil.PerformanceResult correlationResult = PerformanceTestUtil.measurePerformance(
                "Correlation - No Database",
                () -> {
                    try {
                        ParseData.getCorreletion(stats,players);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                1, TEST_RUNS);
        System.out.println(correlationResult);
        analysisResults.add(correlationResult);

        // Export results to CSV for graphing
        exportResultsToFile(analysisResults, "Analysis");

        System.out.println("Performance tests completed. Results exported.");
    }
}

