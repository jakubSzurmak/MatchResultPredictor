package pg.gda.edu.lsea.absPerson;

import java.util.Map;
import java.util.UUID;


// Implementation of abstract class Person - provides basics information about the players/coaches/referees
public abstract class Person {
    final private UUID id;
    private String name;
    private Map<UUID, String> country;

    public Person(UUID id) {
        this.id = id;
    }

    public Person(UUID id, String name, Map<UUID, String> country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public UUID getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<UUID, String> getCountry() {
        return country;
    }

    public void setCountry(Map<UUID, String> country) {
        this.country = country;
    }
}
