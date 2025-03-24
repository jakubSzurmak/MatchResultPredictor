package pg.gda.edu.lsea.dataHandlers.utils;

import pg.gda.edu.lsea.Match;
import pg.gda.edu.lsea.absPerson.Person;
import pg.gda.edu.lsea.absPerson.implPerson.Player;

import java.util.Comparator;

/**
 * In this class implementation of sorting classes using comparator interface
 */
public class ComparatorUtils {


    /**
     * Implementation of sorting matches by the total goals scored
     */
    public static class TotalGoalsComparator implements Comparator<Match> {
        @Override
        public int compare(Match o1, Match o2) {
            return Integer.compare(
                    o1.getHomeScore() + o1.getAwayScore(),
                    o2.getHomeScore() + o2.getAwayScore()
            );
        }
    }

    /**
     * Implementation of sorting Person by their country values
     */
    public static class PersonCountryComparator implements Comparator<Person> {
        @Override
        public int compare(Person p1, Person p2) {
            String country1 = (p1.getCountry() != null) ? p1.getCountry().values().toString() : "Unknown";
            String country2 = (p2.getCountry() != null) ? p2.getCountry().values().toString() : "Unknown";
            return country1.compareToIgnoreCase(country2);
        }
    }

    /**
     * Implementation of sorting Players by their age values
     */
    public static class PlayerAgeComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            return p1.getDateOfBirth().compareTo(p2.getDateOfBirth());
        }
    }
}
