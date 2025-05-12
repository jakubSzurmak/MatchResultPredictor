package pg.gda.edu.lsea.performance;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.dataHandlers.ParseData;
import pg.gda.edu.lsea.event.Event;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Utility class for measuring performance metrics of database operations
 * and data processing tasks.
 */
public class PerformanceTestUtil {

    /**
     * Result class to hold performance measurement data
     */
    public static class PerformanceResult {
        private final String operationName;
        private final List<Long> executionTimes;  // in milliseconds
        private final List<Long> memoryUsages;    // in bytes

        public PerformanceResult(String operationName) {
            this.operationName = operationName;
            this.executionTimes = new ArrayList<>();
            this.memoryUsages = new ArrayList<>();
        }

        public void addMeasurement(long executionTime, long memoryUsage) {
            executionTimes.add(executionTime);
            memoryUsages.add(memoryUsage);
        }

        public String getOperationName() {
            return operationName;
        }

        public List<Long> getExecutionTimes() {
            return executionTimes;
        }

        public List<Long> getMemoryUsages() {
            return memoryUsages;
        }

        public double getAverageExecutionTime() {
            return executionTimes.stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0);
        }

        public double getAverageMemoryUsage() {
            return memoryUsages.stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0);
        }

        public double getStdDevExecutionTime() {
            double mean = getAverageExecutionTime();
            return Math.sqrt(executionTimes.stream()
                    .mapToDouble(value -> Math.pow(value - mean, 2))
                    .average()
                    .orElse(0));
        }

        @Override
        public String toString() {
            return String.format(
                    "Operation: %s\n" +
                            "Execution Time: %.2f ms (StdDev: %.2f ms)\n" +
                            "Memory Usage: %.2f KB\n",
                    operationName,
                    getAverageExecutionTime(),
                    getStdDevExecutionTime(),
                    getAverageMemoryUsage() / 1024.0);
        }
    }

    /**
     * Measures the performance of a given operation
     *
     * @param operationName Name of the operation being measured
     * @param operation Operation to execute
     * @param warmupRuns Number of warmup runs
     * @param measuredRuns Number of measured runs
     * @return PerformanceResult containing the measurements
     */
    public static PerformanceResult measurePerformance(String operationName, Runnable operation, int warmupRuns, int measuredRuns) {
        PerformanceResult result = new PerformanceResult(operationName);

        // Warm-up runs to avoid JVM optimization skewing
        for (int i = 0; i < warmupRuns; i++) {
            operation.run();
        }

        // Measured runs
        for (int i = 0; i < measuredRuns; i++) {
            System.gc(); // Request garbage collection before measurement

            // Memory usage before operation
            long memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Time measurement
            long startTime = System.nanoTime();
            operation.run();
            long endTime = System.nanoTime();

            // Memory usage after operation
            long memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Record results
            long executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            long memoryUsage = memoryAfter - memoryBefore;

            result.addMeasurement(executionTime, memoryUsage);
        }

        return result;
    }

    /**
     * Measures performance of an operation that returns a value
     *
     * @param operationName Name of the operation
     * @param operation Operation to execute
     * @param warmupRuns Number of warmup runs
     * @param measuredRuns Number of measured runs
     * @param <T> Return type of the operation
     * @return The result of the last execution and the performance data
     */
    public static <T> Map.Entry<T, PerformanceResult> measurePerformanceWithResult(
            String operationName, Supplier<T> operation, int warmupRuns, int measuredRuns) {

        PerformanceResult result = new PerformanceResult(operationName);
        T operationResult = null;

        // Warm-up runs
        for (int i = 0; i < warmupRuns; i++) {
            operation.get();
        }

        // Measured runs
        for (int i = 0; i < measuredRuns; i++) {
            System.gc();

            long memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            long startTime = System.nanoTime();

            operationResult = operation.get();

            long endTime = System.nanoTime();
            long memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            long executionTime = (endTime - startTime) / 1_000_000;
            long memoryUsage = memoryAfter - memoryBefore;

            result.addMeasurement(executionTime, memoryUsage);
        }

        return new AbstractMap.SimpleEntry<>(operationResult, result);
    }

    /**
     * Test database read operations with varying query complexity
     * @param dbManager Database manager instance
     * @param runs Number of runs for each test
     * @return List of performance results
     */
    public static List<PerformanceResult> testDatabaseReads(DbManager dbManager, int runs) {
        List<PerformanceResult> results = new ArrayList<>();

        // Test 1: Simple read - get all teams
        results.add(measurePerformance(
                "Simple DB Read - All Teams",
                () -> dbManager.getFromDB("teams", "id", "all"),
                3, runs));

        // Test 2: Filtered read - get specific team
        results.add(measurePerformance(
                "Filtered DB Read - Specific Team",
                () -> dbManager.getFromDB("teams", "name", "Barcelona"),
                3, runs));



        return results;
    }

    /**
     * Test database write operations with varying complexity
     * @param dbManager Database manager instance
     * @param runs Number of runs for each test
     * @return List of performance results
     */
    public static List<PerformanceResult> testDatabaseWrites(DbManager dbManager, int runs) {
        List<PerformanceResult> results = new ArrayList<>();

        // Test 1: Simple insert
        results.add(measurePerformance(
                "Simple DB Insert - Single Team",
                () -> {
                    Team team = new Team(UUID.randomUUID());
                    team.setName("Test Team " + System.currentTimeMillis());
                    dbManager.saveToDb(team);
                },
                3, runs));

        // Test 2: Update operation
        results.add(measurePerformance(
                "DB Update Operation",
                () -> dbManager.updateInDb("teams", "name",
                        "Updated " + System.currentTimeMillis(),
                        "name", "Test Team%"),
                3, runs));

        // Test 3: Delete operation
        results.add(measurePerformance(
                "DB Delete Operation",
                () -> dbManager.deleteFromDb("teams", "name", "Updated%"),
                3, runs));

        return results;
    }

    /**
     * Test parsing performance with and without database
     * @param parsedPlayers Pre-parsed player data
     * @param parsedEvents Pre-parsed event data
     * @param matches Match data
     * @param runs Number of runs
     * @return List of performance results
     */
    public static List<PerformanceResult> testParsingPerformance(
            HashSet<Player> parsedPlayers, List<Event> parsedEvents, List<Match> matches, int runs) {

        List<PerformanceResult> results = new ArrayList<>();

        // Test: Statistics calculation with DB persistence
        PerformanceResult dbPersistenceResult = measurePerformance(
                "Statistics Calculation with DB Persistence",
                () -> ParseData.getStats(parsedPlayers, parsedEvents, matches),
                2, runs);
        results.add(dbPersistenceResult);


        return results;
    }

    /**
     * Exports performance results to CSV format for easy charting
     * @param results List of performance results
     * @return CSV content as string
     */
    public static String exportResultsToCSV(List<PerformanceResult> results) {
        StringBuilder csv = new StringBuilder();
        csv.append("Operation,Run,ExecutionTime(ms),MemoryUsage(KB)\n");

        for (PerformanceResult result : results) {
            String operationName = result.getOperationName();
            List<Long> times = result.getExecutionTimes();
            List<Long> memory = result.getMemoryUsages();

            for (int i = 0; i < times.size(); i++) {
                csv.append(String.format("%s,%d,%.2f,%.2f\n",
                        operationName, i+1,
                        (double)times.get(i),
                        memory.get(i) / 1024.0));
            }
        }

        return csv.toString();
    }
}