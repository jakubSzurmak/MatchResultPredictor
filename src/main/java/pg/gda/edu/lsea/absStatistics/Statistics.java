package pg.gda.edu.lsea.absStatistics;

import java.util.UUID;

public abstract class Statistics {
    private final UUID id;
    private float winPerc;
    private int gamesPlayed;
    private int gamesWon;
    private int goalsScored;
    private int goalsConceded;
    private int totalCleanSheets;

    public Statistics(UUID id) {
        this.id = id;
    }

    public Statistics(int gamesPlayed, int gamesWon, int goalsScored,
                      int goalsConceded, int totalCleanSheets, UUID id) {
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.goalsScored = goalsScored;
        this.goalsConceded = goalsConceded;
        this.totalCleanSheets = totalCleanSheets;
        this.id = id;
        this.winPerc = (float) this.gamesWon / this.gamesPlayed;
    }

    public UUID getId(){
        return id;
    }


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

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public void setGoalsConceded(int goalsConceded) {
        this.goalsConceded = goalsConceded;
    }

    public int getTotalCleanSheets() {
        return totalCleanSheets;
    }

    public void setTotalCleanSheets(int totalCleanSheets) {
        this.totalCleanSheets = totalCleanSheets;
    }
}
