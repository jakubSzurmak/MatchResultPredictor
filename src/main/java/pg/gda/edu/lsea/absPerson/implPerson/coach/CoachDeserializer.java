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


    private static Coach createCoach(JsonNode nodeG, JsonNode node){
        ArrayList<Coach> coaches = new ArrayList<>();
        for (JsonNode element : nodeG) {
            Coach coach = new Coach(convertToUUID(element.get("id").asInt()));
            coach.setName(element.get("name").asText());
            coach.setNickname(element.get("nickname").asText());
            Map<String, HashSet<String>> employment = new HashMap<>();
            HashSet<String> clubs = new HashSet<>();
            clubs.add(node.get("home_team").get("home_team_name").asText());
            employment.put(node.get("season").get("season_name").asText(),
                   clubs);
            coach.setEmployments(employment);
            Map<UUID, String> country = new HashMap<>();
            country.put(convertToUUID(element.get("country").get("id").asInt()),
                    element.get("country").get("name").asText());
            coach.setCountry(country);
            coaches.add(coach);
        }
        return coaches.get(0);
    }

    @Override
    public ResultHolder deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException{
        JsonNode node = jp.getCodec().readTree(jp);
        if(node.get("home_team").get("managers") == null || node.get("away_team").get("managers") == null)
        {
            return null;
        }

        Coach coach = createCoach(node.get("home_team").get("managers"), node);
        Coach coach1 = createCoach(node.get("away_team").get("managers"), node);


        return new ResultHolder(coach, coach1);
    }

}
