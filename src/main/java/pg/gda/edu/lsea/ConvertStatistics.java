package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.fPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.gPlayerStatistics;

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

    private static void checkForShot(Map<UUID, Statistics> playerS,UUID playerId, Event event, Integer flagScored, Player currPlayer){
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
                playerS.put(playerId, new gPlayerStatistics(event.getIdPerformPlayer(), 0,
                        0,flagScored,0,0,0,0,
                        0,0,1,0));
            }else{
                playerS.put(playerId, new fPlayerStatistics(event.getIdPerformPlayer(), 0
                        , 0, flagScored, 0, 0, 0, 0,
                        0, 0, 0, 1, 0));
            }
        }
    }

    private static void checkForSave(Map<UUID, Statistics> playerS, UUID playerId, Event event, Integer flagSaved,
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
            playerS.put(playerId, new gPlayerStatistics(event.getIdPerformPlayer(),0,0,
                    0,0,0,0,0,0,flagSaved,0,flagGoalConceded));
            if(!uniquePlayers.get(playerId).getPositions().contains("Goalkeeper")){
                ArrayList<String> positions = new ArrayList<>();
                positions.add("Goalkeeper");
                uniquePlayers.get(playerId).setPositions(positions);
            }
        }
    }



    private static void checkForPasses(Map<UUID, Statistics> playerS, UUID playerId, Event event, Player currPlayer){
        if(playerS.containsKey(playerId)){
            PlayerStatistics player = (PlayerStatistics) playerS.get(playerId);
            player.setTotalPasses(player.getTotalPasses()+1);
            playerS.put(playerId, player);
        }else{
            if(currPlayer.getPositions().contains("Goalkeeper")) {
                playerS.put(playerId, new gPlayerStatistics(event.getIdPerformPlayer(), 0,
                        0,0,0,0,1,0,
                        0,0,0,0));
            }else{
                playerS.put(playerId, new fPlayerStatistics(event.getIdPerformPlayer(), 0
                        , 0, 0, 0, 0, 1, 0,
                        0, 0, 0, 0, 0));
            }
        }
    }

    private static void checkForBallLoose(Map<UUID, Statistics> playerS, UUID playerId, Event event, Player currPlayer){
        if(playerS.containsKey(playerId)){
            PlayerStatistics player = (PlayerStatistics) playerS.get(playerId);
            player.setTotalBallLosses(player.getTotalBallLosses()+1);
            playerS.put(playerId, player);
        }else{
            if(currPlayer.getPositions().contains("Goalkeeper")) {
                playerS.put(playerId, new gPlayerStatistics(event.getIdPerformPlayer(), 0,
                        0,0,0,0,0,1,
                        0,0,0,0));
            }else{
                playerS.put(playerId, new fPlayerStatistics(event.getIdPerformPlayer(), 0
                        , 0, 0, 0, 0, 0, 1,
                        0, 0, 0, 0, 0));
            }
        }
    }




    public void convert(List<Player> players, List<Event> events){
        System.out.println("Converting statistics...");
        Map<UUID, Statistics> playerS = new HashMap<>();
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
                checkForShot(playerS,playerId,event,1, uniquePlayers.get(playerId));
                counter2++;
            }else if(type.equals("Shot") && !event.getOutcome().equals("Goal")){
                checkForShot(playerS,playerId,event,0, uniquePlayers.get(playerId));
                counter2++;
            }
            //check saves and goals conceeded
            if(type.equals("Goal Keeper") && (event.getOutcome().equals("Success") || event.getOutcome().equals("Saved Twice"))){
                checkForSave(playerS,playerId,event,1,0, uniquePlayers,uniquePlayers.get(playerId));
            }else if (type.equals("Goal Keeper") && event.getOutcome().equals("Goal Conceded") ){
                checkForSave(playerS,playerId,event,0,1,uniquePlayers, uniquePlayers.get(playerId));
            }

            if(type.equals("Pass")){
                checkForPasses(playerS,playerId,event,uniquePlayers.get(playerId));
            }

            if(type.equals("Dispossessed")){
                checkForBallLoose(playerS,playerId,event,uniquePlayers.get(playerId));
            }



            //tu mozesz sprawdzic
            //------------------------------------------
            if(type.equals("Duel")){
                eventList.add(event.getOutcome());
            }


            try {
                System.out.println(playerS.get(playerId).toString());
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
//                if(playerS.containsKey(event.getIdPerformPlayer())){
//                    System.out.println("Player already exists: " + event.getId());
//                }
//                else {
//
//                    if(uniquePlayers.get(event.getIdPerformPlayer()).getPositions().contains("Goalkeeper")){
//                        playerS.put(event.getIdPerformPlayer(), new gPlayerStatistics(event.getIdPerformPlayer(),1
//                        ,0,0,0,0,0,0,0,0,0,0));
//                        if(event.getType().equals("Goal Keeper")){
//                            System.out.println(event.getOutcome());
//                        }
//                    }
//                    else{
//                        playerS.put(event.getIdPerformPlayer(), new fPlayerStatistics(event.getIdPerformPlayer()));
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
        System.out.println(eventList);
        System.out.println(outcomeList);
        System.out.println( counter2);
    }

}
