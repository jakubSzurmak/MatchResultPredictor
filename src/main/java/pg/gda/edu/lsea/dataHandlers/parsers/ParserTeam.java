package pg.gda.edu.lsea.dataHandlers.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pg.gda.edu.lsea.database.DbManager;
import pg.gda.edu.lsea.team.Team;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Utility class for parsing team data from JSON files into Team objects
 */
public class ParserTeam {

    /**
     * Parses team data from a JSON file and
     * returns a list of unique Team objects
     *
     * @param filePath is Path to the JSON file containg team data
     * @return List of unique parsed Team objects
     * @throws IOException if there is an error reading or parsing the JSON file
     */
    public static List<Team> parsing(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Team> parsedTeams = new ArrayList<>();
        DbManager dbManager = new DbManager();

        List<JsonNode> teams = objectMapper.readValue(new File(filePath), new TypeReference<>() {});
        for (JsonNode team : teams) {

            JsonNode homeTeamNode = team.get("home_team");
            if (homeTeamNode != null) {
                UUID teamId = convertToUUID(homeTeamNode.get("home_team_id").asText());
                String teamName = homeTeamNode.get("home_team_name").asText();

                JsonNode countryNode = homeTeamNode.get("country");
                UUID countryID = countryNode != null ? convertToUUID(countryNode.get("id").asText()) : null;
                String countryName = countryNode != null ? countryNode.get("name").asText() : "Unknown";

                Map<UUID, String> country = new HashMap<>();
                if (countryID != null) {
                    country.put(countryID, countryName);
                }

                if (parsedTeams.stream().noneMatch(t -> t.getId().equals(teamId))) {
                    parsedTeams.add(new Team(teamId, teamName, country));
                    dbManager.saveToDb(parsedTeams.get(parsedTeams.size() - 1));
                }
            }

            JsonNode awayTeamNode = team.get("away_team");
            if (awayTeamNode != null) {
                UUID teamIdTwo = convertToUUID(awayTeamNode.get("away_team_id").asText());
                String teamNameTwo = awayTeamNode.get("away_team_name").asText();

                JsonNode countryNodeTwo = awayTeamNode.get("country");
                UUID countryIDTwo = countryNodeTwo != null ? convertToUUID(countryNodeTwo.get("id").asText()) : null;
                String countryNameTwo = countryNodeTwo != null ? countryNodeTwo.get("name").asText() : "Unknown";

                Map<UUID, String> countryTwo = new HashMap<>();
                if (countryIDTwo != null) {
                    countryTwo.put(countryIDTwo, countryNameTwo);
                }

                if (parsedTeams.stream().noneMatch(t -> t.getId().equals(teamIdTwo))) {
                    parsedTeams.add(new Team(teamIdTwo, teamNameTwo, countryTwo));
                    dbManager.saveToDb(parsedTeams.get(parsedTeams.size() - 1));

                }
            }
        }

        return parsedTeams;
    }

    /**
     * Converts a string ID to a UUID
     *
     * @param id is String identifier to convert
     * @return UUID generated from the input string
     */
    private static UUID convertToUUID(String id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
    }

}
