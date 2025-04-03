package pg.gda.edu.lsea.parsers.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.gda.edu.lsea.absPerson.implPerson.coach.ResultHolder;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.match.Match;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ParserReferee {

    public Set<Referee> parseReferee() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<Referee> referees = new HashSet<>();
        String directory = "C:\\Users\\ASUS RoG\\Desktop\\matches\\";
        try {
            List<Path> paths = Files.walk(Paths.get(directory), 2)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .collect(Collectors.toList());

            for (Path path : paths) {
                try {
                    referees.addAll(objectMapper.readValue(path.toFile(),
                            new TypeReference<Set<Referee>>() {}));
                } catch (Exception e) {

                    System.err.println("Failed to parse JSON in file: " + path.toString() + " due to: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        referees = referees.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        return referees;
    }
}
