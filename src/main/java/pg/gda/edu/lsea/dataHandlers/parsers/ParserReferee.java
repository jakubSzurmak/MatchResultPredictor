package pg.gda.edu.lsea.dataHandlers.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.gda.edu.lsea.absPerson.implPerson.coach.ResultHolder;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for parsing referee data from JSON files into Referee objects.
 */
public class ParserReferee {
    /**
     * Parses referee data from JSON files in the
     * "matches" directory and returns a set of
     * Referee objects
     *
     * @return Set of parsed Referee objects
     * @throws IOException if there is an error reading or accessing the files in the directory
     */
    public Set<Referee> parseReferee(String[] filenames) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<Referee> referees = new HashSet<>();
        try {
            for (String path : filenames) {
                try {
                    referees.addAll(objectMapper.readValue("matchesModified/"
                                    + path.substring(2, path.length()-1),
                            new TypeReference<Set<Referee>>() {
                            }));
                } catch (Exception e) {

                    System.err.println("Failed to parse JSON in file: " + path + " due to: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        referees = referees.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        return referees;
    }
}
