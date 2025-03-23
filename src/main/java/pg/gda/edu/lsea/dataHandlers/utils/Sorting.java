package pg.gda.edu.lsea.dataHandlers.utils;

import pg.gda.edu.lsea.Match;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Sorting {
    public static void sortPlayersList(List<Player> list) {
        list.sort(Collections.reverseOrder());
    }

    public static void sortMatchesList(List<Match> list) {
        list.sort(Collections.reverseOrder());
    }

    public static void sortPlayersQueue(PriorityQueue<Player> pq) {
        while (!pq.isEmpty()) {
            pq.poll();
        }
    }

}
