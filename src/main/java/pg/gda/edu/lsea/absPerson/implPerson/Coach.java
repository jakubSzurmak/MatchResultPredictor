package pg.gda.edu.lsea.absPerson.implPerson;

import pg.gda.edu.lsea.absPerson.Person;

import java.util.Date;
import java.util.Map;
import java.util.UUID;


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
    private Date dateOfBirth;
    /** Current employment information of the coach */
    private String currEmployment;

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
     * @param currEmployment is Coach's current employment information
     */
    public Coach(UUID id, String name, Map<UUID, String> country, String nickname, Date dateOfBirth,
                 String currEmployment) {
        super(id, name, country);
        this.nickname = nickname;
        this.dateOfBirth = dateOfBirth;
        this.currEmployment = currEmployment;
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
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of this coach
     *
     * @param dateOfBirth is the date of birth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Return the current employment information about this coach
     *
     * @return the current employment information about this coach
     */
    public String getCurrEmployment() {
        return currEmployment;
    }

    /**
     * Sets the current employment information about this coach
     *
     * @param currEmployment is the current employment information about this coach to set
     */
    public void setCurrEmployment(String currEmployment) {
        this.currEmployment = currEmployment;
    }
}

