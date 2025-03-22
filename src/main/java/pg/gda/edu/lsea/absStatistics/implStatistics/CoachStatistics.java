package pg.gda.edu.lsea.absStatistics.implStatistics;

import pg.gda.edu.lsea.absStatistics.Statistics;

import java.util.UUID;

public class CoachStatistics extends Statistics {
    public CoachStatistics(UUID id) {
        super(id);
    }

    public CoachStatistics(int gamesPlayed, int gamesWon, int goalsScored, int goalsConceded,
                          int totalCleanSheets, UUID id) {
        super(gamesPlayed, gamesWon, goalsScored, goalsConceded, totalCleanSheets, id);
    }
}
