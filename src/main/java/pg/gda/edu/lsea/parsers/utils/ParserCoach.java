package pg.gda.edu.lsea.parsers.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.absPerson.implPerson.coach.ResultHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for parsing coach data from JSON files
 */
public class ParserCoach {

    /**
     * Processes a set of ResultHolder objects to extract coach information
     * and update the coaches map
     *
     * @param bothCoaches is set of ResultHolder objects containing coach data
     * @param coaches is map storing coach objects with UUID as key
     * @param isHome is boolean indicating whether to process home (true) or away (false) coaches
     */
    private static void getCoaches(Set<ResultHolder> bothCoaches, Map<UUID, Coach> coaches, boolean isHome){
        for (ResultHolder resultHolder : bothCoaches) {
            UUID coachId;
            Coach currentCoach;
            if(isHome){
                if(resultHolder.getHomeCoach() == null) continue;
                coachId = resultHolder.getHomeCoach().getId();
                currentCoach = resultHolder.getHomeCoach();
            }
            else{
                if(resultHolder.getAwayCoach() == null) continue;
                coachId = resultHolder.getAwayCoach().getId();
                currentCoach = resultHolder.getAwayCoach();
            }

            if (coaches.containsKey(coachId)) {
                Map<String, HashSet<String>> existingEmployments = coaches.get(coachId).getEmployments();
                for (String season : currentCoach.getEmployments().keySet()) {
                    if (existingEmployments.containsKey(season)) {
                        existingEmployments.get(season).add(
                                currentCoach.getEmployments().get(season).stream().
                                        findFirst().orElse(null)
                        );
                    } else {
                        existingEmployments.put(season, currentCoach.getEmployments().get(season));
                    }
                }
            } else {
                coaches.put(coachId, currentCoach);
            }
        }
    }

    /**
     * Parses coach data from JSON files in the
     * "matches" directory and returns a map of coaches
     *
     * @return map containing Coach objects with UUID as key
     * @throws IOException if there is an error reading or parsing the JSON file
     */
    public Map<UUID,Coach> parseCoache() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<ResultHolder> bothCoaches = new HashSet<>();
        int counter = 0;
        String directory = "matches";
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
        Map<UUID, Coach> coaches = new HashMap<>();
        getCoaches(bothCoaches, coaches, true);
        getCoaches(bothCoaches, coaches, false);
        return coaches;

    }
}



