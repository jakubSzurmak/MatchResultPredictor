package pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics;

import jakarta.persistence.*;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;
import pg.gda.edu.lsea.team.Team;

import java.util.UUID;

/**
 * gPlayerStatistics extends the PlayerStatistics class to represent statistics specific to a gooalkeeper
 *
 * Goalkeeper-specific statistical attributes has been added,
 * such as total saves, which will be a key performance metric for the goalkeepers.
 *
 */
@Entity
@Table(name="gPlayerStatistics")
public class gPlayerStatistics extends PlayerStatistics {
    /** Total number of saves made by the goalkeeper */
    private int totalSaves;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    Player player;
    /**
     * Constructs gPlayerStatistics with the specified ID
     *
     * @param id is the unique identifier for these goalkeeper statistics
     */
    public gPlayerStatistics(UUID id) {
        super(id);
    }

    /**
     * Constructs gPlayerStatistics with the specified attributes
     *
     * @param id is the unique identifier for these goalkeeper statistics
     * @param gamesPlayed is the number of games played
     * @param gamesWon is the number of games won
     * @param goalsScored is the number of goals scored
     * @param totalCleanSheets is the number of clean sheets
     * @param totalAssists is the number of assists provided
     * @param totalPasses is the number of passes completed
     * @param totalBallLooses is  the number of ball losses
     * @param totalSaves is the number of saves made
     * @param totalShots is the number of shots taken
     * @param totalGoalConceded is the number of goals conceded
     */
    public gPlayerStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored, int totalCleanSheets,
                             int totalAssists, int totalPasses, int totalBallLooses,
                             int totalSaves, int totalShots, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalAssists,
                totalPasses, totalBallLooses, totalShots, totalGoalConceded);
        this.totalSaves = totalSaves;
    }

    /**
     * Nameless and parameterless constructor for jpa requirements sake. DO NOT USE
     */
    protected gPlayerStatistics() {
        super(null);
    }


    /**
     * Returns the total number of saves made
     *
     * @return the total number of saves
     */
    public int getTotalSaves() {
        return totalSaves;
    }

    /**
     * Sets the total number of saves made
     *
     * @param totalSaves is the total number of saves to be set
     */
    public void setTotalSaves(int totalSaves) {
        this.totalSaves = totalSaves;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
}
