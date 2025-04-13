package pg.gda.edu.lsea.network.services.servers;

import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.absPerson.implPerson.coach.Coach;
import pg.gda.edu.lsea.absPerson.implPerson.referee.Referee;
import pg.gda.edu.lsea.absStatistics.Statistics;
import pg.gda.edu.lsea.dataHandlers.ParseData;
import pg.gda.edu.lsea.event.Event;
import pg.gda.edu.lsea.match.Match;
import pg.gda.edu.lsea.team.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * TCP Server that accepts and handles clients connection and communication over TCP/IP
 */
public class TCPServer {
    private static final int SERVER_PORT = 6666;
    private static final String WELCOME_MESSAGE = "TCP Server has started. Waiting for connection with the client";
    private static final String CLIENT_ACCEPTED_MESSAGE = "Client has connected";

    public static void main(String[] args) throws Exception {
        List<Match> matches = new ArrayList<>();
        Set<Referee> referees = new HashSet<>();
        Map<UUID, Coach> coaches = new HashMap<>();
        List<Team> parsedTeams = new ArrayList<>();
        HashSet<Player> parsedPlayers = new HashSet<>();
        List<Event> parsedEvents = Collections.synchronizedList(new ArrayList<>());

        ParseData.parseData(matches, referees, coaches, parsedTeams, parsedPlayers, parsedEvents);
        Map<UUID, Statistics> stats = ParseData.getStats(parsedPlayers, parsedEvents, matches);


        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        System.out.println(WELCOME_MESSAGE);

        Socket clientSocket = serverSocket.accept();
        System.out.println(CLIENT_ACCEPTED_MESSAGE);


        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        boolean running = true;
        while (running) {

            out.println("MENU:");
            out.println("1. Get object");
            out.println("2. Get correlation between statistics");
            out.println("3. Get prediction of the match");
            out.println("4. Exit");
            out.println("Choose option:");

            String choice = in.readLine();

            switch (choice) {
                case "4":
                    running = false;
                    break;
                case "1":
                    break;
                case "2":
                    List<String> finalCorr = new ArrayList<>();
                    finalCorr = ParseData.getCorreletion(stats, parsedPlayers);
                    out.println(finalCorr);
                    break;
                case "3":
                    out.println("Choose home team to prediction: ");
                    String homeTeam = in.readLine();
                    out.println(homeTeam);
                    out.println("Choose away team to prediction: ");
                    String awayTeam = in.readLine();
                    out.println(awayTeam);
                    out.println(ParseData.getPrediction(matches, stats, parsedTeams, homeTeam, awayTeam));
                    break;
                default:
                    out.println("Invalid choice. Try again.");
            }
        }

        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
        System.out.println("Server stopped.");
    }
}
