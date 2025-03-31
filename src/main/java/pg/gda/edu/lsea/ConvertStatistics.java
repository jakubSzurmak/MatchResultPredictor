package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.fPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.gPlayerStatistics;

import java.util.*;

public class ConvertStatistics {
    public void convert(List<Player> players, List<Event> events){
        System.out.println("Converting statistics...");
        Map<UUID, PlayerStatistics> playerS = new HashMap<>();
        Map<UUID, Player> uniquePlayers = new HashMap<>();
        for(Player player:players){
            uniquePlayers.put(player.getId(),player);
        }
        System.out.println("Unique players: " + uniquePlayers.size());
        // co to za id w evencie

        for(Event event:events){
            if(uniquePlayers.containsKey(event.getId())){
                System.out.println("Found player: " + event.getId());
                if(playerS.containsKey(event.getId())){
                    System.out.println("Player already exists: " + event.getId());
                }
                else {

                    if(uniquePlayers.get(event.getId()).getPositions().contains("Goalkeeper")){
                        playerS.put(event.getId(), new gPlayerStatistics(event.getId()));
                        System.out.println("Added goalkeeper: " + event.getId() + " " + uniquePlayers.get(event.getId())
                                .getName());
                    }
                    else{
                        playerS.put(event.getId(), new fPlayerStatistics(event.getId()));
                        System.out.println("Added player: " + event.getId() + " " + uniquePlayers.get(event.getId())
                                .getName());

                    }

                }
            }
            else{
                System.out.println("Player not found: " + event.getId());
                continue;
            }

        }

    }

}
