package pg.gda.edu.lsea.match;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchDeserializer extends JsonDeserializer<Match>{

    private static UUID convertToUUID(int id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public Match deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException{
        JsonNode node = jp.getCodec().readTree(jp);
        Match match = new Match(convertToUUID(node.get("match_id").asInt()));


        LocalDate date = LocalDate.parse(node.get("match_date").asText());
        match.setDate(date);

        //LocalDate date = LocalDate.parse(node.get("match_date").asText());
        //match.setDate(date);
        Map<UUID, String> comptMap = new HashMap<>();
        comptMap.put(convertToUUID(node.get("competition").get("competition_id").asInt()),
                node.get("competition").get("competition_name").asText());
        match.setCompt(comptMap);
        match.setSeason(node.get("season").get("season_name").asText());
        match.setHomeTeamId(convertToUUID(node.get("home_team").get("home_team_id").asInt()));
        match.setAwayTeamId(convertToUUID(node.get("away_team").get("away_team_id").asInt()));
        match.setHomeScore(node.get("home_score").asInt());
        match.setAwayScore(node.get("away_score").asInt());
        if(node.has("referee")){
            match.setRefereeId(convertToUUID(node.get("referee").get("id").asInt()));
        }
        else{
            match.setRefereeId(null);
        }
      //  match.setRefereeId(convertToUUID(node.get("referee").get("id").asInt()));
        return match;
    }
}
