package pg.gda.edu.lsea.network.services;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.dataHandlers.ParseData;
import pg.gda.edu.lsea.dataHandlers.utils.Serializer;
import pg.gda.edu.lsea.event.Event;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;

import java.io.IOException;
import java.util.*;

/**
 * Service making handling and parsing the data for the analysis
 */
public class ParseDataService {
    private final List<Match> matches;
    private final Set<Referee> referees;
    private final Map<UUID, Coach> coaches;
    private final List<Team> teams;
    private final HashSet<Player> players;
    private final List<Event> events;
    private Map<UUID, Statistics> statistics;


    /**
     * Constructor initializes all required data collections for this service
     */
    public ParseDataService() {
        this.matches = new ArrayList<>();
        this.referees = new HashSet<>();
        this.coaches = new HashMap<>();
        this.teams = new ArrayList<>();
        this.players = new HashSet<>();
        this.events = Collections.synchronizedList(new ArrayList<>());
        this.statistics = new HashMap<>();
        initializeData();
    }

    /**
     * Initializes all data by parsing from files
     */
    private void initializeData() {
        try{
            ParseData.parseData(matches,referees,coaches,teams,players,events);
            ParseData.handleManyToMany(players);
            this.statistics = ParseData.getStats(players,events,matches);
            // Do testowania jak działa liczenie statystyk i predykcji
            ParseData.getCorreletion(statistics, players);
            System.out.println(ParseData.getPrediction(matches, statistics, teams, "Barcelona", "Borussia Dortmund"));
            System.out.println("Data initialization successful");
        } catch (Exception e) {
            System.err.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Calculates correlation and returns serialized result
     * @return byte array containing serialized list of correlation string
     * @throws IOException if serialization fails
     */
    public byte[] getCorrelationData() throws IOException {
        List<String> correlationResults = ParseData.getCorreletion(statistics,players);
        return Serializer.getSerializedForm(correlationResults);
    }


    /**
     * Gets a prediction who will win between 2 teams and returns result in a serialized form
     * @param homeTeam name of the home team
     * @param awayTeam name of the away team
     * @return byte array containing serialized prediction result
     * @throws IOException if serialization fails
     */
    public byte[] getPredictionData(String homeTeam, String awayTeam) throws IOException {
        try{
            String prediction = ParseData.getPrediction(matches,statistics,teams,homeTeam,awayTeam);
            List<String> result = new ArrayList<>();
            result.add(prediction);
            return Serializer.getSerializedForm(result);
        } catch (Exception e) {
            List<String> error = new ArrayList<>();
            error.add("Error generating prediction" + e.getMessage());
            return Serializer.getSerializedForm(error);
        }
    }

    /**
     * Returns a serialized list of available teams for prediction
     * @return byte array containing serialized list of available team names
     * @throws IOException if serialization fails
     */
    public byte[] getTeamsList() throws IOException {
        List<String> teamNames = new ArrayList<>();
        for (Team team : teams) {
            teamNames.add(team.getName());
        }
        return Serializer.getSerializedForm(teamNames);
    }


}
