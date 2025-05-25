package pg.gda.edu.lsea.dataHandlers.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;
import pg.gda.edu.lsea.dataHandlers.utils.InputToTempFile;


import java.util.ArrayList;
import java.util.List;


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
    public List<Match> parseMatch(String[] filenames) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Match> matches = new ArrayList<>();
        String strippedPath;
        try {
            for (String path : filenames) {

                try {
                    strippedPath = "matchesModified/" + path.substring(1, path.length()-1);

                    matches.addAll(objectMapper.readValue(InputToTempFile.iSToF(ParserMatch.class.getClassLoader().
                                    getResourceAsStream(strippedPath)),
                            new TypeReference<List<Match>>() {}));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DbManager dbManager = DbManager.getInstance();
        for(Match match : matches) {
            Team homeTeam = dbManager.getTableById(match.getHomeTeamId(), Team.class);
            Team awayTeam = dbManager.getTableById(match.getAwayTeamId(), Team.class);
            match.setHomeTeam(homeTeam);
            match.setAwayTeam(awayTeam);

            //Dodawanie do bazy
            dbManager.saveToDb(match);
        }
        return matches;
    }
}
