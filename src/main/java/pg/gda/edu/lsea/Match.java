package pg.gda.edu.lsea;

import java.time.LocalDate;
import java.util.UUID;

public class Match implements Comparable<Match> {
    final private UUID id;
    private LocalDate date;
    private UUID comptID;
    private String season;
    private UUID homeTeamId;
    private UUID awayTeamId;
    private int homeScore;
    private int awayScore;
    private UUID refereeId;


    public Match(UUID id) {
        this.id = id;
    }

    public Match(UUID id, LocalDate date, UUID comptID, String season, UUID homeTeamId,
                 UUID awayTeamId, int homeScore, int awayScore, UUID refereeId) {
        this.id = id;
        this.date = date;
        this.comptID = comptID;
        this.season = season;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.refereeId = refereeId;
    }

    @Override
    public int compareTo(Match o) {
        return this.date.compareTo(o.date);
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public UUID getComptID() {
        return comptID;
    }

    public void setComptID(UUID comptID) {
        this.comptID = comptID;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public UUID getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(UUID homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public UUID getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(UUID awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public UUID getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(UUID refereeId) {
        this.refereeId = refereeId;
    }

}
