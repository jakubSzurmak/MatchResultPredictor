package pg.gda.edu.lsea.absPerson.implPerson;

import pg.gda.edu.lsea.absPerson.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


public class Player extends Person implements Comparable<Player> {
    private String nickname;
    private LocalDate dateOfBirth;
    private int jerseyNr;
    private String currClub;
    private ArrayList<String> positions; //to discuss
    private int rating;

    public Player(UUID id) {
        super(id);
    }

    public Player(UUID id, String name, Map<UUID, String> country, String nickname,
                  LocalDate dateOfBirth, int jerseyNr, String currClub, ArrayList<String> positions, int rating) {
        super(id, name, country);
        this.nickname = nickname;
        this.dateOfBirth = dateOfBirth;
        this.jerseyNr = jerseyNr;
        this.currClub = currClub;
        this.positions = positions;
        this.rating = rating;
    }

    @Override
    public int compareTo(Player o) {
        return Integer.compare(rating, o.getRating());
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getJerseyNr() {
        return jerseyNr;
    }

    public void setJerseyNr(int jerseyNr) {
        this.jerseyNr = jerseyNr;
    }

    public String getCurrClub() {
        return currClub;
    }

    public void setCurrClub(String currClub) {
        this.currClub = currClub;
    }

    public ArrayList<String> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<String> positions) {
        this.positions = positions;
    }
}
