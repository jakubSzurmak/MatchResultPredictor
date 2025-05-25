package pg.gda.edu.lsea.dataHandlers.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.dataHandlers.utils.InputToTempFile;

import java.io.IOException;
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
                strippedPath = path.substring(2, path.length()-1);
                try {
                    referees.addAll(objectMapper.readValue(InputToTempFile.iSToF(ParserMatch.class.getClassLoader().
                                    getResourceAsStream(strippedPath)),
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
