package pg.gda.edu.lsea.absPerson.implPerson;

import jakarta.persistence.*;
import pg.gda.edu.lsea.absPerson.Person;

import pg.gda.edu.lsea.team.Team;

import java.time.LocalDate;
import java.util.*;


/**
 * Player class extends Person class
 * and represents a football player
 * <p>
 * The content of the class is extended
 * by specific attributes like nickname,
 * date of birth, jersey number, current club,
 * positions and player rating, and by
 * the Comparable interface which is used
 * to sort Players by their rating.
 */
@Entity
@Table(name="Players")
public class Player extends Person implements Comparable<Player>, Cloneable {
    /** Nickname of the player */

    @Transient
    private String nickname;
    /** Date of birth of the player */
    @Transient
    private LocalDate dateOfBirth;
    /** Jersey number of the player */
    @Transient
    private int jerseyNr;
    /** Current club of the player */
    @Transient
    private String currClub;
    /** List of positions that the player plays */
    @Transient
    private ArrayList<String> positions; //to discuss
    /** Rating of the player */
    @Transient
    private int rating;
    /** List of positions joined into one string seperated by ",". Empty-only for annotation mechanism */
    private String positionsString;

    @ManyToMany
    @JoinTable(
            name = "player_teams",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<Team> teamSet = new HashSet<>();

    /**
     * Constructs a Player with some specified ID
     *
     * @param id is the unique identifier for this player
     */
    public Player(UUID id) {
        super(id);
    }

    /**
     * Constructs a Player with many specified field attributes
     *
     * @param id is the unique identifier for this player
     * @param name is the name of this player
     * @param country is the country of origin of this player
     * @param nickname is the nickname of this player
     * @param dateOfBirth is the date of birth of this player
     * @param jerseyNr is the jersey number of this player
     * @param currClub is the current club this player belongs to
     * @param positions is the list of positions this player plays at
     * @param rating is the rating of this player
     */
    public Player(UUID id, String name, Map<UUID, String> country, String nickname,
                  LocalDate dateOfBirth, int jerseyNr, String currClub, ArrayList<String> positions, int rating) {
        super(id, name, country);
        this.nickname = nickname;
        this.dateOfBirth = dateOfBirth;
        this.jerseyNr = jerseyNr;
        this.currClub = currClub;
        this.positions = positions;
        this.rating = rating;
        this.positionsString = positions.toString();
    }

    /**
     * Nameless and parameterless constructor for jpa requirements sake. DO NOT USE
     */
    public Player() {
        super(null);
    }

    /**
     * Compares this player with another player based on their rating
     *
     * @param o the player to be compared
     * @return a negative integer, zero, or a positive integer as this player's
     * rating is less than, equal to or greater than the specified player's rating
     */
    @Override
    public int compareTo(Player o) {
        return Integer.compare(rating, o.getRating());
    }


    @Override
    public Player clone() throws CloneNotSupportedException {
        try{
            Player cloned = (Player) super.clone();

            if(this.positions != null){
                cloned.positions = new ArrayList<>(this.positions);
            }

            if(this.getCountry() != null){
                Map<UUID, String> clonedCountry = new HashMap<>();
                this.getCountry().forEach((k, v) -> clonedCountry.put(k, v));
                cloned.setCountry(clonedCountry);
            }

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning Player failed", e);
        }
    }


    protected String joinPositions(ArrayList<String> positions) {
        return String.join(",", positions);
    }

    public String getPositionsString() {
        return joinPositions(this.positions);
    }

    /**
     * Returns the rating of this player
     *
     * @return the rating of this player
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets some rating for this player
     *
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Returns the nickname of this player
     *
     * @return the nickname of this player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname for this player
     *
     * @param nickname the nickname for this player to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns the date of birth of this player
     *
     * @return the date of birth of this player
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of this player
     *
     * @param dateOfBirth is the date of birth to set
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Returns the jersey number of this player
     *
     * @return the jersey number of this player
     */
    public int getJerseyNr() {
        return jerseyNr;
    }

    /**
     * Sets the jersey number of this player
     *
     * @param jerseyNr is the jersey number to set
     */
    public void setJerseyNr(int jerseyNr) {
        this.jerseyNr = jerseyNr;
    }

    /**
     * Returns the current club of this player
     *
     * @return the current club of this player
     */
    public String getCurrClub() {
        return currClub;
    }

    /**
     * Sets the current club of this player
     *
     * @param currClub is the current club to set for this player
     */
    public void setCurrClub(String currClub) {
        this.currClub = currClub;
    }

    /**
     * Returns the list of positions of this player
     *
     * @return the list of positions of this player
     */
    public ArrayList<String> getPositions() {
        return positions;
    }

    /**
     * Sets the list of positions of this player
     *
     * @param positions is the lsit of positions to set for this player
     */
    public void setPositions(ArrayList<String> positions) {

        this.positions = positions;
        this.positionsString = positions.toString();
    }


    /**
     * Compares two players if they are the same
     *
     * @param o is the object to compare
     * @return true/false statement if the players are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return this.getId().equals(player.getId());
    }

    /**
     * Returns hashCode of the UUID of the player
     *
     * @return hashCode of the UUID of the player
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
    private void ensureTeamSet() {
        if (this.teamSet == null) {
            this.teamSet = new HashSet<>();
        }
    }

    public void setTeamSet (Set<Team> teamSet) { this.teamSet = teamSet; }
    public void updateTeamSet(Team team) {
        this.ensureTeamSet();
        this.teamSet.add(team);
    }

    public Set<Team> getTeamSet() {
        return teamSet;
    }
}
