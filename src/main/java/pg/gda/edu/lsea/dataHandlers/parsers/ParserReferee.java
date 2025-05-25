package pg.gda.edu.lsea.dataHandlers.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.dataHandlers.utils.InputToTempFile;

import java.io.IOException;
import java.io.InputStream;
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
        String strippedPath;
        try {
            for (String path : filenames) {
                strippedPath = "matchesModified/" + path.substring(1, path.length()-1);
                InputStream is = ParserMatch.class.getResourceAsStream(strippedPath);
                try {
                    referees.addAll(objectMapper.readValue(is, new TypeReference<List<Referee>>() {}));
                } catch (Exception e) {

                    System.err.println("Failed to REFEREE parse JSON in file: " + strippedPath + " due to: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        referees = referees.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        return referees;
    }
}
