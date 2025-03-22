package pg.gda.edu.lsea.absStatistics.absPlayerStatistics;

import pg.gda.edu.lsea.absStatistics.Statistics;

import java.util.UUID;

public class PlayerStatistics extends Statistics {
    private int totalAssists;
    private int totalPasses;
    private int totalBallLooses;
    private int totalStartingEleven;

    public PlayerStatistics(UUID id) {
        super(id);
    }


    public PlayerStatistics(int gamesPlayed, int gamesWon, int goalsScored, int goalsConceded, int totalCleanSheets,
                            int totalAssists, int totalPasses, int totalBallLooses, int totalStartingEleven, UUID id) {
        super(gamesPlayed, gamesWon, goalsScored, goalsConceded, totalCleanSheets, id);
        this.totalAssists = totalAssists;
        this.totalPasses = totalPasses;
        this.totalBallLooses = totalBallLooses;
        this.totalStartingEleven = totalStartingEleven;
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

    public int getTotalBallLooses() {
        return totalBallLooses;
    }

    public void setTotalBallLooses(int totalBallLooses) {
        this.totalBallLooses = totalBallLooses;
    }

    public int getTotalStartingEleven() {
        return totalStartingEleven;
    }

    public void setTotalStartingEleven(int totalStartingEleven) {
        this.totalStartingEleven = totalStartingEleven;
    }
}
