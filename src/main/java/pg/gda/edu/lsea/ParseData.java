package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.parsers.utils.ParserCoach;
import pg.gda.edu.lsea.parsers.utils.ParserMatch;
import pg.gda.edu.lsea.parsers.utils.ParserReferee;
import pg.gda.edu.lsea.parsers.utils.TeamParser;
import pg.gda.edu.lsea.team.Team;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ParseData {

    public static void main(String[] args) throws CloneNotSupportedException, IOException {
        System.out.println("Parsing data...");
        List<Match> matches = new ParserMatch().parseMatch();
        Set<Referee> referees = new ParserReferee().parseReferee();
        Set<Coach> coaches = new ParserCoach().parseCoache();


        System.out.println(matches.size() + " - matches in total");
        System.out.println(referees.size() + " - referees in total");
        System.out.println(coaches.size() + " - coaches in total");
        List<Team> parsedTeams = new ArrayList<>();
        List<Player> parsedPlayers = new ArrayList<>();
        List<Event> parsedEvents = new ArrayList<>();
        String directory = "C:\\Users\\Mikolaj\\Desktop\\matches\\"; // sciezka dla meczow, trenerow, sedziow, timow
        String directory2 = "C:\\Users\\Mikolaj\\Desktop\\lineups\\";
        String directory3 = "C:\\Users\\Mikolaj\\Desktop\\events\\"; //sciezka dla eventow
        String directory4 = "C:\\Users\\Mikolaj\\Desktop\\player_rating.java"; //sciezka dla ratingow
        try {
            List<Path> paths = Files.walk(Paths.get(directory), 2)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .collect(Collectors.toList());

            for (Path path : paths) {
                parsedTeams.addAll(TeamParser.parsing(String.valueOf(path.toFile())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(matches.size() + " - matches in total");
        System.out.println(referees.size() + " - referees in total");
        System.out.println(coaches.size() + " - coaches in total");
        System.out.println(parsedTeams.size() + " - teams in total");
        System.out.println();


    }
}
