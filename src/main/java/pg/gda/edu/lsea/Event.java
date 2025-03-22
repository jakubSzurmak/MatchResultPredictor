package pg.gda.edu.lsea;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    final private UUID id;
    private int half;
    private LocalDateTime timestamp;
    private String playPattern;
    private UUID idPerformTeam;
    private UUID idPerformPlayer;
    private String performBodyPart;
    private String outcome;


    public Event(UUID id) {
        this.id = id;
    }

    public Event(UUID id, int half, LocalDateTime timestamp, String playPattern,
                 UUID idPerformTeam, UUID idPerformPlayer, String performBodyPart, String outcome) {
        this.id = id;
        this.half = half;
        this.timestamp = timestamp;
        this.playPattern = playPattern;
        this.idPerformTeam = idPerformTeam;
        this.idPerformPlayer = idPerformPlayer;
        this.performBodyPart = performBodyPart;
        this.outcome = outcome;
    }

    public UUID getId() {
        return id;
    }

    public int getHalf() {
        return half;
    }

    public void setHalf(int half) {
        this.half = half;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPlayPattern() {
        return playPattern;
    }

    public void setPlayPattern(String playPattern) {
        this.playPattern = playPattern;
    }

    public UUID getIdPerformTeam() {
        return idPerformTeam;
    }

    public void setIdPerformTeam(UUID idPerformTeam) {
        this.idPerformTeam = idPerformTeam;
    }

    public UUID getIdPerformPlayer() {
        return idPerformPlayer;
    }

    public void setIdPerformPlayer(UUID idPerformPlayer) {
        this.idPerformPlayer = idPerformPlayer;
    }

    public String getPerformBodyPart() {
        return performBodyPart;
    }

    public void setPerformBodyPart(String performBodyPart) {
        this.performBodyPart = performBodyPart;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
