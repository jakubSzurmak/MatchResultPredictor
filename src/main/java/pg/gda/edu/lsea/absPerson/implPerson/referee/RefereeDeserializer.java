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

public class RefereeDeserializer extends JsonDeserializer<Referee> {

    private static UUID convertToUUID(int id) {
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Referee deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException{
        JsonNode node = jp.getCodec().readTree(jp);
        if(node.get("referee") != null) {
            Referee referee = new Referee(convertToUUID(node.get("referee").get("id").asInt()));
            referee.setName(node.get("referee").get("name").asText());
            Map<UUID, String> country = new HashMap<>();
            country.put(convertToUUID(node.get("referee").get("country").get("id").asInt()),
                    node.get("referee").get("country").get("name").asText());
            referee.setCountry(country);
            return referee;
        }
        return null;
    }
}
