package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.Person;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.dataHandlers.utils.Sorting;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * 1st entry point class
 */
public class Main {
    public static void main(String[] args) {
        /**
         * Initialization of the data
         */
        System.out.println("Hello and welcome!");
        Map<UUID, String> m = new HashMap<>();
        ArrayList<String> positions = new ArrayList<>();
        positions.add("Striker");
        m.put(UUID.randomUUID(), "Poland"); // showing the polymorphism usage
        Person person = new Player(UUID.randomUUID(), "Robert Lewandowski", m, "Orzeł Polski",
                LocalDate.of(1988, Month.AUGUST, 21), 9, "Fc Barcelona", positions, 99);
        System.out.println(person.toString());


        /**
         * Comparable Sorting testing
         */
        Player person1 = new Player(UUID.randomUUID(), "person1", m, "Orzeł Polski",
                LocalDate.of(1988, Month.AUGUST, 21), 9, "Fc Barcelona", positions, 0);
        Player person2 = new Player(UUID.randomUUID(), "person2", m, "Orzeł Polski",
                LocalDate.of(1988, Month.AUGUST, 21), 9, "Fc Barcelona", positions, 99);
        Player person3 = new Player(UUID.randomUUID(), "person3", m, "Orzeł Polski",
                LocalDate.of(1988, Month.AUGUST, 21), 9, "Fc Barcelona", positions, 50);
        Player person4 = new Player(UUID.randomUUID(), "person4", m, "Orzeł Polski",
                LocalDate.of(1988, Month.AUGUST, 21), 9, "Fc Barcelona", positions, 98);
        Player person5 = new Player(UUID.randomUUID(), "person5", m, "Orzeł Polski",
                LocalDate.of(1988, Month.AUGUST, 21), 9, "Fc Barcelona", positions, 76);

        List<Player> players = new ArrayList<>();


        Match x = new Match(UUID.randomUUID(), LocalDate.of(1988, Month.AUGUST, 21), UUID.randomUUID(),
                "Season", UUID.randomUUID(), UUID.randomUUID(), 1, 2, UUID.randomUUID());

        Match x1 = new Match(UUID.randomUUID(), LocalDate.of(1990, Month.AUGUST, 21), UUID.randomUUID(),
                "Season", UUID.randomUUID(), UUID.randomUUID(), 1, 2, UUID.randomUUID());

        Match x2 = new Match(UUID.randomUUID(), LocalDate.of(1989, Month.AUGUST, 21), UUID.randomUUID(),
                "Season", UUID.randomUUID(), UUID.randomUUID(), 1, 2, UUID.randomUUID());

        List<Match> matches = new LinkedList<>();
        Set<Match> mySet = new TreeSet<>();
        mySet.add(x);
        mySet.add(x1);
        mySet.add(x2);

        matches.add(x);
        matches.add(x1);
        matches.add(x2);

        players.add(person1);
        players.add(person2);
        players.add(person3);
        players.add(person4);
        players.add(person5);

        /**
         * Printing the results of our tests
         */

        for (Player player : players) {
            System.out.println(player.toString());
        }
        System.out.println("Sorted: ");
        Sorting.sortPlayersList(players);
        for (Player player : players) {
            System.out.println(player.toString());
        }
        System.out.println("==================");
        for (Match match : mySet) {
            System.out.println(match.getDate());
        }

        System.out.println("==================");

        for (Match i: matches) {
            System.out.println(i.getDate());

        }
        System.out.println("Sorted: ");
        Sorting.sortMatchesList(matches);
        for (Match i: matches) {
            System.out.println(i.getDate());
        }
    }
}