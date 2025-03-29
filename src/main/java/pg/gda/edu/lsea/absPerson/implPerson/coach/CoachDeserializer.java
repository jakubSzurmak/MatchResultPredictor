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

public class CoachDeserializer extends JsonDeserializer<ResultHolder> {
    private static UUID convertToUUID(int id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public ResultHolder deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException{
        JsonNode node = jp.getCodec().readTree(jp);
        List<Coach> coaches = new ArrayList<>();
        if(node.get("home_team").get("managers") == null || node.get("away_team").get("managers") == null)
        {
            return null;
        }

        for (JsonNode element : node.get("home_team").get("managers")) {
            Coach coach = new Coach(convertToUUID(element.get("id").asInt()));
            coach.setName(element.get("name").asText());
            coach.setNickname(element.get("nickname").asText());
            coach.setSeason(node.get("season").get("season_name").asText());
            coach.setCurrEmployment(node.get("home_team").get("home_team_name").asText());
            Map<UUID, String> country = new HashMap<>();
            country.put(convertToUUID(element.get("country").get("id").asInt()),
                    element.get("country").get("name").asText());
            coach.setCountry(country);
            coaches.add(coach);
        }

        for(JsonNode element : node.get("away_team").get("managers")){
            Coach coach = new Coach(convertToUUID(element.get("id").asInt()));
            coach.setName(element.get("name").asText());
            coach.setNickname(element.get("nickname").asText());
            coach.setSeason(node.get("season").get("season_name").asText());
            coach.setCurrEmployment(node.get("away_team").get("away_team_name").asText());
            Map<UUID, String> country = new HashMap<>();
            country.put(convertToUUID(element.get("country").get("id").asInt()),
                    element.get("country").get("name").asText());
            coach.setCountry(country);
            coaches.add(coach);
        }

        return new ResultHolder(coaches.get(0), coaches.get(1));
    }

}
