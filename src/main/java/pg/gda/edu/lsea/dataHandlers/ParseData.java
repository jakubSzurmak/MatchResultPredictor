package pg.gda.edu.lsea.dataHandlers;

import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.fPlayerStatistics;
import pg.gda.edu.lsea.analysis.Correlation;
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

public class ParseData {

    // Read-write lock for synchronizing access to shared collections
    private static final ReadWriteLock eventsLock = new ReentrantReadWriteLock();

    // Thread-safe counter for event processing
    private static final AtomicInteger eventCounter = new AtomicInteger(0);

    private static List<Path> getFilePath(String directory, int depth) throws IOException {
        return Files.walk(Paths.get(directory), depth)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".json"))
                .collect(Collectors.toList());
    }


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


    private static List<Team> parseTeams() {
        String directory = "matches";
        List<Team> parsedTeams = new ArrayList<>();
        try {
            List<Path> pathsL = getFilePath(directory, 2);
            for (Path path : pathsL) {
                parsedTeams.addAll(ParserTeam.parsing(String.valueOf(path.toFile())));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return parsedTeams;
    }

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

    private static void functionalThread(Runnable task) {
        Thread t = new Thread(task);
        t.start();
    }

    public static Map<UUID, Statistics> getStats (HashSet<Player> parsedPlayers, List<Event> parsedEvents,List<Match> matches) {
        ConvertStatistics convertStatistics = new ConvertStatistics();
        Map<UUID, Statistics> stats = new HashMap<>();
        convertStatistics.getPlayerStat(parsedPlayers, parsedEvents,stats);
        convertStatistics.getTeamCoachStats(stats, matches);
        return stats;
    }


    public static List<String> getCorreletion(Map<UUID, Statistics> stats, HashSet<Player> parsedPlayers){
        Map<String, List<Integer>> statsList = new HashMap<>();

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
        return finalCorr;
    }

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
