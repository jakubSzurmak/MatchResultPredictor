package pg.gda.edu.lsea.parsers.utils;

import pg.gda.edu.lsea.absPerson.implPerson.Player;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

public class ParserPlayer {

    /**
         * PlayerData class to store both rating and DOB information
         */
        public record PlayerData(int rating, LocalDate dateOfBirth) {
    }

    /**
     * Creates a map of player names to their data (rating and DOB) for fast lookup
     * @param ratingsFilePath Path to the ratings JSON file
     * @return Map with player names as keys and PlayerData objects as values
     * @throws IOException If there's an error reading the file
     */
    public static Map<String, PlayerData> parseRatingsToMap(String ratingsFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, PlayerData> ratingsMap = new HashMap<>();

        List<JsonNode> ratings = objectMapper.readValue(new File(ratingsFilePath),
                new TypeReference<>() {});

        // Pre-process all ratings into a map for O(1) lookup
        for (JsonNode ratingNode : ratings) {
            String playerName = ratingNode.get("long_name").asText();
            int rating = ratingNode.get("overall").asInt();

            // Parse the date of birth if available
            LocalDate dob = LocalDate.parse("1900-01-01"); // Default value
            if (ratingNode.has("dob") && !ratingNode.get("dob").isNull()) {
                try {
                    dob = LocalDate.parse(ratingNode.get("dob").asText());
                } catch (Exception e) {
                    // Keep default if parsing fails
                    //System.err.println("Failed to parse DOB for player: " + playerName);
                }
            }

            ratingsMap.put(playerName, new PlayerData(rating, dob));
        }

        return ratingsMap;
    }

    /**
     * Modified version that accepts a pre-built ratings map for better performance
     * @param filePath Path to the lineup file
     * @param playerDataMap Pre-processed map of player data (ratings and DOB)
     * @return Set of Player objects with their ratings and DOB
     * @throws IOException If there's an error reading the files
     */
    public static HashSet<Player> parsing(String filePath, Map<String, PlayerData> playerDataMap) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashSet<Player> parsedPlayers = new HashSet<>();

        List<JsonNode> teams = objectMapper.readValue(new File(filePath), new TypeReference<>() {});

        for (JsonNode team: teams) {
            String teamName = team.get("team_name").asText();
            JsonNode lineup = team.get("lineup");

            if (lineup != null && lineup.isArray()) {
                for (JsonNode node : lineup) {
                    UUID playerID = convertToUUID(node.get("player_id").asText());
                    String name = "";
                    if (node.has("player_name")) {
                        name = node.get("player_name").asText();
                    }

                    String nickname = node.get("player_nickname").asText();
                    int jerseyNumber = node.get("jersey_number").asInt();

                    Map<UUID, String> country = new HashMap<>();
                    JsonNode countryNode = node.get("country");
                    if (countryNode != null) {
                        UUID countryID = convertToUUID(countryNode.get("id").asText());
                        String countryName = countryNode.get("name").asText();
                        country.put(countryID, countryName);
                    }

                    ArrayList<String> playerPositions = new ArrayList<>();
                    JsonNode positionNode = node.get("positions");
                    if (positionNode != null) {
                        for (JsonNode position : positionNode) {
                            String positionName = position.get("position").asText();
                            playerPositions.add(positionName);
                        }
                    }

                    // O(1) lookup in the map for both rating and DOB
                    int rating = 50; // Default rating
                    LocalDate dateOfBirth = LocalDate.parse("1900-01-01"); // Default DOB

                    if (playerDataMap.containsKey(name)) {
                        PlayerData playerData = playerDataMap.get(name);
                        rating = playerData.rating();
                        dateOfBirth = playerData.dateOfBirth();
                    }

                    Player newPlayer = new Player(playerID, name, country, nickname, dateOfBirth, jerseyNumber, teamName, playerPositions, rating);
                    parsedPlayers.add(newPlayer);
                }
            }
        }

        return parsedPlayers;
    }

    private static UUID convertToUUID(String id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
    }

}