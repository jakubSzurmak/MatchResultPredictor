package pg.gda.edu.lsea.match;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

/**
 * Match represents a football match with details about teams, scores and other match information
 *
 * This class implements the Comparable interface to allow matches to be compared based on their date
 */


@JsonDeserialize(using = MatchDeserializer.class)
public class Match implements Comparable<Match> {
    /** Unique identifier for this match */
    final private UUID id;
    /** Date when the match was played */
    private LocalDate date;
    /** Identifier of the competition */
    private Map<UUID, String> compt;
    /** Season when the match was played */
    private String season;
    /** Identifier of the home team */
    private UUID homeTeamId;
    /** Identifier of the away team */
    private UUID awayTeamId;
    /** Score of the home team */
    private int homeScore;
    /** Score of the away team */
    private int awayScore;
    /** Identifier of the referee */
    private UUID refereeId;
    private UUID homeCoachId;
    private UUID awayCoachId;

    /**
     * Constructs a Match with the specified ID.
     *
     * @param id is the unique identifier for this match
     */
    public Match(UUID id) {
        this.id = id;
    }

    /**
     * Constructs a Match with some specified fields
     *
     * @param id is the unique identifier for this match
     * @param date is the date when the match was played
     * @param compt is the map with identifier and name of the competition
     * @param season is the season when the match was played
     * @param homeTeamId is the identifier of the home team
     * @param awayTeamId is the identifier of the away team
     * @param homeScore is the score of the home team
     * @param awayScore is the score of the away team
     * @param refereeId is the identifier of the referee
     */
    public Match(UUID id, LocalDate date, Map<UUID, String> compt, String season, UUID homeTeamId,
                 UUID awayTeamId, int homeScore, int awayScore, UUID refereeId, UUID homeCoachId, UUID awayCoachId) {
        this.id = id;
        this.date = date;
        this.compt = compt;
        this.season = season;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.refereeId = refereeId;
        this.homeCoachId = homeCoachId;
        this.awayCoachId = awayCoachId;
    }

    /**
     * Compares this match with another match based on their dates
     *
     * @param o is the match to be compared
     * @return a negative integer, zero, or a positive integer as this match's date
     *         is before, the same as, or after the specified match's date
     */
    @Override
    public int compareTo(Match o) {
        return this.date.compareTo(o.date);
    }

    /**
     * Returns the unique identifier of the object
     *
     * @return the unique identifier of the object
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the date of the match
     *
     * @return the date of the match
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date to some date
     *
     * @param date is the date to be set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Return the competition in which the match has taken part
     *
     * @return the competition in which the match has taken part
     */
    public Map<UUID, String> getCompt() {
        return compt;
    }

    /**
     * Set the competition in which the match has taken part
     *
     * @param compt is the competition to be set
     */
    public void setCompt(Map<UUID, String> compt) {
        this.compt = compt;
    }

    /**
     * Return the season in which the match was played
     *
     * @return the season in which the match was played
     */
    public String getSeason() {
        return season;
    }

    /**
     * Sets the season in which the match was played
     *
     * @param season is the season to be set
     */
    public void setSeason(String season) {
        this.season = season;
    }

    /**
     * Returns the Home Team unique identifier
     *
     * @return the Home Team unique identifier
     */
    public UUID getHomeTeamId() {
        return homeTeamId;
    }

    /**
     * Sets the Home Team unique identifier
     *
     * @param homeTeamId is the Home Team unique identifier
     */
    public void setHomeTeamId(UUID homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    /**
     * Returns the Away Team unique identifier
     *
     * @return the Away Team unique identifier
     */
    public UUID getAwayTeamId() {
        return awayTeamId;
    }

    /**
     * Sets the Away Team unique identifier
     *
     * @param awayTeamId is the Away Team unique identifier
     */
    public void setAwayTeamId(UUID awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    /**
     * Returns the score of the Home Team
     *
     * @return the score of the Home Team
     */
    public int getHomeScore() {
        return homeScore;
    }

    /**
     * Sets the score of the Home Team
     *
     * @param homeScore is the score of Home Team to be set
     */
    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    /**
     * Return the score of the Away Team
     *
     * @return the score of the Away Team
     */
    public int getAwayScore() {
        return awayScore;
    }

    /**
     * Sets the score of the Away Team
     *
     * @param awayScore is the score of Away Team to be set
     */
    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    /**
     * Return the unique identifier of the referee of this match
     *
     * @return the unique identifier of the referee of this match
     */
    public UUID getRefereeId() {
        return refereeId;
    }

    /**
     * Sets ID of referee for this match
     *
     * @param refereeId is the referee's ID to be set
     */
    public void setRefereeId(UUID refereeId) {
        this.refereeId = refereeId;
    }

    public UUID getHomeCoachId() {
        return homeCoachId;
    }

    public UUID getAwayCoachId() {
        return awayCoachId;
    }

    public void setHomeCoachId(UUID homeCoachId) {
        this.homeCoachId = homeCoachId;
    }

    public void setAwayCoachId(UUID awayCoachId) {
        this.awayCoachId = awayCoachId;
    }

    public UUID getWinner(){
        if(this.homeScore > this.awayScore){
            return this.homeTeamId;
        }else if(this.homeScore < this.awayScore){
            return this.awayTeamId;
        }else{
            return null;
        }
    }

}
