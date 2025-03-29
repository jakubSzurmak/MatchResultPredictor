package pg.gda.edu.lsea.absPerson.implPerson.referee;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pg.gda.edu.lsea.absPerson.Person;

import java.util.Map;
import java.util.UUID;

/**
 * Referee extends the Person class and
 * represents a fotball referee
 * <p>
 * The class does not implement any additional
 * fields or methods besides the constructors
 */

@JsonDeserialize(using = RefereeDeserializer.class)
public class Referee extends Person {
    /**
     * Constructs a referee with some specified ID
     *
     * @param id is the unique identifier for this referee
     */
    public Referee(UUID id) {
        super(id);
    }

    /**
     * Constructs a Referee with the specified ID, name and country of origin
     *
     * @param id is the unique identifier for this referee
     * @param name is the name for this referee
     * @param country is the country of origin for this referee
     */
    public Referee(UUID id, String name, Map<UUID, String> country) {
        super(id, name, country);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Referee) {
            Referee other = (Referee) obj;
            return this.getId().equals(other.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

}
