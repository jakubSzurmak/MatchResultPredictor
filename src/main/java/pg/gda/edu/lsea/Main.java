package pg.gda.edu.lsea;

import pg.gda.edu.lsea.absPerson.Person;
import pg.gda.edu.lsea.absPerson.implPerson.Player;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        System.out.println( "-------------");
        System.out.println("ID: " + person.getId());
        System.out.println("Name: " + person.getName());
        System.out.println("Country: " + person.getCountry());
        
    }
}