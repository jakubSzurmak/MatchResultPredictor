package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.fPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.gPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.implStatistics.TeamStatistics;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;

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
        convertToG.setTotalStartingEleven(tmp.getTotalStartingEleven());
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
                        0,0,1,0));
            }else{
                playerS.put(playerId, new fPlayerStatistics(playerId, 0
                        , 0, flagScored, 0, 0, 0, 0,
                        0, 0, 0, 1, 0));
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
                    0,0,0,0,0,0,flagSaved,0,flagGoalConceded));
            if(!uniquePlayers.get(playerId).getPositions().contains("Goalkeeper")){
                ArrayList<String> positions = new ArrayList<>();
                positions.add("Goalkeeper");
                uniquePlayers.get(playerId).setPositions(positions);
            }
        }
    }



    private static void checkForPasses(Map<UUID, Statistics> playerS, UUID playerId, Player currPlayer){
        if(playerS.containsKey(playerId)){
            PlayerStatistics player = (PlayerStatistics) playerS.get(playerId);
            player.setTotalPasses(player.getTotalPasses()+1);
            playerS.put(playerId, player);
        }else{
            if(currPlayer.getPositions().contains("Goalkeeper")) {
                playerS.put(playerId, new gPlayerStatistics(playerId, 0,
                        0,0,0,0,1,0,
                        0,0,0,0));
            }else{
                playerS.put(playerId, new fPlayerStatistics(playerId, 0
                        , 0, 0, 0, 0, 1, 0,
                        0, 0, 0, 0, 0));
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
                playerS.put(playerId, new gPlayerStatistics(playerId, 0,
                        0,0,0,0,0,1,
                        0,0,0,0));
            }else{
                playerS.put(playerId, new fPlayerStatistics(playerId, 0
                        , 0, 0, 0, 0, 0, 1,
                        0, 0, 0, 0, 0));
            }
        }
    }

    private static void checkForDuel(Map<UUID, Statistics> playerS, UUID playerId, Player currPlayer, int duelFlag){
        if(playerS.containsKey(playerId)){
            fPlayerStatistics player = (fPlayerStatistics) playerS.get(playerId);
            player.setTotalDuel(player.getTotalDuel()+1);
            player.setTotalDuelWins(player.getTotalDuelWins()+duelFlag);
            player.setDuelPercentage();
            playerS.put(playerId, player);
        }else{
            if(!currPlayer.getPositions().contains("Goalkeeper")) {
                playerS.put(playerId, new fPlayerStatistics(playerId, 0,0,0,0,0,0,
                        0,0,duelFlag,1,0,0));
            }

        }
    }


    private static void setTeamStat(Map<UUID, Statistics> stats, UUID id, Integer rivalScore, Integer ourScore,
                                    Integer cleanSheetFlag, Integer gamesWonFlag){
        if(stats.containsKey(id) && stats.get(id) instanceof TeamStatistics){
            Statistics teamS = (TeamStatistics) stats.get(id);
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
            Statistics teamS = (TeamStatistics) new TeamStatistics(id,1,gamesWonFlag,ourScore,cleanSheetFlag, rivalScore);
            teamS.setCleanSheetPerc();
            teamS.setGoalPerc();
            teamS.setWinPerc();
            stats.put(id, teamS);
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


    public void convert(List<Player> players, List<Event> events, List<Match> matches){
        System.out.println("Converting statistics...");
        Map<UUID, Statistics> stats = new HashMap<>();
        Map<UUID, Player> uniquePlayers = new HashMap<>();
        int counter = 0;
        for(Player player:players){
            uniquePlayers.put(player.getId(),player);
        }
        System.out.println("Unique players: " + uniquePlayers.size());
        // co to za id w evencie

        Set<String> eventList = new HashSet<>();
        Set<String> outcomeList = new HashSet<>();
        int counter2 = 0;
        UUID prevId = null;
        for(Event event:events){
            if(!uniquePlayers.containsKey(event.getIdPerformPlayer())) {
                continue;   // if player not found in the player list
            }
            String type = event.getType();
            UUID playerId = event.getIdPerformPlayer();
            // check goals and shoots
            if(type.equals("Shot") && event.getOutcome().equals("Goal")){
                checkForShot(stats,playerId,1, uniquePlayers.get(playerId));
                counter2++;
            }else if(type.equals("Shot") && !event.getOutcome().equals("Goal")){
                checkForShot(stats,playerId,0, uniquePlayers.get(playerId));
                counter2++;
            }
            //check saves and goals conceeded
            if(type.equals("Goal Keeper") && (event.getOutcome().equals("Success") || event.getOutcome().equals("Saved Twice"))){
                checkForSave(stats,playerId,1,0, uniquePlayers,uniquePlayers.get(playerId));
            }else if (type.equals("Goal Keeper") && event.getOutcome().equals("Goal Conceded") ){
                checkForSave(stats,playerId,0,1,uniquePlayers, uniquePlayers.get(playerId));
            }

            if(type.equals("Pass")){
                checkForPasses(stats,playerId,uniquePlayers.get(playerId));
            }

            if(type.equals("Dispossessed")){
                checkForBallLoose(stats,playerId,uniquePlayers.get(playerId));
            }

            if(type.equals("Duel")){
                checkForDuel(stats,playerId,uniquePlayers.get(playerId),1);
            }



            //tu mozesz sprawdzic
            //------------------------------------------
            if(type.equals("Goal Keeper")){
                eventList.add(event.getOutcome());
            }


            try {
                System.out.println(stats.get(playerId).toString());
            }
            catch (Exception e){
                System.out.println("Player not found: " + playerId);
                continue;
            }




//
//            eventList.add(event.getType());
//            outcomeList.add(event.getOutcome());
//            if(uniquePlayers.containsKey(event.getIdPerformPlayer())){
//                System.out.println("Found player: " + event.getId());
//                if(stats.containsKey(event.getIdPerformPlayer())){
//                    System.out.println("Player already exists: " + event.getId());
//                }
//                else {
//
//                    if(uniquePlayers.get(event.getIdPerformPlayer()).getPositions().contains("Goalkeeper")){
//                        stats.put(event.getIdPerformPlayer(), new gPlayerStatistics(event.getIdPerformPlayer(),1
//                        ,0,0,0,0,0,0,0,0,0,0));
//                        if(event.getType().equals("Goal Keeper")){
//                            System.out.println(event.getOutcome());
//                        }
//                    }
//                    else{
//                        stats.put(event.getIdPerformPlayer(), new fPlayerStatistics(event.getIdPerformPlayer()));
//                        System.out.println("Added player: " + event.getIdPerformPlayer() + " " + uniquePlayers.get(event.getIdPerformPlayer())
//                                .getName());
//
//                    }
//
//                }
//            }
//            else{
//                System.out.println("Player not found: " + event.getId());
//                counter++;
//             //   playerEmpty.add(event.getId());
//                continue;
//            }

        }


        for(Match match:matches){
            UUID homeTeamId = match.getHomeTeamId();
            UUID awayTeamId = match.getAwayTeamId();
            Integer homeScore = match.getHomeScore();
            Integer awayScore = match.getAwayScore();
            setTeamStat(stats, homeTeamId,awayScore, homeScore, checkCleanSheet(awayScore), checkWin(homeScore, awayScore));
            setTeamStat(stats, awayTeamId, homeScore, awayScore, checkCleanSheet(homeScore), checkWin(awayScore, homeScore));
        }
        System.out.println(eventList);
        System.out.println(outcomeList);
        System.out.println( counter2);
        for (Statistics statistics: stats.values()){
            if(statistics.getGoalsScored() > 10 && statistics instanceof fPlayerStatistics){
                System.out.println(uniquePlayers.get(statistics.getId()).getName() + " - " + statistics.getGoalsScored() + " goals scored");
            }else if(statistics.getGoalsScored() > 10 && statistics instanceof TeamStatistics){
                System.out.println(statistics.toString());
            }
        }
    }

}
