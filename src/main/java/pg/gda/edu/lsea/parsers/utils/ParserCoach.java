package pg.gda.edu.lsea.parsers.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.absPerson.implPerson.coach.ResultHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ParserCoach {

    public Set<Coach> parseCoache() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<ResultHolder> bothCoaches = new HashSet<>();
        int counter = 0;
        String directory = "C:\\Users\\Mikolaj\\Desktop\\matches\\";
        try {
            List<Path> paths = Files.walk(Paths.get(directory), 2)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .collect(Collectors.toList());

            for (Path path : paths) {
                try {
                    bothCoaches.addAll(objectMapper.readValue(path.toFile(),
                            new TypeReference<Set<ResultHolder>>() {
                            }));
                } catch (Exception e) {
                    System.err.println("Failed to parse JSON in file: " + path.toString() + " due to: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        bothCoaches = bothCoaches.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Coach> coaches = new HashSet<>();
        for (ResultHolder resultHolder : bothCoaches) {
            coaches.add(resultHolder.getAwayCoach());
            coaches.add(resultHolder.getHomeCoach());
        }
        return coaches;
    }
}



