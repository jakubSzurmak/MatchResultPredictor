package pg.gda.edu.lsea.absPerson.implPerson.coach;

import pg.gda.edu.lsea.absPerson.Person;

import java.time.LocalDate;
import java.util.*;


/**
 * Coach extends Person class
 * It represents a football coach
 * <p>
 * Coach-specific attributes have been added like
 * nickname, date of birth, current employment information
 */
public class Coach extends Person {
    /** Nickname of the coach */
    private String nickname;
    /** Date of the birth of the coach */
    private LocalDate dateOfBirth;
    /** Current employment information of the coach */
    Map<String, HashSet<String>> employments;

    /**
     * Constructs a Coach object with some specified ID
     *
     * @param id is the unique identifier for this object
     */
    public Coach(UUID id) {
        super(id);
    }

    /**
     * Construct a Coach with specified attributes below
     *
     * @param id is the Coach's unique identifier
     * @param name is the Coach's name
     * @param country is Coach's country of origin
     * @param nickname is Coach's nickname
     * @param dateOfBirth is Coach's date of birth
     * @param employments is Coach's current employment information
     */
    public Coach(UUID id, String name, Map<UUID, String> country, String nickname, LocalDate dateOfBirth,
                 Map<String, HashSet<String>> employments) {
        super(id, name, country);
        this.nickname = nickname;
        this.dateOfBirth = dateOfBirth;
        this.employments = employments;
    }


    public Map<String, HashSet<String>> getEmployments() {
        return employments;
    }

    public void setEmployments(Map<String, HashSet<String>> employments) {
        this.employments = employments;
    }
    /**
     * Returns the nickname of this coach
     *
     * @return this coach's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname for this coach
     *
     * @param nickname is the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns the date of birth of this coach
     *
     * @return the date of birth of this coach
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of this coach
     *
     * @param dateOfBirth is the date of birth to set
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Return the current employment information about this coach
     *
     * @return the current employment information about this coach
     */

}

