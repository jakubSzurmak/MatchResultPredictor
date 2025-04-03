package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.parsers.utils.*;
import pg.gda.edu.lsea.team.Team;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ParseData {

    public static List<Path> getFilePath(String directory, int depth) throws IOException {
        return Files.walk(Paths.get(directory), depth)
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().endsWith(".json"))
                .collect(Collectors.toList());


    }


    public static void main(String[] args) throws CloneNotSupportedException, IOException {
        System.out.println("Parsing data...");
        List<Match> matches = new ParserMatch().parseMatch();
        Set<Referee> referees = new ParserReferee().parseReferee();
        Map<UUID, Coach> coaches = new ParserCoach().parseCoache();

        List<Team> parsedTeams = new ArrayList<>();
        List<Player> parsedPlayers = new ArrayList<>();
        List<Event> parsedEvents = new ArrayList<>();
        int counter = 0;
        String directory = "C:\\Users\\ASUS RoG\\Desktop\\matches\\"; // sciezka dla meczow, trenerow, sedziow, timow
        String directory2 = "C:\\Users\\ASUS RoG\\Desktop\\lineupsModified\\";
        String directory3 = "C:\\Users\\ASUS RoG\\Desktop\\events\\"; //sciezka dla eventow
        String directory4 = "C:\\Users\\ASUS RoG\\Desktop\\player_rating.json"; //sciezka dla ratingow
        try {
            List<Path> pathsL = getFilePath(directory, 2);

            List<Path> pathsE = getFilePath(directory3, 1);

            List<Path> pathsP = getFilePath(directory2, 1);

            for (Path path : pathsL) {
                parsedTeams.addAll(ParserTeam.parsing(String.valueOf(path.toFile())));
            }
            for(Path path: pathsE){
                parsedEvents.addAll(ParserEvent.parsing(String.valueOf(path.toFile())));
                counter++;
                System.out.println(counter);
                if(counter == 100){
                    break;
                }
            }
            for(Path path: pathsP){
                parsedPlayers.addAll(ParserPlayer.parsing(String.valueOf(path.toFile()), directory4));
                counter++;
                System.out.println(counter);
                if(counter == 200) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(matches.size() + " - matches in total");
        System.out.println(referees.size() + " - referees in total");
        System.out.println(coaches.size() + " - coaches in total");
        System.out.println(parsedTeams.size() + " - teams in total");
        System.out.println(parsedPlayers.size() + " - players in total");
        System.out.println(parsedEvents.size() + " - events in total");
        System.out.println();
        ConvertStatistics convertStatistics = new ConvertStatistics();
        convertStatistics.convert(parsedPlayers, parsedEvents, matches);


    }
}
