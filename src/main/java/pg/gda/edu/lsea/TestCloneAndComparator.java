package pg.gda.edu.lsea;

import net.bytebuddy.asm.Advice;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.absPerson.Person;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.dataHandlers.utils.Sorting;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * 2nd entry point class
 */
public class TestCloneAndComparator {
    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("Testing Deep Clone and Comparator Implementations");

        /**
         * Creating test data
         */
        Map<UUID, String> country = new HashMap<>();
        country.put(UUID.randomUUID(), "Poland");

        ArrayList<String> positions = new ArrayList<>();
        positions.add("Striker");

        /**
         * Creating original player
         */
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

        /**
         * Test deep cloning
         */
        Player cloned = original.clone();
        System.out.println("Cloned player: " + cloned);

        /**
         * Verify deep cloning works correctly
         */
        System.out.println("\nModifying original player data...");
        original.setName("Robert Zmodyfikowany");
        original.getPositions().add("Midfielder");

        System.out.println("Original player after modification: " + original);
        System.out.println("Cloned player after original was modified: " + cloned);
        System.out.println("Original positions: " + original.getPositions());
        System.out.println("Cloned positions: " + cloned.getPositions());

        /**
         * Test comparator-based sorting
         */
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

        HashMap<UUID, String> compt = new HashMap<>();
        compt.put(UUID.randomUUID(), "PremierLeague");

       // matches.add(new Match(UUID.randomUUID(), LocalDate.of(2020, Month.APRIL, 12), compt, "2020/2021", UUID.randomUUID(), UUID.randomUUID(),
         //       1, 2, UUID.randomUUID()));
        //matches.add(new Match(UUID.randomUUID(), LocalDate.of(2021, Month.JUNE, 21), compt, "2021/2022", UUID.randomUUID(), UUID.randomUUID(),
          //      5, 3, UUID.randomUUID()));
        //matches.add(new Match(UUID.randomUUID(), LocalDate.of(2010, Month.APRIL, 13), compt, "2010/2011", UUID.randomUUID(), UUID.randomUUID(),
          //      0, 1, UUID.randomUUID()));

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

        List<Person> peopleList = new ArrayList<>();

        Map<UUID, String> country1 = new HashMap<>();
        country1.put(UUID.randomUUID(), "Portugal");

        peopleList.add(new Player(UUID.randomUUID(), "Cristiano Ronaldo", country1,
                "CR7", LocalDate.of(1985, Month.FEBRUARY, 5), 7, "Al-Nassr", new ArrayList<>(Arrays.asList("Forward")), 91));

        Map<UUID, String> country2 = new HashMap<>();
        country2.put(UUID.randomUUID(), "Argentina");

        peopleList.add(new Player(UUID.randomUUID(), "Lionel Messi", country2,
                "Leo", LocalDate.of(1987, Month.JUNE, 24), 10, "Inter Miami", new ArrayList<>(Arrays.asList("Forward")), 90));

        Map<UUID, String> country3 = new HashMap<>();
        country3.put(UUID.randomUUID(), "Germany");

        peopleList.add(new Coach(UUID.randomUUID(), "Jurgen Klopp", country3,
                "Kloppo", null, null, null));

        Map<UUID, String> country4 = new HashMap<>();
        country4.put(UUID.randomUUID(), "Spain");

        peopleList.add(new Coach(UUID.randomUUID(), "Pep Guardiola", country4,
                "Pep", null, null, null));



        peopleList.add(new Referee(UUID.randomUUID(), "Szymon Marciniak", country));

        peopleList.add(new Referee(UUID.randomUUID(), "Felix Brych", country3));


        System.out.println("Unsorted people list :");
        for (Person person : peopleList) {
            String strCountry = person.getCountry() != null ?
                    person.getCountry().values().toString() : "Unknown";
            String type = person.getClass().getSimpleName();
            System.out.println(type + ": " + person.getName() + " - Country: " + strCountry);
        }


        Sorting.sortPeopleByCountry(peopleList);


        System.out.println("\nPeople sorted by country:");
        for (Person person : peopleList) {
            String strCountry = person.getCountry() != null ?
                    person.getCountry().values().toString() : "Unknown";
            String type = person.getClass().getSimpleName();
            System.out.println(type + ": " + person.getName() + " - Country: " + strCountry);
        }

    }
}
