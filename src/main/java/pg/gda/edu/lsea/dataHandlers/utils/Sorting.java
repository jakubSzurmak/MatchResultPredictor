package pg.gda.edu.lsea.dataHandlers.utils;

import pg.gda.edu.lsea.Match;
import pg.gda.edu.lsea.absPerson.Person;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Sorting {
    public static void sortPlayersList(List<Player> list) {
        list.sort(Collections.reverseOrder());
    }

    public static void sortMatchesList(List<Match> list) {
        list.sort(Collections.reverseOrder());
    }

    public static void sortMatchesByTotalScore(List<Match> list) {
        Comparator<Match> comparator = new ComparatorUtils.TotalGoalsComparator();
        list.sort(comparator.reversed());
    }

    public static void sortPeopleByCountry(List<Person> list) {
        Comparator<Person> comparator = new ComparatorUtils.PersonCountryComparator();
        list.sort(comparator.reversed());
    }

    public static void sortPlayersByAge(List<Player> list) {
        Comparator<Player> comparator = new ComparatorUtils.PlayerAgeComparator();
        list.sort(comparator.reversed());
    }



}
