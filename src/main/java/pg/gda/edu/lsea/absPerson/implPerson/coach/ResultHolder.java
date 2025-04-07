package pg.gda.edu.lsea.absPerson.implPerson.coach;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pg.gda.edu.lsea.absPerson.implPerson.referee.RefereeDeserializer;

/**
 * Container class that holds information about coaches for both home and away teams.
 * This class is deserialized from JSON using the {@link CoachDeserializer}
 */
@JsonDeserialize(using = CoachDeserializer.class)
public class ResultHolder {
    /** The coach for the home team */
    private Coach homeCoach;
    /** The coach for the away team */
    private Coach awayCoach;

    /**
     * Constructs a new ResultHolder with the specified home and away coaches
     *
     * @param homeCoach is the coach for the home team
     * @param awayCoach is the coach for the away team
     */
    ResultHolder(Coach homeCoach, Coach awayCoach){
        this.homeCoach = homeCoach;
        this.awayCoach = awayCoach;
    }

    /**
     * Gets the coach for the home team
     *
     * @return The home team's coach
     */
    public Coach getHomeCoach(){
        return homeCoach;
    }

    /**
     * Gets the coach for the away team
     *
     * @return The away team's coach
     */
    public Coach getAwayCoach(){
        return awayCoach;
    }

    /**
     * Sets the coach for the away team
     *
     * @param awayCoach is the coach to set for the away team
     */
    public void setAwayCoach(Coach awayCoach) {
        this.awayCoach = awayCoach;
    }

    /**
     * Sets the coach for the home team
     *
     * @param homeCoach is the coach to set for the home team
     */
    public void setHomeCoach(Coach homeCoach) {
        this.homeCoach = homeCoach;
    }
}
