package pg.gda.edu.lsea.absStatistics.statisticHandlers;

import pg.gda.edu.lsea.event.Event;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.fPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.gPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.implStatistics.CoachStatistics;
import pg.gda.edu.lsea.absStatistics.implStatistics.TeamStatistics;
import pg.gda.edu.lsea.match.Match;

import java.util.*;


public class ConvertStatistics {

    private static gPlayerStatistics getGPlayerStatistics(UUID playerId, fPlayerStatistics tmp) {
        gPlayerStatistics convertToG = new gPlayerStatistics(playerId);
        convertToG.setTotalShots(tmp.getTotalShots());
        convertToG.setGoalsScored(tmp.getGoalsScored());
        convertToG.setTotalGoalConceded(tmp.getTotalGoalConceded());
        convertToG.setGamesPlayed(tmp.getGamesPlayed());
        convertToG.setGamesWon(tmp.getGamesWon());
        convertToG.setTotalCleanSheets(tmp.getTotalCleanSheets());
        convertToG.setTotalAssists(tmp.getTotalAssists());
        convertToG.setTotalBallLosses(tmp.getTotalBallLosses());
        convertToG.setTotalPasses(tmp.getTotalPasses());
        return convertToG;
    }

    private static void checkForShot(Map<UUID, Statistics> playerS,UUID playerId, Integer flagScored, Player currPlayer){
        if(playerS.containsKey(playerId)){
            PlayerStatistics player = null;
            if(currPlayer.getPositions().contains("Goalkeeper")) {
                player = (gPlayerStatistics) playerS.get(playerId);
            }
            else {
                player = (fPlayerStatistics) playerS.get(playerId);
            }
            player.setTotalShots(player.getTotalShots()+1);
            player.setGoalsScored(player.getGoalsScored()+flagScored);
            playerS.put(playerId, player);
        }else{
            if(currPlayer.getPositions().contains("Goalkeeper")) {
                playerS.put(playerId, new gPlayerStatistics(playerId, 0,
                        0,flagScored,0,0,0,0,
                        0,1,0));
            }else{
                playerS.put(playerId, new fPlayerStatistics(playerId, 0
                        , 0, flagScored, 0, 0, 0, 0,
                        0, 0, 1, 0));
            }
        }
    }

    private static void checkForSave(Map<UUID, Statistics> playerS, UUID playerId, Integer flagSaved,
                              Integer flagGoalConceded, Map<UUID,Player>uniquePlayers, Player currPlayer){
        if(playerS.containsKey(playerId)){
            if(!currPlayer.getPositions().contains("Goalkeeper")){
                ArrayList<String> positions = new ArrayList<>();
                positions.add("Goalkeeper");
                uniquePlayers.get(playerId).setPositions(positions);
                fPlayerStatistics tmp = (fPlayerStatistics) playerS.get(playerId);
                gPlayerStatistics convertToG = getGPlayerStatistics(playerId, tmp);
                playerS.put(playerId, convertToG);
            }
            else{
            gPlayerStatistics player = (gPlayerStatistics) playerS.get(playerId);
            player.setTotalSaves(player.getTotalShots()+flagSaved);
            player.setTotalGoalConceded(player.getTotalGoalConceded()+flagGoalConceded);
            playerS.put(playerId, player);}
        }
        else
        {
            playerS.put(playerId, new gPlayerStatistics(playerId,0,0,
                    0,0,0,0,0,flagSaved,0,flagGoalConceded));
            if(!uniquePlayers.get(playerId).getPositions().contains("Goalkeeper")){
                ArrayList<String> positions = new ArrayList<>();
                positions.add("Goalkeeper");
                uniquePlayers.get(playerId).setPositions(positions);
            }
        }
    }



    private static void checkForPasses(Map<UUID, Statistics> playerS, UUID playerId, Player currPlayer, int assistsFlag){
        if(playerS.containsKey(playerId)){
            PlayerStatistics player = (PlayerStatistics) playerS.get(playerId);
            player.setTotalPasses(player.getTotalPasses()+1);
            player.setTotalAssists(player.getTotalAssists()+assistsFlag);
            playerS.put(playerId, player);
        }else{
            if(currPlayer.getPositions().contains("Goalkeeper")) {
                playerS.put(playerId, new gPlayerStatistics(playerId, 0,
                        0,0,0,assistsFlag,1,0,
                        0,0,0));
            }else{
                playerS.put(playerId, new fPlayerStatistics(playerId, 0
                        , 0, 0, 0, assistsFlag, 1, 0,
                        0, 0, 0, 0));
            }
        }
    }

    private static void checkForBallLoose(Map<UUID, Statistics> playerS, UUID playerId, Player currPlayer){
        if(playerS.containsKey(playerId)){
            PlayerStatistics player = (PlayerStatistics) playerS.get(playerId);
            player.setTotalBallLosses(player.getTotalBallLosses()+1);
            playerS.put(playerId, player);
        }else{
            if(currPlayer.getPositions().contains("Goalkeeper")) {
                playerS.put(playerId, new gPlayerStatistics(playerId,
                        0,0,0,0,0,1,
                        0,0,0,0));
            }else{
                playerS.put(playerId, new fPlayerStatistics(playerId, 0
                        , 0, 0, 0, 0, 1,
                        0, 0, 0, 0, 0));
            }
        }
    }

    private static void checkForDuel(Map<UUID, Statistics> playerS, UUID playerId, Player currPlayer, int duelFlag){
        if(playerS.containsKey(playerId) && playerS.get(playerId) instanceof fPlayerStatistics){
            fPlayerStatistics player = (fPlayerStatistics) playerS.get(playerId);
            player.setTotalDuel(player.getTotalDuel()+1);
            player.setTotalDuelWins(player.getTotalDuelWins()+duelFlag);
            player.setDuelPercentage();
            playerS.put(playerId, player);
        }else{
            if(!currPlayer.getPositions().contains("Goalkeeper")) {
                playerS.put(playerId, new fPlayerStatistics(playerId, 0,0,0,0,0,
                        0,0,duelFlag,1,0,0));
            }

        }
    }


    private static void setCoachTeamStat(Map<UUID, Statistics> stats, UUID id, Integer rivalScore, Integer ourScore,
                                         Integer cleanSheetFlag, Integer gamesWonFlag, Class<?> classType ){
        if(stats.containsKey(id)){
            Statistics teamS = stats.get(id);
            teamS.setGamesPlayed(teamS.getGamesPlayed()+1);
            teamS.setGamesWon(teamS.getGamesWon()+gamesWonFlag);
            teamS.setTotalCleanSheets(teamS.getTotalCleanSheets()+cleanSheetFlag);
            teamS.setGoalsScored(teamS.getGoalsScored()+ourScore);
            teamS.setTotalGoalConceded(teamS.getTotalGoalConceded()+rivalScore);
            teamS.setCleanSheetPerc();
            teamS.setGoalPerc();
            teamS.setWinPerc();
            stats.put(id, teamS);
        }else{
            if(classType.equals(TeamStatistics.class)) {
                Statistics teamS = new TeamStatistics(id, 1, gamesWonFlag, ourScore, cleanSheetFlag, rivalScore);
                stats.put(id, teamS);
            }else if(classType.equals(CoachStatistics.class)){
                Statistics coachS = new CoachStatistics(id, 1, gamesWonFlag, ourScore, cleanSheetFlag, rivalScore);
                stats.put(id, coachS);
            }

        }
    }


    private static int checkWin(int ourScore, int rivalScore){
        if(ourScore > rivalScore){
            return 1;
        }
        else if(ourScore < rivalScore){
            return 0;
        }
        return 0;
    }

    private static int checkCleanSheet(int rivalScore){
        if(rivalScore == 0){
            return 1;
        }else{
            return 0;
        }
    }

    private static int checkAssist(boolean flag){
        if(flag){
            return 1;
        }
        else{
            return 0;
        }
    };


    private static Integer goalConceedByTeam( Map<UUID, Integer> goalScored, UUID teamId){
        List<UUID> keys = new ArrayList<>(goalScored.keySet());

        UUID keyA = keys.get(0);
        UUID keyB = null;
        if(keys.size() > 1) {
            keyB = keys.get(1);
        }

        if (keyA == null || keyB == null) {
            return 0;
        }

        if (teamId == null) {
            return 0;
        }

        if (!keyA.equals(teamId)) {
            return goalScored.getOrDefault(keyA, 0);
        } else {
            return goalScored.getOrDefault(keyB, 0);
        }

    }

    private static Integer teamCleanSheet(Map<UUID, Integer> goalScored, UUID teamId){
        List<UUID> keys = new ArrayList<>(goalScored.keySet());
        UUID keyA = keys.get(0);
        UUID keyB = null;
        if(keys.size() > 1) {
            keyB = keys.get(1);
        }else{
            return 0;
        }
        if(!keyA.equals(teamId)){
            if(goalScored.get(keyA) == 0){
                return 1;
            }
            else{
                return 0;
            }
        }else {
            if(goalScored.get(keyB) == 0){
                return 1;
            }
            else{
                return 0;
            }
        }
    }


    private static void updatePlayerStatistics(Event prevEvent, Map<UUID, Statistics> stats,
                                               Map<UUID, Integer> goalScoredByTeam){
        for(UUID teamId: prevEvent.getTeam().keySet()){
            int winFlag = 0;
            if(goalScoredByTeam.get(teamId) > goalConceedByTeam(goalScoredByTeam,teamId)) {
                winFlag = 1;
            }

            for(UUID playerId: prevEvent.getTeam().get(teamId)){
                if(stats.containsKey(playerId)){
                    PlayerStatistics playerS = (PlayerStatistics) stats.get(playerId);
                    playerS.setGamesPlayed(playerS.getGamesPlayed()+1);
                    playerS.setGamesWon(playerS.getGamesWon()+winFlag);
                    playerS.setTotalGoalConceded(playerS.getTotalGoalConceded()+
                            goalConceedByTeam(goalScoredByTeam,teamId));
                    playerS.setTotalCleanSheets(playerS.getTotalCleanSheets()+teamCleanSheet(goalScoredByTeam,teamId));
                    playerS.setCleanSheetPerc();
                    playerS.setGoalPerc();
                    playerS.setWinPerc();
                    stats.put(playerId, playerS);
                }
            }
        }
    }

    public void getPlayerStat(HashSet<Player> players, List<Event> events, Map<UUID, Statistics> stats){
        //System.out.println("Converting statistics...");
        Map<UUID, Player> uniquePlayers = new HashMap<>();

        // I want to easily get player after ID, that is why I create hash map
        for(Player player:players){
            uniquePlayers.put(player.getId(),player);
        }

        // Hash map which controls how many goal the team scored during particular match
        Map<UUID, Integer> goalScoredByTeam = new HashMap<>();

        // Hash map which controls what is a goal difference between teams ina particular game
      //  Map<UUID, Integer> goalDiffBetweenTeam = new HashMap<>();

        Event prevEvent = events.getFirst();


        for(Event event:events){
            if(!uniquePlayers.containsKey(event.getIdPerformPlayer())) {
                continue;   // if player not found in the player list
            }



            // If events from particular match has finished we want to save players statistics
            if(event.getMatchID() != prevEvent.getMatchID()){
                if(goalScoredByTeam.size()==2){
                updatePlayerStatistics(prevEvent, stats, goalScoredByTeam);
                }
                goalScoredByTeam = new HashMap<>();
            }

            goalScoredByTeam.putIfAbsent(event.getIdPerformTeam(),0);
            String type = event.getType();
            UUID playerId = event.getIdPerformPlayer();


            // check goals and shoots
            if(type.equals("Shot") && event.getOutcome().equals("Goal")){
                checkForShot(stats,playerId,1, uniquePlayers.get(playerId));
                goalScoredByTeam.put(event.getIdPerformTeam(),goalScoredByTeam.get(event.getIdPerformTeam())+1);
              //  diffBetweenGoalsTeam(goalDiffBetweenTeam,goalScoredByTeam);
            }else if(type.equals("Shot") && !event.getOutcome().equals("Goal")){
                checkForShot(stats,playerId,0, uniquePlayers.get(playerId));
            }


            //check saves and goals conceeded
            if(type.equals("Goal Keeper") && (event.getOutcome().equals("Success") || event.getOutcome().equals("Saved Twice"))){
                checkForSave(stats,playerId,1,0, uniquePlayers,uniquePlayers.get(playerId));
            }else if (type.equals("Goal Keeper") && event.getOutcome().equals("Goal Conceded") ){
                checkForSave(stats,playerId,0,1,uniquePlayers, uniquePlayers.get(playerId));
            }

            //check for passes
            if(type.equals("Pass")){
                checkForPasses(stats,playerId,uniquePlayers.get(playerId), checkAssist(event.getAssist()));
            }

            //check for ball losses
            if(type.equals("Dispossessed")){
                checkForBallLoose(stats,playerId,uniquePlayers.get(playerId));
            }


            // check for duel
            if(type.equals("Duel") && (event.getOutcome().equals("Won") ||
                    event.getOutcome().equals("Success Out") ||
                    event.getOutcome().equals("Success In Play"))){
                checkForDuel(stats,playerId,uniquePlayers.get(playerId),1);
            }else if(type.equals("Duel")){
                checkForDuel(stats,playerId,uniquePlayers.get(playerId),0);
            }

            prevEvent = event;

        }



    }

    public void getTeamCoachStats(Map<UUID, Statistics> stats, List<Match> matches){
        for(Match match:matches){
            UUID homeTeamId = match.getHomeTeamId();
            UUID awayTeamId = match.getAwayTeamId();
            int homeScore = match.getHomeScore();
            int awayScore = match.getAwayScore();
            UUID homeCoachId = match.getHomeCoachId();
            UUID awayCoachId = match.getAwayCoachId();
            setCoachTeamStat(stats, homeTeamId,awayScore, homeScore, checkCleanSheet(awayScore),
                    checkWin(homeScore, awayScore), TeamStatistics.class);
            setCoachTeamStat(stats, awayTeamId, homeScore, awayScore, checkCleanSheet(homeScore),
                    checkWin(awayScore, homeScore), TeamStatistics.class);
            if (homeCoachId != null) {
                setCoachTeamStat(stats, homeCoachId, awayScore, homeScore, checkCleanSheet(awayScore),
                        checkWin(homeScore, awayScore), CoachStatistics.class);
            }

            if (awayCoachId != null) {
                setCoachTeamStat(stats, awayCoachId, homeScore, awayScore, checkCleanSheet(homeScore),
                        checkWin(awayScore, homeScore), CoachStatistics.class);
            }

        }
    }

}
