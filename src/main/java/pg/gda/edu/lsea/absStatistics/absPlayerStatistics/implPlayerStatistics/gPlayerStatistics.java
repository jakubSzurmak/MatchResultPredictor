package pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics;

import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;

import java.util.UUID;

public class gPlayerStatistics extends PlayerStatistics {
    private int totalSaves;

    public gPlayerStatistics(UUID id) {
        super(id);
    }


    public gPlayerStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored, int totalCleanSheets,
                             int totalAssists, int totalPasses, int totalBallLooses, int totalStartingEleven,
                             int totalSaves, int totalShots, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalAssists,
                totalPasses, totalBallLooses, totalStartingEleven, totalShots, totalGoalConceded);
        this.totalSaves = totalSaves;
    }

    public int getTotalSaves() {
        return totalSaves;
    }

    public void setTotalSaves(int totalSaves) {
        this.totalSaves = totalSaves;
    }
}
