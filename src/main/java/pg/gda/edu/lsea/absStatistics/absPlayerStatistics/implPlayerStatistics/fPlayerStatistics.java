package pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics;

import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;

import java.util.UUID;

public class fPlayerStatistics extends PlayerStatistics {
    private int totalDuel;
    private int totalDuelWins;
    private float duelPercentage;


    public fPlayerStatistics(UUID id) {
        super(id);
    }

    public fPlayerStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored, int totalCleanSheets,
                             int totalAssists, int totalPasses, int totalBallLooses, int totalStartingEleven,
                             int totalDuelWins, int totalDuel, int totalShots, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalAssists,
                totalPasses, totalBallLooses, totalStartingEleven, totalShots, totalGoalConceded);
        this.duelPercentage = (float) this.totalDuelWins / this.totalDuel;
        this.totalDuelWins = totalDuelWins;
        this.totalDuel = totalDuel;
    }


    public int getTotalDuel() {
        return totalDuel;
    }

    public void setTotalDuel(int totalDuel) {
        this.totalDuel = totalDuel;
    }

    public int getTotalDuelWins() {
        return totalDuelWins;
    }

    public void setTotalDuelWins(int totalDuelWins) {
        this.totalDuelWins = totalDuelWins;
    }

    public float getDuelPercentage() {
        return duelPercentage;
    }
}
