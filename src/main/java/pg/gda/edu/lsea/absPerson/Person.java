package pg.gda.edu.lsea.absPerson;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Implementation of abstract class Person
 * Provides basics information about
 * the players/coaches/referees
 *
 * @author TeamOrange
 */
@MappedSuperclass
public abstract class Person implements Cloneable {
    /** Unique identifier for the person */
    @Id
    final private UUID id;
    /** Name of the Person */
    @Transient
    private String name;
    /** Person's country of origin done by UUID mapping */
    @Transient
    private Map<UUID, String> country;

    /**
     * Constructs an object of Person
     * with some specified ID
     *
     * @param id is the unique identifier of created Person
     * */
    public Person(UUID id) {
        this.id = id;
    }

    /**
     * Constructs an object of Person with some specified ID, name and country of origin
     *
     * @param id is the unique identifier of created Person
     * @param name is the name of created Person
     * @param country is the country of origin of created Person
     *
     * */
    public Person(UUID id, String name, Map<UUID, String> country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    /**
     * Returns a string representation of this person's field values
     *
     * @return string containing Person's id, name and country of origin
     */
    @Override
    public String toString(){
        return "Person [id=" + id + ", name=" + name + ", country=" + country + "]";
    }

    /**
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Person cloned =  (Person) super.clone();

        if(this.country != null){
            Map<UUID, String> clonedCountry = new HashMap<>();
            this.country.forEach((k,v) -> clonedCountry.put(k, v));
            cloned.country = clonedCountry;
        }

        return cloned;
    }

    /**
     * Returns the unique identifier of the Person
     *
     * @return Person's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the name of the Person
     *
     * @return Person's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Person
     *
     * @param name Person name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns country of origin of the Person
     *
     * @return Person's country of origin
     */
    public Map<UUID, String> getCountry() {
        return country;
    }

    /**
     * Sets the country of origin of the Person
     *
     * @param country Person's country of origin
     */
    public void setCountry(Map<UUID, String> country) {
        this.country = country;
    }
}
