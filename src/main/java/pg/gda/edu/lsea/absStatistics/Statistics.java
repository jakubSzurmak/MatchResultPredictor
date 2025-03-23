package pg.gda.edu.lsea.absStatistics;

import java.util.UUID;

// Implementation of abstract class Statistics
public abstract class Statistics {
    private final UUID id;
    private float winPerc;
    private int gamesPlayed;
    private int gamesWon;
    private int goalsScored;
    private int totalCleanSheets;
    private int totalGoalConceded;

    public Statistics(UUID id) {
        this.id = id;
    }

    public Statistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored, int totalCleanSheets, int totalGoalConceded) {
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.goalsScored = goalsScored;
        this.totalCleanSheets = totalCleanSheets;
        this.id = id;
        this.totalGoalConceded = totalGoalConceded;
        this.winPerc = (float) this.gamesWon / this.gamesPlayed;
    }

    public UUID getId(){
        return id;
    }

    public int getTotalGoalConceded() {
        return totalGoalConceded;
    }

    public void setTotalGoalConceded(int totalGoalConceded) {this.totalGoalConceded = totalGoalConceded;}

    public float getWinPerc() {
        return winPerc;
    }


    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getTotalCleanSheets() {
        return totalCleanSheets;
    }

    public void setTotalCleanSheets(int totalCleanSheets) {
        this.totalCleanSheets = totalCleanSheets;
    }
}
