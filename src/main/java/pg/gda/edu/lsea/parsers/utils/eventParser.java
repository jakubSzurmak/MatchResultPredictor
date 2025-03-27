package pg.gda.edu.lsea.parsers.utils;

import pg.gda.edu.lsea.Event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class eventParser {

    public static List<Event> parsing(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Event> parsedEvents = new ArrayList<>();

        List<JsonNode> events = objectMapper.readValue(new File(filePath), new TypeReference<List<JsonNode>>() {});

        for (JsonNode event : events) {

            UUID eventID = UUID.fromString(event.get("id").asText());

            int eventHalf = event.get("period").asInt();

            LocalTime eventTime = LocalTime.parse(event.get("timestamp").asText());

            // TODO Change the UUID to Int
            //int idPerformTeam = event.get("team").get("id").asInt();
            //int idPerformPlayer = event.has("player") ? event.get("player").get("id").asInt() : -1;

            UUID idPerformTeam = UUID.randomUUID();
            UUID idPerformPlayer = UUID.randomUUID();

            JsonNode playPatternNode = event.get("play_pattern");
            String playPatternName = "";
            if (playPatternNode != null) {
                playPatternName = playPatternNode.get("name").asText();
            }

            String performBodyPart = event.has("pass") && event.get("pass").has("body_part")
                    ? event.get("pass").get("body_part").get("name").asText()
                    : "Unknown";

            String outcome = event.has("pass") && event.get("pass").has("outcome")
                    ? event.get("pass").get("outcome").get("name").asText()
                    : "Unknown";


            Event newEvent = new Event(eventID, eventHalf, eventTime, playPatternName, idPerformTeam, idPerformPlayer, performBodyPart, outcome);
            parsedEvents.add(newEvent);

        }

        return parsedEvents;
    }
}
