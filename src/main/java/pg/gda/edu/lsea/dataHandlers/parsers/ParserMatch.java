package pg.gda.edu.lsea.dataHandlers.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.gda.edu.lsea.match.Match;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for parsing match data from JSON files into Match objects
 */
public class ParserMatch {
    /**
     * Parses match data from JSON files in the "matches" directory
     * and returns a list of Match objects
     *
     * @return List of parsed Match objects
     */
    public List<Match> parseMatch() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Match> matches = new ArrayList<>();
        String directory = "matches";
        try {
            List<Path> paths = Files.walk(Paths.get(directory), 2)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .collect(Collectors.toList());

            for (Path path : paths) {
                try {
                    matches.addAll(objectMapper.readValue(path.toFile(),
                            new TypeReference<List<Match>>() {}));
                } catch (Exception e) {
                    System.err.println("Failed to parse JSON in file: " + path.toString() + " due to: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matches;
    }
}
