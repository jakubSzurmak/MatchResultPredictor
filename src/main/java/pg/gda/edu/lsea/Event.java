package pg.gda.edu.lsea;

import java.time.LocalTime;
import java.util.UUID;

/**
 * Event represents a specific occurrence during a football match
 *
 * This class captures detailed information about in-game events
 * including when they happened, which player and team were involved,
 * and the outcome of this event
 */
public class Event {
    /** Unique identifier for this event */
    final private UUID id;
    /** Half of the match (1st, 2nd) */
    private int half;
    /** Exact time when the event occurred */
    private LocalTime timestamp;
    /** Pattern of play */
    private String playPattern;
    /** Identifier of the team that performed the action */
    private UUID idPerformTeam;
    /** Identifier of the player that performed the action */
    private UUID idPerformPlayer;
    /** Body part used by the player */
    private String performBodyPart;
    /** Type of event */
    private String type;
    /** Outcome of the event (goal, miss, etc.) */
    private String outcome;
    /** */
    private boolean assist;

    /**
     * Constructs an Event with some specified ID
     *
     * @param id is the unique identifier for this event
     */
    public Event(UUID id) {
        this.id = id;
    }

    /**
     * Constructs an Event with some specified field attributes.
     *
     * @param id is the unique identifier for this event
     * @param half is the half of the match
     * @param timestamp is the exact time when the event occurred
     * @param playPattern is the pattern of play
     * @param idPerformTeam is the identifier of the team that performed the action
     * @param idPerformPlayer is the identifier of the player that performed the action
     * @param performBodyPart is the body part used by the player
     * @param outcome is the outcome of the event
     */
    public Event(UUID id, int half, LocalTime timestamp, String playPattern,
                 UUID idPerformTeam, UUID idPerformPlayer, String performBodyPart, String type, String outcome, boolean assist) {
        this.id = id;
        this.half = half;
        this.timestamp = timestamp;
        this.playPattern = playPattern;
        this.idPerformTeam = idPerformTeam;
        this.idPerformPlayer = idPerformPlayer;
        this.performBodyPart = performBodyPart;
        this.type = type;
        this.outcome = outcome;
        this.assist = assist;
    }

    /**
     * Returns the unique identifier of the match event
     *
     * @return the unique identifier of the match event
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns half in which the event has occurred
     *
     * @return half in which the event has occurred
     */
    public int getHalf() {
        return half;
    }

    /**
     * Sets the half in which the event has occurred
     *
     * @param half is the half to be set
     */
    public void setHalf(int half) {
        this.half = half;
    }

    /**
     * Returns the exact time in which the event has occurred
     *
     * @return the exact time in which the event has occurred
     */
    public LocalTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the exact time in which the event has occurred
     *
     * @param timestamp is the exact time to be set
     */
    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the pattern of play
     *
     * @return the pattern of play
     */
    public String getPlayPattern() {
        return playPattern;
    }

    /**
     * Sets the pattern of play
     *
     * @param playPattern is the pattern of play
     */
    public void setPlayPattern(String playPattern) {
        this.playPattern = playPattern;
    }

    /**
     * Returns the unique identifier of team by which event has happened
     *
     * @return the unique identifier of team by which event has happened
     */
    public UUID getIdPerformTeam() {
        return idPerformTeam;
    }

    /**
     * Sets the unique identifier of team by which event has happened
     *
     * @param idPerformTeam is the identifier of team to be set
     */
    public void setIdPerformTeam(UUID idPerformTeam) {
        this.idPerformTeam = idPerformTeam;
    }

    /**
     * Returns the unique identifier of player that has caused the event
     *
     * @return the unique identifier of player that has caused the event
     */
    public UUID getIdPerformPlayer() {
        return idPerformPlayer;
    }

    /**
     * Sets the unique identifier of player that has caused the event
     *
     * @param idPerformPlayer is the identifier of player to be set
     */
    public void setIdPerformPlayer(UUID idPerformPlayer) {
        this.idPerformPlayer = idPerformPlayer;
    }

    /**
     * Returns the body part used to trigger the event
     *
     * @return the body part used to trigger the event
     */
    public String getPerformBodyPart() {
        return performBodyPart;
    }

    /**
     * Sets the body part that was used to trigger the event
     *
     * @param performBodyPart is the body part to be set
     */
    public void setPerformBodyPart(String performBodyPart) {
        this.performBodyPart = performBodyPart;
    }

    /**
     * Returns the type of the event
     *
     * @return the type of the vent
     */
    public String getType(){ return type;}

    /**
     * Sets the type of the event
     *
     * @param type is the type of the event
     */
    public void setType(String type){ this.type = type;}


    /**
     * Returns the outcome of the event
     *
     * @return the outcome of the event
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * Sets the outcome of the event
     *
     * @param outcome is the outcome to be set
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /**
     * Returns where the event is connected with an assist
     *
     * @return the assist of the event
     */
    public boolean getAssist(){return assist;}

    /**
     * Sets the assist of the event
     *
     * @param assist is the assist of the event
     */
    public void setAssist(boolean assist){ this.assist = assist;}

}
