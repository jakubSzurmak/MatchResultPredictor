package pg.gda.edu.lsea.absPerson.implPerson.coach;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pg.gda.edu.lsea.absPerson.implPerson.referee.RefereeDeserializer;

@JsonDeserialize(using = CoachDeserializer.class)
public class ResultHolder {
    private Coach homeCoach;
    private Coach awayCoach;

    ResultHolder(Coach homeCoach, Coach awayCoach){
        this.homeCoach = homeCoach;
        this.awayCoach = awayCoach;
    }

    public Coach getHomeCoach(){
        return homeCoach;
    }

    public Coach getAwayCoach(){
        return awayCoach;
    }

    public void setAwayCoach(Coach awayCoach) {
        this.awayCoach = awayCoach;
    }

    public void setHomeCoach(Coach homeCoach) {
        this.homeCoach = homeCoach;
    }
}
