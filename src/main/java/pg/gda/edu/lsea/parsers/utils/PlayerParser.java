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

public class PlayerParser {

    public static List<Player> parsing(String filePath, String filePathTwo) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Player> parsedPlayers = new ArrayList<>();

        List<JsonNode> teams = objectMapper.readValue(new File(filePath), new TypeReference<List<JsonNode>>() {});
        List<JsonNode> ratings = objectMapper.readValue(new File(filePathTwo), new TypeReference<List<JsonNode>>() {});

        for (JsonNode team: teams) {

            String teamName = team.get("team_name").asText();

            JsonNode lineup = team.get("lineup");

            if (lineup != null && lineup.isArray()){

                for (JsonNode node : lineup){
                    UUID playerID = UUID.randomUUID();
                    String name = node.get("player_name").asText();
                    String nickname = node.get("player_nickname").asText();
                    int jerseyNumber = node.get("jersey_number").asInt();

                    Map<UUID, String> country = new HashMap<>();

                    JsonNode countryNode = node.get("country");

                    if (countryNode != null){
                        UUID countryID = convertToUUID(countryNode.get("id").asText());
                        String countryName = countryNode.get("name").asText();

                        country.put(countryID, countryName);
                    }

                    ArrayList<String> playerPositions = new ArrayList<>();

                    JsonNode positionNode = node.get("positions");

                    if (positionNode != null){
                        for (JsonNode position : positionNode){
                            String positionName = position.get("position").asText();

                            playerPositions.add(positionName);
                        }
                    }

                    int rating = 0;
                    LocalDate dateOfBirth = LocalDate.parse("1900-01-01"); // Basic dateOfBirth

                    for (JsonNode ratingNode : ratings){
                        if (ratingNode.get("long_name").asText().equals(name)){
                            rating = ratingNode.get("overall").asInt();
                            dateOfBirth = LocalDate.parse(ratingNode.get("dob").asText());
                            break;
                        }
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
