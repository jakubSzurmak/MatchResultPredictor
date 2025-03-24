package pg.gda.edu.lsea.absStatistics.implStatistics;

import pg.gda.edu.lsea.absStatistics.Statistics;

import java.util.UUID;

/**
 * CoachStatistics extends the Statistics class to represent statistics specific to a football coach
 *
 * This class inherits all attributes and methods from the Statistics class
 * and does not have any additional attributes or methods besides the constructors
 */
public class CoachStatistics extends Statistics {
    /**
     * Constructs CoachStatistics with the specified ID
     *
     * @param id is the unique identifer for these coach statistics
     */
    public CoachStatistics(UUID id) {
        super(id);
    }

    /**
     * Constructs CoachStatistics with some specified attributes
     *
     * @param id is the unique identifier for these coach statistics
     * @param gamesPlayed is the number of games played
     * @param gamesWon is the number of games won
     * @param goalsScored is the number of goals scored by the coach's team
     * @param totalCleanSheets is the number of clean sheets by the coach's team
     * @param totalGoalConceded is the number of goals conceded by the coach's team
     */
    public CoachStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored,
                           int totalCleanSheets, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalGoalConceded);
    }
}
