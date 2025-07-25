package pg.gda.edu.lsea.absStatistics.implStatistics;

import jakarta.persistence.*;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.team.Team;

import java.util.UUID;

/**
 * TeamStatistics extends the Statistics class to represent statistics specific to a football team
 *
 * This class inherits all attributes and methods from the Statistics class
 * and does not have any additional attributes or methods besides the constructors
 */
@Entity
@Table(name="TeamStats")
public class TeamStatistics extends Statistics {
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    Team team;
    /**
     * Constructs TeamStatistics with the specified ID
     *
     * @param id is the unique identifier for these team statistics
     */

    public TeamStatistics(UUID id) {
        super(id);
    }

    /**
     * Constructs TeamStatistics with some specified attributes
     *
     * @param id is the unique identifier for these team statistics
     * @param gamesPlayed is the number of games played
     * @param gamesWon is the number of games won
     * @param goalsScored is the number of goals scored
     * @param totalCleanSheets is the number of clean sheets
     * @param totalGoalConceded is the number of goals conceded
     */
    public TeamStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored,
                          int totalCleanSheets, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalGoalConceded);
    }

    protected TeamStatistics() {}

    public void setTeam(Team team) {
        this.team = team;
    }
}
