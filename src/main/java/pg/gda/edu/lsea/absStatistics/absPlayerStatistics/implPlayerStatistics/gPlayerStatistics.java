package pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics;

import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;

import java.util.UUID;

public class gPlayerStatistics extends PlayerStatistics {
    private int totalSaves;

    public gPlayerStatistics(UUID id) {
        super(id);
    }


    public gPlayerStatistics(int gamesPlayed, int gamesWon, int goalsScored, int goalsConceded, int totalCleanSheets,
                             int totalAssists, int totalPasses, int totalBallLooses, int totalStartingEleven, UUID id,
                             int totalSaves) {
        super(gamesPlayed, gamesWon, goalsScored, goalsConceded, totalCleanSheets, totalAssists,
                totalPasses, totalBallLooses, totalStartingEleven, id);
        this.totalSaves = totalSaves;
    }

    public int getTotalSaves() {
        return totalSaves;
    }

    public void setTotalSaves(int totalSaves) {
        this.totalSaves = totalSaves;
    }
}
