package pg.gda.edu.lsea.absStatistics.implStatistics;

import pg.gda.edu.lsea.absStatistics.Statistics;

import java.util.UUID;

public class CoachStatistics extends Statistics {
    public CoachStatistics(UUID id) {
        super(id);
    }

    public CoachStatistics(UUID id, int gamesPlayed, int gamesWon, int goalsScored,
                           int totalCleanSheets, int totalGoalConceded) {
        super(id, gamesPlayed, gamesWon, goalsScored, totalCleanSheets, totalGoalConceded);
    }
}
