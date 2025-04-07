package pg.gda.edu.lsea.dataHandlers.utils;

import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.absPerson.Person;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * In this class implementation of sorting classes using comparable and comparator interface
 */
public class Sorting {

    /**
     * Static function for sorting collections of Players in descending order by their rating
     *
     * @param list any class implementing list interface
     */
    public static void sortPlayersList(List<Player> list) {
        list.sort(Collections.reverseOrder());
    }

    /**
     * Static function for sorting collections of Matches in descending order by their date
     *
     * @param list any class implementing list interface
     */
    public static void sortMatchesList(List<Match> list) {
        list.sort(Collections.reverseOrder());
    }

    /**
     * Static function for sorting collections of Matches in descending order by their total amount of goals
     *
     * @param list any class implementing list interface
     */
    public static void sortMatchesByTotalScore(List<Match> list) {
        Comparator<Match> comparator = new ComparatorUtils.TotalGoalsComparator();
        list.sort(comparator.reversed());
    }

    /**
     * Static function for sorting collections of People in alphabetically lexicographically order by their country
     *
     * @param list any class implementing list interface
     */
    public static void sortPeopleByCountry(List<Person> list) {
        Comparator<Person> comparator = new ComparatorUtils.PersonCountryComparator();
        list.sort(comparator);
    }

    /**
     * Static function for sorting collections of People in descending order by their age
     *
     * @param list any class implementing list interface
     */
    public static void sortPlayersByAge(List<Player> list) {
        Comparator<Player> comparator = new ComparatorUtils.PlayerAgeComparator();
        list.sort(comparator.reversed());
    }



}
