package pg.gda.edu.lsea.prediction;

import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;
import pg.gda.edu.lsea.absStatistics.*;
import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MatchPrediction {

    public static String predictMatch(String team1Name, String team2Name, List<Team> teams,
                                    Logistic model, Map<UUID, Statistics> statistics, Instances datasetStructure) throws Exception {
        Team team1 = teams.stream().filter(t -> t.getName().equalsIgnoreCase(team1Name)).findFirst().orElse(null);
        Team team2 = teams.stream().filter(t -> t.getName().equalsIgnoreCase(team2Name)).findFirst().orElse(null);

        if (team1 == null || team2 == null) {
            return "Wrong team";
        }

        Statistics stats1 = statistics.get(team1.getId());
        Statistics stats2 = statistics.get(team2.getId());
        if (stats1 == null || stats2 == null) {
            return "Cannot get stats";
        }

        Instances testSet = new Instances(datasetStructure, 0);
        DenseInstance instance = createInstance(stats1, stats2, testSet);


        double[] probabilities = model.distributionForInstance(instance);
        return "Probability of winning " + team1Name + ": " + probabilities[0] + "\n" +
                "Probability of winning " + team2Name + ": " + probabilities[1];

    }


    public static Logistic trainModel(List<Match> matches, Map<UUID, Statistics> statistics) throws Exception {
        ArrayList<Attribute> attributes = defineAttributes();
        Instances dataset = new Instances("MatchPrediction", attributes, matches.size());
        dataset.setClassIndex(dataset.numAttributes() - 1);

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

        Logistic logistic = new Logistic();
        logistic.buildClassifier(dataset);
        return logistic;
    }

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