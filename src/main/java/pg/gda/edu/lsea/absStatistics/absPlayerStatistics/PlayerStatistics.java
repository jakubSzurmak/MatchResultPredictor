package pg.gda.edu.lsea.absStatistics.absPlayerStatistics;

import pg.gda.edu.lsea.absStatistics.Statistics;

import java.util.UUID;

/**
 * PlayerStatistics extends the Statistics class and represents
 * statistics to a specific football player.
 *
 * Player-specific statistical attributes
 * such as assists, passes, ball losses etc.
 * have been added to this class.
 */
public abstract class PlayerStatistics extends Statistics {
    /** Number of assists provided */
    private int totalAssists;
    /** Number of passes completed */
    private int totalPasses;
    /** Number of ball losses */
    private int totalBallLosses;
    /** Number of appearances in starting eleven */
    private int totalStartingEleven;
    /** Number of shots taken */
    private int totalShots;

    /**
     * Constructs PlayerStatistics with the specified ID
     *
     * @param id is the unique identifier for these player statistics
     */
    public PlayerStatistics(UUID id) {
        super(id);
    }


    /**
     * Constructs PlayerStatistics with the specified attributes.
     *
     * @param id is the unique identifier for these player statistics
     * @param gamesPlayed is the number of games played
     * @param gamesWon is the number of games won
     * @param goalsScored is the number of goals scored
     * @param totalCleanSheets is the number of clean sheets
     * @param totalAssists is the number of assists provided
     * @param totalPasses is the number of passes completed
     * @param totalBallLosses is the number of ball losses
     * @param totalStartingEleven is the number of appearances in starting eleven
     * @param totalShots is the number of shots taken
     * @param totalGoalConceded is the number of goals conceded
     */
    public PlayerStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored, int totalCleanSheets,
                            int totalAssists, int totalPasses, int totalBallLosses, int totalStartingEleven, int totalShots, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalGoalConceded);
        this.totalAssists = totalAssists;
        this.totalPasses = totalPasses;
        this.totalBallLosses = totalBallLosses;
        this.totalStartingEleven = totalStartingEleven;
        this.totalShots = totalShots;
    }

    /**
     * Returns the number of shots taken
     *
     * @return the number of shots taken
     */
    public int getTotalShots() {
        return totalShots;
    }

    /**
     * Sets the number of shots taken
     *
     * @param totalShots is the number of shots to set
     */
    public void setTotalShots(int totalShots) {
        this.totalShots = totalShots;
    }

    /**
     * Returns the number of assists
     *
     * @return the number of assists
     */
    public int getTotalAssists() {
        return totalAssists;
    }

    /**
     * Sets the number of assists
     *
     * @param totalAssists is the number of assists to set
     */
    public void setTotalAssists(int totalAssists) {
        this.totalAssists = totalAssists;
    }

    /**
     * Returns the number of total passes
     *
     * @return is the number of passes
     */
    public int getTotalPasses() {
        return totalPasses;
    }

    /**
     * Sets the number of total passes
     *
     * @param totalPasses is the number of total passes to set
     */
    public void setTotalPasses(int totalPasses) {
        this.totalPasses = totalPasses;
    }

    /**
     * Returns the number of ball losses
     *
     * @return the number of ball losses
     */
    public int getTotalBallLosses() {
        return totalBallLosses;
    }

    /**
     * Sets the number of ball losses
     *
     * @param totalBallLosses is the number of ball losses to set
     */
    public void setTotalBallLosses(int totalBallLosses) {
        this.totalBallLosses = totalBallLosses;
    }

    /**
     * Returns the number of apperances in starting eleven
     *
     * @return the number of apperances in starting eleven
     */
    public int getTotalStartingEleven() {
        return totalStartingEleven;
    }

    /**
     * Sets the number of appearances in starting eleven
     *
     * @param totalStartingEleven is the number of appearances to set
     */
    public void setTotalStartingEleven(int totalStartingEleven) {
        this.totalStartingEleven = totalStartingEleven;
    }
}
