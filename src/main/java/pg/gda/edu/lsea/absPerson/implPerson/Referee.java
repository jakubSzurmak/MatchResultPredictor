package pg.gda.edu.lsea.absPerson.implPerson;

import pg.gda.edu.lsea.absPerson.Person;

import java.util.Map;
import java.util.UUID;

public class Referee extends Person {
    public Referee(UUID id) {
        super(id);
    }

    public Referee(UUID id, String name, Map<Integer, String> country) {
        super(id, name, country);
    }


}
