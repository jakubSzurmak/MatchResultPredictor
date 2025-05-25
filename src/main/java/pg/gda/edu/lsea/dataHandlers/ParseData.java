package pg.gda.edu.lsea.dataHandlers;

import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.fPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.gPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.implStatistics.TeamStatistics;
import pg.gda.edu.lsea.analysis.Correlation;
import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.event.Event;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.dataHandlers.parsers.*;
import pg.gda.edu.lsea.absStatistics.statisticHandlers.ConvertStatistics;
import pg.gda.edu.lsea.team.Team;
import pg.gda.edu.lsea.prediction.MatchPrediction;
import java.util.function.Function;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * Utility class for parsing data from JSON files
 */
public class ParseData {

    /**
     * Converts a string ID to a UUID using UTF-8 encoding.
     *
     * @param id the string to convert
     * @return UUID generated from the string
     */
    private static UUID convertToUUID(String id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
    }

    // Read-write lock for synchronizing access to shared collections
    private static final ReadWriteLock eventsLock = new ReentrantReadWriteLock();

    // Thread-safe counter for event processing
    private static final AtomicInteger eventCounter = new AtomicInteger(0);


    /**
     * Retrieves a list of JSON file paths from a directory up to the given depth.
     *
     * @param directory the root directory to search
     * @param depth the maximum depth to traverse
     * @return list of Paths to JSON files
     * @throws IOException if an I/O error occurs
     */
    private static List<Path> getFilePath(String directory, int depth) throws IOException {
        return Files.walk(Paths.get(directory), depth)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".json"))
                .collect(Collectors.toList());
    }


    /**
     * Parses event data from JSON files in the "events" directory.
     *
     * @return a list of parsed Event objects
     */
    private static List<Event> parseEvents() {
        List<Event> parsedEvents = Collections.synchronizedList(new ArrayList<>());
        String directory = "events";

        try {
            List<Path> pathsE = getFilePath(directory, 1);
            int numThreads = 8;
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            CountDownLatch eventLatch = new CountDownLatch(pathsE.size());
            for (Path path : pathsE) {
                executor.submit(() -> {
                    try {
                        // CRITICAL SECTION #1: Multiple threads getting an event counter
                        int currentCounter = eventCounter.getAndIncrement();
                        List<Event> threadEvents = ParserEvent.parsing(String.valueOf(path.toFile()), currentCounter);

                        // CRITICAL SECTION #2: Multiple threads updating shared collection
                        eventsLock.writeLock().lock();
                        try {
                            parsedEvents.addAll(threadEvents);
                        } finally {
                            eventsLock.writeLock().unlock();
                        }

                       // System.out.println("Parsed events from: " + path.getFileName() + " - Added " + threadEvents.size() + " events");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        eventLatch.countDown();
                    }
                });
            }
            // Wait for all event parsing to complete
            eventLatch.await();
            executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parsedEvents;
    }

    /**
     * Parses team data from JSON files in the "matches" directory.
     *
     * @return a list of parsed Team objects
     */
    private static List<Team> parseTeams() {
        String directory = "matches";
        List<Team> parsedTeams = new ArrayList<>();
        Set<Team> hashTeam = new HashSet<>();
        DbManager dbManager = DbManager.getInstance();
        try {
            List<Path> pathsL = getFilePath(directory, 2);
            for (Path path : pathsL) {
                parsedTeams.addAll(ParserTeam.parsing(String.valueOf(path.toFile())));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        hashTeam.addAll(parsedTeams);
        System.out.println(hashTeam.size() + " size unique teams");
        for(Team team:parsedTeams){
            team.setPlayerSet(null);
            dbManager.saveToDb(team);
        }

        return parsedTeams;
    }

    /**
     * Parses player data from JSON files in the "players" directory.
     *
     * @return a list of parsed Player objects
     */
    private static HashSet<Player> parsePlayers() {
        String directory2 = "lineupsModified";
        String directory4 = "player_rating.json";
        HashSet<Player> parsedPlayers = new HashSet<>();
        try {
            // Parse ratings file once and create a map for O(1) lookups with player data including DOB
            Map<String, ParserPlayer.PlayerData> playerDataMap = ParserPlayer.parseRatingsToMap(directory4);

            List<Path> pathsP = getFilePath(directory2, 1);
            for (Path path : pathsP) {
                // Use the method that accepts the player data map
                parsedPlayers.addAll(ParserPlayer.parsing(String.valueOf(path.toFile()), playerDataMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return parsedPlayers;
    }

    /**
     * Starts a new thread to run the given task asynchronously.
     *
     * @param task the Runnable task to execute in a new thread
     */
    private static void functionalThread(Runnable task) {
        Thread t = new Thread(task);
        t.start();
    }

    /**
     * Computes and returns statistics for players, teams, and coaches.
     *
     * It processes player and event data to generate statistics, associates
     * team and coach stats from matches, and persists all statistics to the database.
     *
     * @param parsedPlayers the set of players to analyze
     * @param parsedEvents the list of events related to matches and players
     * @param matches the list of matches to gather team/coach statistics
     * @return a map of UUIDs to their corresponding Statistics objects
     */
    public static Map<UUID, Statistics> getStats (HashSet<Player> parsedPlayers, List<Event> parsedEvents,List<Match> matches) {
        ConvertStatistics convertStatistics = new ConvertStatistics();
        Map<UUID, Statistics> stats = new HashMap<>();
        DbManager dbManager = DbManager.getInstance();
        convertStatistics.getPlayerStat(parsedPlayers, parsedEvents,stats);
        convertStatistics.getTeamCoachStats(stats, matches);
        // Pakowanie do bazy team statistics, player statistics, goalkeeper statistics
        for(Map.Entry<UUID, Statistics> stat: stats.entrySet()) {
            if (stat.getValue() instanceof TeamStatistics teamS) {
                Team team = dbManager.getTableById(stat.getKey(), Team.class);
                teamS.setTeam(team);
               dbManager.saveToDb(teamS);
            } else if (stat.getValue() instanceof fPlayerStatistics) {
                Player player = dbManager.getTableById(stat.getKey(), Player.class);
                fPlayerStatistics playerS = (fPlayerStatistics) stat.getValue();
                playerS.setPlayer(player);
                dbManager.saveToDb(playerS);
            } else if (stat.getValue() instanceof gPlayerStatistics){
                Player player = dbManager.getTableById(stat.getKey(), Player.class);
                gPlayerStatistics playerS = (gPlayerStatistics) stat.getValue();
                playerS.setPlayer(player);
                dbManager.saveToDb(playerS);
            }
        }
        return stats;

    }

    /**
     * Calculates Pearson correlations between different player statistics.
     *
     * @param stats         Map of player IDs to their statistics.
     * @param parsedPlayers Set of players to consider.
     * @return List of correlation results as formatted strings.
     */
    public static List<String> getCorreletion(Map<UUID, Statistics> stats, HashSet<Player> parsedPlayers){
        Map<String, List<Integer>> statsList = new HashMap<>();
        DbManager dbManager = DbManager.getInstance();
        List<Integer> totalShots = new ArrayList<>();
        List<Integer> totalPasses = new ArrayList<>();
        List<Integer> totalAssists = new ArrayList<>();
        List<Integer> totalDuelWins = new ArrayList<>();
        List<Integer> wonMatches = new ArrayList<>();
        List<Integer> gamesPlayed = new ArrayList<>();
        List<Integer> goalsScored = new ArrayList<>();
        List<Integer> totalCleanSheets = new ArrayList<>();
        List<Integer> totalBallLosses = new ArrayList<>();
        List<Integer> totalGoalConceded = new ArrayList<>();

        statsList.put("totalShots", totalShots);
        statsList.put("totalPasses", totalPasses);
        statsList.put("totalAssists", totalAssists);
        statsList.put("totalDuelWins", totalDuelWins);
        statsList.put("wonMatches", wonMatches);
        statsList.put("gamesPlayed", gamesPlayed);
        statsList.put("goalsScored", goalsScored);
        statsList.put("totalCleanSheets", totalCleanSheets);
        statsList.put("totalBallLosses", totalBallLosses);
        statsList.put("totalGoalConceded", totalGoalConceded);

        Object result = dbManager.getFromDB("fplayerstatistics", "all", "all");

        List<Object[]> resultList = (List<Object[]>) result;
        // Liczenie korelacji z bazą - do testowania
        for (Object[] row : resultList) {
          //  System.out.println(Arrays.toString(row));
            gamesPlayed.add((Integer) row[0]);
            wonMatches.add((Integer) row[1]);
            goalsScored.add((Integer) row[2]);
            totalAssists.add((Integer) row[4]);
            totalBallLosses.add((Integer) row[5]);
            totalCleanSheets.add((Integer) row[6]);
            totalDuelWins.add((Integer) row[8]);
            totalGoalConceded.add((Integer) row[9]);
            totalPasses.add((Integer) row[10]);
            totalShots.add((Integer) row[11]);
        }

 /*
   // Liczenie korelacji bez korzystania z bazy - do testowania info

        for (Player player : parsedPlayers){
            if( !player.getPositions().contains("Goalkeeper") && stats.get(player.getId()) instanceof PlayerStatistics){
                fPlayerStatistics pStatistics = (fPlayerStatistics) stats.get(player.getId());
                if(pStatistics != null) {
                    totalShots.add(pStatistics.getTotalShots());
                    totalPasses.add(pStatistics.getTotalPasses());
                    totalAssists.add(pStatistics.getTotalAssists());
                    totalDuelWins.add(pStatistics.getTotalDuelWins());
                    wonMatches.add(pStatistics.getGamesWon());
                    gamesPlayed.add(pStatistics.getGamesPlayed());
                    goalsScored.add(pStatistics.getGoalsScored());
                    totalCleanSheets.add(pStatistics.getTotalCleanSheets());
                    totalBallLosses.add(pStatistics.getTotalBallLosses());
                    totalGoalConceded.add(pStatistics.getTotalGoalConceded());

                }
            }
        }


  */
        List<String> keys = new ArrayList<>(statsList.keySet());
        List<String> finalCorr = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            for (int j = i + 1; j < keys.size(); j++) {
                String keyA = keys.get(i);
                String keyB = keys.get(j);

                List<Integer> listA = statsList.get(keyA);
                List<Integer> listB = statsList.get(keyB);

                double correlation = Correlation.calculatePearson(listA, listB);
                finalCorr.add("Correlation between " + keyA + " and " + keyB + ": " + correlation + "\n");
            }
        }
        System.out.println(finalCorr);
        return finalCorr;
    }

    /**
     * Predicts the outcome of a match between two teams using a trained model.
     *
     * @param matches      List of all matches.
     * @param stats        Map of team/player statistics.
     * @param parsedTeams  List of parsed teams.
     * @param teamHome     Name of the home team.
     * @param teamAway     Name of the away team.
     * @return Prediction result as a string.
     * @throws Exception if model training or prediction fails.
     */
    public static String getPrediction(List<Match> matches,Map<UUID, Statistics> stats , List<Team> parsedTeams, String teamHome,
                                     String teamAway) throws Exception {
        System.out.println("Train data...");

        Logistic logisticModel = new Logistic();
        logisticModel = MatchPrediction.trainModel(matches, stats);

        try {
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("team1_winPercentage"));
            attributes.add(new Attribute("team1_totalGoals"));
            attributes.add(new Attribute("team1_totalMatches"));
            attributes.add(new Attribute("team1_totalWinMatches"));
            attributes.add(new Attribute("team1_totalCleanSheets"));
            attributes.add(new Attribute("team1_totalGoalsConceded"));
            attributes.add(new Attribute("team1_goalPercentage"));
            attributes.add(new Attribute("team1_cleanSheetPercentage"));

            attributes.add(new Attribute("team2_winPercentage"));
            attributes.add(new Attribute("team2_totalGoals"));
            attributes.add(new Attribute("team2_totalMatches"));
            attributes.add(new Attribute("team2_totalWinMatches"));
            attributes.add(new Attribute("team2_totalCleanSheets"));
            attributes.add(new Attribute("team2_totalGoalsConceded"));
            attributes.add(new Attribute("team2_goalPercentage"));
            attributes.add(new Attribute("team2_cleanSheetPercentage"));

            List<String> classValues = List.of("Team1 win", "Team2 win");
            attributes.add(new Attribute("result", classValues));

            Instances datasetStructure = new Instances("MatchPrediction", attributes, 0);
            datasetStructure.setClassIndex(datasetStructure.numAttributes() - 1);


            return MatchPrediction.predictMatch(teamHome, teamAway, parsedTeams, logisticModel, stats, datasetStructure);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Cannot get prediction";
    }

    /**
     * Handles many-to-many relationships between players and teams and saves players to the database.
     *
     * @param players Set of parsed players.
     */
    public static void handleManyToMany(HashSet<Player>players){
        // Pakowanie playersow do bazy przy jednoczesnym zajęciem się relacją wiele do wiele między teams a players
        DbManager dbManager = DbManager.getInstance();
        for(Player player:players){
            Team team = dbManager.getValueFromColumn(player.getCurrClub(), Team.class, "name" );
            player.updateTeamSet(team);
            team.updatePlayerSet(player);
            dbManager.saveToDb(player);
        }
    }

    /**
     * Parses matches, referees, coaches, teams, players, and events concurrently using threads.
     *
     * @param matches       List to store parsed matches.
     * @param referees      Set to store parsed referees.
     * @param coaches       Map to store parsed coaches.
     * @param parsedTeams   List to store parsed teams.
     * @param parsedPlayers Set to store parsed players.
     * @param parsedEvents  List to store parsed events.
     * @throws Exception if thread execution is interrupted or parsing fails.
     */
    public static void parseData(List<Match> matches, Set<Referee> referees, Map<UUID, Coach> coaches,
                                 List<Team> parsedTeams, HashSet<Player> parsedPlayers, List<Event> parsedEvents) throws Exception {
        System.out.println("Parsing data...");

        final CountDownLatch latch = new CountDownLatch(6);
    /*
        List<Match> matches = new ArrayList<>();
        Set<Referee> referees = new HashSet<>();
        Map<UUID, Coach> coaches = new HashMap<>();
        List<Team> parsedTeams = new ArrayList<>();
        HashSet<Player> parsedPlayers = new HashSet<>();
        List<Event> parsedEvents = Collections.synchronizedList(new ArrayList<>());
*/


        functionalThread(() -> {
            try {
                try {
                    referees.addAll(new ParserReferee().parseReferee());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Referees parsing complete: " + referees.size());
            } finally {
                latch.countDown();
            }
        });


        functionalThread(() -> {
            try {
                try {
                    coaches.putAll(new ParserCoach().parseCoache());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Coaches parsing complete: " + coaches.size());
            } finally {
                latch.countDown();
            }
        });

        functionalThread(() -> {
            try {
                parsedTeams.addAll(parseTeams());
                System.out.println("Teams parsing complete: " + parsedTeams.size());
            } finally {
                latch.countDown();
            }
        });

        functionalThread(() -> {
            try {
                matches.addAll(new ParserMatch().parseMatch());
                System.out.println("Matches parsing complete: " + matches.size());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });


        functionalThread(() -> {
            try {
                parsedPlayers.addAll(parsePlayers());
                System.out.println("Players parsing complete: " + parsedPlayers.size());
            } finally {
                latch.countDown();
            }
        });
        functionalThread(() -> {
            try {
                parsedEvents.addAll(parseEvents());
                System.out.println("Events parsing complete: " + parsedEvents.size());
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        System.out.println(matches.size() + " - matches in total");
        System.out.println(referees.size() + " - referees in total");
        System.out.println(coaches.size() + " - coaches in total");
        System.out.println(parsedTeams.size() + " - teams in total");
        System.out.println(parsedPlayers.size() + " - players in total");
        System.out.println(parsedEvents.size() + " - events in total");


     //   Map<UUID, Statistics> stats = getStats(parsedPlayers, parsedEvents, matches);
     //   getCorreletion(stats, parsedPlayers);
       // getPrediction(matches, stats, parsedTeams, "Barcelona", "Real Madrid");


    }
}
