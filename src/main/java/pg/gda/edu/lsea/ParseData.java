package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.parsers.utils.*;
import pg.gda.edu.lsea.team.Team;

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

public class ParseData {

    // Read-write lock for synchronizing access to shared collections
    private static final ReadWriteLock eventsLock = new ReentrantReadWriteLock();
    private static final ReadWriteLock playersLock = new ReentrantReadWriteLock();

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
            int numThreads = 4;
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

            eventLatch.await(); // Wait for all event parsing to complete
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

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Parsing data...");
        final CountDownLatch latch = new CountDownLatch(6);

        List<Match> matches = new ArrayList<>();
        Set<Referee> referees = new HashSet<>();
        Map<UUID, Coach> coaches = new HashMap<>();
        List<Team> parsedTeams = new ArrayList<>();
        HashSet<Player> parsedPlayers = new HashSet<>();
        List<Event> parsedEvents = Collections.synchronizedList(new ArrayList<>());


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

        // Similar pattern for other parsing tasks...
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

        // Wait for all parsing operations to complete
        latch.await();
        System.out.println(matches.size() + " - matches in total");
        System.out.println(referees.size() + " - referees in total");
        System.out.println(coaches.size() + " - coaches in total");
        System.out.println(parsedTeams.size() + " - teams in total");
        System.out.println(parsedPlayers.size() + " - players in total");
        System.out.println(parsedEvents.size() + " - events in total");

        ConvertStatistics convertStatistics = new ConvertStatistics();
        Map<UUID, Statistics> stats = new HashMap<>();
        convertStatistics.getPlayerStat(parsedPlayers, parsedEvents,stats);
        convertStatistics.getTeamCoachStats(stats, matches);
        System.out.println("Done");
    }
}
