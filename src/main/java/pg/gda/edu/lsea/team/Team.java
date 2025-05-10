package pg.gda.edu.lsea.team;

import jakarta.persistence.*;
import pg.gda.edu.lsea.absPerson.implPerson.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="Teams")
public class Team {
    /** Unique identifier of team */
    @Id
    final private UUID id;
    /** Name of the team */
    private String name;
    /** Country which team is located in */
    @Transient
    private Map<UUID, String> country;

    @ManyToMany(mappedBy = "teamSet")
    private Set<Player> playerSet = new HashSet<>();


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
     * Nameless constructor for JPA sake. DO NOT USE
     */
    protected Team() {this.id = null;}

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


    public void setPlayerSet(Set playerSet){this.playerSet = playerSet;}
    public void ensurePlayerSet(){
        if(playerSet == null){
            playerSet = new HashSet<>();
        }
    }
    public void updatePlayerSet(Player player){
        this.ensurePlayerSet();
        this.playerSet.add(player);
    }
}
