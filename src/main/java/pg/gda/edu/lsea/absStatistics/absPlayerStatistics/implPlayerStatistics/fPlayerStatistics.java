package pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics;

import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;

import java.util.UUID;

/**
 * fPlayerStatistics extends the PlayerStatistics class to represent statistics specific to a field player
 *
 * This class has additional fields like duel, duel wins, and calculates the duel win percentage.
 * Those metrics are used for assessing the performance of these players
 */
public class fPlayerStatistics extends PlayerStatistics {
    /** Total number of duels engaged in */
    private int totalDuel;
    /** Total number of duels won */
    private int totalDuelWins;
    /** Percentage of duels won */
    private float duelPercentage;


    /**
     * Constructs fPlayerStatistics with the specified ID
     *
     * @param id is the unique identifier for the object
     */
    public fPlayerStatistics(UUID id) {
        super(id);
    }

    /**
     * Constructs fPlayerStatistics with some specified attributes
     * Calculates the duel win percentage based on total duels and duels won
     *
     * @param id is the unique identifier for these field player statistics
     * @param gamesPlayed is the number of games played
     * @param gamesWon is the number of games won
     * @param goalsScored is the number of goals scored
     * @param totalCleanSheets is the number of clean sheets
     * @param totalAssists is the number of assists provided
     * @param totalPasses is the number of passes completed
     * @param totalBallLooses is the number of ball losses
     * @param totalStartingEleven is the number of appearances in starting eleven
     * @param totalDuelWins is the number of duels won
     * @param totalDuel is the total number of duels engaged in
     * @param totalShots is the number of shots taken
     * @param totalGoalConceded is the number of goals conceded
     */
    public fPlayerStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored, int totalCleanSheets,
                             int totalAssists, int totalPasses, int totalBallLooses, int totalStartingEleven,
                             int totalDuelWins, int totalDuel, int totalShots, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalAssists,
                totalPasses, totalBallLooses, totalStartingEleven, totalShots, totalGoalConceded);
        this.duelPercentage = (float) this.totalDuelWins / this.totalDuel;
        this.totalDuelWins = totalDuelWins;
        this.totalDuel = totalDuel;
    }


    /**
     * Returns the number of duels engaged in
     *
     * @return the number of duels engaged in
     */
    public int getTotalDuel() {
        return totalDuel;
    }

    /**
     * Sets the number of duels engaged in
     *
     * @param totalDuel is the number of duels to set
     */
    public void setTotalDuel(int totalDuel) {
        this.totalDuel = totalDuel;
    }

    /**
     * Returns the total number of duels won
     *
     * @return the total number of duels won
     */
    public int getTotalDuelWins() {
        return totalDuelWins;
    }

    /**
     * Set the number of total duels won
     *
     * @param totalDuelWins is the number of total duels won to be set
     */
    public void setTotalDuelWins(int totalDuelWins) {
        this.totalDuelWins = totalDuelWins;
    }

    /**
     * Return the percentage of duels won
     *
     * @return the percentage of duels won
     */
    public float getDuelPercentage() {
        return duelPercentage;
    }
}
