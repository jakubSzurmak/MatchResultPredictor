package pg.gda.edu.lsea.absStatistics;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Statistics is an abstract class that provides the foundation for tracking
 * various performance metrics in the football system
 *
 * Core statistical attributes and methods have been implemented in it.
 */

@MappedSuperclass
public abstract class Statistics {
    /** Unique identifier for these statistics */
    @Id
    private final UUID id;

    /** Winning percentage */
    @Transient
    private float winPerc;
    /** Number of games played */
    private int gamesPlayed;
    /** Number of games won */
    private int gamesWon;
    /** Number of goals scored */
    private int goalsScored;
    /** Number of clean sheets (no goals conceded) */
    private int totalCleanSheets;
    /** Number of goals conceded */
    private int totalGoalConceded;

    @Transient
    private float goalPerc;
    @Transient
    private float cleanSheetPerc;

    /**
     * Constructs Statistics with the specified ID
     *
     * @param id is the unique identifier for these statistics
     */
    public Statistics(UUID id) {
        this.id = id;
    }

    /**
     * Constructs Statistics with the specified attributes
     * This constructor also calculates the winning percentage based on
     * games played and games won
     *
     * @param id is the unique identifier for these statistics
     * @param gamesPlayed is the number of games played
     * @param gamesWon is the number of games won
     * @param goalsScored is the number of goals scored
     * @param totalCleanSheets is the number of clean sheets
     * @param totalGoalConceded is the number of goals conceded
     */
    public Statistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored, int totalCleanSheets, int totalGoalConceded) {
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.goalsScored = goalsScored;
        this.totalCleanSheets = totalCleanSheets;
        this.id = id;
        this.totalGoalConceded = totalGoalConceded;
    }

    /**
     * Nameless constructor for JPA sake. DO NOT USE
     */
    protected Statistics() {
        this.id = null;
    }

    /**
     * Returns the unique identifier of these statistics
     *
     * @return the UUID of these statistics
     */
    public UUID getId(){
        return id;
    }

    /**
     * Returns the number of goals conceded
     *
     * @return the number of goals conceded
     */
    public int getTotalGoalConceded() {
        return totalGoalConceded;
    }

    /**
     * Sets the number of goals conceded
     *
     * @param totalGoalConceded is the number of goals conceded to set
     */
    public void setTotalGoalConceded(int totalGoalConceded) {this.totalGoalConceded = totalGoalConceded;}

    /**
     * Returns the winning percentage
     *
     * @return the winning percentage
     */
    public float getWinPerc() {
        return winPerc;
    }

    /**
     * Returns the number of games played
     *
     * @return the number of games played
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Sets the number of games played
     *
     * @param gamesPlayed is the number of games played to set
     */
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /**
     * Returns the number of games won
     *
     * @return the number of games won
     */
    public int getGamesWon() {
        return gamesWon;
    }

    /**
     * Sets the number of games won
     *
     * @param gamesWon is the number of games won to set
     */
    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    /**
     * Returns the number of goals scored
     *
     * @return the number of goals scored
     */
    public int getGoalsScored() {
        return goalsScored;
    }

    /**
     * Sets the number of goals scored
     *
     * @param goalsScored is the number of goals scored to set
     */
    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    /**
     * Returns the number of clean sheets
     *
     * @return the number of clean sheets
     */
    public int getTotalCleanSheets() {
        return totalCleanSheets;
    }

    /**
     * Sets the number of clean sheets
     *
     * @param totalCleanSheets is the number of clean sheets to set
     */
    public void setTotalCleanSheets(int totalCleanSheets) {
        this.totalCleanSheets = totalCleanSheets;
    }

    public void setGoalPerc(){
        this.goalPerc = ( (float) this.goalsScored / (float) this.gamesPlayed);
    }

    public void setCleanSheetPerc(){
        this.cleanSheetPerc =  ( (float) this.totalCleanSheets / (float) this.gamesPlayed);
    }

    /**
     * Calculates win rate percentage
     */
    public void setWinPerc(){
        this.winPerc = ( (float)  this.gamesWon / (float)  this.gamesPlayed);
    }

    /**
     * Returns goal percentage
     *
     * @return goal percentage
     */
    public float getGoalPerc(){
        return goalPerc;
    }

    /**
     * Returns clean sheet percentage
     *
     * @return clean sheet percentage
     */
    public float getCleanSheetPerc(){
        return cleanSheetPerc;
    }

    /**
     * Returns a string representation of the Statistics object
     *
     * @return a string representation of this Statistics object
     */
    @Override
    public String toString() {
        return "Statistics{" +
                "id=" + id +
                ", winPerc=" + winPerc +
                ", gamesPlayed=" + gamesPlayed +
                ", gamesWon=" + gamesWon +
                ", goalsScored=" + goalsScored +
                ", totalCleanSheets=" + totalCleanSheets +
                ", totalGoalConceded=" + totalGoalConceded +
                ", goalPerc=" + goalPerc +
                ", cleanSheetPerc=" + cleanSheetPerc +
                '}';
    }
}
