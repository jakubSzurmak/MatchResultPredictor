package pg.gda.edu.lsea.absPerson.implPerson;

import pg.gda.edu.lsea.absPerson.Person;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Coach extends Person {
    private String nickname;
    private Date dateOfBirth;
    private String currEmployment;

    public Coach(UUID id) {
        super(id);
    }

    public Coach(UUID id, String name, Map<UUID, String> country, String nickname, Date dateOfBirth,
                 String currEmployment) {
        super(id, name, country);
        this.nickname = nickname;
        this.dateOfBirth = dateOfBirth;
        this.currEmployment = currEmployment;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCurrEmployment() {
        return currEmployment;
    }

    public void setCurrEmployment(String currEmployment) {
        this.currEmployment = currEmployment;
    }
}

