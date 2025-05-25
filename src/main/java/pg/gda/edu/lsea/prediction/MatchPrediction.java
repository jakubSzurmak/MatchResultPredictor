package pg.gda.edu.lsea.prediction;

import pg.gda.edu.lsea.absStatistics.implStatistics.TeamStatistics;
import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;
import pg.gda.edu.lsea.absStatistics.*;
import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import javax.sound.midi.SysexMessage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * Provides functionality for training a logistic regression model and predicting match outcomes
 * using team statistics. Utilizes the Weka library for machine learning.
 */
public class MatchPrediction {
    private static UUID convertToUUID(String id) {
        return UUID.fromString(id);
    }

    /**
     * Predicts the outcome of a match between two teams using a trained logistic regression model.
     *
     * @param team1Name        The name of the first team (home).
     * @param team2Name        The name of the second team (away).
     * @param teams            List of all teams (used when not querying DB, currently unused).
     * @param model            Trained Weka logistic regression model.
     * @param statistics       Map of team UUIDs to their statistics (used when not querying DB).
     * @param datasetStructure Dataset structure used to create the instance (class index must be set).
     * @return A string describing the probability of each team winning.
     * @throws Exception If team data or statistics cannot be retrieved or model prediction fails.
     */
    public static String predictMatch(String team1Name, String team2Name, List<Team> teams,
                                    Logistic model, Map<UUID, Statistics> statistics, Instances datasetStructure) throws Exception {
        DbManager dbManager = DbManager.getInstance();

        //Baza
        Team team1 = dbManager.getValueFromColumn(team1Name, Team.class, "name");
        Team team2 = dbManager.getValueFromColumn(team2Name, Team.class, "name");

        //Nie Baza
       // Team team1 = teams.stream().filter(t -> t.getName().equalsIgnoreCase(team1Name)).findFirst().orElse(null);
       // Team team2 = teams.stream().filter(t -> t.getName().equalsIgnoreCase(team2Name)).findFirst().orElse(null);

        if (team1 == null || team2 == null) {
            return "Wrong team";
        }
        //Baza
       Statistics stats1 = dbManager.getTableById(team1.getId(), TeamStatistics.class);
       Statistics stats2 = dbManager.getTableById(team2.getId(), TeamStatistics.class);

        //Nie baza
        //Statistics stats1 = statistics.get(team1.getId());
        //Statistics stats2 = statistics.get(team2.getId());
        if (stats1 == null || stats2 == null) {
            return "Cannot get stats";
        }

        Instances testSet = new Instances(datasetStructure, 0);
        DenseInstance instance = createInstance(stats1, stats2, testSet);


        double[] probabilities = model.distributionForInstance(instance);
        return "Probability of winning " + team1Name + ": " + probabilities[0] + "\n" +
                "Probability of winning " + team2Name + ": " + probabilities[1];

    }


    /**
     * Trains a logistic regression model based on historical match outcomes and team statistics.
     * Fetches data directly from the database and skips incomplete or ambiguous records.
     *
     * @param matches     List of matches (used when not training from DB, currently unused).
     * @param statistics  Map of team UUIDs to statistics (used when not training from DB).
     * @return Trained Weka logistic regression model.
     * @throws Exception If there is an issue accessing the database or training the model.
     */
    public static Logistic trainModel(List<Match> matches, Map<UUID, Statistics> statistics) throws Exception {
        ArrayList<Attribute> attributes = defineAttributes();
        Instances dataset = new Instances("MatchPrediction", attributes, matches.size());
        dataset.setClassIndex(dataset.numAttributes() - 1);

        DbManager dbManager = DbManager.getInstance();

        Object result = dbManager.getFromDB("matches", "all", "all");

        List<Object[]> resultList = (List<Object[]>) result;
        System.out.println("Len: " + resultList.size());
        // Model się uczy z bazy danych
        int counter = 0;
        for (Object[] row : resultList) {
            String stringHomeId = (String) row[4];
            String stringAwayId = (String) row[3];
            if(stringAwayId == null || stringHomeId ==null){
                System.out.println("Brak id dla zespołu, pomijam mecz.");  //Duzo pomija / dużo więcej niż w przypadku nie z bazy danych, teamy mogą się źle wrzcuać do bazy danych
                counter++;
                continue;
            }
            UUID homeTeamId = convertToUUID(stringHomeId);
            UUID awayTeamId = convertToUUID(stringAwayId);
            Statistics homeStats = dbManager.getTableById(homeTeamId, TeamStatistics.class);
            Statistics awayStats = dbManager.getTableById(awayTeamId, TeamStatistics.class);
            if (homeStats == null || awayStats == null) {
                System.out.println("Brak statystyk dla jednego z zespołów, pomijam mecz."); //Duzo pomija / dużo więcej niż w przypadku nie z bazy danych, teamy mogą się źle wrzcuać do bazy danych ten sam powód;
                counter++;
                continue;
            }

            DenseInstance instance = createInstance(homeStats, awayStats, dataset);

            Integer homeScore = (Integer) row[2];
            Integer awayScore = (Integer) row[1];
            if(homeScore > awayScore){
                instance.setValue(attributes.get(16), "Team1 win");
            }
            else if(homeScore < awayScore){
                instance.setValue(attributes.get(16), "Team2 win");
            }else{
                continue;
            }
            dataset.add(instance);

        }


/*
        // Model NIE uczy się z bazy danych - do testów

        for (Match match : matches) {
            UUID homeTeamId = match.getHomeTeamId();
            UUID awayTeamId = match.getAwayTeamId();
            Statistics homeTeamStats = statistics.get(homeTeamId);
            Statistics awayTeamStats = statistics.get(awayTeamId);

            if (homeTeamStats == null || awayTeamStats == null) {
                System.out.println("Brak statystyk dla jednego z zespołów, pomijam mecz.");
                continue;
            }

            DenseInstance instance = createInstance(homeTeamStats, awayTeamStats, dataset);

            UUID id = match.getWinner();
            if (id != null && id == homeTeamId) {
                instance.setValue(attributes.get(16), "Team1 win");
            } else if (id != null && id == awayTeamId) {
                instance.setValue(attributes.get(16), "Team2 win");
            } else {
                continue;
            }

            dataset.add(instance);
        }

 */

        Logistic logistic = new Logistic();
        logistic.buildClassifier(dataset);
        return logistic;
    }

    /**
     * Defines and returns the list of attributes used in the logistic regression model.
     *
     * @return List of Weka attributes.
     */
    private static ArrayList<Attribute> defineAttributes() {
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

        return attributes;
    }

    /**
     * Creates a Weka DenseInstance representing a match between two teams based on their statistics.
     *
     * @param stats1   Statistics for team 1 (home team).
     * @param stats2   Statistics for team 2 (away team).
     * @param dataset  Dataset to which this instance belongs (needed for attribute structure).
     * @return A DenseInstance ready for classification or training.
     */
    private static DenseInstance createInstance(Statistics stats1, Statistics stats2, Instances dataset) {
        DenseInstance instance = new DenseInstance(dataset.numAttributes());
        instance.setValue(0, stats1.getWinPerc());
        instance.setValue(1, stats1.getGoalsScored());
        instance.setValue(2, stats1.getGamesPlayed());
        instance.setValue(3, stats1.getGamesWon());
        instance.setValue(4, stats1.getTotalCleanSheets());
        instance.setValue(5, stats1.getTotalGoalConceded());
        instance.setValue(6, stats1.getGoalPerc());
        instance.setValue(7, stats1.getCleanSheetPerc());

        instance.setValue(8, stats2.getWinPerc());
        instance.setValue(9, stats2.getGoalsScored());
        instance.setValue(10, stats2.getGamesPlayed());
        instance.setValue(11, stats2.getGamesWon());
        instance.setValue(12, stats2.getTotalCleanSheets());
        instance.setValue(13, stats2.getTotalGoalConceded());
        instance.setValue(14, stats2.getGoalPerc());
        instance.setValue(15, stats2.getCleanSheetPerc());

        instance.setDataset(dataset);
        return instance;
    }
}