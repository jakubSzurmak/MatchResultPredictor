package pg.gda.edu.lsea.absPerson.implPerson.coach;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * JSON deserializer for coaches data
 * This  class is responsible for converting JSON data into {@link ResultHolder} objects
 * containing information about home and away team coaches
 */
public class CoachDeserializer extends JsonDeserializer<ResultHolder> {
    /**
     * Converts an integer ID to UUID
     *
     * @param id is the integer ID to convert
     * @return is UUID generated from the provided ID
     */
    private static UUID convertToUUID(int id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
    }


    /**
     * Creates a Coach object from the provided JSON nodes
     *
     * @param nodeG is the JSON node containing coach-specific information
     * @param node is the parent JSON node containing context information like team and season
     * @return a fully populated Coach object
     */
    private static Coach createCoach(JsonNode nodeG, JsonNode node){
        ArrayList<Coach> coaches = new ArrayList<>();
        for (JsonNode element : nodeG) {
            Coach coach = new Coach(convertToUUID(element.get("id").asInt()));
            coach.setName(element.get("name").asText());
            coach.setNickname(element.get("nickname").asText());

            // Set employment information
            Map<String, HashSet<String>> employment = new HashMap<>();
            HashSet<String> clubs = new HashSet<>();
            clubs.add(node.get("home_team").get("home_team_name").asText());
            employment.put(node.get("season").get("season_name").asText(),
                   clubs);
            coach.setEmployments(employment);

            // Set country information
            Map<UUID, String> country = new HashMap<>();
            country.put(convertToUUID(element.get("country").get("id").asInt()),
                    element.get("country").get("name").asText());
            coach.setCountry(country);
            coaches.add(coach);
        }
        return coaches.get(0);
    }

    /**
     * Deserializes JSON content into a ResultHolder object
     *
     * @param jp is the JSON Parser
     * @param ctxt is the deserialization context
     * @return ResultHolder containing home and away team coach information, or null if neither team has managers
     * @throws IOException if an error occurs during JSON processing
     */
    @Override
    public ResultHolder deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException{
        JsonNode node = jp.getCodec().readTree(jp);

        // Handles cases where one or both teams don't have manager info
        if(node.get("home_team").get("managers") == null && node.get("away_team").get("managers") == null)
        {
            return null;
        }else if(node.get("home_team").get("managers") == null)
        {
            return new ResultHolder(null, createCoach(node.get("away_team").get("managers"), node));
        }
        else if(node.get("away_team").get("managers") == null){
            return new ResultHolder(createCoach(node.get("home_team").get("managers"), node), null);
        }

        // Both team have managers
        Coach coach = createCoach(node.get("home_team").get("managers"), node);
        Coach coach1 = createCoach(node.get("away_team").get("managers"), node);


        return new ResultHolder(coach, coach1);
    }

}
