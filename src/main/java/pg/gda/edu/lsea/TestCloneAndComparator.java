package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.dataHandlers.utils.Sorting;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class TestCloneAndComparator {
    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("Testing Deep Clone and Comparator Implementations");

        // Create test data
        Map<UUID, String> country = new HashMap<>();
        country.put(UUID.randomUUID(), "Poland");

        ArrayList<String> positions = new ArrayList<>();
        positions.add("Striker");

        // Create original player
        Player original = new Player(
                UUID.randomUUID(),
                "Robert Oryginalny",
                country,
                "Robercik",
                LocalDate.of(1990, Month.JANUARY, 1),
                10,
                "FC Test",
                positions,
                85
        );

        System.out.println("Original player: " + original);

        // Test deep cloning
        Player cloned = original.clone();
        System.out.println("Cloned player: " + cloned);

        // Verify deep cloning works correctly
        System.out.println("\nModifying original player data...");
        original.setName("Robert Zmodyfikowany");
        original.getPositions().add("Midfielder");

        System.out.println("Original player after modification: " + original);
        System.out.println("Cloned player after original was modified: " + cloned);
        System.out.println("Original positions: " + original.getPositions());
        System.out.println("Cloned positions: " + cloned.getPositions());

        // Test comparator-based sorting
        System.out.println("\nTesting Comparator sorting");

        List<Player> players = new ArrayList<>();

        players.add(new Player(UUID.randomUUID(), "Adam Smith", country, "Ace",
                LocalDate.of(1993, Month.MARCH, 15), 7, "FC United", positions, 78));

        positions = new ArrayList<>();
        positions.add("Goalkeeper");
        players.add(new Player(UUID.randomUUID(), "Bob Johnson", country, "Keeper",
                LocalDate.of(1988, Month.JULY, 22), 1, "City FC", positions, 92));

        positions = new ArrayList<>();
        positions.add("Defender");
        players.add(new Player(UUID.randomUUID(), "Charlie Brown", country, "Rock",
                LocalDate.of(1995, Month.FEBRUARY, 10), 4, "Athletic FC", positions, 85));

        System.out.println("\nUnsorted players:");
        for (Player player : players) {
            System.out.println(player.getName() +
                    " - DOB: " + player.getDateOfBirth());
        }

        Sorting.sortPlayersByAge(players);
        System.out.println("\nSorted players by age:");
        for (Player player : players) {
            System.out.println(player.getName() +
                    " - DOB: " + player.getDateOfBirth());
        }

        List<Match> matches = new ArrayList<>();

        matches.add(new Match(UUID.randomUUID(), LocalDate.of(2020, Month.APRIL, 12), UUID.randomUUID(), "2020/2021", UUID.randomUUID(), UUID.randomUUID(),
                1, 2, UUID.randomUUID()));
        matches.add(new Match(UUID.randomUUID(), LocalDate.of(2021, Month.JUNE, 21), UUID.randomUUID(), "2021/2022", UUID.randomUUID(), UUID.randomUUID(),
                5, 3, UUID.randomUUID()));
        matches.add(new Match(UUID.randomUUID(), LocalDate.of(2010, Month.APRIL, 13), UUID.randomUUID(), "2010/2011", UUID.randomUUID(), UUID.randomUUID(),
                0, 1, UUID.randomUUID()));

        System.out.println("\nUnsorted matches:");
        for (Match match : matches) {
            System.out.println(match.getId() +
                    " - Home Score: " + match.getHomeScore()+ " - Away Score: " +match.getAwayScore());
        }

        Sorting.sortMatchesByTotalScore(matches);
        System.out.println("\nSorted matches by total goals:");
        for (Match match : matches) {
            System.out.println(match.getId() +
                    " - Home Score: " + match.getHomeScore() + " - Away Score: " + match.getAwayScore());


        }
    }
}
