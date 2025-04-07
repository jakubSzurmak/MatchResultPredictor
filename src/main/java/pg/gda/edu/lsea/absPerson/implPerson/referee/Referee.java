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

    /**
     * Compares this Referee with another object
     *
     * @param obj is the object to compare with this Referee
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Referee) {
            Referee other = (Referee) obj;
            return this.getId().equals(other.getId());
        }
        return false;
    }

    /**
     * Calculates a hash code for this Referee
     *
     * @return a hash code value for this referee
     */
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

}
