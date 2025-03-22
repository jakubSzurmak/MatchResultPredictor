package pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics;

import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.PlayerStatistics;

import java.util.UUID;

public class fPlayerStatistics extends PlayerStatistics {
    private int totalDuel;
    private int totalDuelWins;
    private int duelPercentage;
    private int totalShots;


    public fPlayerStatistics(UUID id) {
        super(id);
    }

    public fPlayerStatistics(int gamesPlayed, int gamesWon, int goalsScored, int goalsConceded, int totalCleanSheets,
                             int totalAssists, int totalPasses, int totalBallLooses, int totalStartingEleven,
                             int duelPercentage, int totalDuelWins, int totalDuel, UUID id, int totalShots) {
        super(gamesPlayed, gamesWon, goalsScored, goalsConceded, totalCleanSheets, totalAssists,
                totalPasses, totalBallLooses, totalStartingEleven, id);
        this.duelPercentage = duelPercentage;
        this.totalDuelWins = totalDuelWins;
        this.totalDuel = totalDuel;
        this.totalShots = totalShots;
    }

    public int getTotalShots() {
        return totalShots;
    }

    public void setTotalShots(int totalShots) {
        this.totalShots = totalShots;
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

    public int getDuelPercentage() {
        return duelPercentage;
    }

    public void setDuelPercentage(int duelPercentage) {
        this.duelPercentage = duelPercentage;
    }
}
