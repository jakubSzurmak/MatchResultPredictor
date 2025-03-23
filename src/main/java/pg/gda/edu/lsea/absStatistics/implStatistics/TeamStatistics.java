package pg.gda.edu.lsea.absStatistics.implStatistics;

import pg.gda.edu.lsea.absStatistics.Statistics;

import java.util.UUID;

public class TeamStatistics extends Statistics {
    public TeamStatistics(UUID id) {
        super(id);
    }

    public TeamStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored,
                          int totalCleanSheets, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalGoalConceded);
    }
}
