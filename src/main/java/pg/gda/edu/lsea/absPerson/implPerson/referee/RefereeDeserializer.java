package pg.gda.edu.lsea.absPerson.implPerson.referee;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Custom JSON deserializer for referee data.
 * This class is responsible for converting JSON data into {@link Referee} objects
 */
public class RefereeDeserializer extends JsonDeserializer<Referee> {

    /**
     * Converts an integer ID to UUID
     *
     * @param id is the integer ID to convert
     * @return a UUID generated from the provided ID
     */
    private static UUID convertToUUID(int id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Deserializes JSON contnet into a Referee objects
     *
     * @param jp is the JSON Parser
     * @param ctxt is the deserialization context
     * @return a populated referee object or null if no referee data is present
     * @throws IOException if an error occurs during JSON processing
     */
    @Override
    public Referee deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException{
        JsonNode node = jp.getCodec().readTree(jp);
        if(node.get("referee") != null) {
            // Create a new Referee instance with UUID generated from the ID
            Referee referee = new Referee(convertToUUID(node.get("referee").get("id").asInt()));

            // Set the referee's name
            referee.setName(node.get("referee").get("name").asText());

            // Create and set the country information
            Map<UUID, String> country = new HashMap<>();
            country.put(convertToUUID(node.get("referee").get("country").get("id").asInt()),
                    node.get("referee").get("country").get("name").asText());
            referee.setCountry(country);
            return referee;
        }
        return null;
    }
}
