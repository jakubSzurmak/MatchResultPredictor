package pg.gda.edu.lsea.match;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.absPerson.implPerson.coach.ResultHolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

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

        if(node.get("home_team").get("managers") == null && node.get("away_team").get("managers") == null)
        {
            match.setHomeCoachId(null);
            match.setAwayCoachId(null);
        }
        else if(node.get("home_team").get("managers") == null)
        {
            match.setHomeCoachId(null);
            match.setAwayCoachId(convertToUUID(node.get("away_team").get("managers").get(0).get("id").asInt()));
        }
        else if(node.get("away_team").get("managers") == null){
            match.setHomeCoachId(convertToUUID(node.get("home_team").get("managers").get(0).get("id").asInt()));
            match.setAwayCoachId(null);
        }else if(node.get("home_team").get("managers") != null && node.get("away_team").get("managers") != null){
            match.setHomeCoachId(convertToUUID(node.get("home_team").get("managers").get(0).get("id").asInt()));
            match.setAwayCoachId(convertToUUID(node.get("away_team").get("managers").get(0).get("id").asInt()));
        }
        return match;
    }
}
