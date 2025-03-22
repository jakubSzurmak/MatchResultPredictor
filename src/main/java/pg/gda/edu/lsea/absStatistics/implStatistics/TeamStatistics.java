package pg.gda.edu.lsea.absStatistics.implStatistics;

import pg.gda.edu.lsea.absStatistics.Statistics;

import java.util.UUID;

public class TeamStatistics extends Statistics {
    public TeamStatistics(UUID id) {
        super(id);
    }

    public TeamStatistics(int gamesPlayed, int gamesWon, int goalsScored, int goalsConceded,
                          int totalCleanSheets, UUID id) {
        super(gamesPlayed, gamesWon, goalsScored, goalsConceded, totalCleanSheets, id);
    }
}
