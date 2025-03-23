package pg.gda.edu.lsea.absStatistics.absPlayerStatistics;

import pg.gda.edu.lsea.absStatistics.Statistics;

import java.util.UUID;

public class PlayerStatistics extends Statistics {
    private int totalAssists;
    private int totalPasses;
    private int totalBallLosses;
    private int totalStartingEleven;
    private int totalShots;

    public PlayerStatistics(UUID id) {
        super(id);
    }


    public PlayerStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored, int totalCleanSheets,
                            int totalAssists, int totalPasses, int totalBallLosses, int totalStartingEleven, int totalShots, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalGoalConceded);
        this.totalAssists = totalAssists;
        this.totalPasses = totalPasses;
        this.totalBallLosses = totalBallLosses;
        this.totalStartingEleven = totalStartingEleven;
        this.totalShots = totalShots;
    }

    public int getTotalShots() {
        return totalShots;
    }

    public void setTotalShots(int totalShots) {
        this.totalShots = totalShots;
    }

    public int getTotalAssists() {
        return totalAssists;
    }

    public void setTotalAssists(int totalAssists) {
        this.totalAssists = totalAssists;
    }

    public int getTotalPasses() {
        return totalPasses;
    }

    public void setTotalPasses(int totalPasses) {
        this.totalPasses = totalPasses;
    }

    public int getTotalBallLosses() {
        return totalBallLosses;
    }

    public void setTotalBallLosses(int totalBallLosses) {
        this.totalBallLosses = totalBallLosses;
    }

    public int getTotalStartingEleven() {
        return totalStartingEleven;
    }

    public void setTotalStartingEleven(int totalStartingEleven) {
        this.totalStartingEleven = totalStartingEleven;
    }
}
