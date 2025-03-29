package pg.gda.edu.lsea.teams;

import java.util.Map;
import java.util.UUID;

public class Team {
    /** Unique identifier of team */
    final private UUID id;
    /** Name of the team */
    private String name;
    /** Country which team is located in */
    private Map<UUID, String> country;

    /**
     * Constructs object with some specified ID
     *
     * @param id is the unique identifier of the team
     */
    public Team(UUID id){
        this.id = id;
    }

    /**
     * Constructs object with some specified attributes
     *
     * @param id is the unique identifier of the team
     * @param name is the name of the team
     * @param country is the country team is located in
     */
    public Team(UUID id, String name, Map<UUID, String> country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    /**
     * Returns the unique identifier of the team
     *
     * @return the unique identifier of the team
     */
    public UUID getId(){return this.id;}

    /**
     * Returns the name of the team
     *
     * @return the name of the team
     */
    public String getName(){ return this.name;}

    /**
     * Sets the name of the object
     *
     * @param name is the name of the team
     */
    public void setName(String name){this.name = name;}

    /**
     * Returns the country team is located in
     *
     * @return the country team is located in
     */
    public Map<UUID, String> getCountry(){return this.country;}

    /**
     * Sets the country of the object
     *
     * @param country is the country of the team
     */
    public void setCountry(Map<UUID, String> country){this.country = country;}

}
