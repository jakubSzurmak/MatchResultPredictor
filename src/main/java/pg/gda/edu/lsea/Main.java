package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.Person;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.dataHandlers.utils.Sorting;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");
        Map<UUID, String> m = new HashMap<>();
        ArrayList<String> positions = new ArrayList<>();
        positions.add("Striker");
        m.put(UUID.randomUUID(), "Poland");
        Person person = new Player(UUID.randomUUID(), "Robert Lewandowski", m, "Orzeł Polski",
                LocalDate.of(1988, Month.AUGUST, 21), 9, "Fc Barcelona", positions, 99);
        System.out.println(person.toString());


        // Comparable Sorting testing
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

        ArrayList<Player> players = new ArrayList<>();
        PriorityQueue<Player> playerQueue = new PriorityQueue<>(players);

        players.add(person1);
        players.add(person2);
        players.add(person3);
        players.add(person4);
        players.add(person5);

        for (Player player : players) {
            System.out.println(player.toString());
        }
        System.out.println("Sorted: ");
        Sorting.sortPlayersList(players);
        for (Player player : players) {
            System.out.println(player.toString());
        }
        System.out.println("==================");
        System.out.println(playerQueue);
        Sorting.sortPlayersQueue(playerQueue);
        System.out.println(playerQueue);

    }
}