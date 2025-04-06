package pg.gda.edu.lsea.parsers.utils;

import pg.gda.edu.lsea.event.Event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.*;

public class ParserEvent {

    private static void setTeam(Map<UUID, ArrayList<UUID>> team, JsonNode teamNode, UUID teamId){
        ArrayList<UUID> teamIds = new ArrayList<>();
        for(JsonNode node : teamNode){
            teamIds.add(convertToUUID(node.get("player").get("id").asText()));
        }
        team.put(teamId,teamIds);

    }

    public static List<Event> parsing(String filePath, int matchId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Event> parsedEvents = new ArrayList<>();

        List<JsonNode> events = objectMapper.readValue(new File(filePath), new TypeReference<List<JsonNode>>() {});
        Map<UUID, ArrayList<UUID>> team1 = new HashMap<>();
        int counter = 0;
        for (JsonNode event : events) {
            // Event id - nie chodzilo tutaj o event tylko o player id do zmiany
            UUID eventID = UUID.fromString(event.get("id").asText());
            if(counter == 0){
                UUID teamId = convertToUUID(event.get("team").get("id").asText());
                team1.put(teamId,null);
                counter++;
                setTeam(team1,event.get("tactics").get("lineup"),teamId);
            }
            else if(counter ==1){
                UUID teamId = convertToUUID(event.get("team").get("id").asText());
                team1.put(teamId,null);
                counter++;
                setTeam(team1,event.get("tactics").get("lineup"), teamId);
            }

            int eventHalf = event.get("period").asInt();

            LocalTime eventTime = LocalTime.parse(event.get("timestamp").asText());

            String type = event.get("type").get("name").asText();

            UUID idPerformTeam = convertToUUID(event.get("team").get("id").asText());
            UUID idPerformPlayer = event.has("player")
                    ? convertToUUID(event.get("player").get("id").asText())
                    : UUID.fromString("00000000-0000-0000-0000-000000000000");


            JsonNode playPatternNode = event.get("play_pattern");
            String playPatternName = "";
            if (playPatternNode != null) {
                playPatternName = playPatternNode.get("name").asText();
            }

            String performBodyPart = event.has("pass") && event.get("pass").has("body_part")
                    ? event.get("pass").get("body_part").get("name").asText()
                    : "Unknown";


            String outcome = "Unknown";
            String[] possibleTypes = { "pass", "ball_receipt", "interception", "goalkeeper", "shot", "duel"};

            boolean eventAssist = false;

            if (event.has("pass") && event.get("pass").has("shot_assist")) {
                String eventAssistText = event.get("pass").get("shot_assist").asText();
                eventAssist = Boolean.parseBoolean(eventAssistText);
            }

            for (String typer : possibleTypes) {
                if (event.has(typer) && event.get(typer).has("outcome") && event.get(typer).get("outcome").has("name")) {
                    outcome = event.get(typer).get("outcome").get("name").asText();

                    break;
                }
            }
            if(event.get("type").get("name").asText().equals("Substitution")){
                UUID teamId = convertToUUID(event.get("team").get("id").asText());
                team1.get(teamId).
                        add(convertToUUID(event.get("substitution").get("replacement").get("id").asText()));
            }

            Event newEvent = new Event(eventID, eventHalf, eventTime, playPatternName, idPerformTeam, idPerformPlayer,
                    performBodyPart, type, outcome, eventAssist,team1,matchId);
            parsedEvents.add(newEvent);

        }

        return parsedEvents;
    }

    private static UUID convertToUUID(String id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
    }

}
