package pg.gda.edu.lsea.network.services.servers;

import pg.gda.edu.lsea.network.services.ParseDataService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TCP Server that accepts and handles clients connection and communication over TCP/IP
 * Supports serialized object transfer for football match analysis functionality
 */
public class TCPServer {
    // Initialize Server Port
    private static final int SERVER_PORT = 6666;
    // Initialize welcome message
    private static final String WELCOME_MESSAGE = "TCP Server has started. Waiting for connection with the client";
    // Initialize accept message
    private static final String CLIENT_ACCEPTED_MESSAGE = "Client has connected";
    // Initialize ending message
    private static final String CLIENT_ENDING_MESSAGE = "Connection with the server has been disconnected. Thank you!";

    // Initialize number of maximum available clients at one moment
    private static final int MAX_CLIENTS = 10;
    // Handle up to MAX_CLIENTS connections
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(MAX_CLIENTS);

    public static void main(String[] args) throws Exception {
        // Initialize the Data Service
        ParseDataService dataService = new ParseDataService();

        // Create server socket
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

        System.out.println(WELCOME_MESSAGE);

        // Accept client connections
        // Work until closed
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                // Display information about user address
                System.out.println(CLIENT_ACCEPTED_MESSAGE + " from " + clientSocket.getInetAddress());

                // Handle client in a separate thread
                threadPool.execute(new ClientHandler(clientSocket, dataService));
            } catch (IOException e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
        }
    }

    /**
     * Handles client connections in separate threads
     */
    private static class ClientHandler implements Runnable {
        // Initializes socket
        private final Socket clientSocket;
        // Initializes separate service for handling the data usage
        private final ParseDataService dataService;

        /**
         * Constructs ClientHandler object
         *
         * @param socket is the socket to be set
         * @param service is the parsing data service object to be set
         */
        public ClientHandler(Socket socket, ParseDataService service) {
            this.clientSocket = socket;
            this.dataService = service;
        }

        /**
         * Handles server logic running
         */
        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    ObjectOutputStream objectOut = new ObjectOutputStream(clientSocket.getOutputStream())
            ) {
                // Initializes boolean for handling running of the client
                boolean clientRunning = true;

                while (clientRunning) {
                    // Display menu
                    out.println("=============================");
                    out.println("MENU:");
                    out.println("1. Get team list");
                    out.println("2. Get correlation between statistics");
                    out.println("3. Get prediction of the match");
                    out.println("4. Exit");
                    out.println("Choose option:");

                    // Variable for user input
                    String userChoice = in.readLine();

                    // Handle user input accordingly
                    switch (userChoice) {
                        case "1":
                            // Send team list as serialized object of bytes
                            byte[] teamListData = dataService.getTeamsList();
                            objectOut.writeObject(teamListData);
                            objectOut.flush();
                            break;

                        case "2":
                            // Send correlation data as serialized object of bytes
                            byte[] correlationData = dataService.getCorrelationData();
                            objectOut.writeObject(correlationData);
                            objectOut.flush();
                            break;

                        case "3":
                            // Get teams for prediction
                            out.println("Choose home team for the prediction: ");
                            String homeTeam = in.readLine();
                            out.println("Choose away team for the prediction: ");
                            String awayTeam = in.readLine();

                            // Send prediction as serialized object of bytes
                            byte[] predictionData = dataService.getPredictionData(homeTeam, awayTeam);
                            objectOut.writeObject(predictionData);
                            objectOut.flush();
                            break;

                        case "4":
                            // Handles exit the program
                            clientRunning = false;
                            out.println(CLIENT_ENDING_MESSAGE);
                            break;

                        default:
                            // Handles invalid input
                            out.println("Invalid input!");
                    }
                }
            } catch (IOException e) {
                // Display error message
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    // Close the Client Socket
                    clientSocket.close();
                    System.out.println("Client connection closed");
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    }
}